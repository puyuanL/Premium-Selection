# Premium-Selection
A Java project which allow people to select products on web

## 管理端

管理端前端：  http://localhost:3001/

Swagger Test: http://localhost:8501/doc.html#/home

### WSL
#### MinIO
Minio Account:  root  | Password:   lpy123456 

Management Console:     http://172.18.91.89:9090/

Access Policy:  

lpy_wsl@DESKTOP-RUHSJPO:~$ mc alias set myminio http://localhost:9001 root lpy123456

lpy_wsl@DESKTOP-RUHSJPO:~$ mc ls myminio

lpy_wsl@DESKTOP-RUHSJPO:~$ mc anonymous policy set public myminio/premium-bucket

#### MySQL
MySQL Account:  root  | Password:   lpy123

## 客户端

客户端前端：  http://172.18.91.89:80/


