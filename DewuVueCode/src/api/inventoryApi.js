// 导入请求工具类
import request from '@/utils/request.js';

export const addInventoryService = (skuIdList) => {
    return request.post('/inventory/add', skuIdList, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
};


export const getInventoryListService = () => {
  return request.get('/inventory/get')
}

export const getInventoryDetailListService = (params) => {
  return request.get('/inventory/getDetail',{params})
}

export const updateRecordService = (inventoryList) => {
  return request.post('/inventory/update',inventoryList)
}

export const deleteRecordService = (inventoryIdList) => {
  console.log(inventoryIdList)
  return request.post('/inventory/delete',inventoryIdList)
}

export const outboundService = (skuIdList) => {
  return request.post('/inventory/out',skuIdList)
}