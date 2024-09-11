<template>
  <div class="p-2">
    <!-- 上容器 -->
    <div class="flex flex-col lg:flex-row gap-4 mb-4">
      <!-- 右容器（统计框） -->
      <div class="grid grid-cols-2 gap-4 lg:w-2/5">
        <div class="flex flex-col rounded-lg bg-blue-50 px-4 py-8 text-center">
          <dt class="order-last text-lg font-medium text-gray-500">总收入</dt>
          <dd class="text-4xl font-extrabold text-blue-600 md:text-4xl">{{ totalIncome }}</dd>
        </div>
        <div class="flex flex-col rounded-lg bg-blue-50 px-4 py-8 text-center">
          <dt class="order-last text-lg font-medium text-gray-500">总数量</dt>
          <dd class="text-4xl font-extrabold text-blue-600 md:text-4xl">{{ total }}</dd>
        </div>
      </div>
      <!-- 左容器 -->
      <div class="flex-1 lg:w-3/5">
        <!-- 输入框行 -->
        <div class="flex flex-wrap gap-4 mb-4">
          <label for="articleNumber"
                 class="relative block overflow-hidden rounded-md border border-gray-200 px-3 pt-3 shadow-sm focus-within:border-blue-600 focus-within:ring-1 focus-within:ring-blue-600 flex-1">
            <input type="text" id="articleNumber" placeholder="货号" v-model="articleNumber"
                   class="peer h-8 w-full border-none bg-transparent p-0 placeholder-transparent focus:border-transparent focus:outline-none focus:ring-0 sm:text-sm"/>
            <span
              class="absolute start-3 top-3 -translate-y-1/2 text-xs text-gray-700 transition-all peer-placeholder-shown:top-1/2 peer-placeholder-shown:text-sm peer-focus:top-3 peer-focus:text-xs">
                货号
              </span>
          </label>
          <label for="size"
                 class="relative block overflow-hidden rounded-md border border-gray-200 px-3 pt-3 shadow-sm focus-within:border-blue-600 focus-within:ring-1 focus-within:ring-blue-600 flex-1">
            <input type="text" id="size" placeholder="尺寸" v-model="size"
                   class="peer h-8 w-full border-none bg-transparent p-0 placeholder-transparent focus:border-transparent focus:outline-none focus:ring-0 sm:text-sm"/>
            <span
              class="absolute start-3 top-3 -translate-y-1/2 text-xs text-gray-700 transition-all peer-placeholder-shown:top-1/2 peer-placeholder-shown:text-sm peer-focus:top-3 peer-focus:text-xs">
                尺寸
              </span>
          </label>
        </div>
        <!-- 时间选择器行 -->
        <div class="flex flex-wrap gap-4 mb-4">
          <input type="date" v-model="startTime" class="flex-1 p-2 border rounded"/>
          <input type="date" v-model="endTime" class="flex-1 p-2 border rounded"/>
        </div>
        <!-- 按钮行 -->
        <div class="flex gap-4">
          <button class="flex-1 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700" @click="searchOrders">
            查询
          </button>
          <button class="flex-1 bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700" @click="resetFilters">
            重置
          </button>
          <button class="flex-1 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700" @click="getRealTime">
            导入对账单
          </button>
        </div>
      </div>
    </div>
    <!-- 下容器 -->
    <div class="rounded-lg border border-gray-200">
      <div class="overflow-x-auto rounded-t-lg" style="max-height: 50%; overflow-y: auto;">
        <table class="min-w-full divide-y-2 divide-gray-200 bg-white text-sm">
          <thead class="ltr:text-left rtl:text-right">
          <tr>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">订单号</th>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">订单类型</th>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">货号</th>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">尺码</th>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">售价</th>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">结算费用</th>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">实际结算时间</th>
            <th class="sticky top-0 bg-white whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">SKU ID</th>
          </tr>
          </thead>

          <tbody class="divide-y divide-gray-200">
          <tr v-for="order in records" :key="order.orderNo">
            <td class="whitespace-nowrap px-4 py-2 font-medium text-gray-900">{{ order.orderNo }}</td>
            <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ order.orderType }}</td>
            <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ order.articleNumber }}</td>
            <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ order.props }}</td>
            <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ order.skuPrice }}</td>
            <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ order.stmtFee }}</td>
            <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ new Date(order.realStmtTime).toLocaleString() }}</td>
            <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ order.skuId }}</td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="flex justify-end mt-4">
        <div class="inline-flex items-center justify-center rounded bg-blue-600 py-1 text-white">
          <a href="#" @click.prevent="prevPage" class="inline-flex size-8 items-center justify-center rtl:rotate-180">
            <span class="sr-only">Prev Page</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="size-3" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd"
                    d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                    clip-rule="evenodd"/>
            </svg>
          </a>

          <span class="h-4 w-px bg-white/25" aria-hidden="true"></span>

          <div>
            <label for="PaginationPage" class="sr-only">Page</label>

            <input type="number"
                   class="h-8 w-12 rounded border-none bg-transparent p-0 text-center text-xs font-medium [-moz-appearance:_textfield] focus:outline-none focus:ring-inset focus:ring-white [&::-webkit-inner-spin-button]:m-0 [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:m-0 [&::-webkit-outer-spin-button]:appearance-none"
                   min="1" :value="current" @change="handlePageChange" id="PaginationPage"/>
          </div>

          <span class="h-4 w-px bg-white/25"></span>

          <a href="#" @click.prevent="nextPage" class="inline-flex size-8 items-center justify-center rtl:rotate-180">
            <span class="sr-only">Next Page</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="size-3" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd"
                    d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                    clip-rule="evenodd"/>
            </svg>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElNotification, ElLoading } from 'element-plus'
