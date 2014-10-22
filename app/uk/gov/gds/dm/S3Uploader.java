package uk.gov.gds.dm;

import com.amazon.s3shell.S3Store;
import org.jets3t.Constants;
import play.Logger;

import java.io.IOException;
import java.util.HashMap;

public class S3Uploader {

    private S3Store client;
    private static final String S3_ENDPOINT = "s3-eu-west-1.amazonaws.com";

    public S3Uploader() {
        String key = "AKIAJZU7XZ5RGNP2NUCA";//System.getenv("AWS_ACCESS_KEY");
        String secretKey = "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";//System.getenv("AWS_SECRET_ACCESS_KEY");

        client = new S3Store(S3_ENDPOINT, key, secretKey);
    }

    public String upload(byte[] data, String bucket, String key) {
        client.setBucket(bucket);
        try {
            client.storeItem(key, data, Constants.ACL_PRIVATE);
        } catch (IOException e) {
            Logger.error("Upload failed", e);
            e.printStackTrace();
        }
        return "";
    }

    private void ensureBucketExists(String bucket) {

    }


}
