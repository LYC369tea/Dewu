<template>
  <div class="container mx-auto p-5">
    <!-- 输入框 -->
    <div class="flex flex-col md:flex-row gap-4 mb-4">
      <!-- 第一个输入框 -->
      <div class="flex-1 flex">
        <input
          type="text"
          id="barcodeInput"
          v-model="barcodeInput"
          ref="barcodeInputRef"
          @input="debouncedHandleScan"
          @keyup.enter="handleScan"
          class="flex-grow px-4 py-2 border rounded-l-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="条形码（扫描或粘贴，不要手动输入）"
        />
        <button
          id="scanButton"
          class="inline-block rounded bg-indigo-600 px-8 py-3 text-sm font-medium text-white transition hover:scale-110 hover:shadow-xl focus:outline-none focus:ring active:bg-indigo-500"
          @click="handleScan"
        >
          扫描
        </button>
      </div>
      <!-- 第二个输入框和按钮 -->
      <div class="flex-1 flex">
        <input
          type="text"
          class="flex-grow px-4 py-2 border rounded-l-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="数据库没有的数据，统一输入货号导入"
          v-model="articleNumberInput"
        />
        <button
          class="inline-block rounded bg-indigo-600 px-8 py-3 text-sm font-medium text-white transition hover:scale-110 hover:shadow-xl focus:outline-none focus:ring active:bg-indigo-500"
          @click="saveArticleNumber"
        >
          导入
        </button>
      </div>
    </div>

    <!-- 表格 -->
    <Transition
      enter-active-class="transition ease-out duration-300"
      enter-from-class="opacity-0 transform scale-95"
      enter-to-class="opacity-100 transform scale-100"
      leave-active-class="transition ease-in duration-200"
      leave-from-class="opacity-100 transform scale-100"
      leave-to-class="opacity-0 transform scale-95"
    >
      <div v-if="ShoeList.length" class="overflow-x-auto overflow-y-auto">
        <table class="w-full table-fixed text-xs md:text-sm text-left text-gray-500">
          <thead class="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th class="w-1/8 px-1 py-1"></th>
              <th class="w-1/8 px-1 py-1 hidden md:table-cell">skuId</th>
              <th class="w-1/8 px-1 py-1">货号</th>
              <th class="w-1/8 px-1 py-1 hidden md:table-cell">品牌</th>
              <th class="w-1/8 px-3 py-1">尺码</th>
              <th class="w-1/8 px-1 py-1">条形码</th>
              <th class="w-1/8 px-4 py-1">出价</th>
              <th class="w-1/8 px-1 py-1"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(shoe, index) in ShoeList" :key="shoe.barcode" class="bg-white border-b hover:bg-gray-50">
              <td class="px-1 py-1">
                <img :src="shoe.spuLogo" alt="商品图片" class="h-8 w-20 object-cover" />
              </td>
              <td class="px-1 py-1 hidden md:table-cell word-break-all">{{ shoe.skuId }}</td>
              <td class="px-1 py-1 break-all whitespace-normal">{{ shoe.articleNumber }}</td>
              <td class="px-1 py-1 hidden md:table-cell word-break-all">{{ shoe.brandName }}</td>
              <td class="px-4 py-1">{{ shoe.size }}</td>
              <td class="px-1 py-1 break-all whitespace-normal">{{ shoe.barcode }}</td>
              <td class="px-2 py-1">
                <button
                  @click="fetchCurrentPrice(shoe)"
                  class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded text-xs"
                >
                  {{ shoe.currentPrice ? shoe.currentPrice : '查看' }}
                </button>
              </td>
              <td>
                <button
                  @click="removeShoe(index)"
                  class="bg-slate-300 hover:bg-slate-500 text-white font-bold py-1 px-2 rounded text-xs"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    class="h-4 w-4"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                    />
                  </svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="flex justify-between items-center">
          <!-- 显示 shoeList 的数量 -->
          <span class="text-xl font-medium">Total : {{ ShoeList.length }}</span>

          <!-- 右侧按钮 -->
          <div class="flex justify-end">
            <button
              class="group relative inline-flex items-center overflow-hidden rounded bg-green-500 mt-4 px-8 py-3 text-white focus:outline-none focus:ring active:bg-green-600 w-xl mr-2"
              @click="downloadExcel"
            >
              <span class="text-center text-l font-medium transition-all group-hover:me-4">下载 Excel</span>
              <span class="absolute -end-full transition-all group-hover:end-4">
                <svg
                  class="h-5 w-5 rtl:rotate-180"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M17 8l4 4m0 0l-4 4m4-4H3"
                  />
                </svg>
              </span>
            </button>
            <button
              class="group relative inline-flex items-center overflow-hidden rounded bg-indigo-600 mt-4 px-8 py-3 text-white focus:outline-none focus:ring active:bg-indigo-500 w-xl"
              @click="confirmPopupVisible=true, confirmAction='入库'"
            >
              <span class="text-center text-l font-medium transition-all group-hover:me-4">入库</span>
              <span class="absolute -end-full transition-all group-hover:end-4">
                <svg
                  class="h-5 w-5 rtl:rotate-180"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M17 8l4 4m0 0l-4 4m4-4H3"
                  />
                </svg>
              </span>
            </button>
            <button
              class="group relative inline-flex items-center overflow-hidden rounded bg-indigo-600 mt-4 ml-2 px-8 py-3 text-white focus:outline-none focus:ring active:bg-indigo-500 w-xl"
              @click="confirmPopupVisible=true, confirmAction='出库'"
            >
              <span class="text-center text-l font-medium transition-all group-hover:me-4">出库</span>
              <span class="absolute -end-full transition-all group-hover:end-4">
                <svg
                  class="h-5 w-5 rtl:rotate-180"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M17 8l4 4m0 0l-4 4m4-4H3"
                  />
                </svg>
              </span>
            </button>
          </div>
        </div>
      </div>
      <div v-else class="text-center py-8 px-4">
        <div class="mb-4 text-lg font-semibold text-gray-500">教程</div>
          <p class="mb-2 text-gray-500">如果弹出数据库没有该数据，就先选择第二个输入框导入货号，正常情况下会导入该货号的所有规格。</p>
          <p class="mb-2 text-gray-500">如果货号成功还会弹出没有该数据，则是得物数据库也没有这个条形码，是商家新出的，需要自定义保存条形码。</p>
      </div>
    </Transition>
  </div>

  <!-- 上传图片的弹窗 -->
  <Transition
    enter-active-class="transition ease-out duration-300"
    enter-from-class="opacity-0 transform scale-95"
    enter-to-class="opacity-100 transform scale-100"
    leave-active-class="transition ease-in duration-200"
    leave-from-class="opacity-100 transform scale-100"
    leave-to-class="opacity-0 transform scale-95"
  >
    <div v-show="uploadfilePopupVisible" class="fixed inset-0 z-50 flex items-center justify-center">
      <div class="mx-auto max-w-sm rounded-2xl border border-blue-100 bg-white p-4 shadow-lg sm:p-6 lg:p-8 relative" role="alert">
        <!-- 关闭按钮 -->
        <button @click="closePopup" class="absolute top-2 right-2 text-gray-400 hover:text-gray-600 focus:outline-none">
          <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>

        <!-- 顶部切换 -->
        <div class="flex justify-center mb-4">
          <button @click="isArticleNumberView = true" :class="isArticleNumberView === true ? 'text-blue-500' : 'text-gray-500'" class="px-4 py-2 focus:outline-none">
            输入货号
          </button>
          <button @click="isArticleNumberView = false" :class="isArticleNumberView === false ? 'text-blue-500' : 'text-gray-500'" class="px-4 py-2 focus:outline-none">
            上传图片
          </button>
        </div>

        <div v-if="isArticleNumberView">
          <!-- 输入货号视图 -->
          <div class="flex flex-col items-center gap-4">
            <input type="text" v-model="articleNumberViewInput" placeholder="请输入货号" class="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <input type="text" v-model="sizeInput" placeholder="请输入尺码" class="w-full px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
            <button @click="searchArticleNumber(articleNumberViewInput, sizeInput, barcodeInput)" class="w-full rounded-lg bg-blue-500 px-5 py-3 text-center text-sm font-semibold text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
              查询货号
            </button>
          </div>
        </div>

        <div v-else>
          <!-- 上传图片视图 -->
          <p class="mt-4 text-gray-500">请拍照上传图片</p>
          <div class="mt-6 space-y-4">
            <div class="relative">
              <input type="file" accept="image/*" @change="onFileSelected" class="hidden" id="fileInput">
              <label for="fileInput" class="flex items-center justify-center w-full px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                </svg>
                选择文件
              </label>
            </div>
            <div v-if="selectedFileName" class="text-sm text-gray-500 truncate">
              已选择: {{ selectedFileName }}
            </div>
            <button @click="uploadImage" class="w-full rounded-lg bg-blue-500 px-5 py-3 text-center text-sm font-semibold text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
              上传图片
            </button>
          </div>
        </div>
      </div>
    </div>
  </Transition>
  
  <!-- 确认入库/出库弹窗 -->
  <Transition
    enter-active-class="transition ease-out duration-300"
    enter-from-class="opacity-0 transform scale-95"
    enter-to-class="opacity-100 transform scale-100"
    leave-active-class="transition ease-in duration-200"
    leave-from-class="opacity-100 transform scale-100"
    leave-to-class="opacity-0 transform scale-95"
  >
    <div v-show="confirmPopupVisible" class="z-50 fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full" id="my-modal">
      <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white mt-20 w-80"
          x-show="open"
          x-transition:enter="transition ease-out duration-300"
          x-transition:enter-start="opacity-0 scale-90"
          x-transition:enter-end="opacity-100 scale-100"
          x-transition:leave="transition ease-in duration-300"
          x-transition:leave-start="opacity-100 scale-100"
          x-transition:leave-end="opacity-0 scale-90">
        <div class="mt-3 text-center">
          <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-blue-100">
            <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
            </svg>
          </div>
          <h3 class="text-lg leading-6 font-medium text-gray-900 mt-5">{{ confirmAction }}确认</h3>
          <div class="mt-2 px-7 py-3">
            <p class="text-sm text-gray-500">
              您确定要将这些商品{{ confirmAction }}吗？此操作将更新库存记录。
            </p>
          </div>
          <div class="items-center px-4 py-3">
            <button
              @click="confirmAction === '入库' ? inbound() : outbound()"
              id="ok-btn"
              class="px-4 py-2 bg-blue-500 text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-300"
            >
              确认{{ confirmAction }}
            </button>
            <button
              @click="confirmPopupVisible = false"
              id="cancel-btn"
              class="mt-3 px-4 py-2 bg-white text-gray-500 text-base font-medium rounded-md w-full border border-gray-200 shadow-sm hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-300"
            >
              取消
            </button>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

    
