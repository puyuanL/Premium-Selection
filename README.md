# Premium-Selection
A Java project which allow people to select products on web

## 管理端

管理端前端：  http://localhost:3001/

Swagger Test Back: http://localhost:8501/doc.html#/home

Swagger Test Front: http://localhost:8512/doc.html#/home

### WSL

Minio

    Account:    root 
    Password:   lpy123456
    Management Console:     http://172.18.91.89:9090/

Access Policy:  

    lpy_wsl@DESKTOP-RUHSJPO:~$ mc alias set myminio http://localhost:9001 root lpy123456
    lpy_wsl@DESKTOP-RUHSJPO:~$ mc ls myminio
    lpy_wsl@DESKTOP-RUHSJPO:~$ mc anonymous policy set public myminio/premium-bucket

MySQL 

    Account:    root  
    Password:   lpy123

Harbor:

    Account:    admin
    Password:   Harbor12345

RabbitMQ:

    Account:    admin
    Password:   lpy123
    Management Console:     http://172.18.91.89:15672/

## 客户端

客户端前端：  http://172.18.91.89:80/

Nacos管理页：http://172.18.91.89:8848/nacos

阿里云短信验证码服务：

https://marketnext.console.aliyun.com/bizlist?trial=1

    AppKey：204950929
    AppSecret：YGYto9sIrHRBjDMqvxCsjE17JNTTAdso
    AppCode：177a6bea1bde42ab97fa24699b3f389c

## 其他配置

### 内网穿透

    使用 ngrok 内网穿透服务
    账号 lpylpy
    隧道id： 185748454746
    域名： http://lpypremium.free.idcfengye.com

