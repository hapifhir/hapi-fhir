package ca.uhn.fhir.jpa.dao.r4.corevalidator.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.TestEntry;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.TestResult;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class CoreValidatorTestUtils {

    /**
     * Goes through the {@link OperationOutcome} and counts the error and warning counts, then compares those totals
     * to the expected values within the passes in {@link TestResult}.
     *
     * These result counts are then compared using {@link Assertions#assertEquals(int, int)}.
     *
     * @param result {@link TestEntry} expected validation results.
     * @param oo {@link OperationOutcome} actual validation results.
     */
    public static void testOutputs(TestResult result, OperationOutcome oo) {

        int errorCount = 0;
        int warningCount = 0;

        List<OperationOutcome.OperationOutcomeIssueComponent> issues = oo.getIssue() == null ? new ArrayList<>() : oo.getIssue();
        for (OperationOutcome.OperationOutcomeIssueComponent o : issues) {
            switch (o.getSeverity()) {
                case ERROR:
                    errorCount++;
                    break;
                case WARNING:
                    warningCount++;
                    break;
                case INFORMATION:
                case FATAL:
                case NULL:
                default:
                    break;
            }
        }

        System.out.println("");

        int finalErrorCount = errorCount;
        int finalWarningCount = warningCount;
        Assertions.assertAll("Error counts and warnings should match test results from manifest.xml file...",
                () -> Assertions.assertEquals(result.getErrorCount(), finalErrorCount),
                () -> Assertions.assertEquals(result.getWarningCount(), finalWarningCount)
        );
    }

    /**
     * Examines the passed in filename and returns the appropriate {@link EncodingEnum}
     *
     * @param testFile {@link String} filename. ie "Person.json"
     * @return {@link EncodingEnum}
     */
    public static EncodingEnum getEncoding(String testFile) {
        EncodingEnum encoding;
        if (testFile.endsWith(".json")) {
            encoding = EncodingEnum.JSON;
        } else {
            encoding = EncodingEnum.XML;
        }
        return encoding;
    }

    /**
     * Validates the passed in {@link IBaseResource} using the provided {@link IFhirResourceDao} then returns the
     * {@link OperationOutcome} of the result.
     *
     * @param input The {@link IBaseResource} to validate.
     * @param enc {@link EncodingEnum}
     * @param resourceDao The {@link IFhirResourceDao} to use to validate the passed in {@link IBaseResource}
     * @param mySrd {@link ServletRequestDetails}
     * @param myFhirCtx Current {@link FhirContext}
     *
     * @return The resulting {@link OperationOutcome} from validating the resource.
     */
    public static <T extends IBaseResource> OperationOutcome validate(IBaseResource resource, String input, EncodingEnum enc, IFhirResourceDao resourceDao, ServletRequestDetails mySrd, FhirContext myFhirCtx) {

        try {
            return (OperationOutcome) resourceDao.validate(resource, null, input, EncodingEnum.detectEncoding(input), null, null, null).getOperationOutcome();
        } catch (PreconditionFailedException e) {
            return (OperationOutcome) e.getOperationOutcome();
        }
    }
}
