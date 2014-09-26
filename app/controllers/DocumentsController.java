package controllers;

import models.Document;
import play.mvc.*;
import java.io.File;
import java.util.List;

import uk.gov.gds.dm.DocumentUtils;


public class DocumentsController extends Controller{

    public static void captureDocuments() {
        List<Document> documents = Document.all(Document.class).fetch();
        render(documents);
    }

    public static void submitDocuments(int listingId, int pageId, File doc) {

        if(!DocumentUtils.validateDocumentFormat(doc)){
            System.out.println("Document did not upload - incorrect file type.");
        } else {
            if(!DocumentUtils.validateDocumentFileSize(doc)){
                System.out.println("Document did not upload - file is too large.");
            } else {
                Document document = new Document(doc.getName(), listingId, pageId, doc);
                document.insert();
                document.pushDocumentToStorage();
            }
        }
        redirect("/documents");
    }
}
