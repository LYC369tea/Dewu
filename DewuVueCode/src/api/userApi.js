// 导入request.js请求工具和vue相关功能
import request from '@/utils/request.js'
import forge from 'node-forge';

// 假设你有一个 RSA 公钥
const publicKeyPem = `
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArt3LBccY12xItTwbqStpATMrkq8CNUflj4ky/X1oSU9amjtDXkVjdRjsEQ54i7wVw0gDED+85ZqiSujCTp1Vky+lkrRwvLihMMWykt0WeZA6mAmkTTUi+8jmTnpre11ZDf2L3MRldx5dfVbHQ8ZjdUveY5yyzUJcXbB30a5mvymq+033qGFtnYiBV+u5TbfalQ9MlApHbym+HBf1mCGOoz6P3jls+e1KMYDmAZeAoh+NxWKn8fM1xJzJzrbmTfopCsf2mY3MspiKcevCC8J5flRcv4BImge8AVkxbX78Rmz/5aeAxLtPRvP5kNV4L5E/VHsBs7PE/EhBN9VpSb47gwIDAQAB
-----END PUBLIC KEY-----
`;

/* // 注册服务函数
export const userRegisterService = (registerData) => {
  const params = new URLSearchParams()
  for (let key in registerData) {
    params.append(key, registerData[key]);
  }
  return request.post('/user/register', params)
} */

// 登录服务函数
export const userLoginService = (loginData)=>{
  /* // 加载公钥
  const publicKey = forge.pki.publicKeyFromPem(publicKeyPem);

  // 对密码进行 RSA 加密
  if (loginData.password) {
    const encryptedPassword = publicKey.encrypt(loginData.password, 'RSA-OAEP');
    loginData.password = forge.util.encode64(encryptedPassword);
  } */
  const params = new URLSearchParams();
  for(let key in loginData){
    params.append(key,loginData[key])   
  }
  return request.post('/user/1',params)
}

// 退出登录函数
export const userOutLoginService = (token)=>{
  const params = new URLSearchParams()
  params.append("token", token);
  // 发送POST请求到后端的/logout接口
  return request.post('/user/logout', params)
}


// 获取用户名服务函数
export const getUsernameService = () => {
  return request.get('/user/userInfo'); // 确保 request 已经正确配置
};
