package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_40;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionUtilTest {

	private static final String URN_UUID_VALUE = "urn:uuid:52046729-f2e6-42e0-849d-a9303b6113a5";
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@ParameterizedTest
	@EnumSource(value = FhirVersionEnum.class, names = {"DSTU2", "DSTU3", "R4", "R5"})
	public void testParseTransactionResponse_SuccessfulCreate(FhirVersionEnum theVersion) {
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
		FhirContext context = FhirContext.forCached(theVersion);
		IBaseBundle requestBundleVersioned = toVersion(requestBundle, theVersion);
		IBaseBundle responseBundleVersioned = toVersion(responseBundle, theVersion);
		List<TransactionUtil.StorageOutcome> outcomes = TransactionUtil.parseTransactionResponse(context, requestBundleVersioned, responseBundleVersioned).getStorageOutcomes();

		// Verify
		assertEquals(1, outcomes.size());
		assertEquals(storageOutcome, outcomes.get(0).getStorageResponseCode());
		assertEquals("Patient/123", outcomes.get(0).getTargetId().getValue());
		assertEquals(URN_UUID_VALUE, outcomes.get(0).getSourceId().getValue());
		if (theVersion.isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			assertEquals("http://source#123", outcomes.get(0).getRequestMetaSource());
		} else {
			assertNull(outcomes.get(0).getRequestMetaSource());
		}
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

		OperationOutcome oo = newOperationOutcome(null, "Constraint error");

		Bundle responseBundle = new Bundle();
		responseBundle.setType(Bundle.BundleType.TRANSACTIONRESPONSE);
		responseBundle.addEntry().getResponse().setOutcome(oo).setStatus("422 Precondition Failed");

		// Test
		List<TransactionUtil.StorageOutcome> outcomes = TransactionUtil.parseTransactionResponse(myCtx, requestBundle, responseBundle).getStorageOutcomes();

		// Verify
		assertEquals(1, outcomes.size());
		assertEquals(StorageResponseCodeEnum.FAILURE, outcomes.get(0).getStorageResponseCode());
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
		String detailSystem = null;
		String detailCode = null;
		String detailDescription = null;
		if (storageOutcome != null) {
			detailSystem = StorageResponseCodeEnum.SYSTEM;
			detailCode = storageOutcome.getCode();
			detailDescription = storageOutcome.getDisplay();
		}

		OperationOutcomeUtil.addIssue(
			myCtx, oo, "information", theDiagnostics, null, "informational", detailSystem, detailCode, detailDescription);
		return oo;
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private static IBaseBundle toVersion(Bundle theBundle, FhirVersionEnum theVersion) {
		return switch (theVersion) {
			case DSTU2 -> {
				ca.uhn.fhir.model.dstu2.resource.Bundle dstu2Bundle = toDstu2(theBundle);
				for (int i = 0; i < theBundle.getEntry().size(); i++) {
					Resource outcome = theBundle.getEntry().get(i).getResponse().getOutcome();
					if (outcome != null) {
						dstu2Bundle.getEntry().get(i).setResource(toDstu2(outcome));
					}
				}
				yield dstu2Bundle;
			}
			case DSTU3 -> {
				org.hl7.fhir.dstu3.model.Bundle target = (org.hl7.fhir.dstu3.model.Bundle) VersionConvertorFactory_30_40.convertResource(theBundle);
				MetaUtil.setSource(FhirContext.forDstu3Cached(), target, MetaUtil.getSource(FhirContext.forR4Cached(), theBundle));
				for (int i = 0; i < theBundle.getEntry().size(); i++) {
					Resource inputResource = theBundle.getEntry().get(i).getResource();
					if (inputResource != null) {
						org.hl7.fhir.dstu3.model.Resource outputResource = target.getEntry().get(i).getResource();
						MetaUtil.setSource(FhirContext.forDstu3Cached(), outputResource, MetaUtil.getSource(FhirContext.forR4Cached(), inputResource));
					}
				}
				yield target;
			}
			case R4 -> theBundle;
			case R5 -> (IBaseBundle) VersionConvertorFactory_40_50.convertResource(theBundle);
			default -> throw new IllegalArgumentException("Unsupported version " + theVersion);
		};
	}

	@SuppressWarnings("unchecked")
	private static <T extends IBaseResource> T toDstu2(org.hl7.fhir.r4.model.Resource theInput) {
		IBaseResource hl7orgBundle = VersionConvertorFactory_10_40.convertResource(theInput);
		return (T) FhirContext.forDstu2Cached().newJsonParser().parseResource(FhirContext.forDstu2Hl7OrgCached().newJsonParser().encodeToString(hl7orgBundle));
	}

}
