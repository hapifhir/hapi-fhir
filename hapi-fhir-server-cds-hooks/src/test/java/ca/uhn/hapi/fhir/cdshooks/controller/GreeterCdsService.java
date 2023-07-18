package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.hapi.fhir.cdshooks.api.CdsService;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServicePrefetch;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceIndicatorEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardSourceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class GreeterCdsService implements IPointcutLatch {
	public static final String TEST_HOOK_STRING_ID = "greeter-service-string";
	public static final String TEST_HOOK_OBJECT_ID = "greeter-service-object";
	public static final String TEST_HOOK = "test-hook";
	public static final String TEST_HOOK_DESCRIPTION = "Hook description";
	public static final String TEST_HOOK_TITLE = "Hook title";
	public static final CdsServiceIndicatorEnum TEST_HOOK_RESPONSE_INDICATOR = CdsServiceIndicatorEnum.INFO;
	public static final String TEST_HOOK_RESPONSE_SOURCE_LABEL = "Response source label";
	public static final String TEST_HOOK_RESPONSE_DETAIL = "Response detail";
	public static final String TEST_HOOK_CONTEXT_USERID_KEY = "userId";
	public static final String TEST_HOOK_CONTEXT_PATIENTID_KEY = "patientId";
	public static final String TEST_HOOK_PREFETCH_PATIENT_KEY = "patient";
	public static final String TEST_HOOK_PREFETCH_USER_KEY = "user";
	public static final String TEST_HOOK_PREFETCH_PATIENT_QUERY = "Patient/{{context.patientId}}";
	public static final String TEST_HOOK_PREFETCH_USER_QUERY = "{{context.userId}}";
	private static final Logger ourLog = LoggerFactory.getLogger(GreeterCdsService.class);
	private final PointcutLatch myPointcutLatch = new PointcutLatch("Greeter CDS-Hook");
	@Autowired
	FhirContext myFhirContext;

	@CdsService(value = TEST_HOOK_STRING_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = TEST_HOOK_PREFETCH_PATIENT_QUERY),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_USER_KEY, query = TEST_HOOK_PREFETCH_USER_QUERY)
		}
	)
	public TestResponseJson greetWithString(String theJsonRequest) {
		myPointcutLatch.call(theJsonRequest);

		ObjectMapper mapper = new ObjectMapper();
		Patient patient;
		Practitioner user;
		try {
			JsonNode root = mapper.readTree(theJsonRequest);
			String patientJson = root.path("prefetch").path(TEST_HOOK_PREFETCH_PATIENT_KEY).toPrettyString();
			String practitionerJson = root.path("prefetch").path(TEST_HOOK_PREFETCH_USER_KEY).toString();
			IParser parser = myFhirContext.newJsonParser();
			patient = parser.parseResource(Patient.class, patientJson);
			user = parser.parseResource(Practitioner.class, practitionerJson);
		} catch (IOException e) {
			throw new AssertionError("Failed to parse request: " + theJsonRequest);
		}


		TestResponseJson retval = new TestResponseJson();
		TestCardJson card = new TestCardJson();

		card.mySummary = "Hello " + getName(patient) + " and " + getName(user);
		card.myIndicator = TEST_HOOK_RESPONSE_INDICATOR.toString().toLowerCase();
		TestCardSourceJson source = new TestCardSourceJson();
		source.myLabel = TEST_HOOK_RESPONSE_SOURCE_LABEL;
		card.mySource = source;
		card.myDetail = TEST_HOOK_RESPONSE_DETAIL;
		retval.addCard(card);
		return retval;
	}

	private String getName(Patient thePatient) {
		if (thePatient == null) {
			return null;
		}
		List<HumanName> names = thePatient.getName();
		if (names == null || names.size() == 0 || names.get(0).isEmpty()) {
			return "This patient";
		}
		HumanName nameItem = names.get(0);
		return nameItem.getNameAsSingleString();
	}

	private String getName(Practitioner thePractitioner) {
		if (thePractitioner == null) {
			return null;
		}
		List<HumanName> names = thePractitioner.getName();
		if (names == null || names.size() == 0 || names.get(0).isEmpty()) {
			return "This patient";
		}
		HumanName nameItem = names.get(0);
		return nameItem.getNameAsSingleString();
	}

	@CdsService(value = TEST_HOOK_OBJECT_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = TEST_HOOK_PREFETCH_PATIENT_QUERY),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_USER_KEY, query = TEST_HOOK_PREFETCH_USER_QUERY)
		},
		allowAutoFhirClientPrefetch = true)

	public CdsServiceResponseJson greetWithJson(CdsServiceRequestJson theCdsRequest) {
		myPointcutLatch.call(theCdsRequest);
		Patient patient = (Patient) theCdsRequest.getPrefetch("patient");
		CdsServiceResponseJson response = new CdsServiceResponseJson();
		CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
		Practitioner practitioner = (Practitioner) theCdsRequest.getPrefetch("user");

		card.setSummary("Hello " + getName(patient) + " and " + getName(practitioner));
		card.setIndicator(CdsServiceIndicatorEnum.INFO);
		CdsServiceResponseCardSourceJson source = new CdsServiceResponseCardSourceJson();
		source.setLabel(TEST_HOOK_RESPONSE_SOURCE_LABEL);
		card.setSource(source);
		response.addCard(card);
		return response;
	}

	@Override
	public void clear() {
		myPointcutLatch.clear();
	}

	@Override
	public void setExpectedCount(int theCount) {
		myPointcutLatch.setExpectedCount(theCount);
	}

	@Override
	public List<HookParams> awaitExpected() throws InterruptedException {
		return myPointcutLatch.awaitExpected();
	}
}