<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { findShoeByBarcodeSerivce, saveArticleNumberService, fetchCurrentPriceService, uploadImageService, searchArticleNumberService } from '@/api/dataApi.js';
import { addInventoryService,outboundService } from '@/api/inventoryApi.js';
import { ElMessage, ElNotification, ElLoading } from 'element-plus';
import * as XLSX from 'xlsx';

const ShoeList = ref([]);
const barcodeInputRef = ref(null);
const barcodeInput = ref(''); // 绑定 v-model 的数据
const articleNumberInput = ref('');
const sizeInput = ref('');
const uploadfilePopupVisible = ref(false);
const selectedFileName = ref('');
const selectedFile = ref(null);
const confirmPopupVisible = ref(false);
const isArticleNumberView = ref(true);
const articleNumberViewInput = ref('');
const confirmAction = ref(''); // 新增变量来区分当前操作是入库还是出库

let debounceTimer = null;

// 在组件挂载时聚焦输入框
onMounted(() => {
  if (barcodeInputRef.value) {
    barcodeInputRef.value.focus();
  }
});

const backBarInput = () => {
  if (barcodeInputRef.value) {
    barcodeInputRef.value.focus(); // 重新聚焦
  }
};

const debouncedHandleScan = () => {
  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => {
    if (barcodeInput.value.length >= 8) { // 假设条形码至少有8位
      handleScan();
    }
  }, 300); // 300ms 延迟
};

