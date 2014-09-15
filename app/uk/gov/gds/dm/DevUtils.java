package uk.gov.gds.dm;

import java.util.Random;

/**
 * This class is purely a placeholder for things that are not yet implemented.
 * Should be deleted when features are implemented.
 */
public class DevUtils {
    
    public final static String supplierId1 = "test-supplier-id-1";
    public final static String supplierId2 = "test-supplier-id-2";
    
    private final static Random rand = new Random();
    
    public static String randomSupplierId() {
        int randomNum = rand.nextInt(2);
        switch(randomNum) {
            case 0:
                return supplierId1;
            case 1:
                return supplierId2;
            default:
                // Should never happen
                return "DEFAULT-" + randomNum;
        }
    }
}

