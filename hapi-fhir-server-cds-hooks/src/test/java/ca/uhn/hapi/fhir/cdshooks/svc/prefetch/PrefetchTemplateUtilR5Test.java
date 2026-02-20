package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * R5-specific tests for PrefetchTemplateUtil.
 */
class PrefetchTemplateUtilR5Test {
	private static final FhirContext ourFhirContext = FhirContext.forR5();
	private static final String TEST_PATIENT_ID = "P2401";
	private static final String SERVICE_ID1 = "serviceId1";
	private static final String OBSERVATION_ID = "observationId1";
	private static final String SERVICE_ID2 = "serviceId2";
	private static final String PATIENT_ID_CONTEXT_KEY = "patientId";
	private static final String DRAFT_ORDERS_CONTEXT_KEY = "draftOrders";

	@Test
	@DisplayName("Should support multiple DaVinci prefetch tokens for different resource types")
	void substituteTemplateMultipleDaVinciPrefetchTokens() {
		String template =
				"{{context.draftOrders.ServiceRequest.id}} a {{context.draftOrders.Observation.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContext);
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new ServiceRequest().setId(SERVICE_ID2));
		builder.addCollectionEntry(new Observation().setId(OBSERVATION_ID));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContext);
		assertEquals(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + OBSERVATION_ID + " a " + TEST_PATIENT_ID, result);
	}
}