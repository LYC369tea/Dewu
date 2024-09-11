import { defineStore } from "pinia";
import {ref} from 'vue';

/*
    defineStore参数描述：
        第一个参数：给状态起名，具有唯一性
        第二个参数：函数，可以把定义该状态中拥有的内容

    defineStore返回值描述：
        返回的是一个函数，将来可以调用该函数，得到第二个参数中返回的内容
*/
export const useTokenStore = defineStore('token',()=>{
    //1.定义描述token
    const token = ref('')

    //2.定义修改token的方法
    const setToken = (newToken)=>{
        token.value = newToken
    }

    //3.定义移除token的方法
    const removeToken = ()=>{
        token.value=''
    }
    return {
        token,setToken,removeToken
    }
    
},
//参数持久化
{
    persist:true
}
)