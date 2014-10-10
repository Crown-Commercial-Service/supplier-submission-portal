package models;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import play.Play;
import play.data.validation.Check;
import siena.*;
import uk.gov.gds.dm.DocumentUtils;
import uk.gov.gds.dm.S3Uploader;

import javax.persistence.Lob;
import java.io.*;

@Table("documents")
public class Document extends Model{

    private transient File file;
    private transient String supplierName;
    private static final S3Uploader uploader = new S3Uploader();
    // For GAE :
    // 1. @Id annotated field corresponding to the primary key must be Long type
    // 2. @Id annotated field corresponding to the primary key must be called "id"
    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    @Column("name")
    @NotNull
    public String name;

    @Column("type")
    @NotNull
    public String type;

    @Column("listingId")
    @NotNull
    public long listingId;

    @Column("questionId")
    @NotNull
    public String questionId;

    @Column("documentUrl")
    @NotNull
    public String documentUrl;

    public Document(String name, long listingId, String questionId, File file, String supplierName) {
        this.name = name;
        this.listingId = listingId;
        this.questionId = questionId;
        this.type = getFileType(name);
        this.file = file;
        this.supplierName = supplierName;
    }

    public void pushDocumentToStorage() {

        String bucket = String.valueOf(Play.configuration.get("application.s3.bucket.name"));
        String documentKey = String.format("%s/%d/%s/%s", this.supplierName, this.listingId, this.questionId, this.name);
        String documentUrl = uploader.upload(this.file, bucket, documentKey);
        this.documentUrl = documentUrl;

    }

    public String getFileType(String name){
        return FilenameUtils.getExtension(name);
    }

    public static DocumentBuilder forQuestion(String question) {
        return new DocumentBuilder().forQuestion(question);
    }

    public static DocumentBuilder forListing(long listingId) {
        return new DocumentBuilder().forListing(listingId);
    }

    public static DocumentBuilder fromFile(File file) {
        return new DocumentBuilder().fromFile(file);
    }

    public static DocumentBuilder withName(String name) {
        return new DocumentBuilder().withName(name);
    }

    public static DocumentBuilder forSupplier(String bucket) {
        return new DocumentBuilder().forSupplier(bucket);
    }

    public static class DocumentBuilder {

        private String question;
        private long listingId;
        private File file;
        private String name;
        private String supplierName;

        public DocumentBuilder forQuestion(String question) {
            this.question = question;
            return this;
        }

        public DocumentBuilder forListing(long listingId) {
            this.listingId = listingId;
            return this;
        }

        public DocumentBuilder fromFile(File file) {
            this.file = file;
            return this;
        }

        public DocumentBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public Document build() {
            return new Document(name, listingId, question, file, supplierName);
        }

        public DocumentBuilder forSupplier(String supplierName) {
            this.supplierName = supplierName;
            return this;
        }

    }
}
