package ca.uhn.hapi.fhir.cdshooks.module;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSuggestionActionJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSuggestionJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSystemActionJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
class SerializationTest {
	public static final String HOOK_NAME = "Hook Name";
	public static final String FHIR_SERVER = "https://localhost:2401";
	public static final String FAMILY = "Jehoshaphat";
	public static final String PATIENT_KEY = "patientKey";
	public static final String CONTEXT_PATIENT_KEY = "patientId";
	public static final String CONTEXT_SELECTIONS_KEY = "selections";
	public static final String CONTEXT_DRAFT_ORDERS_KEY = "draftOrders";
	public static final String CONTEXT_PATIENT_VALUE = "Patient/123";
	public static final String CONTEXT_SELECTIONS_VALUE1 = "MedicationRequest/456";
	public static final String CONTEXT_SELECTIONS_VALUE2 = "MedicationRequest/789";
	public static final String CONTEXT_DRAFT_ORDERS_VALUE1 = "abc";
	public static final String CONTEXT_DRAFT_ORDERS_VALUE2 = "def";
	private static final Logger ourLog = LoggerFactory.getLogger(SerializationTest.class);
	private static final String DAUGHTER_KEY = "daughterKey";
	private static final String PARENT_KEY = "parentKey";
	private static final String DAUGHTER = "Athaliah";
	private final FhirContext ourFhirContext = FhirContext.forR4();
	private final ObjectMapper ourObjectMapper = new CdsHooksObjectMapperFactory(ourFhirContext).newMapper();
	public String myResponseJson;
	@Value("classpath:CdsServiceRequestJson.json")
	Resource myRequestJsonResource;
	@Value("classpath:CdsServiceResponseJson.json")
	Resource myResponseJsonResource;
	private String myRequestJson;

	@BeforeEach
	public void loadJson() throws Exception {
		myRequestJson = IOUtils.toString(myRequestJsonResource.getInputStream(), Charsets.UTF_8);
		myResponseJson = IOUtils.toString(myResponseJsonResource.getInputStream(), Charsets.UTF_8);
	}

	/**
	 * From the Spec:
	 * <p>
	 * The CDS Client MUST NOT send any prefetch template key that it chooses not to satisfy. Similarly, if the CDS Client
	 * encounters an error while prefetching any data, the prefetch template key MUST NOT be sent to the CDS Service. If the
	 * CDS Client has no data to populate a template prefetch key, the prefetch template key MUST have a value of null. Note
	 * that the null result is used rather than a bundle with zero entries to account for the possibility that the prefetch
	 * url is a single-resource request.
	 */

	// These tests verify that nulls prefetch values are preserved in serialization and deserialization so their
	// missing status is properly determined
	@Test
	void testSerializeRequest() throws Exception {
		CdsServiceRequestJson cdsServiceRequestJson = new CdsServiceRequestJson();
		cdsServiceRequestJson.setHook(HOOK_NAME);
		cdsServiceRequestJson.setFhirServer(FHIR_SERVER);

		Patient patient = new Patient();
		patient.addName().setFamily(FAMILY);
		cdsServiceRequestJson.addPrefetch(PATIENT_KEY, patient);

		Patient daughter = new Patient();
		daughter.addName().setFamily(DAUGHTER);
		cdsServiceRequestJson.addPrefetch(DAUGHTER_KEY, daughter);

		cdsServiceRequestJson.addPrefetch(PARENT_KEY, null);

		cdsServiceRequestJson.addContext(CONTEXT_PATIENT_KEY, CONTEXT_PATIENT_VALUE);
		cdsServiceRequestJson.addContext(CONTEXT_SELECTIONS_KEY, List.of(CONTEXT_SELECTIONS_VALUE1, CONTEXT_SELECTIONS_VALUE2));
		MedicationRequest order1 = new MedicationRequest();
		order1.setId(CONTEXT_DRAFT_ORDERS_VALUE1);
		MedicationRequest order2 = new MedicationRequest();
		order2.setId(CONTEXT_DRAFT_ORDERS_VALUE2);
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(order1);
		bundle.addEntry().setResource(order2);
		cdsServiceRequestJson.getContext().put(CONTEXT_DRAFT_ORDERS_KEY, bundle);

		String json = ourObjectMapper.writeValueAsString(cdsServiceRequestJson);
		ourLog.debug(json);
		assertThat(json).isEqualToIgnoringWhitespace(myRequestJson);
	}

