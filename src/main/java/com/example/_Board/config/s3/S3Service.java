package com.example._Board.config.s3;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

//    // 파일 업로드 (파일 NULL 가능)
//    public List<String> upload(List<MultipartFile> multipartFile) {
//        List<String> imgUrlList = new ArrayList<>();
//
//        // null 체크 or 비어있는지 체크
//        if (multipartFile == null || multipartFile.isEmpty()) {
//            return imgUrlList; // 빈 리스트 리턴 (에러 X)
//        }
//
//
//        for (MultipartFile file : multipartFile) {
//            if (file.isEmpty()) {
//                continue;
//            }
//
//            String fileName = createFileName(file.getOriginalFilename());
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(file.getSize());
//            objectMetadata.setContentType(file.getContentType());
//
//            try(InputStream inputStream = file.getInputStream()) {
//                s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
//                imgUrlList.add(fileName);
//            } catch(IOException e) {
//                throw new RuntimeException("file upload error");
//            }
//        }
//        return imgUrlList;
//    }

    public List<String> upload(List<MultipartFile> multipartFile) {
        List<String> imgUrlList = new ArrayList<>();

        if (multipartFile == null || multipartFile.isEmpty()) {
            return imgUrlList;
        }

        for (MultipartFile file : multipartFile) {
            if (file.isEmpty()) {
                continue;
            }

            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(
                        new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                );

                String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
                imgUrlList.add(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("file upload error");
            }
        }
        return imgUrlList;
    }



    // 이미지파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new RuntimeException("file name is empty");
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new RuntimeException("file extension is invalid");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    // DeleteObject를 통해 S3 파일 삭제
    public void deleteFile(String fileName){
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        s3Client.deleteObject(deleteObjectRequest);
    }
}