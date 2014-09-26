package models;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import play.data.validation.Check;
import siena.*;
import uk.gov.gds.dm.DocumentUtils;

import javax.persistence.Lob;
import java.io.*;

@Table("documents")
public class Document extends Model{

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
    public int listingId;

    @Column("pageId")
    @NotNull
    public int pageId;

    @Lob
    @Column("data")
    @Max(5400000)
    public byte[] data;

    public Document(String name, int listingId, int pageId, File file) {
        this.name = name;
        this.listingId = listingId;
        this.pageId = pageId;
        this.data = convertFileToBytesForStorage(file);
        this.type = getFileType(name);
    }

    public byte[] convertFileToBytesForStorage(File file){
        try {
                return FileUtils.readFileToByteArray(file);
            } catch (Exception e) {
                System.out.println("There was an error converting the file: " + e.getMessage());
            }
        System.out.println("Returning file input as null");
        return null;
    }

    public void pushDocumentToStorage() {
        try {
            FileOutputStream fos = new FileOutputStream("tmp/uploads/" + this.name);
            fos.write(this.data);
            fos.close();
        }
        catch(FileNotFoundException ex)   {
            System.out.println("FileNotFoundException : " + ex);
        }
        catch(IOException ioe)  {
            System.out.println("IOException : " + ioe);
        }

    }

    public String getFileType(String name){
        return FilenameUtils.getExtension(name);
    }
}