	@Test
	void testDeserializeRequest() throws Exception {
		CdsServiceRequestJson cdsServiceRequestJson = ourObjectMapper.readValue(myRequestJson, CdsServiceRequestJson.class);
		assertEquals(HOOK_NAME, cdsServiceRequestJson.getHook());

		assertThat(cdsServiceRequestJson.getPrefetchKeys()).containsExactly(PATIENT_KEY, DAUGHTER_KEY, PARENT_KEY);
		Patient patient = (Patient) cdsServiceRequestJson.getPrefetch(PATIENT_KEY);
		assertEquals(FAMILY, patient.getNameFirstRep().getFamily());

		Patient daughter = (Patient) cdsServiceRequestJson.getPrefetch(DAUGHTER_KEY);
		assertEquals(DAUGHTER, daughter.getNameFirstRep().getFamily());

		Patient parent = (Patient) cdsServiceRequestJson.getPrefetch(PARENT_KEY);
		assertNull(parent);

		assertEquals(CONTEXT_PATIENT_VALUE, cdsServiceRequestJson.getContext().getString(CONTEXT_PATIENT_KEY));
		List<String> selections = cdsServiceRequestJson.getContext().getArray(CONTEXT_SELECTIONS_KEY);
		assertThat(selections).containsExactly(CONTEXT_SELECTIONS_VALUE1, CONTEXT_SELECTIONS_VALUE2);
		cdsServiceRequestJson.getContext().getResource(CONTEXT_DRAFT_ORDERS_KEY);
	}

	@Test
	void testSerializeResponse() throws Exception {
		Patient patient = new Patient();
		patient.addName().setFamily(FAMILY);
		CdsServiceResponseSystemActionJson systemAction = new CdsServiceResponseSystemActionJson();
		systemAction.setResource(patient);
		CdsServiceResponseJson cdsServiceRequestJson = new CdsServiceResponseJson();
		cdsServiceRequestJson.addServiceAction(systemAction);

		Patient daughter = new Patient();
		daughter.addName().setFamily(DAUGHTER);
		CdsServiceResponseSuggestionActionJson suggestionAction = new CdsServiceResponseSuggestionActionJson();
		suggestionAction.setResource(daughter);
		CdsServiceResponseSuggestionJson suggestion = new CdsServiceResponseSuggestionJson();
		suggestion.addAction(suggestionAction);
		CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
		card.addSuggestion(suggestion);
		cdsServiceRequestJson.addCard(card);

		String json = ourObjectMapper.writeValueAsString(cdsServiceRequestJson);
		ourLog.debug(json);
		assertThat(json).isEqualToIgnoringWhitespace(myResponseJson);
	}

	@Test
	void testDeserializeResponse() throws Exception {
		CdsServiceResponseJson cdsServiceResponseJson = ourObjectMapper.readValue(myResponseJson, CdsServiceResponseJson.class);
		Patient patient = (Patient) cdsServiceResponseJson.getServiceActions().get(0).getResource();
		assertEquals(FAMILY, patient.getNameFirstRep().getFamily());
	}

	@Test
	void testSerializeDeserializeEmptyRequest() throws Exception {
		// setup
		final CdsServiceRequestJson expected = new CdsServiceRequestJson();
		final String expectedAsJson = ourObjectMapper.writeValueAsString(expected);
		// execute
		final CdsServiceRequestJson actual = ourObjectMapper.readValue(expectedAsJson, CdsServiceRequestJson.class);
		// validate
		final String actualAsJson = ourObjectMapper.writeValueAsString(actual);
		assertNotSame(expected, actual);
		assertEquals(expectedAsJson, actualAsJson);
	}

	@Test
	void testSerializeDeserializeRequestWithEmptyContext() throws Exception {
		// setup
		final CdsServiceRequestJson expected = new CdsServiceRequestJson();
		expected.setContext(new CdsServiceRequestContextJson());
		final String expectedAsJson = ourObjectMapper.writeValueAsString(expected);
		// execute
		final CdsServiceRequestJson actual = ourObjectMapper.readValue(expectedAsJson, CdsServiceRequestJson.class);
		// validate
		final String actualAsJson = ourObjectMapper.writeValueAsString(actual);
		assertNotSame(expected, actual);
		assertEquals(expectedAsJson, actualAsJson);
	}

}
