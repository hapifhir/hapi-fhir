package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class PrefetchTemplateUtilTest {
	private static final FhirContext ourFhirContextR4 = FhirContext.forR4();
	private static final FhirContext ourFhirContextDstu3 = FhirContext.forDstu3();
	private static final FhirContext ourFhirContextR5 = FhirContext.forR5();

	private static final String TEST_PATIENT_ID = "P2401";
	private static final String TEST_USER_ID = "userfoo";
	private static final String SERVICE_ID1 = "serviceId1";
	private static final String OBSERVATION_ID = "observationId1";
	private static final String SERVICE_ID2 = "serviceId2";
	private static final String PATIENT_ID_CONTEXT_KEY = "patientId";
	private static final String DRAFT_ORDERS_CONTEXT_KEY = "draftOrders";

	@Test
	void substituteTemplateShouldInterpolatePrefetchTokensWithContextValues() {
		String template = "{{context.userId}} a {{context.patientId}} b {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("userId", TEST_USER_ID);
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
		assertEquals(TEST_USER_ID + " a " + TEST_PATIENT_ID + " b " + TEST_PATIENT_ID, result);
	}

	@Test
	void substituteTemplateShouldThrowForMissingPrefetchTokens() {
		String template = "{{context.userId}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2375: Either request context was empty or it did not provide a value for key <userId>.  Please make sure you are including a context with valid keys.", e.getMessage());
		}
	}

	@Test
	void substituteTemplateShouldThrow400ForMissingContext() {
		String template = "{{context.userId}} a {{context.patientId}}";
		//Leave the context empty for the test.
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();

		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2375: Either request context was empty or it did not provide a value for key <userId>.  Please make sure you are including a context with valid keys.", e.getMessage());
		}
	}

	@Test
	void substituteTemplateShouldThrowForMissingNestedPrefetchTokens() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2372: Request context did not provide a value for key <draftOrders>.  Available keys in context are: [patientId]", e.getMessage());
		}
	}

	@Test
	void substituteTemplateShouldSupportNestedPrefetchTokensForSTU3() {
		String template = "{{context.draftOrders.Observation.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContextDstu3);
		org.hl7.fhir.dstu3.model.Observation observation = new org.hl7.fhir.dstu3.model.Observation();
		observation.setId(OBSERVATION_ID);
		builder.addCollectionEntry(observation);
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextDstu3);
		assertEquals(OBSERVATION_ID + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void substituteTemplateShouldReturnCSVForMultipleSupportNestedPrefetchTokensForR4() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContextR4);
		builder.addCollectionEntry(new org.hl7.fhir.r4.model.ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new org.hl7.fhir.r4.model.ServiceRequest().setId(SERVICE_ID2));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
		assertEquals(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void substituteTemplateShouldSupportMultipleDaVincePrefetchTokensForR5() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.draftOrders.Observation.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContextR5);
		builder.addCollectionEntry(new org.hl7.fhir.r5.model.ServiceRequest().setId(SERVICE_ID1));
		builder.addCollectionEntry(new org.hl7.fhir.r5.model.ServiceRequest().setId(SERVICE_ID2));
		builder.addCollectionEntry(new org.hl7.fhir.r5.model.Observation().setId(OBSERVATION_ID));
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		String result = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR5);
		assertEquals(SERVICE_ID1 + "," + SERVICE_ID2 + " a " + OBSERVATION_ID + " a " + TEST_PATIENT_ID, result);
	}

	@Test
	void substituteTemplateShouldThrowForDaVinciTemplateIfResourcesAreNotFoundInContextForR4() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		BundleBuilder builder = new BundleBuilder(ourFhirContextR4);
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
			fail("substituteTemplate call was successful with a null context field.");
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2373: Request context did not provide for resource(s) matching template. ResourceType missing is: ServiceRequest", e.getMessage());
		}
	}

	@Test
	void substituteTemplateShouldThrowForDaVinciTemplateIfResourceIsNotBundle() {
		String template = "{{context.draftOrders.ServiceRequest.id}} a {{context.patientId}}";
		CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, new org.hl7.fhir.r4.model.Observation().setId(OBSERVATION_ID));
		try {
			PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HAPI-2374: Request context did not provide valid " + ourFhirContextR4.getVersion().getVersion() + " Bundle resource for template key <draftOrders>", e.getMessage());
		}
	}

	/** TODO:
	 * working test cases for
	 * DSTU3 -> Bundle test -> done
	 *       -> resource test -> done
	 *       -> unsupported function test -> done
	 * R4 -> Bundle test -> done
	 * 	  -> resource test -> done
	 * 	  -> invalid function test -> done
	 * R5 -> Bundle test
	 *    -> resource test
	 *    -> invalid function test
	 * failing test case for invalid path -> done
	 * failing test case for resource not in bundle -> done
	 * failing test case for method not supported like dstu3 ofType() -> done
	 * ofType -> done, resolve -> done, OR operator, context operator,
	 * make templates more realistic -> done
	 * add-on : Support for using resolve() using contained resources
	 * check if array references can be directly evaluated.
	 */
	@Test
	void substituteTemplateWithFhirPathUsingUnsupportedMethodForDSTU3ShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template = "Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContextDstu3);
		final org.hl7.fhir.dstu3.model.DeviceRequest deviceRequest1 = new org.hl7.fhir.dstu3.model.DeviceRequest();
		deviceRequest1.setCode(new org.hl7.fhir.dstu3.model.Reference(deviceId1));
		builder.addCollectionEntry(deviceRequest1);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextDstu3))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Unable to evaluate FHIRPath for prefetch template with expression Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference}} for FHIR version DSTU3");
	}

	@Test
	void substituteTemplateWithFhirPathUsingWhereMethodForDSTU3WhenContextIsBundleAndResultIsPartOfBundleShouldParseSuccessFully() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template = "DeviceRequest?device={{context.draftOrders.entry.resource.where(id = 'Device/1').id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContextDstu3);
		builder.addCollectionEntry(new org.hl7.fhir.dstu3.model.Device().setId(deviceId1));
		builder.addCollectionEntry(new org.hl7.fhir.dstu3.model.Device().setId(deviceId2));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextDstu3);
		// validate
		assertThat(actual).isEqualTo("DeviceRequest?device=" + deviceId1);
	}

	@Test
	void substituteTemplateWithFhirPathUsingWhereMethodForDSTU3WhenContextIsBundleAndResultIsNotPartOfBundleShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template = "DeviceRequest?device={{context.draftOrders.entry.resource.where(id = 'Device/2').id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContextDstu3);
		builder.addCollectionEntry(new org.hl7.fhir.dstu3.model.Device().setId(deviceId1));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextDstu3))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("FHIRPath expression did not return any results for query: entry.resource.where(id = 'Device/2').id");
	}

	@Test
	void substituteTemplateWithFhirPathForDSTU3WhenContextIsResourceAndPathExistShouldParseSuccessfully() {
		// setup
		final String encounterId = "Encounter/1";
		final String template = "Condition?patient={{context.patientId}}&context={{context.encounter.id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("encounter", new org.hl7.fhir.dstu3.model.Encounter().setId(encounterId));
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextDstu3);
		// validate
		assertThat(actual).isEqualTo("Condition?patient=" + TEST_PATIENT_ID + "&context=" + encounterId);
	}

	@Test
	void substituteTemplateWithFhirPathForDSTU3WhenContextIsResourceAndPathDoesNotExistShouldFail() {
		// setup
		final String template = "Condition?patient={{context.patientId}}&context={{context.encounter.id}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put("encounter", new org.hl7.fhir.dstu3.model.Encounter());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextDstu3))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("FHIRPath expression did not return any results for query: id");
	}

	@Test
	void substituteTemplateWithFhirPathUsingOfTypeMethodForR4WhenContextIsBundleAndResultIsPartOfBundleShouldParseSuccessfully() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template = "Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.reference}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContextR4);
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest1 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest1.setCode(new org.hl7.fhir.r4.model.Reference(deviceId1));
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest2 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest2.setCode(new org.hl7.fhir.r4.model.Reference(deviceId2));
		builder.addCollectionEntry(deviceRequest1);
		builder.addCollectionEntry(deviceRequest2);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
		// validate
		assertThat(actual).isEqualTo("Device?_id=" + deviceId1 + "," + deviceId2);
	}

	@Test
	void substituteTemplateWithFhirPathUsingResolveMethodForR4WhenContextIsBundleAndResultIsPartOfBundleShouldParseSuccessfully() {
		// setup
		final String deviceId1 = "Device/1";
		final String deviceId2 = "Device/2";
		final String template = "Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContextR4);
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest1 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest1.setCode(new org.hl7.fhir.r4.model.Reference(deviceId1));
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest2 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest2.setCode(new org.hl7.fhir.r4.model.Reference(deviceId2));
		builder.addCollectionEntry(deviceRequest1);
		builder.addCollectionEntry(deviceRequest2);
		builder.addCollectionEntry(new org.hl7.fhir.r4.model.Device().setId(deviceId1));
		builder.addCollectionEntry(new org.hl7.fhir.r4.model.Device().setId(deviceId2));
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
		// validate
		assertThat(actual).isEqualTo("Device?_id=" + 1 + "," + 2);
	}

	@Test
	void substituteTemplateWithFhirPathUsingResolveMethodForR4WhenContextIsBundleAndResultIsNotPartOfBundleShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template = "Device?_id={{context.draftOrders.entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContextR4);
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest1 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest1.setCode(new org.hl7.fhir.r4.model.Reference(deviceId1));
		builder.addCollectionEntry(deviceRequest1);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("FHIRPath expression did not return any results for query: entry.resource.ofType(DeviceRequest).code.resolve().as(Device).id");
	}

	@Test
	void substituteTemplateWithFhirPathUsingInvalidMethodForR4ShouldFail() {
		// setup
		final String deviceId1 = "Device/1";
		final String template = "Device?_id={{context.draftOrders.entry.resource.RandomMethod(DeviceRequest).code.reference}}";
		final BundleBuilder builder = new BundleBuilder(ourFhirContextR4);
		final org.hl7.fhir.r4.model.DeviceRequest deviceRequest1 = new org.hl7.fhir.r4.model.DeviceRequest();
		deviceRequest1.setCode(new org.hl7.fhir.r4.model.Reference(deviceId1));
		builder.addCollectionEntry(deviceRequest1);
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		context.put(DRAFT_ORDERS_CONTEXT_KEY, builder.getBundle());
		// execute & validate
		assertThatThrownBy(() -> PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Unable to evaluate FHIRPath for prefetch template with expression Device?_id={{context.draftOrders.entry.resource.RandomMethod(DeviceRequest).code.reference}} for FHIR version R4");
	}

	@Test
	void substituteTemplateWithFhirPathForR4WithComplexQueryWhenContextIsResourceAndPathExistShouldParseSuccessfully() {
		// setup
		final String encounterId = "Encounter/1";
		final String template = "Condition?patient={{context.patientId}}&context={{context.encounter.id}}&recorded-date={{context.encounter.period.start + 2 days}}";
		final CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
		context.put(PATIENT_ID_CONTEXT_KEY, TEST_PATIENT_ID);
		final org.hl7.fhir.r4.model.Encounter encounter = new org.hl7.fhir.r4.model.Encounter();
		encounter.setId(encounterId);
		final Date encounterStartDate = new Date();
		encounter.setPeriod(new org.hl7.fhir.r4.model.Period().setStart(encounterStartDate));
		context.put("encounter", encounter);
		final org.hl7.fhir.r4.model.DateTimeType expectedDateTime = new org.hl7.fhir.r4.model.DateTimeType(DateUtils.addDays(encounterStartDate, 2));
		// execute
		final String actual = PrefetchTemplateUtil.substituteTemplate(template, context, ourFhirContextR4);
		// validate
		assertThat(actual).isEqualTo("Condition?patient=" + TEST_PATIENT_ID + "&context=" + 1 + "&recorded-date=" + expectedDateTime.getValueAsString());
	}



}
