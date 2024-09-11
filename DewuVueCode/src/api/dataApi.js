//导入请求工具类
import request from '@/utils/request.js'
//导入@/stores/token.js



export const findShoeByBarcodeSerivce = (barcode) => {
    const params = new URLSearchParams();
    params.append('barcode', barcode);
    return request.post('/data/getSkuByCustomCode', params)
  }
export const fetchCurrentPriceService = (skuId) => {
    const params = new URLSearchParams();
    params.append('skuId', skuId);
    return request.post('/data/getLowestPrice', params.toString())
  }

export const saveArticleNumberService = (articleNumber) => {
  const params = new URLSearchParams();
  params.append('articleNumber', articleNumber);
  return request.post('/data/addSku', params)
}

export const uploadImageService = (formData) => {
  return request.post('/data/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

export const searchArticleNumberService = (articleNumber,size,barcode) => {
  const params = new URLSearchParams();
  params.append('articleNumber', articleNumber);
  params.append('size', size);
  params.append('customCode', barcode);
  return request.post('/data/addCustomCode', params)
}
