package com.xuecheng.media.api.test;

import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MediaApiTest {

    @Test
    void minioTest(){
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://43.136.50.242:9001")
                .credentials("admin", "Rsl,990905")
                .build();
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("mediafiles")
                            .object("pic01.png")    // 同一个桶内对象名不能重复
                            .filename("C:\\Users\\renshaolong\\Pictures\\wallhaven-2yz3m9_2560x1440.png")
                            .build()
            );
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

    }
}
