package uk.gov.gds.dm;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class S3Uploader {

    private static final String ACCESS_KEY = "AKIAJZZLG2T6ZHFO4OPQ";
    private static final String SECRET_KEY = "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";

    private AmazonS3 client;

    public S3Uploader() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        client = new AmazonS3Client(credentials);
        Region region = Region.getRegion(Regions.EU_WEST_1);
        client.setRegion(region);
    }


    public String upload(File file, String bucket, String key) {
        ensureBucketExists(bucket);
        client.putObject(new PutObjectRequest(bucket, key, file));
        String bucketLocation = client.getBucketLocation(bucket);
        String downloadUrl = String.format("https://s3-%s.amazonaws.com/%s/%s", bucketLocation, bucket, key);
        return downloadUrl;
    }

    private void ensureBucketExists(String bucket) {
        try {
            client.createBucket(bucket);
        } catch(AmazonS3Exception e) {
            // nowt
        }
    }


}
