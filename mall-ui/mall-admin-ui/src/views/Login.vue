<script setup>
import {ref} from "vue";
import {User, Lock} from "@element-plus/icons-vue";
import adminApi from "@/api/admin.js";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {useTokenStore} from "@/store/token.js";

const tokenStore = useTokenStore()

const router = useRouter()


const user = ref({

    username: '',
    password: ''
})

const login = () => {
    adminApi.login(user.value).then(result => {

        if (result.code == 0){
            ElMessage.success(result.msg);
            tokenStore.setToken(result.data)
            router.push('/')
        }
        else {
            ElMessage.error(result.msg)
        }
    })
}


</script>

<template>
    <div class="login-bg">
        <!-- 登录表单 -->
        <el-form class="form-login" size="large" autocomplete="off" :model="user" :rules="rules">
            <el-form-item>
                <h1>Login</h1>
            </el-form-item>
            <el-form-item prop="name">
                <el-input :prefix-icon="User" placeholder="Please enter user name" v-model="user.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input name="password" :prefix-icon="Lock" type="password" placeholder="Please enter password"
                          v-model="user.password"></el-input>
            </el-form-item>
            <el-form-item class="flex">
                <div class="flex">
                    <el-checkbox>Remember me</el-checkbox>
                    <el-link type="primary" :underline="false">Forget Password?</el-link>
                </div>
            </el-form-item>
            <!-- 登录按钮 -->
            <el-form-item>
                <el-button class="button" type="primary" auto-insert-space @click="login">Login</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<style scoped>
.login-bg {
    height: 100%;
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    background-size: cover;
}

.form-login {
    width: 280px;
    padding: 20px;
    position: absolute;
    top: 20%;
    left: calc(50% - 150px);
    background-color: #FFF;
    box-shadow: 10px 10px 30px #000;
}

</style>