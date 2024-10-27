package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

	@Test
	public void testAddIssueWithMessageId() {
		OperationOutcome oo = (OperationOutcome) OperationOutcomeUtil.newInstance(myCtx);
		OperationOutcomeUtil.addIssueWithMessageId(myCtx, oo, "error", "message", "messageID", "location", "processing");
		assertThat(oo.getIssueFirstRep().getDetails()).as("OO.issue.details is empty").isNotNull();
	}

	@Test
	public void hasIssuesOfSeverity_noMatchingIssues() {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.WARNING);
		oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR);
		oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.INFORMATION);

		assertFalse(OperationOutcomeUtil.hasIssuesOfSeverity(myCtx, oo, OperationOutcome.IssueSeverity.FATAL.toCode()));
	}

	@Test
	public void hasIssuesOfSeverity_withMatchingIssues() {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.WARNING);
		oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR);
		oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.FATAL);
		oo.addIssue().setSeverity(OperationOutcome.IssueSeverity.INFORMATION);

		assertTrue(OperationOutcomeUtil.hasIssuesOfSeverity(myCtx, oo, OperationOutcome.IssueSeverity.FATAL.toCode()));
	}
}
