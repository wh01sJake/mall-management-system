<script setup>
    import {
        Management,
        Promotion,
        UserFilled,
        User,
        Crop,
        EditPen,
        SwitchButton,
        CaretBottom,
        Document,
        DataAnalysis,
        TrendCharts,
        Menu as MenuIcon
    } from '@element-plus/icons-vue'
    import avatar from '@/assets/default.png'

    // Function called when menu item is clicked
    import {useRouter} from 'vue-router'

    const router = useRouter();
    import {ElMessage, ElMessageBox} from 'element-plus'

    import {useAdminInfoStore} from '@/store/adminInfo.js'
    import adminApi from "@/api/admin.js";
    import {useTokenStore} from "@/store/token.js";
    import {ref} from "vue";

    const adminInfoStore = useAdminInfoStore()
    const tokenStore = useTokenStore()

    // Mobile menu state
    const isMobileMenuOpen = ref(false)
    const isMobile = ref(false)

    // Check if mobile
    const checkMobile = () => {
        isMobile.value = window.innerWidth <= 768
        if (!isMobile.value) {
            isMobileMenuOpen.value = false
        }
    }

    // Toggle mobile menu
    const toggleMobileMenu = () => {
        isMobileMenuOpen.value = !isMobileMenuOpen.value
    }


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
        // Check command
        if (command === 'logout') {
            // Logout
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
            // Route navigation
            router.push('/admin/' + command)
        }
    }

    const dialogResetPasswordVisible = ref(false)

    const adminPasswordDTO = ref({
        oldPassword : '',
        newPassword : ''
    })

    const resetForm = ref()
    // Custom password confirmation validation function
    const rePasswordValid = (rule, value, callback) => {
        if (value == null || value === '') {
            return callback(new Error('Please confirm password again'))
        }
        // For reactive objects: use registerData.value to get the value
        if (adminPasswordDTO.value.newPassword !== value) {
            return callback(new Error('Passwords do not match'))
        }

        callback()
    }
    const rules = ref({
        oldPassword: [
            {required: true, message: 'Please enter password', trigger: 'blur'},
            {min: 3, max: 16, message: 'Password length must be 3-16 characters', trigger: 'blur'}
        ],
        newPassword: [
            {required: true, message: 'Please enter password', trigger: 'blur'},
            {min: 3, max: 16, message: 'Password length must be 3-16 characters', trigger: 'blur'}
        ],
        reNewPassword: [
            {required: true, message: 'Please enter password', trigger: 'blur'},
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
                        // Redirect to login
                        router.push('/login')
                    } else {
                        ElMessage.error(result.msg)
                    }
                })
            } else {
                ElMessage.error('Form validation failed');
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
        adminUser.value.avatar = result.data
    }


    const headers = ref({
        Authorization: tokenStore.token
    })
</script>

<template>
	<!-- Element Plus container -->
	<el-container class="layout-container">
		<!-- Left sidebar menu -->
		<el-aside width="200px">
			<div class="el-aside__logo"></div>
			<!-- Element Plus menu component -->
			<el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff"
					 router>
				<el-menu-item index="/admin">
					<el-icon>
						<Management/>
					</el-icon>
					<span>Admin portal</span>
				</el-menu-item>
				<el-menu-item index="/product">
					<el-icon>
						<Promotion/>
					</el-icon>
					<span>Product Management</span>
				</el-menu-item>
                <el-menu-item index="/category">
                    <el-icon>
                        <Promotion/>
                    </el-icon>
                    <span>Category Management</span>
                </el-menu-item>
                <el-menu-item index="/order">
                    <el-icon>
                        <Document/>
                    </el-icon>
                    <span>Order Management</span>
                </el-menu-item>
                <el-menu-item index="/order-chart">
                    <el-icon>
                        <TrendCharts/>
                    </el-icon>
                    <span>Order Statistics</span>
                </el-menu-item>
                <el-menu-item index="/chart">
                    <el-icon>
                        <DataAnalysis/>
                    </el-icon>
                    <span>Category Chart</span>
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
		<!-- Right main area -->
		<el-container>
			<!-- Header area -->
			<el-header>
				<div><strong>Backend Management System</strong></div>
				<!-- Dropdown menu -->
				<!-- command: Triggered when item is clicked, can declare a parameter in event function to receive corresponding command -->
				<el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="adminInfoStore.admin?.avatar || avatar"/>
                        <el-icon>
                            <CaretBottom/>
                        </el-icon>
                    </span>
					<template #dropdown>
						<el-dropdown-menu>
							<el-dropdown-item command="updateAdminInfo" :icon="User">Profile</el-dropdown-item>
<!--
							<el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
-->
							<el-dropdown-item command="resetPassword" :icon="EditPen">Reset Password</el-dropdown-item>
							<el-dropdown-item command="logout" :icon="SwitchButton">Logout</el-dropdown-item>
						</el-dropdown-menu>
					</template>
				</el-dropdown>
			</el-header>
			<!-- Main content area -->
			<el-main>
				<!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->
				<router-view></router-view>
			</el-main>
			<!-- Footer area -->
			<el-footer>Admin Management ©2025 Created by EasyJava</el-footer>
		</el-container>
	</el-container>

    <el-dialog v-model="dialogUpdateAdminInfoVisible" title="Update Profile" width="500" :lock-scroll="false">
    <el-form :model="adminUser">
        <el-form-item label="Name" :label-width="60">
            <el-input v-model="adminUser.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Email" :label-width="60">
            <el-input v-model="adminUser.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Phone" :label-width="60">
            <el-input v-model="adminUser.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Avatar" :label-width="60">
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
            <el-button @click="dialogUpdateAdminInfoVisible = false">Cancel</el-button>
            <el-button type="primary" @click="updateAdminInfo">
                Confirm
            </el-button>
        </div>
    </template>
    </el-dialog>
    <el-dialog  v-model="dialogResetPasswordVisible" title="Reset Password" width="500" :lock-scroll="false">
    <el-form ref="resetForm" :rules="rules" :model="adminPasswordDTO">
        <el-form-item prop="oldPassword" label="Old Password" :label-width="100">
            <el-input v-model="adminPasswordDTO.oldPassword" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="newPassword" label="New Password" :label-width="100">
            <el-input v-model="adminPasswordDTO.newPassword" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="reNewPassword" label="Confirm Password" :label-width="100">
            <el-input v-model="adminPasswordDTO.reNewPassword" autocomplete="off"/>
        </el-form-item>
    </el-form>
    <template #footer>
        <div class="dialog-footer">
            <el-button @click="dialogResetPasswordVisible = false">Cancel</el-button>
            <el-button type="primary" @click="resetPassword(resetForm)">
                Confirm
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
