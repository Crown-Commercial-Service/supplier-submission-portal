package unit;

import play.Play;
import play.test.UnitTest;
import org.junit.*;
import uk.gov.gds.dm.Security;
import java.util.regex.Pattern;

public class SecurityTest extends UnitTest {

    @Test
    public void testSupplierShouldBeAllowedOnQa() {
        String appName = Play.configuration.getProperty("application.name");
        Play.configuration.setProperty("application.name", "ssp-qa");

        String testEmail = "digital-marketplace-development+test-supplier-100@digital.cabinet-office.gov.uk";
        assertTrue(Security.supplierIsAllowed("supplier-id", testEmail));

        String nonTestEmail = "invalid-email@digital.cabinet-office.gov.uk";
        assertFalse(Security.supplierIsAllowed("supplier-id", nonTestEmail));

        Play.configuration.setProperty("application.name", appName);
    }
}
