<template>
  <div class="container mx-auto p-4">
    <div class="flex justify-between items-center mb-4">
      <div class="flex items-center">
        <!-- 返回按钮 -->
        <a
          @click.prevent="handleBack"
          class="inline-block rounded-full border border-indigo-600 p-3 text-indigo-600 hover:bg-indigo-600 hover:text-white focus:outline-none focus:ring active:bg-indigo-500 mr-4"
        >
          <span class="sr-only">返回</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M10 19l-7-7m0 0l7-7m-7 7h18"
            />
          </svg>
        </a>
        <h1 class="text-2xl font-bold">{{ articleNumber }}</h1>
      </div>
      <div class=" flex justify-end">
        <!-- 操作按钮 -->
        <span class="inline-flex overflow-hidden rounded-md border bg-white shadow-sm">
          <button
            @click="handleEdit"
            class="inline-block border-e p-3 text-gray-700 hover:bg-gray-50 focus:relative"
            title="Edit Product"
            :disabled="selectedRecords.length === 0"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="h-4 w-4"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
              />
            </svg>
          </button>
          <button
            class="inline-block p-3 text-gray-700 hover:bg-gray-50 focus:relative"
            title="Delete Product"
            @click="handleDelete"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="h-4 w-4"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"
              />
            </svg>
          </button>
        </span>
      </div>
    </div>
    <!-- 记录列表 -->
    <Transition
      enter-active-class="transition ease-out duration-300"
      enter-from-class="opacity-0 transform scale-95"
      enter-to-class="opacity-100 transform scale-100"
      leave-active-class="transition ease-in duration-200"
      leave-from-class="opacity-100 transform scale-100"
      leave-to-class="opacity-0 transform scale-95"
    >
      <div v-show="records.length" class="overflow-x-auto overflow-y-auto">
        <div style="max-height: 400px;">
          <table class="min-w-full divide-y-2 divide-gray-200 bg-white text-sm">
            <thead>
              <tr>
                <th class="sticky inset-y-0 start-0 bg-white px-4 py-2 text-left">
                  <label for="SelectAll" class="sr-only">Select All</label>
                  <input 
                    type="checkbox" 
                    id="SelectAll" 
                    class="size-5 rounded border-gray-300" 
                    @change="selectAll" 
                    :checked="selectedRecords.length === records.length"
                  />
                </th>
                <!-- <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">库存ID</th> -->
                <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">SKU ID</th>
                <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">尺码</th>
                <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">成本</th>
                <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">批次号</th>
                <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">入库</th>
                <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">出库</th>
                <th class="whitespace-nowrap px-4 py-2 font-medium text-gray-900 text-left">备注</th>
              </tr>
            </thead>

            <tbody class="divide-y divide-gray-200">
              <tr v-for="record in records" :key="record.inventoryId" class="bg-white hover:bg-gray-50">
                <td class="sticky inset-y-0 start-0 bg-white px-4 py-2">
                  <label class="sr-only" :for="'Row' + record.inventoryId">Select Row</label>
                  <input 
                    type="checkbox" 
                    :id="'Row' + record.inventoryId" 
                    class="size-5 rounded border-gray-300" 
                    :value="record" 
                    v-model="selectedRecords"
                  />
                </td>
