package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.hapi.fhir.cdshooks.api.CdsService;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServiceFeedback;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServicePrefetch;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;

import java.util.List;

public class HelloWorldService implements IPointcutLatch {
	public static final String TEST_HOOK = "hello-world";
	public static final String TEST_HOOK_DESCRIPTION = "hwdesc";
	public static final String TEST_HOOK_TITLE = "hwname";
	public static final String TEST_HOOK_WORLD_ID = "hwid";
	public static final String TEST_HOOK_UNIVERSE_ID = "hwid2";
	public static final String TEST_HOOK_PLAYBACK_ID = "hwid3";
	public static final String TEST_HOOK_PREFETCH_PATIENT_KEY = "patient";
	public static final String TEST_HOOK_PREFETCH_MEDS_KEY = "medications";

	private final PointcutLatch myPointcutLatch = new PointcutLatch("Hello World CDS-Hook");

	@CdsService(value = TEST_HOOK_WORLD_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = "Patient/{{context.patientId}}"),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_MEDS_KEY, query = "MedicationRequest?patient={{context.patientId}}")
		})
	public String helloWorld(String theJson) {
		return "{\n" +
			"  \"cards\" : [ {\n" +
			"    \"summary\" : \"Hello World!\",\n" +
			"    \"indicator\" : \"warning\",\n" +
			"    \"source\" : {\n" +
			"      \"label\" : \"World Greeter\"\n" +
			"    },\n" +
			"    \"detail\" : \"This is a test.  Do not be alarmed.\"\n" +
			"  } ]\n" +
			"}";
	}

	@CdsServiceFeedback(TEST_HOOK_WORLD_ID)
	public String feedback(CdsServiceFeedbackJson theFeedback) {
		myPointcutLatch.call(theFeedback);
		return "{\"message\": \"Thank you for your feedback dated " + theFeedback.getOutcomeTimestamp() + "!\"}";
	}

	@CdsService(value = TEST_HOOK_UNIVERSE_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = "Patient/{{context.patientId}}"),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_MEDS_KEY, query = "MedicationRequest?patient={{context.patientId}}")
		})
	public String helloUniverse(String theJson) {
		return "{\n" +
			"  \"cards\" : [ {\n" +
			"    \"summary\" : \"Hello Universe!\",\n" +
			"    \"indicator\" : \"critical\",\n" +
			"    \"source\" : {\n" +
			"      \"label\" : \"World Greeter\"\n" +
			"    },\n" +
			"    \"detail\" : \"This is a test.  Do not be alarmed.\"\n" +
			"  } ]\n" +
			"}";
	}

	@CdsService(value = TEST_HOOK_PLAYBACK_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = "Patient/{{context.patientId}}"),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_MEDS_KEY, query = "MedicationRequest?patient={{context.patientId}}")
		})
	public String playback(CdsServiceRequestJson theCdsServiceRequestJson) {
		return "{\n" +
			"  \"cards\" : [ {\n" +
			"    \"summary\" : \"FhirServer: " + theCdsServiceRequestJson.getFhirServer() +
			" Hook: " + theCdsServiceRequestJson.getHook() +
			" Hook Instance: " + theCdsServiceRequestJson.getHookInstance() +
			"\",\n" +
			"    \"indicator\" : \"critical\",\n" +
			"    \"source\" : {\n" +
			"      \"label\" : \"World Greeter\"\n" +
			"    },\n" +
			"    \"detail\" : \"This is a test.  Do not be alarmed.\"\n" +
			"  } ]\n" +
			"}";
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
