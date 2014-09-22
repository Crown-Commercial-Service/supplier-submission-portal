package uk.gov.gds.dm;

import java.util.Arrays;
import java.util.List;

public class ServiceSubmissionJourneyFlows {

    private final static List<Long> iaasFlow = Arrays.asList(1l, 4l);
    private final static List<Long> saasFlow = Arrays.asList(2l, 4l);
    private final static List<Long> paasFlow = Arrays.asList(4l);
    private final static List<Long> scsFlow = Arrays.asList(3l, 4l);
    
    public static List<Long> getFlow(String lot) throws IllegalArgumentException {
        if (lot.equals("IaaS")) {
            return iaasFlow;
        }
        if (lot.equals("SaaS")) {
            return saasFlow;
        }
        if (lot.equals("PaaS")) {
            return paasFlow;
        }
        if (lot.equals("SCS")) {
            return scsFlow;
        }
        throw new IllegalArgumentException("Invalid lot type.");
    } 
}
