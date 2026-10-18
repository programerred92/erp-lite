package com.magicbox.erp_lite.persistence.aws.adapters;

import com.magicbox.erp_lite.domain.exceptions.BusinessException;
import com.magicbox.erp_lite.domain.ports.ImagesStorageService;
import com.magicbox.erp_lite.domain.product.ProductImage;
import com.magicbox.erp_lite.persistence.aws.models.AwsConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsImageStorageService implements ImagesStorageService {

    private final S3Client s3Client;
    private final AwsConfigModel awsConfig;

    private String getKeyFromUrl(String url) {
        var bucketName = awsConfig.bucketName();
        var parts = url.split("/"+bucketName+"/");
        if(parts.length>1){
            return parts[1];
        }
        log.warn("No Bucket name found for url: "+url);
        return url;
    }

    private String buildUrlImg(String key){
        final var placeholder = "%s/%s/%s";
        return String.format(placeholder, awsConfig.endpoint(), awsConfig.bucketName(), key);
    }

    private String determineContentType(String filename){
        final var extension = filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }

    @Override
    public ProductImage upload(String imageName, byte[] imageData) {
        try{
            final var key = "products/"+imageName;
            final var putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsConfig.bucketName())
                    .key(key)
                    .contentType(determineContentType(imageName))
                    .contentLength((long) imageData.length)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageData));
            var imageUrl =  buildUrlImg(key);
            log.info("Uploaded image {} to S3 successfully in {}", imageName, imageUrl);

            return new ProductImage(imageUrl);
        } catch (S3Exception s3e){
            log.error("Error uploading image: "+imageName, s3e);
            throw new BusinessException("Error uploading image "+s3e.getMessage());
        } catch (Exception e){
            log.error("Unexpected error uploading image: "+imageName, e);
            throw new BusinessException("Error uploading image "+e.getMessage());
        }
    }

    @Override
    public void delete(ProductImage img) {
        try{
            final var key = getKeyFromUrl(img.imageUrl());
            s3Client.deleteObject(builder -> builder.bucket(awsConfig.bucketName()).key(key).build());
            log.info("Deleted image from S3: {}", img.imageUrl());
        }catch(S3Exception s3e){
            log.error("Error deleting image: "+img, s3e);
            throw new BusinessException("Error deleting image "+s3e.getMessage());
        }catch(Exception e){
            log.error("Unexpected error deleting image: "+ e);
            throw new BusinessException("Error deleting image "+e.getMessage());
        }
    }

    @Override
    public byte[] download(ProductImage img) {
        try{
            final var key = getKeyFromUrl(img.imageUrl());
            var getObjectRequest = GetObjectRequest.builder()
                    .bucket(awsConfig.bucketName())
                    .key(key)
                    .build();
            var bytes = s3Client.getObjectAsBytes(getObjectRequest).asByteArray();

            log.info("Downloaded image from S3: {} bytes", bytes.length);

            return bytes;

        }catch(S3Exception s3e){
            log.error("Error downloading image: "+img, s3e);
            throw new BusinessException("Error downloading image "+s3e.getMessage());
        }catch(Exception e){
            log.error("Unexpected error downloading image: "+ e);
            throw new BusinessException("Error downloading image "+e.getMessage());
        }
    }
}
