# OpenSSL 客户端&服务端证书生成
- 准备ca.conf
```
  [ req ]
  default_bits       = 4096
  distinguished_name = req_distinguished_name

  [ req_distinguished_name ]
  countryName                 = Country Name (2 letter code)
  countryName_default         = CN
  stateOrProvinceName         = State or Province Name (full name)
  stateOrProvinceName_default = guangzhou
  localityName                = Locality Name (eg, city)
  localityName_default        = shenzhen
  organizationName            = Organization Name (eg, company)
  organizationName_default    = bsoft
  commonName                  = Common Name (e.g. server FQDN or YOUR name)
  commonName_max              = 64
  commonName_default          = CA Test
```
- cd src/main/java/com/example/demo/io/Netty
- mkdir rsa
- cd rsa
- mkdir server
- mkdir client
- cd server
## ca 相关证书
- openssl genrsa -out ca.key 4096 [ca.key]
- openssl req -new -sha256 -out ca.csr -key ca.key -config ./ca.conf [ca.csr]
  ``Country Name (2 letter code) []:CN
  State or Province Name (full name) []:Province
  Locality Name (eg, city) []:City
  Organization Name (eg, company) []:WIZ Technology Co. Ltd.
  Common Name (eg, fully qualified host name) []:WIZ Technology Co. Ltd.``
- openssl x509 -req -days 3650 -in ca.csr -signkey ca.key -out ca.crt [ca.crt]
## 生成服务端和客户端私钥
- openssl genrsa -des3 -out server.key 1024
  ``123456``
- openssl genrsa -des3 -out client.key 1024
  ``123456``
## 根据key生成csr文件 | -config openssl.cnf 默认在cnf文件夹，如果未复制出来，需要指定路径“/System/Library/OpenSSL/openssl.cnf”
- openssl req -new -key server.key -out server.csr -config /System/Library/OpenSSL/openssl.cnf
  ``123456``
```
  Country Name (2 letter code) [AU]:CN
  State or Province Name (full name) [Some-State]:Province
  Locality Name (eg, city) []:City
  Organization Name (eg, company) [Internet Widgits Pty Ltd]:WIZ Technology Co. Ltd.
  Organizational Unit Name (eg, section) []:WIZ Technology Co. Ltd.
  Common Name (e.g. server FQDN or YOUR name) []:WIZ Technology Co. Ltd.
  Email Address []:wenshenglaalal@foxmail.com

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:123456
An optional company name []:test
```
- openssl req -new -key client.key -out client.csr -config /System/Library/OpenSSL/openssl.cnf
  ``123456``
```
Country Name (2 letter code) [AU]:CN
State or Province Name (full name) [Some-State]:Province
Locality Name (eg, city) []:City
Organization Name (eg, company) [Internet Widgits Pty Ltd]:WIZ Technology Co. Ltd.
Organizational Unit Name (eg, section) []:WIZ Technology Co. Ltd.
Common Name (e.g. server FQDN or YOUR name) []:WIZ Technology Co. Ltd.
Email Address []:wenshenglaalal@foxmail.com

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:123456
An optional company name []:test
```
## 根据ca证书server.csr、client.csr生成x509证书
- openssl x509 -req -days 3650 -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt
- openssl x509 -req -days 3650 -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out client.crt

## 将key文件进行PKCS#8编码
- openssl pkcs8 -topk8 -in server.key -out pkcs8_server.key -nocrypt
- openssl pkcs8 -topk8 -in client.key -out pkcs8_client.key -nocrypt
## 完成，一共需要6个文件
- server端：ca.crt、server.crt、pkcs8_server.key
- client端：ca.crt、client.crt、pkcs8_client.key