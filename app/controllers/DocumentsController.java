package controllers;

import models.Document;
import play.data.Upload;
import play.mvc.*;

import java.util.List;

import uk.gov.gds.dm.DocumentUtils;


public class DocumentsController extends Controller{

    public static void captureDocuments() {
        List<Document> documents = Document.all(Document.class).fetch();
        render(documents);
    }

    public static void submitDocuments(int listingId, int pageId, Upload doc) {

        if(!DocumentUtils.validateDocumentFormat(doc)){
            System.out.println("Document did not upload - incorrect file type.");
        } else {
            if(!DocumentUtils.validateDocumentFileSize(doc)){
                System.out.println("Document did not upload - file is too large.");
            } else {
//                Document document = new Document(doc.getFileName(), listingId, pageId, doc.asFile(), "", "");
//                document.insert();
//                document.pushDocumentToStorage();
            }
        }
        redirect("/documents");
    }
}
