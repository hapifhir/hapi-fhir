package ca.uhn.fhir.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class ValidatorInstantiatorDstu2Test {

    @AfterAll
    public static void afterClassClearContext() {
        TestUtil.randomizeLocaleAndTimezone();
    }

    private static FhirContext ourCtx = FhirContext.forDstu2();

    @Test
    public void testValidator() {

        FhirValidator val = ourCtx.newValidator();
        val.validateWithResult(new Patient());

        // We have a full classpath, so take advantage
        assertTrue(val.isValidateAgainstStandardSchema());
        assertTrue(val.isValidateAgainstStandardSchematron());
    }
}
