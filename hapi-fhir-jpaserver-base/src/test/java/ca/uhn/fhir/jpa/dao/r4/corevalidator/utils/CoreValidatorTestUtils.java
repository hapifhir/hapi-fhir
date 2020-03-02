package ca.uhn.fhir.jpa.dao.r4.corevalidator.utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.TestEntry;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.TestResult;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class CoreValidatorTestUtils {

    /**
     * Pulls the filename of the profile from the test entry. Returns null if no data is set.
     *
     * @param testEntry {@link TestEntry}
     * @return {@link String} filename of the profile to use in testing, or null if not set.
     */
    public static String getProfileFilename(TestEntry testEntry) {
        if (testEntry.getProfile() == null) return null;
        return testEntry.getProfile().getSource();
    }

    /**
     * Goes through the {@link OperationOutcome} and counts the error and warning counts, then compares those totals
     * to the expected values within the passes in {@link TestResult}.
     * <p>
     * These result counts are then compared using {@link Assertions#assertEquals(int, int)}.
     *
     * @param result {@link TestEntry} expected validation results.
     * @param oo     {@link OperationOutcome} actual validation results.
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

        System.out.println("Expected test output ::\n" + result + "\n");
        System.out.println("Actual test output ::\n" + prettyPrint(oo));

        int finalErrorCount = errorCount;
        int finalWarningCount = warningCount;
        Assertions.assertAll("Error counts and warnings should match test results from manifest.xml file...",
                () -> Assertions.assertEquals(result.getErrorCount(), finalErrorCount),
                () -> Assertions.assertEquals(result.getWarningCount(), finalWarningCount)
        );
    }

    private static String prettyPrint(OperationOutcome oo) {
        String output = "";
        for (OperationOutcome.OperationOutcomeIssueComponent i : oo.getIssue()) {
            output += i.getSeverity() + "\n";
            output += i.getDiagnostics() + "\n";
        }
        return output;
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
     * @param input
     * @param resourceDao The {@link IFhirResourceDao} to use to validate the passed in {@link IBaseResource}
     * @return The resulting {@link OperationOutcome} from validating the resource.
     */
    public static OperationOutcome validate(FhirContext ctx, String testProfile, String resourceName, String input, IFhirResourceDao resourceDao) {
        try {
            OperationOutcome operationOutcome = (OperationOutcome) resourceDao.validate(ctx.getResourceDefinition(resourceName).newInstance(), null, input,
                    EncodingEnum.detectEncoding(input), null, testProfile, null).getOperationOutcome();
            return operationOutcome;
        } catch (PreconditionFailedException e) {
            return (OperationOutcome) e.getOperationOutcome();
        } catch (DataFormatException | NullPointerException e) {
            OperationOutcome dataFormatOperationOutcome = new OperationOutcome();
            dataFormatOperationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).setDiagnostics(e.getMessage());
            return dataFormatOperationOutcome;
        }
    }
}
