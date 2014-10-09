package uk.gov.gds.dm;

import java.util.Arrays;
import java.util.List;

public class ServiceSubmissionJourneyFlows {

    private final static List<Long> iaasFlow = Arrays.asList(1l, 4l, 5l, 6l, 7l, 8l, 9l, 10l, 11l, 12l, 13l, 14l, 15l, 17l, 18l, 19l, 20l, 22l, 23l, 24l, 25l, 26l, 27l, 28l, 29l, 30l, 31l, 32l, 33l, 34l, 35l, 36l, 37l, 38l, 39l, 40l);
    private final static List<Long> saasFlow = Arrays.asList(2l, 4l, 5l, 6l, 7l, 8l, 9l, 10l, 11l, 12l, 13l, 14l, 15l, 16l, 17l, 18l, 19l, 20l, 21l, 22l, 23l, 24l, 25l, 26l, 27l, 28l, 29l, 30l, 31l, 32l, 33l, 34l, 35l, 36l, 37l, 38l, 39l, 40l);
    private final static List<Long> paasFlow = Arrays.asList(4l, 5l, 6l, 7l, 8l, 9l, 10l, 11l, 12l, 13l, 14l, 15l, 17l, 18l, 19l, 20l, 22l, 23l, 24l, 25l, 26l, 27l, 28l, 29l, 30l, 31l, 32l, 33l, 34l, 35l, 36l, 37l, 38l, 39l, 40l);
    private final static List<Long> scsFlow = Arrays.asList(3l, 4l, 5l, 6l, 7l, 8l, 10l, 20l, 24l, 28l, 30l);

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
