// 先导入vue-router
import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    { path: '/login', component: () => import('@/components/Login.vue') },
    { path: '/register', component: () => import('@/components/Resigter.vue') },
    {
        path: '/', component: () => import('@/components/Layout.vue'), redirect: '/warehouse', children: [
            { path: '/warehouse', component: () => import('@/components/Warehouse.vue') },
            { path: '/shoe', component: () => import('@/components/ScanBarcode.vue') },
            { path: '/inventory', component: () => import('@/components/Inventory.vue') },
            { path: '/shoeDetail', component: () => import('@/components/Detail.vue') },
            { path: '/order', component: () => import('@/components/Order.vue') },
        ]
    }
];

// 创建路由器
const router = createRouter({
    history: createWebHistory(), // 使用 HTML5 History 模式
    routes // 确保这里使用的是修正后的变量名
});

export default router;
