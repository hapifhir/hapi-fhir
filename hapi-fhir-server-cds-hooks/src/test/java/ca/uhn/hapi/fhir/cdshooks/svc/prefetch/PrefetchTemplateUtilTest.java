package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PrefetchTemplateUtilTest {
	private static final String TEST_PATIENT_ID = "P2401";
	private static final String TEST_USER_ID = "userfoo";
	private static final String SERVICE_ID1 = "serviceId1";
	private static final String OBSERVATION_ID = "observationId1";
	private static final String SERVICE_ID2 = "serviceId2";

	@Test
	void testShouldInterpolatePrefetchTokensWithContextValues() {
		String template = "{{context.userId}} a {{context.patientId}} b {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("userId", TEST_USER_ID);
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
		assertEquals(TEST_USER_ID + " a " + TEST_PATIENT_ID + " b " + TEST_PATIENT_ID, result);
	}

	@Test
	void testShouldThrowForMissingPrefetchTokens() {
		String template = "{{context.userId}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2375: Either request context was empty or it did not provide a value for key <userId>.  Please make sure you are including a context with valid keys.", e.getMessage());
		}
	}

	@Test
	void testShouldThrow400ForMissingContext() {
		String template = "{{context.userId}} a {{context.patientId}}";
		//Leave the context empty for the test.
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();

		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2375: Either request context was empty or it did not provide a value for key <userId>.  Please make sure you are including a context with valid keys.", e.getMessage());
		}
	}

	@Test
	void testShouldThrowForMissingNestedPrefetchTokens() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2372: Request context did not provide a value for key <draftOrders>.  Available keys in context are: [patientId]", e.getMessage());
		}
	}

	@Test
	void testShouldSupportNestedPrefetchTokensForSTU3() {
		String template = "{{context.draftOrders.Observation.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(new FhirContext(FhirVersionEnum.DSTU3));
		org.hl7.fhir.dstu3.model.Observation observation = new org.hl7.fhir.dstu3.model.Observation();
		observation.setId(OBSERVATION_ID);
		builder.addCollectionEntry(observation);
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("draftOrders", builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forDstu3());
		assertEquals(OBSERVATION_ID + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void testShouldReturnCSVForMultipleSupportNestedPrefetchTokensForR4() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(new FhirContext(FhirVersionEnum.R4));
		builder.addCollectionEntry(new org.hl7.fhir.r4.model.ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new org.hl7.fhir.r4.model.ServiceRequest().setId(SERVICE_ID2));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("draftOrders", builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
		assertEquals(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void testShouldSupportMultipleDaVincePrefetchTokensForR5() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.draftOrders.Observation.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(new FhirContext(FhirVersionEnum.R5));
		builder.addCollectionEntry(new org.hl7.fhir.r5.model.ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new org.hl7.fhir.r5.model.ServiceRequest().setId(SERVICE_ID2));
		builder.addCollectionEntry(new org.hl7.fhir.r5.model.Observation().setId(OBSERVATION_ID));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("draftOrders", builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR5());
		assertEquals(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + OBSERVATION_ID + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void testShouldThrowForDaVinciTemplateIfResourcesAreNotFoundInContextForR4() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(new FhirContext(FhirVersionEnum.R4));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("draftOrders", builder.getBundle());
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
			fail("substituteTemplate call was successful with a null context field.");
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2373: Request context did not provide for resource(s) matching template. ResourceType missing is: ServiceRequest", e.getMessage());
		}
	}

	@Test
	void testShouldThrowForDaVinciTemplateIfResourceIsNotBundle() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		FhirContext fhirContextR4 = new FhirContext(FhirVersionEnum.R4);
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("draftOrders", new org.hl7.fhir.r4.model.Observation().setId(OBSERVATION_ID));
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2374: Request context did not provide valid " + fhirContextR4.getVersion().getVersion() + " Bundle resource for template key <draftOrders>", e.getMessage());
		}
	}

	/** TODO:
	 * working test cases for
	 * R3 -> single nested
	 * R4 -> multiple nested
	 * R5 -> multiple nested with multiple replacement templates
	 * failing test case for resource not in bundle
	 * ofType, resolve, OR operator, context operator,
	 */
	@Test
	void testShouldSupportTemplateWithFhirPathUsingOfTypeMethodForR4() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template = "{{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference}} a {{context.patientId}}";
		final BundleBuilder builder = new BundleBuilder(new FhirContext(FhirVersionEnum.R4));
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest1 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest2 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest2.setCode(new Reference(deviceId2));
		builder.addCollectionEntry(deviceRequest1);
		builder.addCollectionEntry(deviceRequest2);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("draftOrders", builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
		// validate
		assertThat(actual).isEqualTo(deviceId1 + "," + deviceId2 + " a " + TEST_PATIENT_ID);
	}

	@Test
	void testShouldSupportTemplateWithFhirPathUsingResolveMethodForR4() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template = "{{context.draftOrders.entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id}} a {{context.patientId}}";
		final BundleBuilder builder = new BundleBuilder(new FhirContext(FhirVersionEnum.R4));
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest1 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest1.setCode(new Reference(deviceId1));
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest2 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest2.setCode(new Reference(deviceId2));
		builder.addCollectionEntry(deviceRequest1);
		builder.addCollectionEntry(deviceRequest2);
		builder.addCollectionEntry(new Device().setId(deviceId1));
		builder.addCollectionEntry(new Device().setId(deviceId2));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put("patientId", TEST_PATIENT_ID);
		context.put("draftOrders", builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, FhirContext.forR4());
		// validate
		assertThat(actual).isEqualTo(1 + "," + 2 + " a " + TEST_PATIENT_ID);
	}



}
