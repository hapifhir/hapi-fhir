package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionUtilTest {

	private static final String URN_UUID_VALUE = "urn:uuid:52046729-f2e6-42e0-849d-a9303b6113a5";
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Test
	public void testParseTransactionResponse_SuccessfulCreate() {
		Resource requestPatient = new Patient();
		requestPatient.getMeta().setSource("http://source#123");

		Bundle requestBundle = new Bundle();
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryComponent requestEntry = requestBundle.addEntry();
		requestEntry.setFullUrl(URN_UUID_VALUE);
		requestEntry.setResource(requestPatient);
		requestEntry.getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");

		StorageResponseCodeEnum storageOutcome = StorageResponseCodeEnum.SUCCESSFUL_CREATE;
		OperationOutcome oo = newOperationOutcome(storageOutcome);

		Bundle responseBundle = new Bundle();
		responseBundle.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		responseBundle.addEntry().getResponse().setOutcome(oo).setStatus("200 OK").setLocation("http://foo.com/Patient/123");

		// Test
		List<TransactionUtil.StorageOutcome> outcomes = TransactionUtil.parseTransactionResponse(myCtx, requestBundle, responseBundle).getStorageOutcomes();

		// Verify
		assertEquals(1, outcomes.size());
		assertEquals(storageOutcome, outcomes.get(0).getStorageResponseCode());
		assertEquals("Patient/123", outcomes.get(0).getTargetId().getValue());
		assertEquals(URN_UUID_VALUE, outcomes.get(0).getSourceId().getValue());
		assertEquals("http://source#123", outcomes.get(0).getRequestMetaSource());
		assertEquals(200, outcomes.get(0).getStatusCode());
		assertEquals("200 OK", outcomes.get(0).getStatusMessage());
	}

	@Test
	public void testParseTransactionResponse_FailureCreate() {
		Resource requestPatient = new Patient();
		requestPatient.setId("123");
		requestPatient.getMeta().setSource("http://source#123");

		Bundle requestBundle = new Bundle();
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryComponent requestEntry = requestBundle.addEntry();
		requestEntry.setResource(requestPatient);
		requestEntry.setFullUrl("http://example.com/Patient/123");
		requestEntry
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("Patient/123");

		StorageResponseCodeEnum storageOutcome = StorageResponseCodeEnum.FAILURE;
		OperationOutcome oo = newOperationOutcome(storageOutcome, "Constraint error");

		Bundle responseBundle = new Bundle();
		responseBundle.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		responseBundle.addEntry().getResponse().setOutcome(oo).setStatus("422 Precondition Failed");

		// Test
		List<TransactionUtil.StorageOutcome> outcomes = TransactionUtil.parseTransactionResponse(myCtx, requestBundle, responseBundle).getStorageOutcomes();

		// Verify
		assertEquals(1, outcomes.size());
		assertEquals(storageOutcome, outcomes.get(0).getStorageResponseCode());
		assertEquals("Patient/123", outcomes.get(0).getSourceId().getValue());
		assertNull(outcomes.get(0).getTargetId());
		assertEquals("http://source#123", outcomes.get(0).getRequestMetaSource());
		assertEquals(422, outcomes.get(0).getStatusCode());
		assertEquals("422 Precondition Failed", outcomes.get(0).getStatusMessage());
	}

	@Test
	public void testParseTransactionResponse_BundleSourcePropagated() {
		Resource requestPatient = new Patient();
		requestPatient.setId("123");

		Bundle requestBundle = new Bundle();
		requestBundle.getMeta().setSource("http://source#123");
		requestBundle.setType(Bundle.BundleType.TRANSACTION);
		Bundle.BundleEntryComponent requestEntry = requestBundle.addEntry();
		requestEntry.setResource(requestPatient);
		requestEntry.setFullUrl("http://example.com/Patient/123");
		requestEntry
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("Patient/123");

		StorageResponseCodeEnum storageOutcome = StorageResponseCodeEnum.SUCCESSFUL_CREATE;
		OperationOutcome oo = newOperationOutcome(storageOutcome);

		Bundle responseBundle = new Bundle();
		responseBundle.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		responseBundle.addEntry().getResponse().setOutcome(oo).setStatus("422 Precondition Failed");

		// Test
		List<TransactionUtil.StorageOutcome> outcomes = TransactionUtil.parseTransactionResponse(myCtx, requestBundle, responseBundle).getStorageOutcomes();

		// Verify
		assertEquals(1, outcomes.size());
		assertEquals("http://source#123", outcomes.get(0).getRequestMetaSource());
	}

	@Nonnull
	private OperationOutcome newOperationOutcome(StorageResponseCodeEnum storageOutcome) {
		return newOperationOutcome(storageOutcome, "Success");
	}

	@Nonnull
	private OperationOutcome newOperationOutcome(StorageResponseCodeEnum storageOutcome, String theDiagnostics) {
		OperationOutcome oo = (OperationOutcome) OperationOutcomeUtil.newInstance(myCtx);
		String detailSystem = StorageResponseCodeEnum.SYSTEM;
		String detailCode = storageOutcome.getCode();
		String detailDescription = storageOutcome.getDisplay();
		OperationOutcomeUtil.addIssue(
			myCtx, oo, "information", theDiagnostics, null, "informational", detailSystem, detailCode, detailDescription);
		return oo;
	}

}
