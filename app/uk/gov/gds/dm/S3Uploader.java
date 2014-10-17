package uk.gov.gds.dm;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import play.Logger;

import java.io.File;

public class S3Uploader {

    private AmazonS3 client;

    public S3Uploader() {
        client = new AmazonS3Client(new DefaultAWSCredentialsProviderChain());
        Region region = Region.getRegion(Regions.EU_WEST_1);
        client.setRegion(region);
    }

    public String upload(File file, String bucket, String key) {
        ensureBucketExists(bucket);
        Logger.info("[S3Uploader] Uploading document to S3 with key: " + key);
        client.putObject(new PutObjectRequest(bucket, key, file));
        String bucketLocation = client.getBucketLocation(bucket);
        String fileUrlOnS3 = String.format("https://s3-%s.amazonaws.com/%s/%s", bucketLocation, bucket, key);
        Logger.info("[S3Uploader] Successful upload to S3: " + fileUrlOnS3);
        return fileUrlOnS3;
    }

    private void ensureBucketExists(String bucket) {
        try {
            client.createBucket(bucket);
        } catch(AmazonS3Exception e) {
            // nowt
        }
    }


}
