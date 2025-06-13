<script setup>
    import {
        Management,
        Promotion,
        UserFilled,
        User,
        Crop,
        EditPen,
        SwitchButton,
        CaretBottom
    } from '@element-plus/icons-vue'
    import avatar from '@/assets/default.png'

    //条目被点击后,调用的函数
    import {useRouter} from 'vue-router'

    const router = useRouter();
    import {ElMessage, ElMessageBox} from 'element-plus'

    import {useAdminInfoStore} from '@/store/adminInfo.js'
    import adminApi from "@/api/admin.js";
    import {useTokenStore} from "@/store/token.js";
    import {ref} from "vue";

    const adminInfoStore = useAdminInfoStore()
    const tokenStore = useTokenStore()


    const getAdminInfo = () => {
        adminApi.adminInfo().then(result =>{
            if (result.code == 0){
                adminInfoStore.setAdminInfo(result.data);
            }
        })
    }

    getAdminInfo()

    const dialogUpdateAdminInfoVisible = ref(false)
    const adminUser = ref({})



    const handleCommand = (command) => {
        //判断指令
        if (command === 'logout') {
            //退出登录
            tokenStore.removeToken()
            adminInfoStore.removeAdminInfo()
            router.push('/login')
        }
        else if (command === 'updateAdminInfo'){
            dialogUpdateAdminInfoVisible.value = true
/*
            adminUser.value = adminInfoStore.admin
*/
            Object.assign(adminUser.value,adminInfoStore.admin)
        }
        else if(command === 'resetPassword'){
            dialogResetPasswordVisible.value = true
        }
        else {
            //路由
            router.push('/admin/' + command)
        }
    }

    const dialogResetPasswordVisible = ref(false)

    const adminPasswordDTO = ref({
        oldPassword : '',
        newPassword : ''
    })

    const resetForm = ref()
    //自定义确认密码的校验函数
    const rePasswordValid = (rule, value, callback) => {
        if (value == null || value === '') {
            return callback(new Error('请再次确认密码'))
        }
        //响应式对象要：registerData.value才能拿到值
        if (adminPasswordDTO.value.newPassword !== value) {
            return callback(new Error('两次输入密码不一致'))
        }

        callback()
    }
    const rules = ref({
        oldPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 3, max: 16, message: '密码长度必须为3~16位', trigger: 'blur'}
        ],
        newPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 3, max: 16, message: '密码长度必须为3~16位', trigger: 'blur'}
        ],
        reNewPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {validator: rePasswordValid, trigger: 'blur' }
        ]
    })
    const resetPassword = async (formEl) => {
        if (!formEl) return
        await formEl.validate((valid, fields) => {
            if (valid) {
                adminApi.resetPassword(adminPasswordDTO.value).then(result => {
                    if (result.code === 0) {
                        ElMessage.success(result.msg)
                        dialogResetPasswordVisible.value = false
                        tokenStore.removeToken();
                        adminInfoStore.removeAdminInfo();
                        // 跳转到登录
                        router.push('/login')
                    } else {
                        ElMessage.error(result.msg)
                    }
                })
            } else {
                ElMessage.error('表单验证失败');
            }
        })
    }

    const updateAdminInfo = () =>{

        adminApi.updateById(adminUser.value).then(result => {
            if (result.code == 0){
                ElMessage.success(result.msg )
                dialogUpdateAdminInfoVisible.value = false
                getAdminInfo()
            }
            else {
                ElMessage.error(result.msg)
            }
        })

    }

    const handleAvatarSuccess = (result) => {
        adminUser.value.avatar = '/api/pic/'+result.data
    }


    const headers = ref({
        Authorization: tokenStore.token
    })
</script>

