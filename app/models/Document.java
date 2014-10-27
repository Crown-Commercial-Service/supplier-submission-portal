package models;

import org.apache.commons.io.FilenameUtils;
import siena.*;
import siena.embed.EmbeddedMap;
import uk.gov.gds.dm.DocumentUtils;
import uk.gov.gds.dm.S3Uploader;

@EmbeddedMap
public class Document extends Model {

    private transient byte[] bytes;
    private transient String supplierName;
    private static final S3Uploader uploader = new S3Uploader();
    // For GAE :
    // 1. @Id annotated field corresponding to the primary key must be Long type
    // 2. @Id annotated field corresponding to the primary key must be called "id"
    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    @Column("name")
    @NotNull
    public String filename;

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

    public Document(String name, long listingId, String questionId, byte[] bytes, String supplierName) {
        this.filename = name;
        this.listingId = listingId;
        this.questionId = questionId;
        this.type = getFileType(name);
        this.bytes = bytes;
        this.supplierName = supplierName;
    }

    public void pushDocumentToStorage() {

        String documentKey = String.format("%s/%d/%s", this.supplierName, this.listingId, DocumentUtils.s3Filename(this.listingId, this.questionId, this.filename));
        String documentUrl = uploader.upload(this.bytes, documentKey);
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

    public static DocumentBuilder withName(String name) {
        return new DocumentBuilder().withName(name);
    }

    public static DocumentBuilder forSupplier(String bucket) {
        return new DocumentBuilder().forSupplier(bucket);
    }

    public static class DocumentBuilder {

        private String question;
        private long listingId;
        private String filename;
        private String supplierName;
        private byte[] bytes;

        public DocumentBuilder forQuestion(String question) {
            this.question = question;
            return this;
        }

        public DocumentBuilder forListing(long listingId) {
            this.listingId = listingId;
            return this;
        }

        public DocumentBuilder withName(String name) {
            this.filename = name;
            return this;
        }

        public Document build() {
            return new Document(filename, listingId, question, bytes, supplierName);
        }

        public DocumentBuilder forSupplier(String supplierName) {
            this.supplierName = supplierName;
            return this;
        }

        public DocumentBuilder withBytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", filename='" + filename + "'" +
                ", listingId='" + listingId + "'" +
                ", questionId='" + questionId + "'" +
                ", type='" + type + "'" +
                ", supplierName='" + supplierName + "'" +
                '}';
    }
}
