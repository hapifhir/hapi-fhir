package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionUtilTest {

	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Test
	public void testParseTransactionResponse() {
		OperationOutcome oo = (OperationOutcome) OperationOutcomeUtil.newInstance(myCtx);
		String detailSystem = StorageResponseCodeEnum.SYSTEM;
		String detailCode = StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH.getCode();
		String detailDescription = StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH.getDisplay();
		OperationOutcomeUtil.addIssue(
			myCtx, oo, "information", "Success", null, "informational", detailSystem, detailCode, detailDescription);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		bundle.addEntry().getResponse().setOutcome(oo).setStatus("200 OK").setLocation("http://foo.com/Patient/123");

		// Test
		List<TransactionUtil.StorageOutcome> outcomes = TransactionUtil.parseTransactionResponse(myCtx, bundle).getStorageOutcomes();

		// Verify
		assertEquals(1, outcomes.size());
		assertEquals(StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH ,outcomes.get(0).getStorageResponseCode());
		assertEquals("http://foo.com/Patient/123" ,outcomes.get(0).getTargetId().getValue());
		assertNull(outcomes.get(0).getSourceId());
	}

}
