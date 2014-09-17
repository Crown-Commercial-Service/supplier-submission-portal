package uk.gov.gds.dm;

import java.util.Arrays;
import java.util.List;

public class ServiceSubmissionJourneyFlows {
    
    private final static List<String> saasFlow = Arrays.asList("start", "1a", "2");
    private final static List<String> iaasFlow = Arrays.asList("start", "1b", "2");
    private final static List<String> paasFlow = Arrays.asList("start", "1c", "2");
    private final static List<String> scsFlow = Arrays.asList("start", "1d", "2");
    
    public static List<String> getFlow(String lot) throws IllegalArgumentException {
        if (lot.equals("SaaS")) {
            return saasFlow;
        }
        if (lot.equals("IaaS")) {
            return iaasFlow;
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
