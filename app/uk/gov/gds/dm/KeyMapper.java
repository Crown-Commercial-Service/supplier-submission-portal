package uk.gov.gds.dm;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KeyMapper {

        static final List<String> ARRAYS_OF_STRING = Arrays.asList("p1q1", "p2q1", "p3q1", "p5q1", "p5q2", "p10q1", 
                "p16q1", "p18q1", "p19q1", "p19q3", "p20q1", "p21q1");

        static final List<String> STRINGS = Arrays.asList("p4q1", "p4q2", "p6q1", "p7q2", "p7q3", "p8q1", "p8q1Unit", 
                "p8q1Interval", "p8q6", "p8q7", "p10q3", "p10q4", "p14q2", "p14q3", "p17q2", "p22q3");

        static final List<String> BOOLEANS = Arrays.asList("p7q1", "p8q2", "p8q3", "p8q4", "p8q5", "p9q1", "p10q2", 
                "p10q5", "p11q1", "p11q2", "p12q1", "p13q1", "p13q2", "p13q3", "p14q1", "p15q1", "p17q1", "p19q2",
                "p22q1", "p22q2", "p22q4", "p22q5");

        static final List<String> NUMBERS = Arrays.asList("p8q1MinPrice", "p8q1MaxPrice");

        static final List<String> ARRAYS_OF_STRING_WITH_ASSURANCE = Arrays.asList("p24q1","p24q2", "p31q1", 
                "p36q1", "p37q2");

        static final List<String> CONVERT_TO_ARRAY_OF_STRING_WITH_ASSURANCE = Arrays.asList("p23q1", "p23q2", "p23q3", 
                "p24q5", "p24q7");

        static final List<String> STRINGS_WITH_ASSURANCE = Arrays.asList("p24q3", "p24q6", "p24q7", "p25q1", "p25q2", 
                "p38q1", "p39q1", "p40q1");

        static final List<String> BOOLEANS_WITH_ASSURANCE = Arrays.asList("p24q4", "p24q8", "p24q9", "p25q3", "p25q4", 
                "p26q1", "p27q1", "p27q2", "p28q1", "p28q2", "p28q3", "p28q4", "p28q5", "p29q1", "p30q1", "p30q2", 
                "p30q3", "p32q1", "p32q2", "p32q3", "p33q1", "p33q2", "p33q3", "p33q4", "p33q5", "p34q1", "p34q2",
                "p35q1", "p35q2", "p35q3", "p37q1", "p40q2", "p40q3");

        static final List<String> NUMBERS_WITH_ASSURANCE = Arrays.asList("p24q10");

    static final Map<String, String> KEYS_MAP = ImmutableMap.<String, String> builder()
        .put("p1q1", "serviceTypes")                // Array of String

        .put("p2q1", "serviceTypes")                // Array of String

        .put("p3q1", "serviceTypes")                // Array of String

        .put("p4q1", "serviceName")                 // String
        .put("p4q2", "serviceSummary")              // String

        .put("p5q1", "serviceFeatures")             // Array of String
        .put("p5q2", "serviceBenefits")             // Array of String

        .put("p6q1", "serviceDefinitionDocument")   // String (=document filename)

        .put("p7q1", "terminationCost")             // Boolean
        .put("p7q2", "minimumContractPeriod")       // String
        .put("p7q3", "termsAndConditionsDocument")  // String (=document filename)

        .put("p8q1", "priceString")                 // String
        .put("p8q1MinPrice", "priceMin")            // String (use Number?)
        .put("p8q1MaxPrice", "priceMax")            // String (use Number?)
        .put("p8q1Unit", "priceUnit")               // String
        .put("p8q1Interval", "priceInterval")       // String
        .put("p8q2", "vatIncluded")                 // Boolean
        .put("p8q3", "educationPricing")            // Boolean
        .put("p8q4", "trialOption")                 // Boolean
        .put("p8q5", "freeOption")                  // Boolean
        .put("p8q6", "pricingDocument")             // String (=document filename)
        .put("p8q7", "sfiaRateDocument")            // String (=document filename)

        .put("p9q1", "openStandardsSupported")      // Boolean

        .put("p10q1", "supportTypes")               // Array of String
        .put("p10q2", "supportForThirdParties")     // Boolean
        .put("p10q3", "supportAvailability")        // String
        .put("p10q4", "supportResponseTime")        // String
        .put("p10q5", "incidentEscalation")         // Boolean

        .put("p11q1", "serviceOnboarding")          // Boolean
        .put("p11q2", "serviceOffboarding")         // Boolean

        .put("p12q1", "analyticsAvailable")         // Boolean

        .put("p13q1", "elasticCloud")               // Boolean
        .put("p13q2", "guaranteedResources")        // Boolean
        .put("p13q3", "persistentStorage")          // Boolean

        .put("p14q1", "selfServiceProvisioning")    // Boolean
        .put("p14q2", "provisioningTime")           // String
        .put("p14q3", "deprovisioningTime")         // String

        .put("p15q1", "openSource")                 // Boolean

        .put("p16q1", "codeLibraryLanguages")       // Array of String

        .put("p17q1", "apiAccess")                  // Boolean
        .put("p17q2", "apiType")                    // String

        .put("p18q1", "networksConnected")          // Array of String

        .put("p19q1", "supportedBrowsers")          // Array of String
        .put("p19q2", "offlineWorking")             // Boolean
        .put("p19q3", "supportedDevices")           // Array of String

        .put("p20q1", "vendorCertifications")       // Array of String

        .put("p21q1", "identityStandards")          // Array of String

        .put("p22q1", "datacentresEUCode")          // Boolean
        .put("p22q2", "datacentresSpecifyLocation") // Boolean
        .put("p22q3", "datacentreTier")             // String
        .put("p22q4", "dataBackupRecovery")         // Boolean
        .put("p22q5", "dataExtractionRemoval")      // Boolean

        .put("p23q1", "dataProtectionBetweenUserAndService")     // Object {answer:String[], assurance:String} ** Currently radio button but should be checkboxes?
        .put("p23q2", "dataProtectionWithinService")             // Object {answer:String[], assurance:String} ** Currently radio button but should be checkboxes?
        .put("p23q3", "dataProtectionBetweenServices")           // Object {answer:String[], assurance:String} ** Currently radio button but should be checkboxes?

        .put("p24q1", "datacentreLocations")                     // Object {answer:String[], assurance:String} 
        .put("p24q2", "dataManagementLocations")                 // Object {answer:String[], assurance:String}
        .put("p24q3", "legalJurisdiction")                       // Object {answer:String, assurance:String}
        .put("p24q4", "datacentreProtectionDisclosure")          // Object {answer:Boolean, assurance:String}
        .put("p24q5", "dataAtRestProtections")                   // Object {answer:String[], assurance:String} ** Currently radio button but should be checkboxes
        .put("p24q6", "dataSecureDeletion")                      // Object {answer:String, assurance:String}
        .put("p24q7", "dataMediaDisposal")                       // Object {answer:String[], assurance:String} ** Currently radio button but should be checkboxes? 
        .put("p24q8", "dataSecureEquipmentDisposal")             // Object {answer:Boolean, assurance:String}
        .put("p24q9", "dataRedundantEquipmentAccountsRevoked")   // Object {answer:Boolean, assurance:String}
        .put("p24q10", "serviceAvailabilityPercentage")          // Object {answer:Number, assurance:String}

        .put("p25q1", "cloudDeploymentModel")                    // Object {answer:String, assurance:String} 
        .put("p25q2", "otherConsumers")                          // Object {answer:String, assurance:String}
        .put("p25q3", "servicesSeparation")                      // Object {answer:Boolean, assurance:String}
        .put("p25q4", "servicesManagementSeparation")            // Object {answer:Boolean, assurance:String}

        .put("p26q1", "governanceFramework")                     // Object {answer:Boolean, assurance:String}

        .put("p27q1", "configurationTracking")                   // Object {answer:Boolean, assurance:String} 
        .put("p27q2", "changeImpactAssessment")                  // Object {answer:Boolean, assurance:String}

        .put("p28q1", "vulnerabilityAssessment")                 // Object {answer:Boolean, assurance:String} 
        .put("p28q2", "vulnerabilityMonitoring")                 // Object {answer:Boolean, assurance:String} 
        .put("p28q3", "vulnerabilityMitigationPrioritisation")   // Object {answer:Boolean, assurance:String} 
        .put("p28q4", "vulnerabilityTracking")                   // Object {answer:Boolean, assurance:String} 
        .put("p28q5", "vulnerabilityTimescales")                 // Object {answer:Boolean, assurance:String} 

        .put("p29q1", "eventMonitoring")                         // Object {answer:Boolean, assurance:String}

        .put("p30q1", "incidentManagementProcess")               // Object {answer:Boolean, assurance:String} 
        .put("p30q2", "incidentManagementReporting")             // Object {answer:Boolean, assurance:String} 
        .put("p30q3", "incidentDefinitionPublished")             // Object {answer:Boolean, assurance:String}

        .put("p31q1", "personnelSecurityChecks")                 // Object {answer:String[], assurance:String}

        .put("p32q1", "secureDevelopment")                       // Object {answer:Boolean, assurance:String} 
        .put("p32q2", "secureDesign")                            // Object {answer:Boolean, assurance:String} 
        .put("p32q3", "secureConfigurationManagement")           // Object {answer:Boolean, assurance:String} 

        .put("p33q1", "thirdPartyDataSharingInformation")        // Object {answer:Boolean, assurance:String} 
        .put("p33q2", "thirdPartySecurityRequirements")          // Object {answer:Boolean, assurance:String} 
        .put("p33q3", "thirdPartyRiskAssessment")                // Object {answer:Boolean, assurance:String} 
        .put("p33q4", "thirdPartyComplianceMonitoring")          // Object {answer:Boolean, assurance:String} 
        .put("p33q5", "hardwareSoftwareVerification")            // Object {answer:Boolean, assurance:String}

        .put("p34q1", "userAuthenticateManagement")              // Object {answer:Boolean, assurance:String} 
        .put("p34q2", "userAuthenticateSupport")                 // Object {answer:Boolean, assurance:String}

        .put("p35q1", "userAccessControlManagement")             // Object {answer:Boolean, assurance:String} 
        .put("p35q2", "restrictAdministratorPermissions")        // Object {answer:Boolean, assurance:String} 
        .put("p35q3", "managementInterfaceProtection")           // Object {answer:Boolean, assurance:String}

        .put("p36q1", "identityAuthenticationControls")          // Object {answer:String[], assurance:String} 

        .put("p37q1", "onboardingGuidance")                      // Object {answer:Boolean, assurance:String}
        .put("p37q2", "interconnectionMethods")                  // Object {answer:String[], assurance:String} 

        .put("p38q1", "serviceManagementModel")                  // Object {answer:String, assurance:String} 

        .put("p39q1", "auditInformationProvided")                // Object {answer:String, assurance:String} 

        .put("p40q1", "deviceAccessMethod")                      // Object {answer:String, assurance:String} 
        .put("p40q2", "serviceConfigurationGuidance")            // Object {answer:Boolean, assurance:String}
        .put("p40q3", "trainingProvided")                        // Object {answer:Boolean, assurance:String}

        .build();
}
