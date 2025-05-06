# netty ssl https://github.com/yqbjtu/springboot
- https://blog.csdn.net/russle/article/details/99086684
# TLS
- 传输层安全性协议 (Transport Layer Security，缩写作 TLS)，它的前身是安全套接层 (Secure Sockets Layer，缩写作 SSL)
- 采用的加密算法 TLS1.3 -> TLS_AES_256_GCM_SHA384
## 路径
- /Users/yz/asiainnovations/personal-master-client/src/main/java/com/example/demo/io/Netty/jkl/
## 指令
- keypass选项用于密钥的密码，storepass选项用于密钥库的密码(keytool -export),JDK11及以上 这俩个密码必须一致
### 列出OpenSSL支持的加密算法
- openssl ciphers -v 
### TLS_AES_256_GCM_SHA384
- openssl ciphers -V | grep AES | grep GCM | grep 384
## 双向验证-server
### 第一步 生成服务器端私钥和证书仓库命令
- keytool -genkey -alias serverkey -keysize 2048 -validity 365 -keyalg RSA -dname "CN=localhost" -keypass serverpass -storepass serverpass -keystore kserver.ks
``
  对于websocket ssl 需要对域名ip授信
  -ext SAN=dns:ws.test.com,ip:127.0.0.1
``
- keytool -genkey -alias serverkey -keysize 2048 -validity 365 -keyalg RSA -dname "CN=localhost" -keypass serverpass -storepass serverpass -keystore kserver.ks -ext SAN=dns:ws.test.com,ip:127.0.0.1
### 第二步 生成服务器端自签名证书
- keytool -export -alias serverkey -keystore kserver.ks -storepass serverpass -file server.crt
### 第三步 ：将服务器端证书导入到客户端的证书仓库中(客户端需要使用clientStorePass 才能访问 tclient.ks)
- keytool -import -trustcacerts -alias serverkey -file server.crt -storepass clientpublicpass -keystore tclient.ks
## 双向验证-client
### 第一步 ：生成客户端的密钥对和证书仓库，用于将服务器端的证书保存到客户端的授信证书仓库中
- keytool -genkey -alias clientkey -keysize 2048 -validity 365 -keyalg RSA -dname "CN=localhost" -keypass clientpass -storepass clientpass -keystore kclient.ks
``
  对于websocket ssl 需要对域名ip授信
  -ext SAN=dns:ws.test.com,ip:127.0.0.1
``
- keytool -genkey -alias clientkey -keysize 2048 -validity 365 -keyalg RSA -dname "CN=localhost" -keypass clientpass -storepass clientpass -keystore kclient.ks -ext SAN=dns:ws.test.com,ip:127.0.0.1
### 第二步 生成客户端自签名证书
- keytool -export -alias clientkey -keystore kclient.ks -storepass clientpass -file client.crt
### 第三步 将客户端的自签名证书导入到服务器端的信任证书仓库中：
- keytool -import -trustcacerts -alias clientkey -file client.crt -storepass serverpublicpass -keystore tserver.ks