<template>
	<!-- element-plus中的容器 -->
	<el-container class="layout-container">
		<!-- 左侧菜单 -->
		<el-aside width="200px">
			<div class="el-aside__logo"></div>
			<!-- element-plus的菜单标签 -->
			<el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff"
					 router>
				<el-menu-item index="/admin">
					<el-icon>
						<Management/>
					</el-icon>
					<span>Admin portal</span>
				</el-menu-item>
				<el-menu-item index="/student">
					<el-icon>
						<Promotion/>
					</el-icon>
					<span>Student management</span>
				</el-menu-item>
                <el-menu-item index="/classInfo">
                    <el-icon>
                        <Promotion/>
                    </el-icon>
                    <span>Class management</span>
                </el-menu-item>
                <el-menu-item index="/chart">
                    <el-icon>
                        <Promotion/>
                    </el-icon>
                    <span>E Chart</span>
                </el-menu-item>
				<el-sub-menu>
					<template #title>
						<el-icon>
							<UserFilled/>
						</el-icon>
						<span>My Account</span>
					</template>
					<el-menu-item index="/user/info">
						<el-icon>
							<User/>
						</el-icon>
						<span>Basic info</span>
					</el-menu-item>
					<el-menu-item index="/user/avatar">
						<el-icon>
							<Crop/>
						</el-icon>
						<span>change avatar</span>
					</el-menu-item>
					<el-menu-item index="/user/resetPassword">
						<el-icon>
							<EditPen/>
						</el-icon>
						<span>reset password</span>
					</el-menu-item>
				</el-sub-menu>
			</el-menu>
		</el-aside>
		<!-- 右侧主区域 -->
		<el-container>
			<!-- 头部区域 -->
			<el-header>
				<div><strong>Backend Management System</strong></div>
				<!-- 下拉菜单 -->
				<!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->
				<el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="adminInfoStore.admin?.avatar || avatar"/>
                        <el-icon>
                            <CaretBottom/>
                        </el-icon>
                    </span>
					<template #dropdown>
						<el-dropdown-menu>
							<el-dropdown-item command="updateAdminInfo" :icon="User">基本资料</el-dropdown-item>
<!--
							<el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
-->
							<el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
							<el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
						</el-dropdown-menu>
					</template>
				</el-dropdown>
			</el-header>
			<!-- 中间区域 -->
			<el-main>
				<!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->
				<router-view></router-view>
			</el-main>
			<!-- 底部区域 -->
			<el-footer>后台管理 ©2025 Created by EasyJava</el-footer>
		</el-container>
	</el-container>

    <el-dialog v-model="dialogUpdateAdminInfoVisible" :title="修改个人信息" width="500" :lock-scroll="false">
    <el-form :model="adminUser">
        <el-form-item label="名字" :label-width="60">
            <el-input v-model="adminUser.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="邮箱" :label-width="60">
            <el-input v-model="adminUser.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="手机号" :label-width="60">
            <el-input v-model="adminUser.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="头像" :label-width="60">
            <el-upload
                class="avatar-uploader"
                action="/api/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :headers="headers">
                <img v-if="adminUser.avatar" :src="adminUser.avatar" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
        </el-form-item>
    </el-form>
    <template #footer>
        <div class="dialog-footer">
            <el-button @click="dialogUpdateAdminInfoVisible = false">取消</el-button>
            <el-button type="primary" @click="updateAdminInfo">
                确认
            </el-button>
        </div>
    </template>
    </el-dialog>
    <el-dialog  v-model="dialogResetPasswordVisible" title="重置密码" width="500" :lock-scroll="false">
    <el-form ref="resetForm" :rules="rules" :model="adminPasswordDTO">
        <el-form-item prop="oldPassword" label="原密码" :label-width="100">
            <el-input v-model="adminPasswordDTO.oldPassword" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="newPassword" label="新密码" :label-width="100">
            <el-input v-model="adminPasswordDTO.newPassword" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="reNewPassword" label="重复新密码" :label-width="100">
            <el-input v-model="adminPasswordDTO.reNewPassword" autocomplete="off"/>
        </el-form-item>
    </el-form>
    <template #footer>
        <div class="dialog-footer">
            <el-button @click="dialogResetPasswordVisible = false">取消</el-button>
            <el-button type="primary" @click="resetPassword(resetForm)">
                确认
            </el-button>
        </div>
    </template>
    </el-dialog>
</template>

<style lang="scss" scoped>
    .layout-container {
        height: 100vh;

        .el-aside {
            background-color: #232323;

            &__logo {
                height: 120px;
                background: url('@/assets/logo.png') no-repeat center / 120px auto;
            }

            .el-menu {
                border-right: none;
            }
        }

        .el-header {
            background-color: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .el-dropdown__box {
                display: flex;
                align-items: center;

                .el-icon {
                    color: #999;
                    margin-left: 10px;
                }

                &:active,
                &:focus {
                    outline: none;
                }
            }
        }

        .el-footer {
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            color: #666;
        }
    }
</style>
