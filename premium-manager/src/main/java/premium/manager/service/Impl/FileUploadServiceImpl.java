package premium.manager.service.Impl;

import cn.hutool.core.date.DateUtil;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import premium.common.exception.MyException;
import premium.manager.service.FileUploadService;
import premium.manager.properties.MyMinioProperties;
import premium.model.vo.common.ResultCodeEnum;

import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MyMinioProperties minioProperties;

    /**
     * MinIO用户头像上传
     */
    @Override
    public String upload(MultipartFile file) {

        try {
            // 创建一个 MinioClient 对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpointUrl())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecreteKey())
                    .build();

            // 判断桶是否存在
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build()
            );
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            }
//            else {  // 如果存在打印信息
//                 System.out.println("Bucket 'premium-bucket' already exists.");
//            }

            // get file name.  ** every filename must be different (use uuid and date) **
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            String fileName = dateDir + "/" + uuid + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );

            // return minio upload path
            return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + fileName ;

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }


}
