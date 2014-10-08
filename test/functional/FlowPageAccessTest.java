package functional;

import controllers.Fixtures;
import org.junit.*;
import play.Logger;
import play.mvc.Http;
import play.test.FunctionalTest;
import uk.gov.gds.dm.FixtureDataSetup;
import uk.gov.gds.dm.ServiceSubmissionJourneyFlows;

import java.util.HashSet;
import java.util.Set;

public class FlowPageAccessTest extends FunctionalTest {

    private final static String[] flows = {"IaaS", "SaaS", "PaaS", "SCS"};
    
    @Before
    public void loadFixtures() {
        Logger.info("Loading fixtures...");
        Http.Response response = GET("/fixtures");
        Logger.info("Loaded fixtures: " + response.status + " - " + response.toString());
    }

    @Test
    public void checkCorrectPagesCanBeAccessedForAllFlows() {
        for (String flow : flows) {
            Long listingId = FixtureDataSetup.createListing(flow);
            checkCorrectPagesCanBeAccessedForFlow(flow, listingId);
            FixtureDataSetup.deleteListing(listingId);
        }
    }

    private void checkCorrectPagesCanBeAccessedForFlow(String flowType, Long listingId) {
        for (Long pageId : ServiceSubmissionJourneyFlows.getFlow(flowType)){
            Logger.info(String.format("   Testing page works [%s]: /page/%d/%d", flowType, pageId, listingId)); 
            testThatPageWorks(String.format("/page/%d/%d", pageId, listingId));
        }

        for (Long pageId : pagesNotInFlow(flowType)){
            Logger.info(String.format("   Testing page doesn't work [%s]: /page/%d/%d", flowType, pageId, listingId));
            testThatPageNotInFlowDoesNotWork(String.format("/page/%d/%d", pageId, listingId));
        }
    }
    
    private void testThatPageWorks(String url) {
        Http.Response response = GET(url);
        Logger.info("     Response: " + response.status);
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    private void testThatPageNotInFlowDoesNotWork(String url) {
        Http.Response response = GET(url);
        Logger.info("     Response: " + response.status);
        assertIsNotFound(response);
    }
    
    private Set<Long> pagesNotInFlow(String flow) {
        Set<Long> notInFlow = new HashSet<Long>();
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("SaaS"));
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("PaaS"));
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("SCS"));
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("IaaS"));
        notInFlow.removeAll(ServiceSubmissionJourneyFlows.getFlow(flow));
        return notInFlow;
    }
}
