package controllers;

import models.Listing;
import models.Page;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import com.elevenware.util.tokenlib.SimpleAesEncryptor;
import uk.gov.gds.dm.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;


public abstract class AbstractQuestionPage extends Controller {


    static  String supplierIdFromCookie = "";
    static String supplierName = "";

    @Before
    public static void checkAuthentificationCookie() {

        if(request.current().cookies.get("gdmssosession")!=null){
            supplierIdFromCookie =  Security.getSupplierIdFromCookie(request.cookies.get("gdmssosession"));
            supplierName = Security.getSupplierNameFromCookie(request.cookies.get("gdmssosession"));
        }
        else{
            Logger.info("cookie not exist/ expired");
            redirect("https://digitalmarketplace.service.gov.uk/digitalmarketplace");
        }
    }
}
