<script setup>

    import productApi from "@/api/product.js";
    import {reactive, ref} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import { Plus } from '@element-plus/icons-vue'

    const list = ref([])

    const total = ref(0)

    const productQuery = reactive({
        name: '',
        page: 1,
        limit: 10
    })

    const loadData = () => {
        productApi.list(productQuery).then(result => {
            if (result.code == 0){
                list.value = result.data.records
                total.value = result.data.total
            }
        })
    }

    loadData()

    const onSearch = () => {
        loadData()
    }

    //deleteById
    const deleteById = (id) => {
        ElMessageBox.confirm(
            'Are you sure to delete?',
            'Warning',
            {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'warning',
                lockScroll: false //防止抖动
            }
        ).then(() => {
            productApi.deleteById(id).then(result => {
                if (result.code === 0) {
                    ElMessage.success(result.msg)
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        })
    }

    const ids = ref([])
    const handleSelectionChange = (rows) => {
        console.log(rows)
        ids.value = rows.map(row => row.id)
        console.log(ids.value)
    }

    //deleteAll
    const deleteAll = () => {
        ElMessageBox.confirm(
            'Are you sure to delete selected?',
            'Warning',
            {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'warning',
                lockScroll: false //防止抖动
            }
        ).then(() => {
            productApi.deleteAll(ids.value).then(result => {
                if (result.code === 0) {
                    ElMessage.success(result.msg)
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        })
    }

    // add/edit
    const dialogFormVisible = ref(false)
    const product = ref({})
    const title = ref('')

    const showAddDialog = () => {
        dialogFormVisible.value = true
        title.value = 'add product'
        product.value = {}
    }

    const showUpdateDialog = (id) => {
        dialogFormVisible.value = true
        title.value = 'update product'
        product.value = {}

        productApi.selectById(id).then(result => {
            product.value = result.data
        })
    }

    const addOrUpdate = () => {
        if (product.value.id) {
            productApi.updateById(product.value).then(result => {
                if (result.code === 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        } else {
            productApi.add(product.value).then(result => {
                if (result.code === 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        }
    }

    const handleAvatarSuccess = (result) => {
        product.value.avatar = '/api/pic/'+result.data
    }

    import {useTokenStore} from '@/store/token.js'

    const tokenStore = useTokenStore()

    const headers = ref({
        Authorization: tokenStore.token
    })



</script>

<template>
    <el-card class="">
    <template #header>
        <div class="header">
            <el-button type="primary" @click="showAddDialog">添加</el-button>
            <el-button type="primary" @click="deleteAll">批量删除</el-button>
        </div>
    </template>


        <el-form :inline="true">
        <el-form-item label="名字">
            <el-input v-model="productQuery.name" placeholder="请输入名字" clearable/>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="onSearch">搜索</el-button>
        </el-form-item>
        </el-form>

        <el-table :data="list" style="width: 100%" ref="multipleTableRef" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column fixed prop="id" label="ID"/>
        <el-table-column prop="name" label="name"/>
        <el-table-column prop="categoryId" label="categoryId"/>
        <el-table-column prop="price" label="price"/>
        <el-table-column prop="stock" label="stock"/>

          <el-table-column prop="mainImage" label="mainImage">
            <template #default="scope">
                <img v-if="scope.row.mainImage" :src="scope.row.mainImage" style="max-height: 40px; max-width: 120px;" />
            </template>
        </el-table-column>
        <el-table-column fixed="right" label="Operations">
            <template #default="{ row }">
                <el-button type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
                <el-button type="danger" @click="deleteById(row.id)">删除</el-button>
            </template>
        </el-table-column>
        </el-table>

        <el-pagination
            v-model:current-page="productQuery.page"
            v-model:page-size="productQuery.limit"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @change="loadData"
            style="margin-top: 20px; justify-content: flex-end"
        />
    </el-card>

    <!--添加、编辑弹出框-->
    <el-dialog v-model="dialogFormVisible" :title="title" width="500" :lock-scroll="false">
        <el-form :model="product">
            <el-form-item label="name" :label-width="60">
                <el-input v-model="product.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="categoryId" :label-width="60">
                <el-input v-model="product.categoryId" autocomplete="off" />
            </el-form-item>
          <el-form-item label="subtitle" :label-width="60">
            <el-input v-model="product.subtitle" autocomplete="off" />
          </el-form-item>
            <el-form-item label="price" :label-width="60">
                <el-input v-model="product.price" autocomplete="off" />
            </el-form-item>
          <el-form-item label="stock" :label-width="60">
            <el-input v-model="product.stock" autocomplete="off" />
          </el-form-item>
            <el-form-item label="mainImage" :label-width="60">
            <el-upload
                class="avatar-uploader"
                action="/api/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :headers="headers"
            >
                <img class="mainImage" v-if="product.mainImage" :src="product.mainImage" />
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus/>
                </el-icon>
            </el-upload>
            </el-form-item>
            <el-form-item label="detail" :label-width="60">
              <el-input v-model="product.detail" autocomplete="off" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogFormVisible = false">cancel</el-button>
                <el-button type="primary" @click="addOrUpdate">
                    confirm
                </el-button>
            </div>
        </template>
    </el-dialog>


</template>


<style>
.avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
}
.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}
</style>
