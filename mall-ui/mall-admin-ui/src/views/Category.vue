<script setup>

    import categoryApi from "@/api/category.js";
    import {reactive, ref} from "vue";
    import {ElMessage, ElMessageBox} from "element-plus";
    import { Plus } from '@element-plus/icons-vue'

    const list = ref([])

    const total = ref(0)

    const categoryQuery = reactive({
        name: '',
        page: 1,
        limit: 10
    })

    const loadData = () => {
        categoryApi.list(categoryQuery).then(result => {
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
            categoryApi.deleteById(id).then(result => {
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
            categoryApi.deleteAll(ids.value).then(result => {
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
    const category = ref({})
    const title = ref('')

    const showAddDialog = () => {
        dialogFormVisible.value = true
        title.value = 'add category'
        category.value = {}
    }

    const showUpdateDialog = (id) => {
        dialogFormVisible.value = true
        title.value = 'update category'
        category.value = {}

        categoryApi.selectById(id).then(result => {
            category.value = result.data
        })
    }

    const addOrUpdate = () => {
        if (category.value.id) {
            categoryApi.updateById(category.value).then(result => {
                if (result.code === 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        } else {
            categoryApi.add(category.value).then(result => {
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
        category.value.avatar = '/api/pic/'+result.data
    }

    import {useTokenStore} from '@/store/token.js'
    import WangEditor from "@/components/WangEditor.vue";

    const tokenStore = useTokenStore()

    const headers = ref({
        Authorization: tokenStore.token
    })

    const onEditorChange = (detail) => {
      console.log(detail)
      category.value.detail = detail
    }



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
            <el-input v-model="categoryQuery.name" placeholder="请输入名字" clearable/>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="onSearch">搜索</el-button>
        </el-form-item>
        </el-form>

        <el-table :data="list" style="width: 100%" ref="multipleTableRef" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column fixed prop="id" label="ID"/>
        <el-table-column prop="name" label="name"/>
        <el-table-column prop="parentId" label="parentId"/>
        <el-table-column fixed="right" label="Operations">
            <template #default="{ row }">
                <el-button type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
                <el-button type="danger" @click="deleteById(row.id)">删除</el-button>
            </template>
        </el-table-column>
        </el-table>

        <el-pagination
            v-model:current-page="categoryQuery.page"
            v-model:page-size="categoryQuery.limit"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @change="loadData"
            style="margin-top: 20px; justify-content: flex-end"
        />
    </el-card>

    <!--添加、编辑弹出框-->
    <el-dialog v-model="dialogFormVisible" :title="title" width="70%" :lock-scroll="false">
        <el-form :model="category">
            <el-form-item label="name" :label-width="60">
                <el-input v-model="category.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="parentId" :label-width="60">
                <el-input v-model="category.parentId" autocomplete="off" />
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