const onFileSelected = (event) => {
  selectedFile.value = event.target.files[0];
  selectedFileName.value = event.target.files[0]?.name || '';
};

const closePopup = () => {
  uploadfilePopupVisible.value = false;
  articleNumberViewInput.value = '';
  sizeInput.value = '';
  barcodeInput.value = '';
  nextTick(() => {
    if (barcodeInputRef.value) {
      barcodeInputRef.value.focus(); // 重新聚焦
    }
  });
};

const handleScan = async () => {
  if (barcodeInput.value.trim()) {
    const shoeData = await findShoeByBarcode(barcodeInput.value);
    if (shoeData !== null) {
      ShoeList.value.push(shoeData);
      barcodeInput.value = ''; // 清空输入框
    }
    nextTick(() => {
      if (barcodeInputRef.value) {
        barcodeInputRef.value.focus(); // 重新聚焦
      }
    });
  }
};

const findShoeByBarcode = async (barcode) => {
  try {
    const result = await findShoeByBarcodeSerivce(barcode);
    if (result.code === 0) {
      if (result.data === null) {
        uploadfilePopupVisible.value = true;
        return null; // 返回 null 而不是未定义的值
      }
      return {
        barcode: barcode,
        skuId: result.data?.skuId || null,
        articleNumber: result.data?.articleNumber || null,
        brandName: result.data?.brandName || null,
        size: result.data?.size || null,
        otherNumbers: result.data?.otherNumbers || null,
        spuLogo: result.data?.spuLogo || null,
      };
    }
  } catch (error) {
    uploadfilePopupVisible.value = true;
  }
  return null; // 确保在所有情况下都返回一个值
};