<!--                 <td class="whitespace-nowrap px-4 py-2 font-medium text-gray-900">{{ record.inventoryId }}</td> -->
                <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ record.skuId }}</td>
                <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ record.size }}</td>
                <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ record.cost }}</td>
                <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ record.batchNumber }}</td>
                <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ formatDate(record.inboundTime) }}</td>
                <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ record.outboundTime ? formatDate(record.outboundTime) : '-' }}</td>
                <td class="whitespace-nowrap px-4 py-2 text-gray-700">{{ record.notes || '-' }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 分页 -->
        <div class="fixed bottom-4 right-4 inline-flex items-center justify-center rounded bg-blue-600 py-1 px-3 text-white shadow-lg">
          <a
            href="#"
            @click.prevent="changePage(-1)"
            class="inline-flex items-center justify-center rtl:rotate-180"
            :class="{ 'cursor-not-allowed opacity-50': current === 1 }"
            :disabled="current === 1"
          >
            <span class="sr-only">Prev Page</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" viewBox="0 0 20 20" fill="currentColor">
              <path
                fill-rule="evenodd"
                d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                clip-rule="evenodd"
              />
            </svg>
          </a>

          <span class="h-4 w-px bg-white/25 mx-2" aria-hidden="true"></span>

          <div>
            <label for="PaginationPage" class="sr-only">Page</label>
            <input
              type="number"
              class="h-8 w-12 rounded border-none bg-transparent p-0 text-center text-xs font-medium [-moz-appearance:_textfield] focus:outline-none focus:ring-inset focus:ring-white [&::-webkit-inner-spin-button]:m-0 [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:m-0 [&::-webkit-outer-spin-button]:appearance-none"
              min="1"
              :max="pages"
              :value="current"
              id="PaginationPage"
              @change="goToPage($event.target.value)"
            />
          </div>

          <span class="h-4 w-px bg-white/25 mx-2"></span>

          <a
            href="#"
            @click.prevent="changePage(1)"
            class="inline-flex items-center justify-center rtl:rotate-180"
            :class="{ 'cursor-not-allowed opacity-50': current === pages }"
            :disabled="current === pages"
          >
            <span class="sr-only">Next Page</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" viewBox="0 0 20 20" fill="currentColor">
              <path
                fill-rule="evenodd"
                d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                clip-rule="evenodd"
              />
            </svg>
          </a>
        </div>

      </div>
    </Transition>

    <!-- 编辑信息的弹窗 -->
    <transition
      enter-active-class="transition ease-out duration-300"
      enter-from-class="opacity-0 transform scale-95"
      enter-to-class="opacity-100 transform scale-100"
      leave-active-class="transition ease-in duration-200"
      leave-from-class="opacity-100 transform scale-100"
      leave-to-class="opacity-0 transform scale-95"
    >
      <div v-show="editPopupVisible" class="fixed inset-0 z-50 flex items-center justify-center">
        <div class="mx-auto max-w-sm rounded-2xl border border-blue-100 bg-white p-4 shadow-lg sm:p-6 lg:p-8 relative" role="alert">
          <!-- 关闭按钮 -->
          <button @click="editPopupVisible = false;" class="absolute top-2 right-2 text-gray-400 hover:text-gray-600 focus:outline-none">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>

          <!-- 修改信息表单 -->
          <div class="flex flex-col items-center gap-4">
            <h2 class="text-lg font-semibold text-gray-700">修改信息</h2>

            <input type="text" v-model="currentRecord.cost" placeholder="成本" class="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <textarea v-model="currentRecord.notes" placeholder="备注" class="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"></textarea>

            <div class="flex gap-4 mt-4">
              <button @click="closeEditPopup" class="w-full rounded-lg bg-gray-500 px-5 py-3 text-center text-sm font-semibold text-white hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2">
                取消
              </button>
              <button @click="submitEdit" @keyup.enter="submitEdit" class="w-full rounded-lg bg-blue-500 px-5 py-3 text-center text-sm font-semibold text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
                确认
              </button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getInventoryDetailListService, updateRecordService, deleteRecordService } from '@/api/inventoryApi.js';

const route = useRoute();
const router = useRouter();
const articleNumber = ref(null);
const records = ref([]);
const total = ref(0);
const size = ref(8);
const current = ref(1);
const pages = ref(10);
const editPopupVisible = ref(false);
const selectedRecords = ref([]);
const currentRecord = ref({
  cost: '',
  notes: ''
});

// 获取库存详情列表
const getDetailList = async (currentPage, pageSize, articleNumber) => {
  const params = {
    page: currentPage,
    pageSize: pageSize,
    articleNumber: articleNumber
  };
  const result = await getInventoryDetailListService(params);

  records.value = result.data.records;
  total.value = result.data.total;
  current.value = result.data.current;
  pages.value = result.data.pages;

};

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '-';
  const date = new Date(dateString);
  return date.toLocaleString();
};

// 分页切换
const changePage = async (direction) => {
  const newPage = current.value + direction;
  if (newPage >= 1 && newPage <= pages.value) {
    current.value = newPage;
    await getDetailList(current.value, size.value, articleNumber.value);
  }
};

// 输入页码切换
const goToPage = async (page) => {
  const pageNumber = parseInt(page, 10);
  if (pageNumber >= 1 && pageNumber <= pages.value) {
    current.value = pageNumber;
    await getDetailList(current.value, size.value, articleNumber.value);
  }
};


// 返回上一页
const handleBack = () => {
  router.push('/inventory');
};

// 全选或取消全选
const selectAll = (event) => {
  if (event.target.checked) {
    selectedRecords.value = [...records.value];
  } else {
    selectedRecords.value = [];
  }
}

// 编辑操作
const handleEdit = () => {
  if (selectedRecords.value.length > 0) {
    selectedRecords.value.forEach(record => {
      delete record.size;
    });
    // 重置 currentRecord
    currentRecord.value = {
      cost: '',
      notes: ''
    };
    editPopupVisible.value = true;
  }
};

// 提交编辑
const submitEdit = async () => {
  const updatedRecords = selectedRecords.value.map(record => ({
    ...record,
    cost: currentRecord.value.cost || record.cost,
    notes: currentRecord.value.notes || record.notes
  }));

  await updateRecordService(updatedRecords);

  editPopupVisible.value = false;
  selectedRecords.value = [];
  await getDetailList(current.value, size.value, articleNumber.value);
};

// 关闭编辑弹窗
const closeEditPopup = () => {
  editPopupVisible.value = false;
};

// 删除操作
const handleDelete = async () => {
  if (selectedRecords.value.length > 0) {
    await deleteRecordService(selectedRecords.value.map(record => record.inventoryId));
    selectedRecords.value = [];
    await getDetailList(current.value, size.value, articleNumber.value);
  }
};

// 组件挂载时获取数据
onMounted(async () => {
  articleNumber.value = route.query.articleNumber;
  await getDetailList(current.value, size.value, articleNumber.value);
});
</script>

<style scoped>

</style>
