import org.junit.*;
import play.mvc.Http;
import play.test.FunctionalTest;
import uk.gov.gds.dm.FixtureDataSetup;
import uk.gov.gds.dm.ServiceSubmissionJourneyFlows;

import java.util.HashSet;
import java.util.Set;

public class FlowTest extends FunctionalTest {

    private final static String[] flows = {"IaaS", "SaaS", "PaaS", "SCS"};
    
    @Test
    public void checkCorrectPagesCanBeAccessedForAllFlows() {
        for (String flow : flows) {
            Long listingId = FixtureDataSetup.createListing(flow);
            checkCorrectPagesCanBeAccessedForFlow(flow, listingId);
            FixtureDataSetup.deleteListing(listingId);
        }
    }

    private void checkCorrectPagesCanBeAccessedForFlow(String flowType, Long listingId) {
        for (String pageId : ServiceSubmissionJourneyFlows.getFlow(flowType)){
            testThatPageWorks(String.format("/page/%s/%d", pageId, listingId));
        }

        for (String pageId : pagesNotInFlow(flowType)){
            testThatPageNotInFlowDoesNotWork(String.format("/page/%s/%d", pageId, listingId));
        }
    }
    
    private void testThatPageWorks(String url) {
        Http.Response response = GET(url);
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    private void testThatPageNotInFlowDoesNotWork(String url) {
        Http.Response response = GET(url);
        assertIsNotFound(response);
    }
    
    private Set<String> pagesNotInFlow(String flow) {
        Set<String> notInFlow = new HashSet<String>();
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("SaaS"));
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("PaaS"));
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("SCS"));
        notInFlow.addAll(ServiceSubmissionJourneyFlows.getFlow("IaaS"));
        notInFlow.removeAll(ServiceSubmissionJourneyFlows.getFlow(flow));
        return notInFlow;
    }
}
