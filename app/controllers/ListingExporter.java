package controllers;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import models.Listing;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import uk.gov.gds.dm.DocumentUtils;
import uk.gov.gds.dm.ListingToJSONConverter;
import uk.gov.gds.dm.S3Uploader;

import java.util.List;
import java.util.Arrays;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

public class ListingExporter extends Controller {

    private static final S3Uploader completedUploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.completed.export.bucket.name")));
    private static final S3Uploader draftUploader = new S3Uploader(String.valueOf(Play.configuration.get("s3.draft.export.bucket.name")));

    private final static int EXPORT_PAGE_SIZE = 500;

    private final static List<String> OK_SUPPLIER_IDS  = Arrays.asList("92187","92188","92189","92191","92192","92193","92195","92196","92197",
            "92198","92199","92200","92201","92204","92205","92206","92208","92209","92210","92211","92212","92213","92214","92215","92216",
            "92217","92218","92219","92220","92222","92223","92224","92225","92227","92228","92229","92230","92231","92232","92234","92237",
            "92238","92240","92241","92245","92246","92247","92248","92249","92250","92251","92252","92253","92254","92256","92257","92258",
            "92259","92260","92261","92264","92266","92267","92269","92270","92271","92272","92273","92274","92275","92276","92277","92278",
            "92281","92282","92283","92284","92286","92287","92288","92289","92290","92291","92292","92293","92294","92295","92296","92297",
            "92299","92300","92301","92302","92303","92304","92305","92306","92307","92308","92309","92310","92311","92312","92314","92315",
            "92316","92321","92322","92323","92324","92325","92326","92327","92329","92330","92331","92333","92334","92335","92336","92337",
            "92338","92339","92340","92341","92342","92343","92344","92345","92346","92347","92348","92349","92350","92351","92352","92355",
            "92356","92357","92359","92360","92362","92363","92365","92366","92367","92368","92369","92371","92372","92373","92376","92377",
            "92379","92380","92382","92383","92384","92387","92388","92390","92391","92392","92393","92395","92396","92397","92399","92400",
            "92401","92402","92404","92406","92407","92408","92409","92410","92412","92415","92416","92417","92418","92419","92421","92422",
            "92423","92424","92425","92426","92427","92428","92429","92430","92431","92433","92434","92436","92437","92438","92439","92440",
            "92441","92442","92445","92446","92447","92448","92449","92450","92452","92453","92454","92455","92456","92457","92458","92459",
            "92461","92462","92463","92464","92465","92466","92467","92468","92469","92471","92472","92473","92475","92476","92477","92478",
            "92479","92481","92482","92483","92485","92486","92487","92489","92490","92491","92494","92495","92496","92497","92499","92500",
            "92501","92502","92504","92505","92507","92508","92509","92510","92511","92512","92515","92517","92518","92520","92523","92524",
            "92525","92527","92528","92529","92530","92531","92532","92533","92534","92536","92538","92540","92541","92542","92543","92544",
            "92546","92547","92548","92549","92553","92554","92555","92557","92559","92561","92563","92564","92565","92566","92568","92569",
            "92570","92571","92574","92575","92577","92578","92579","92580","92582","92584","92585","92589","92591","92593","92594","92596",
            "92597","92598","92603","92604","92605","92606","92607","92608","92609","92614","92615","92616","92618","92619","92620","92622",
            "92623","92624","92627","92628","92629","92630","92631","92633","92634","92635","92637","92641","92642","92645","92646","92648",
            "92649","92650","92651","92652","92654","92656","92658","92659","92660","92664","92665","92666","92673","92677","92679","92682",
            "92683","92684","92686","92687","92689","92692","92693","92695","92698","92699","92700","92706","92709","92710","92711","92712",
            "92713","92714","92716","92718","92719","92720","92722","92724","92726","92728","92729","92730","92731","92732","92736","92737",
            "92738","92739","92742","92745","92747","92750","92751","92752","92753","92755","92756","92757","92758","92760","92762","92768",
            "92769","92771","92773","92774","92775","92778","92779","92780","92781","92782","92783","92784","92786","92787","92788","92789",
            "92790","92792","92794","92795","92796","92799","92800","92801","92805","92806","92808","92810","92811","92812","92813","92817",
            "92819","92821","92822","92823","92824","92825","92826","92827","92829","92831","92833","92834","92835","92838","92839","92842",
            "92843","92845","92847","92848","92849","92850","92851","92852","92853","92856","92857","92860","92861","92863","92864","92865",
            "92866","92869","92872","92873","92874","92876","92877","92881","92883","92884","92885","92886","92887","92888","92890","92891",
            "92892","92893","92898","92899","92900","92901","92903","92904","92905","92907","92908","92909","92910","92911","92913","92914",
            "92915","92917","92918","92920","92922","92924","92925","92926","92927","92928","92930","92935","92936","92937","92939","92941",
            "92943","92945","92947","92948","92949","92950","92951","92954","92955","92956","92958","92960","92961","92963","92966","92967",
            "92973","92974","92975","92976","92978","92980","92981","92982","92983","92985","92987","92989","92990","92991","92992","92993",
            "92994","92995","92996","92997","92998","93007","93009","93011","93012","93014","93015","93017","93018","93019","93020","93021",
            "93024","93025","93027","93028","93029","93030","93032","93033","93035","93036","93037","93041","93043","93046","93047","93050",
            "93053","93054","93055","93056","93057","93059","93060","93061","93062","93063","93064","93066","93069","93072","93074","93075",
            "93078","93079","93080","93082","93083","93084","93085","93089","93090","93097","93098","93101","93102","93104","93105","93106",
            "93111","93115","93116","93118","93121","93122","93123","93126","93127","93128","93129","93131","93134","93135","93139","93141",
            "93142","93144","93146","93149","93151","93153","93155","93156","93160","93161","93162","93163","93164","93165","93168","93170",
            "93171","93174","93176","93178","93179","93180","93184","93185","93186","93187","93188","93189","93190","93191","93193","93195",
            "93196","93197","93200","93201","93202","93203","93205","93208","93210","93212","93213","93215","93216","93217","93220","93221",
            "93223","93226","93227","93228","93229","93232","93234","93241","93244","93246","93248","93250","93251","93252","93253","93255",
            "93256","93257","93258","93260","93265","93266","93268","93271","93272","93273","93279","93282","93283","93290","93292","93294",
            "93296","93298","93300","93301","93303","93304","93305","93307","93308","93309","93312","93313","93315","93317","93319","93320",
            "93322","93324","93325","93326","93327","93329","93331","93332","93333","93339","93342","93343","93344","93345","93346","93347",
            "93348","93349","93353","93354","93356","93357","93359","93361","93363","93367","93370","93371","93372","93373","93375","93376",
            "93377","93378","93380","93381","93382","93383","93384","93387","93388","93392","93393","93394","93396","93398","93400","93405",
            "93406","93408","93409","93413","93419","93420","93424","93425","93429","93433","93438","93440","93444","93445","93447","93451",
            "93453","93454","93455","93456","93457","93459","93460","93461","93462","93464","93466","93467","93471","93473","93475","93476",
            "93477","93478","93481","93483","93485","93487","93488","93490","93491","93494","93496","93497","93498","93499","93500","93503",
            "93506","93514","93517","93518","93520","93521","93523","93524","93525","93526","93527","93528","93529","93531","93532","93535",
            "93537","93538","93541","93542","93543","93544","93545","93547","93550","93551","93552","93554","93555","93557","93559","93561",
            "93563","93565","93566","93567","93568","93570","93571","93572","93577","93579","93582","93583","93584","93585","93587","93589",
            "93591","93592","93594","93595","93598","93599","93601","93602","93603","93606","93607","93608","93610","93611","93613","93615",
            "93617","93618","93620","93623","93624","93626","93627","93631","93633","93637","93641","93643","93647","93649","93653","93660",
            "93667","93669","93672","93676","93679","93681","93683","93689","93690","93694","93695","93696","93697","93702","579024","579026",
            "579028","579067","579085","579086","579088","579090","579107","579114","579127","579139","579150","579162","579163","579173",
            "579179","579197","579198","579199","579200","579226","579237","579249","579274","579281","579292","579299","579312","579324",
            "579331","579334","579347","579359","579371","579374","579387","579389","579420","579431","579472","579474","579492","579506",
            "579509","579511","579521","579533","579553","579559","579561","579563","579569","579575","579591","579603","579606","579613",
            "579634","579636","579644","579650","579651","579652","579663","579672","579675","579699","579700","579715","579716","579722",
            "579726","579740","579754","579774","579783","579787","579844","579845","579849","579870","579876","579884","579887","579895",
            "579897","579935","579949","579951","579954","579966","579998","580020","580025","580027","580037","580045","580047","580070",
            "580073","580099","580120","580128","580132","580142","580146","580147","580150","580156","580157","580161","580170","580172",
            "580183","580191","580215","580225","580248","580271","580283","580285","580286","580292","580298","580299","580310","580328",
            "580332","580340","580341","580346","580350","580351","580396","580400","580401","580410","580420","580425","580428","580433",
            "580434","580436","580438","580441","580447","580450","580453","580489","580500","580509","580514","580525","580533","580535",
            "580565","580587","580611","580646","580714","580736","580743","580876","580908","580910","580940","580947","580948","580951",
            "580983","580995","580999","581002","581019","581024","581031","581032","581033","581044","581052","581063","581109","581192",
            "581206","581232","581236","581238","581239","581251","581271","581278","581302","581317","581387","581398","581545","581561",
            "581585","581617","581633","581651","581653","581716","581721","581903","582159","582282","582283","582296","582315","582318",
            "582332","582362","582373","582376","582391","582398","582405","582413","582416","582427","582431","582448","582474","582592",
            "582659","582665","582687","582691","582778","582793","582798","582832","582938","582946","582949","582977","583011","583016",
            "583033","583035","583046","583053","583054","583065","583070","583071","583086","583109","583130","583134","583145","583149",
            "583167","583209","583216","583231","583318","583325","583337","583353","583360","583378","583392","583399","583420","583459",
            "583469","583471","583479","583480","583516","583651","583662","583667","583703","583742","583758","583766","583811","583947",
            "583957","584101","584178","584339","584362","584370","584384","584394","584407","584408","584424","584425","584428","584453",
            "584487","584488","584508","584522","584558","584561","584576","584614","584628","584711","584723","584735","584749","584783",
            "584790","584802","584805","584806","584833","584859","584865","584902","584933","584942","584951","584952","584955","584972",
            "584982","584997","585023","585143","585188","585224","585236","585243","585261","585262","585264","585274","585291","585292",
            "585293","585306","585314","585346","585351","585359","585378","585383","585384","585413","585425","585509","585519","585535",
            "585564","585575","585577","585580","585590","585596","585604","585615","585630","585634","585636","585659","585669","585670",
            "585693","585699","585706","585707","585716","585732","585734","585735","585754","585757","585758","585784","585791","585793",
            "585820","585843","585858","585865","585872","585875","585895","585983","586007","586016","586039","586042","586043","586073",
            "586080","586081","586088","586095","586110","586121","586125","586136","586142","586158","586176","586184","586185","586197",
            "586209","586210","586212","586220","586221","586236","586237","586241","586269","586278","586318","586334","586380","586394",
            "586409","586412","586413","586422","586440","586443","586447","586448","586453","586469","586471","586472","586485","586493",
            "586523","586550","586551","586553","586595","586634","586639","586649","586661","586662","586664","586675","586694","586706",
            "586728","586729","586733","586751","586772","586784","586785","586800","586807","586808","586814","586823","586830","586872",
            "586909","586937","586945","586955","586960","586988","587016","587054","587057","587067","587068","587076","587087","587100",
            "587126","587129","587130","587146","587165","587167","587173","587179","587180","587200","587208","587210","587219","587232",
            "587234","587235","587236","587241","587270","587271","587276","587283","587286","587299","587323","587324","587327","587332",
            "587346","587350","587351","587352","587370","587373","587385","587409","587420","587426","587480","587493","587524","587527",
            "587562","587569","587571","587578","587606","587635","587639","587640","587645","587681","587685","587692");

