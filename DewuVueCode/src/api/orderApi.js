// 导入请求工具类
import request from '@/utils/request.js';

// 导出接口
export const getOrderListService = (params) => {
    return request.get('/order/getOrder',{params})
}

export const getRealTimeService = (params) => {
    return request.get('/order/getRealTime',{params})
}
