package uk.gov.gds.dm;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class DocumentUtils {

    public static final int MAX_FILE_SIZE = 5400000;

    public static boolean validateDocumentFormat(File file){
        String fileType = FilenameUtils.getExtension(file.getName());
        return (fileType.equals("pdf") || fileType.equals("odf") || fileType.equals("pda"));
    }

    public static boolean validateDocumentFileSize(File file){
        return (file.length() <= MAX_FILE_SIZE);
    }
}