    public static void exportCompletedListingsAsJson() {

        Queue queue = QueueFactory.getDefaultQueue();
        try {
            queue.add(withUrl("/cron/paginatedexport"));
        } catch (Exception ex) {
            Logger.error(ex, "Error adding export task to the queue");
        }
        ok();
    }

    public static void paginatedExport(String cursor) {
        Queue queue = QueueFactory.getDefaultQueue();
        String dateString = DocumentUtils.dateString();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(EXPORT_PAGE_SIZE);

        if (cursor != null) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
        }

        Query q = new Query("listing");
        PreparedQuery pq = datastore.prepare(q);
        QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);

        boolean thereWereResultsThisTime = false;
        for (Entity listing : results) {
            if ((boolean)listing.getProperty("serviceSubmitted")) {
                // It's a completed listing
                try {
                    if (OK_SUPPLIER_IDS.contains(listing.getProperty("supplierId"))) {
                        queue.add(withUrl("/cron/exportcompleted").param("date", dateString).param("id", Long.toString(listing.getKey().getId())));
                    }
                } catch (Exception ex) {
                    Logger.error(ex, "Error adding a completed listing to the export queue");
                }
            } else {
                // It's a draft listing
                try {
                        queue.add(withUrl("/cron/exportdraft").param("date", dateString).param("id", Long.toString(listing.getKey().getId())));
                } catch (Exception ex) {
                    Logger.error(ex, "Error adding a draft listing to the export queue");
                }
            }
            thereWereResultsThisTime = true;
        }

        String cursorString = results.getCursor().toWebSafeString();

        if (thereWereResultsThisTime) {
            try {
                // We haven't got to the end yet - add a task to fetch the next page of listings
                queue.add(withUrl("/cron/paginatedexport").param("cursor", cursorString));
            } catch (Exception ex) {
                Logger.error(ex, "Error adding export task to the queue");
            }
        }
        ok();
    }

    public static void exportCompletedListing(String date, Long id) {
        exportListing(date, id, completedUploader);
    }

    public static void exportDraftListing(String date, Long id) {
        exportListing(date, id, draftUploader);
    }

    private static void exportListing(String date, Long id, S3Uploader uploader) {
        Listing listing = Listing.getByListingId(id);
        if(listing == null) {
            Logger.warn(String.format("Export listing: Invalid listing id [%d] provided", id));
            return;
        }
        String listingJSON = ListingToJSONConverter.convertToJson(listing);
        byte[] listingBytes;
        try {
            listingBytes = listingJSON.getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException ex) {
            listingBytes = listingJSON.getBytes();
        }
        String documentKey = String.format("%s/%s/%s", date, listing.supplierId, DocumentUtils.s3ExportFilename(listing.id));
        String documentUrl = uploader.upload(listingBytes, documentKey);
        Logger.info(String.format("Uploaded listing to: %s", documentUrl));
    }
}