const fetchCurrentPrice = async (shoe) => {
  const result = await fetchCurrentPriceService(shoe.skuId);
  if (result?.code === 0) {
    shoe.currentPrice = result.data;
  } else {
    shoe.currentPrice = 0;
  }
};

const saveArticleNumber = async () => {
  const result = await saveArticleNumberService(articleNumberInput.value);
  if (result?.code === 0) {
    ElMessage({
      message: '导入成功',
      type: 'success',
      showClose: true,
    });
    articleNumberInput.value = '';
    backBarInput();
  } else {
    ElMessage({
      message: '导入失败',
      type: 'error',
      showClose: true,
    });
    articleNumberInput.value = '';
    backBarInput();
  }
};

const removeShoe = (index) => {
  ShoeList.value.splice(index, 1);
};

const uploadImage = async () => {
  if (!selectedFile.value) {
    ElNotification({
      title: '错误',
      message: '请先选择一个文件',
      type: 'error',
    });
    return;
  }

  const loadingInstance = ElLoading.service({
    fullscreen: true,
    text: '正在上传图片...',
    background: 'rgba(0, 0, 0, 0.7)',
  });

  try {
    const formData = new FormData();
    formData.append('file', selectedFile.value);

    const result = await uploadImageService(formData);

    if (result.code === 0) {
      ElNotification({
        title: '上传成功',
        dangerouslyUseHTMLString: true,
        message: `货号: ${result.data.articleNumber}<br>尺码: ${result.data.size}<br>条形信息: ${result.data.customCode}`,
        duration: 5000,
      });
    } else {
      throw new Error(result.message || '上传失败');
    }
  } catch (error) {
    console.error('上传图片时出错:', error);
    ElNotification({
      title: '错误',
      message: error.message || '上传失败',
      type: 'error',
    });
  } finally {
    // 重置所有状态
    uploadfilePopupVisible.value = false;
    selectedFile.value = '';
    selectedFileName.value = '';
    barcodeInput.value = '';
    loadingInstance.close();
    backBarInput();
  }
};

const inbound = async () => {
  try {
    // 提取 ShoeList.value 中的 skuId 字段到 skuList
    const skuIdList = ShoeList.value.map((shoe) => Number(shoe.skuId));
    // 调用 addInventoryService
    const result = await addInventoryService(skuIdList);
    if (result.code === 0) {
      // 重置状态
      confirmPopupVisible.value = false;
      ShoeList.value = [];
      backBarInput();
    }
  } catch {
    confirmPopupVisible.value = false;
    ShoeList.value = [];
    backBarInput();
  }
};

const outbound = async () => {
  try {
    // 提取 ShoeList.value 中的 skuId 字段到 skuList
    const skuIdList = ShoeList.value.map((shoe) => Number(shoe.skuId));
    // 调用 addInventoryService
    const result = await outboundService(skuIdList);
    if (result.code === 0) {
      // 重置状态
      confirmPopupVisible.value = false;
      ShoeList.value = [];
      backBarInput();
    }
  } catch {
    confirmPopupVisible.value = false;
    backBarInput();
  }
};

// 定义一个函数来重新映射字段名并提取特定字段
const mapShoeListToNewFields = (list) => {
  return list.map((item) => {
    return {
      货号: item.articleNumber || '', // 确保 articleNumber 存在
      其他货号: item.otherNumbers || '', // 确保 otherNumbers 存在
      品牌: item.brandName || '', // 确保 brandName 存在
      尺码: item.size || '', // 确保 size 存在
      成本: '', // 可以初始化为空字符串
    };
  });
};

const downloadExcel = () => {
  const mappedData = mapShoeListToNewFields(ShoeList.value);
  const headers = ['货号', '其他货号', '品牌', '尺码', '成本']; // 定义 Excel 列标题
  const worksheet = XLSX.utils.json_to_sheet(mappedData, { header: headers });
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, 'ShoeList');
  XLSX.writeFile(workbook, '入库.xlsx');
};

const searchArticleNumber = async (articleNumber, size, barcode) => {
  const result = await searchArticleNumberService(articleNumber, size, barcode);
  ElNotification({
    title: '查询结果',
    dangerouslyUseHTMLString: true,
    message: `新条形码: ${result.data}`,
    duration: 5000,
  });
  closePopup();
  backBarInput();
};

// 清理定时器
onUnmounted(() => {
  if (debounceTimer) {
    clearTimeout(debounceTimer);
  }
});
</script>

