<template>
  <div class="flex flex-col h-screen">
    <!-- Header -->
    <header class="bg-white z-10 shadow-md">
      <div class="container mx-auto flex flex-wrap p-5 flex-col md:flex-row items-center">
        <a class="flex title-font font-medium items-center text-gray-900 mb-4 md:mb-0">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            stroke="currentColor"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            class="w-10 h-10 text-white p-2 bg-indigo-500 rounded-full"
            viewBox="0 0 24 24"
          >
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"></path>
          </svg>
          <span class="ml-3 text-xl">Dw</span>
        </a>
        <nav class="md:mr-auto md:ml-4 md:py-1 md:pl-4 md:border-l md:border-gray-400 flex flex-wrap items-center text-base justify-center">
          <a href="/warehouse" class="mr-5 hover:text-gray-900">首页</a>
          <a href="/inventory" class="mr-5 hover:text-gray-900">库存</a>
          <a href="/order" class="mr-5 hover:text-gray-900">订单</a>
          <button class="icon-button mr-5 hover:text-gray-900" @click="showModal = true">
              <!-- 这里可以放置一个设置图标，例如使用 Font Awesome 图标库 -->
              <i class="iconfont icon-Settings"></i>
          </button>
      </nav>  
        <!-- Logout Button -->
        
      </div>
    </header>
  
    <!-- Main Content (router-view) -->
    <main class="flex-1 overflow-auto">
      <div class="container mx-auto py-4">
        <router-view></router-view>
      </div>
    </main>

    <!-- Setting Modal -->
    <div v-if="showModal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
      <div class="bg-white p-6 rounded shadow-md w-1/3">
        <h2 class="text-xl mb-4">设置</h2>
        <label
          for="appKey"
          class="relative block rounded-md border border-gray-300 shadow-sm focus-within:border-blue-500 focus-within:ring-1 focus-within:ring-blue-500 mb-4"
        >
          <input
            type="text"
            id="appKey"
            v-model="appKey"
            class="peer block w-full rounded-md border-none bg-transparent p-2 placeholder-transparent focus:border-transparent focus:outline-none focus:ring-0"
            placeholder="App Key"
          />
          <span
            class="pointer-events-none absolute left-3 top-2 -translate-y-1/2 bg-white px-1 text-xs text-gray-500 transition-all peer-placeholder-shown:top-1/2 peer-placeholder-shown:text-base peer-focus:top-2 peer-focus:text-xs"
          >
            App Key
          </span>
        </label>
        <label
          for="secret"
          class="relative block rounded-md border border-gray-300 shadow-sm focus-within:border-blue-500 focus-within:ring-1 focus-within:ring-blue-500"
        >
          <input
            type="text"
            id="secret"
            v-model="secret"
            class="peer block w-full rounded-md border-none bg-transparent p-2 placeholder-transparent focus:border-transparent focus:outline-none focus:ring-0"
            placeholder="Secret"
          />
          <span
            class="pointer-events-none absolute left-3 top-2 -translate-y-1/2 bg-white px-1 text-xs text-gray-500 transition-all peer-placeholder-shown:top-1/2 peer-placeholder-shown:text-base peer-focus:top-2 peer-focus:text-xs"
          >
            Secret
          </span>
        </label>
        <div class="flex justify-end mt-4">
          <button @click="showModal = false" class="bg-gray-500 text-white px-4 py-2 rounded mr-2">取消</button>
          <button @click="saveSettings" class="bg-blue-500 text-white px-4 py-2 rounded">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      showModal: false,
      appKey: '',
      secret: ''
    };
  },
  methods: {
    saveSettings() {
      console.log('Saved data:', { appKey: this.appKey, secret: this.secret });
      // 处理保存的数据，例如调用API或更新状态
      this.showModal = false;
    },
    logout() {
      // 处理退出登录逻辑，例如清除认证信息或重定向到登录页面
      console.log('Logged out');
    }
  }
};
</script>

<style scoped>
</style>
