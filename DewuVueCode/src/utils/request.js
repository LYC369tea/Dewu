//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';
import { reactive } from 'vue';
import { ElMessage } from 'element-plus'
import router from '@/router'

//定义一个变量,记录公共的前缀  ,  baseURL
const baseURL = 'http://localhost:8080';/* http://45.207.212.48:8080 */
const instance = axios.create({baseURL})

// 扩展全局状态，增加用于控制'服务异常'提示的标志
const globalState = reactive({
    loginPromptShown: false,
    serviceErrorShown: false
  });

//导入token状态
import { useTokenStore } from '@/stores/token.js';
//添加请求拦截器
instance.interceptors.request.use(
    (config)=>{
        //在发送请求之前做什么
        let tokenStore = useTokenStore()
        /* console.log(tokenStore.token) */
        //如果token中有值，在携带
        if(tokenStore.token){
            config.headers.Authorization=tokenStore.token
        }
        return config
    },
    (err)=>{
        //如果请求错误做什么
        Promise.reject(err)
    }
)


//添加响应拦截器
instance.interceptors.response.use(
    result => {
        // 正常业务处理
        if (result.data.code == 0) {
            return result.data;
        }
        // 控制'服务异常'的提示只显示一次
        if (!globalState.serviceErrorShown) {
            ElMessage.error(result.data.message || '服务异常');
            globalState.serviceErrorShown = true; // 更新状态为已提示
            setTimeout(() => { globalState.serviceErrorShown = false; }, 5000); // 5秒后重置提示状态
        }
        return Promise.reject(result.data);
    },
    err => {
        if (err.response && err.response.status === 401) {
            // 控制'请先登录！'的提示只显示一次
            if (!globalState.loginPromptShown) {
                ElMessage.error('请先登录！');
                globalState.loginPromptShown = true;
                setTimeout(() => { globalState.loginPromptShown = false; }, 5000);
            }
            router.push('/login');
        } else {
            // 同样控制'服务异常'的提示只显示一次
            if (!globalState.serviceErrorShown) {
                ElMessage.error('服务异常');
                globalState.serviceErrorShown = true;
                setTimeout(() => { globalState.serviceErrorShown = false; }, 5000);
            }
        }
        return Promise.reject(err);
    }
);

export default instance;