<template>
    <!--
      Heads up! 👋
    
      Plugins:
        - @tailwindcss/forms
    -->
    
    <section class="bg-gray-100 ">
      <div class="mx-auto max-w-screen-xl px-4 py-16 sm:px-6 lg:px-8 max-w-2xl">
        <div class="rounded-lg bg-white p-8 shadow-lg lg:col-span-3 lg:p-12">
            <p class="font-sans ... text-2xl text-center mb-4">注册</p>
            <form @submit.prevent="submitForm" class="space-y-4">
                <div>
                <input
                    class="w-full rounded-lg border-gray-200 p-3 text-sm"
                    placeholder="用户名 (4-16位)"
                    type="text"
                    id="username"
                    v-model="username"
                    @blur="validateUsername"
                />
                <p v-if="errors.username" class="text-red-500 text-xs mt-1">{{ errors.username }}</p>
                </div>

                <div>
                <input
                    class="w-full rounded-lg border-gray-200 p-3 text-sm"
                    placeholder="密码 (8-32位)"
                    type="password"
                    id="password"
                    v-model="password"
                    @blur="validatePassword"
                />
                <p v-if="errors.password" class="text-red-500 text-xs mt-1">{{ errors.password }}</p>
                </div>

                <div>
                <input
                    class="w-full rounded-lg border-gray-200 p-3 text-sm"
                    placeholder="确认密码"
                    type="password"
                    id="confirmPassword"
                    v-model="confirmPassword"
                    @blur="validateConfirmPassword"
                />
                <p v-if="errors.confirmPassword" class="text-red-500 text-xs mt-1">{{ errors.confirmPassword }}</p>
                </div>

                <div class="flex items-center space-x-2">
                <input
                    class="flex-grow rounded-lg border-gray-200 p-3 text-sm"
                    placeholder="邮箱"
                    type="email"
                    id="email"
                    v-model="email"
                    @blur="validateEmail"
                />
                <button
                    type="button"
                    @click="sendVerificationCode"
                    class="rounded-lg bg-gray-200 px-4 py-3 font-medium text-black hover:bg-gray-300"
                >
                    发送验证码
                </button>
                </div>
                <p v-if="errors.email" class="text-red-500 text-xs mt-1">{{ errors.email }}</p>

                <div class="mt-4">
                <button
                    type="submit"
                    class="inline-block w-full rounded-lg bg-black px-6 py-3 font-medium text-white sm:w-auto"
                >
                    注册
                </button>
                </div>
            </form>

          </div>
      </div>
    </section>
</template>

<script setup>
import { ref, reactive } from 'vue'

const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const email = ref('')

const errors = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
})

const validateUsername = () => {
  if (username.value.length < 4 || username.value.length > 16) {
    errors.username = '用户名必须是4-16位'
  } else {
    errors.username = ''
  }
}

const validatePassword = () => {
  if (password.value.length < 8 || password.value.length > 32) {
    errors.password = '密码必须是8-32位'
  } else {
    errors.password = ''
  }
}

const validateConfirmPassword = () => {
  if (password.value !== confirmPassword.value) {
    errors.confirmPassword = '两次输入的密码不一致'
  } else {
    errors.confirmPassword = ''
  }
}

const validateEmail = () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email.value)) {
    errors.email = '请输入有效的邮箱地址'
  } else {
    errors.email = ''
  }
}

const sendVerificationCode = () => {
  // 实现发送验证码的逻辑
  console.log('发送验证码到', email.value)
}

const submitForm = () => {
  validateUsername()
  validatePassword()
  validateConfirmPassword()
  validateEmail()

  if (!Object.values(errors).some(error => error !== '')) {
    // 如果没有错误，提交表单
    console.log('表单提交', {
      username: username.value,
      password: password.value,
      email: email.value,
    })
  }
}
</script>