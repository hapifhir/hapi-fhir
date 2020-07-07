package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OperationOutcomeUtilTest {

	private FhirContext myCtx = FhirContext.forR4();

	@Test
	public void testHasIssueTrue() {
		OperationOutcome oo =new OperationOutcome();
		oo.addIssue().setDiagnostics("foo");
		assertTrue(OperationOutcomeUtil.hasIssues(myCtx, oo));
	}

	@Test
	public void testHasIssueFalse() {
		OperationOutcome oo =new OperationOutcome();
		assertFalse(OperationOutcomeUtil.hasIssues(myCtx, oo));
	}

	@Test
	public void testAddIssue() {
		OperationOutcome oo = (OperationOutcome) OperationOutcomeUtil.newInstance(myCtx);
		IBase issue = OperationOutcomeUtil.addIssue(myCtx, oo, "error", "Help i'm a bug", "/Patient", "throttled");
		OperationOutcomeUtil.addLocationToIssue(myCtx, issue, null);
		OperationOutcomeUtil.addLocationToIssue(myCtx, issue, "");
		OperationOutcomeUtil.addLocationToIssue(myCtx, issue, "line 3");
		assertEquals("{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"throttled\",\"diagnostics\":\"Help i'm a bug\",\"location\":[\"/Patient\",\"line 3\"]}]}", myCtx.newJsonParser().encodeResourceToString(oo));
	}

}