import { getOrderListService, getRealTimeService } from '@/api/orderApi.js';

// 定义响应式变量
const records = ref([]);
const current = ref(1);
const pageSize = ref(10);
const total = ref(null);
const totalIncome = ref(0);
const pages = ref(null);
const articleNumber = ref(null);
const size = ref(null);
const startTime = ref(null);
const endTime = ref(null);

// 获取今天的日期
const getTodayDate = () => {
  const today = new Date();
  const yyyy = today.getFullYear();
  const mm = String(today.getMonth() + 1).padStart(2, '0'); // Months are zero-based
  const dd = String(today.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
};

// 获取一个月前的日期
const getOneMonthAgoDate = () => {
  const today = new Date();
  today.setMonth(today.getMonth() - 1);
  const yyyy = today.getFullYear();
  const mm = String(today.getMonth() + 1).padStart(2, '0'); // Months are zero-based
  const dd = String(today.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
};

// 在组件挂载时设置默认值
onMounted(async () => {
  endTime.value = getTodayDate();
  startTime.value = getOneMonthAgoDate();
  await getOrderList();
});

// 获取订单列表
const getOrderList = async () => {
  const params = {
    page: current.value,
    pageSize: pageSize.value,
    articleNumber: articleNumber.value,
    size: size.value,
    startTime: startTime.value,
    endTime: endTime.value,
  };
  const result = await getOrderListService(params);
  records.value = result.data.records;
  total.value = result.data.total;
  pages.value = result.data.pages;
  totalIncome.value = result.data.totalIncome;
};

// 搜索处理
const searchOrders = () => {
  current.value = 1;
  getOrderList();
};

// 分页处理
const prevPage = () => {
  if (current.value > 1) {
    current.value--;
    getOrderList();
  }
};

const nextPage = () => {
  if (current.value < pages.value) {
    current.value++;
    getOrderList();
  }
};

const handlePageChange = (event) => {
  const pageValue = parseInt(event.target.value, 10);
  if (pageValue >= 1 && pageValue <= pages.value) {
    current.value = pageValue;
    getOrderList();
  }
};

const resetFilters = () => {
  articleNumber.value = null;
  size.value = null;
  startTime.value = getOneMonthAgoDate();
  endTime.value = getTodayDate();
  current.value = 1;
  getOrderList();
};

const getRealTime = async () =>{
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '导入中，请勿关闭',
    background: 'rgba(0, 0, 0, 0.7)',
  });

  const params = {
    startTime: startTime.value,
    endTime: endTime.value,
  }
  try {
    const result = await getRealTimeService(params);
    if (result.code === 0) {
      ElNotification({
        title: '导入成功',
        dangerouslyUseHTMLString: true,
        message: result.data,
      });
      getOrderList();
    } else {
      ElNotification({
        title: '导入失败',
        message: result.message,
        type: 'error',
      });
    }
  } catch (error) {
    ElNotification({
      title: '导入失败',
      message: '发生错误，请稍后再试',
      type: 'error',
    });
  } finally {
    loadingInstance.close();
  }
}
</script>

<style scoped>
/* 你可以添加自定义样式 */
</style>
