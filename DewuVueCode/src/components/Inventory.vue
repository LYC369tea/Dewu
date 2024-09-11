<template>
  <div v-if="inventoryList.length > 0" class="container px-5 py-5 mx-auto changeHeight">
    <div class="flex flex-wrap -m-4 text-center">
      <div class="p-4 sm:w-1/2 w-1/2">
        <h2 class="title-font font-medium sm:text-4xl text-3xl text-gray-900">{{ totalQuantity }}</h2>
        <p class="leading-relaxed">总库存</p>
      </div>
      <div class="p-4 sm:w-1/2 w-1/2">
        <h2 class="title-font font-medium sm:text-4xl text-3xl text-gray-900">{{ totalCost.toFixed(2) }}</h2>
        <p class="leading-relaxed">总成本</p>
      </div>
    </div>
    
    <div class="flex flex-wrap -m-2">
      <div v-for="item in inventoryList" :key="item.articleNumber" class="p-2 lg:w-1/3 md:w-1/2 w-full">
        <div class="h-full flex flex-col border-gray-200 border p-4 rounded-lg relative">
          <!-- 添加详情图标按钮 -->
          <button @click='shoeDetail(item.articleNumber)' class="absolute top-2 right-2 text-gray-500 hover:text-gray-700">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </button>
          
          <div class="flex items-start mb-4">
            <img :alt="item.brandName" :src="item.spuLogo" class="w-20 h-12 bg-gray-100 object-cover object-center mr-4" />
            <div class="flex-grow">
              <h2 class="text-gray-900 title-font font-medium">{{ item.brandName }}</h2>
              <div class="flex justify-between text-gray-500">
                <p>{{ item.articleNumber }}</p>
                <p>平均成本: {{ item.avgCost }}</p>
              </div>
            </div>
          </div>
          <div class="mt-2">
            <ul class="flex flex-wrap -m-1">
              <li v-for="sizeQuality in item.sizeQualityList" :key="sizeQuality.size" class="w-1/3 p-1">
                <div class="bg-gray-50 rounded-md py-2 text-center text-sm">
                  <span class="text-black font-bold">{{ Math.floor(sizeQuality.size) || sizeQuality.size }}</span>
                  <span class="text-gray-400 text-xs ml-2">库存：{{ sizeQuality.quality }}</span>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-else class="container px-5 py-5 mx-auto text-center">
    <h2 class="title-font font-medium sm:text-4xl text-3xl text-gray-900">暂无库存</h2>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getInventoryListService } from '@/api/inventoryApi.js';
import { useRouter } from 'vue-router';
const router = useRouter();

onMounted(() => {
  getInventoryList();
});

const totalQuantity = ref(0);
const totalCost = ref(0);

const inventoryList = ref([]);

const getInventoryList = async () => {
  const result = await getInventoryListService();
  inventoryList.value = result.data;
  
  // 计算总库存数量
  totalQuantity.value = inventoryList.value.reduce((total, item) => {
    const itemQuantity = item.sizeQualityList.reduce((itemTotal, sizeQuality) => {
      return itemTotal + sizeQuality.quality;
    }, 0);
    return total + itemQuantity;
  }, 0);
  
  // 计算总成本
  totalCost.value = inventoryList.value.reduce((total, item) => {
    return total + item.avgCost;
  }, 0);
};

const shoeDetail = async (articleNumber) => {
  router.push({
    path: '/shoeDetail',
    query: { articleNumber: articleNumber }
  });
};
</script>

<style scoped>
.changeHeight {
  height: 550px; /* 默认高度 */
}

@media (max-width: 768px) {
  .changeHeight {
    height: 600px; /* 在宽度小于768px的设备上使用的高度 */
  }
}

@media (max-width: 400px) {
  .changeHeight {
    height: 400px; /* 在宽度小于480px的设备上使用的高度 */
  }
}
</style>
