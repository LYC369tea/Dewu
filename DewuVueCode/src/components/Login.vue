<template>

    <section class="relative flex flex-wrap lg:h-screen lg:items-center ">
      <div class="w-full px-4 py-12 sm:px-6 sm:py-16 lg:w-1/2 lg:px-8 lg:py-24">
        <div class="mx-auto max-w-lg text-center">
          <h1 class="text-2xl font-bold sm:text-3xl">Welcome to DeWu System!</h1>
    
          <p class="mt-4 text-gray-500">
            
            登录以开始您的旅程
          </p>
        </div>
    
        <form  :model="loginData" class="mx-auto mb-0 mt-8 max-w-md space-y-4">
          <div>
            <label for="usernameOrEmail" class="sr-only">usernameOrEmail</label>
    
            <div class="relative" prop="usernameOrEmail">
              <input
                type="usernameOrEmail"
                class="w-full rounded-lg border-gray-200 p-4 pe-12 text-sm shadow-sm"
                placeholder="用户名" 
                v-model="loginData.usernameOrEmail"
              />
    
              <span class="absolute inset-y-0 end-0 grid place-content-center px-4">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="size-4 text-gray-400"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207"
                  />
                </svg>
              </span>
            </div>
          </div>
    
          <div>
            <label for="password" class="sr-only">Password</label>
    
            <div class="relative" prop="password">
              <input
                type="password"
                class="w-full rounded-lg border-gray-200 p-4 pe-12 text-sm shadow-sm"
                placeholder="密码"
                name="password" 
                v-model="loginData.password" 
                @keyup.enter.native="() => login()"
              />
    
              <span class="absolute inset-y-0 end-0 grid place-content-center px-4">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="size-4 text-gray-400"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  />
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                  />
                </svg>
              </span>
            </div>
          </div>
    
          <div class="flex items-center justify-between">
            <p class="text-sm text-gray-500">
              No account?
              <a class="underline">Sign up</a>
            </p>
    
            <el-button class="button" size="large" color="#303133" @click="() => login()">登录</el-button> <!-- 在这里传递 router -->

          </div>
        </form>
      </div>
    
      <div class="hidden lg:block relative h-64 w-full sm:h-96 lg:h-full lg:w-1/2">
        <img
          alt=""
          src="../assets/login.jpg"
          class="absolute inset-0 h-full w-full object-cover"
        />
      </div>

    </section>
  </template>
<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref, onMounted, onUnmounted } from 'vue';
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/token.js'
import {  userLoginService  } from '@/api/userApi.js';

const router = useRouter(); // 获取 router 实例
//调用useTokenStore得到状态
const tokenStore = useTokenStore();

// 登录表单的数据模型
const loginData = ref({
    usernameOrEmail: '',
    password: ''
})

/* // 登录表单的校验规则
const loginDataRules = ref({
    usernameOrPhone: [
      { required: true, message: '请输入用户名或者手机号码', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ]
  })
 */
// 用于登录的事件函数
const login = async () => {
    let result = await userLoginService(loginData.value)
            if (result.code === 0) {
                ElMessage.success('登录成功');
                // 处理注册成功逻辑
                //保存token
                tokenStore.setToken(result.data)
                router.push('/warehouse')
            } else {
                // 处理其他结果，但不是异常
                ElMessage.error(result.message ? result.message : '登录失败');
            }
}
</script>
<style lang="scss" scoped>


</style>