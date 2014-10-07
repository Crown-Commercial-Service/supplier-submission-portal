package uk.gov.gds.dm;

import org.apache.commons.io.FilenameUtils;
import play.data.Upload;

import java.io.File;

public class DocumentUtils {

    public static final int MAX_FILE_SIZE = 5400000;

    public static boolean validateDocumentFormat(Upload file){
        String fileType = FilenameUtils.getExtension(file.getFileName().toLowerCase());
        return (fileType.equals("pdf") || fileType.equals("odf") || fileType.equals("pda"));
    }

    public static boolean validateDocumentFileSize(Upload file){
        return (file.getSize() <= MAX_FILE_SIZE);
    }
}
