package com.artista.main.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * uuid 형식을 추가한 파일명 추출
     * @param multipartFile
     * @return
     */
    public String getUuidFileName(MultipartFile multipartFile){
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + multipartFile.getOriginalFilename();

        return fileName;
    }

    /**
     * S3 파일 업로드
     * @param filePath
     * @param fileName
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String upload(String filePath, String fileName, MultipartFile multipartFile) throws IOException {
        String keyBucket = bucket + filePath;

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getSize());
        amazonS3.putObject(keyBucket, fileName, multipartFile.getInputStream(), objMeta);

        return amazonS3.getUrl(keyBucket, fileName).toString();
    }

    /**
     * S3 파일 삭제
     * @param uploadFilePath
     * @param uuidFileName
     */
    public void delete(String uploadFilePath, String uuidFileName) {
        try {
            String keyBucket = bucket + uploadFilePath;
            boolean isObjectExist = amazonS3.doesObjectExist(keyBucket, uuidFileName);
            if (isObjectExist) {
                amazonS3.deleteObject(keyBucket, uuidFileName);
            } else {
                log.info("S3 file not found");
            }

        } catch (Exception e) {
            log.info("Delete File failed", e);
        }
    }

    /**
     * url 가져오기
     * @param bucket
     * @param fileName
     * @return
     */
    private String getS3(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
