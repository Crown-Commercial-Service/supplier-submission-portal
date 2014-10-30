package uk.gov.gds.dm;

import com.amazon.s3shell.S3Store;
import org.jets3t.Constants;
import play.Logger;

import java.io.IOException;

public class S3Uploader {

    private S3Store client;
    private static final String S3_ENDPOINT = "s3-eu-west-1.amazonaws.com";

    public S3Uploader(String bucket) {
        String key = System.getProperty("aws.access.key", System.getenv("AWS_ACCESS_KEY"));
        String secretKey = System.getProperty("aws.secret.access.key", System.getenv("AWS_SECRET_ACCESS_KEY"));
        if (System.getenv("AWS_DISABLE_STORE") == null) {
            client = new S3Store(S3_ENDPOINT, key, secretKey, bucket);
        }
    }

    public String upload(byte[] data, String key) {
        if (client == null) {
            return "";
        } else {
            try {
                if(!client.storeItem(key, data, Constants.ACL_PRIVATE)) {
                    throw new RuntimeException("Upload failed");
                }
            } catch (IOException e) {
                Logger.error("Upload failed", e);
                throw new RuntimeException("Upload failed", e);
            }
            String downloadUrl = String.format("https://s3-eu-west-1.amazonaws.com/%s/%s", client.getBucket(), key);
            Logger.info(String.format("[S3Uploader] Uploaded to %s", downloadUrl));
            return downloadUrl;
        }
    }
}
