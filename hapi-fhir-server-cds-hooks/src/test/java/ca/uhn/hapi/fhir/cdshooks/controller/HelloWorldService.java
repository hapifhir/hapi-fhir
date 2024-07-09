package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.hapi.fhir.cdshooks.api.CdsService;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServiceFeedback;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServicePrefetch;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceAcceptedSuggestionJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceIndicatorEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardSourceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.custom.extensions.model.ExampleConfigExtension;
import ca.uhn.hapi.fhir.cdshooks.custom.extensions.model.RequestExtension;
import ca.uhn.hapi.fhir.cdshooks.custom.extensions.model.ResponseExtension;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HelloWorldService implements IPointcutLatch {
	public static final String TEST_HOOK = "hello-world";
	public static final String TEST_HOOK_DESCRIPTION = "hwdesc";
	public static final String TEST_HOOK_TITLE = "hwname";
	public static final String TEST_HOOK_WORLD_ID = "hwid";
	public static final String TEST_HOOK_UNIVERSE_ID = "hwid2";
	public static final String TEST_HOOK_PLAYBACK_ID = "hwid3";
	public static final String TEST_HOOK_PREFETCH_PATIENT_KEY = "patient";
	public static final String TEST_HOOK_PREFETCH_MEDS_KEY = "medications";
	public static final String CDS_HOOKS_EXTENSION_PROPERTY_PRACTITIONER_SPECIALITY = "myextension-practitionerspecialty";
	public static final String CDS_HOOKS_EXTENSION_PROPERTY_TIMESTAMP = "timestamp";
	public static final String CDS_HOOKS_EXTENSION_VALUE_PRACTITIONER_SPECIALITY = "some-speciality";

	private final PointcutLatch myPointcutLatch = new PointcutLatch("Hello World CDS-Hook");

	@CdsService(value = TEST_HOOK_WORLD_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = "Patient/{{context.patientId}}"),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_MEDS_KEY, query = "MedicationRequest?patient={{context.patientId}}")
		})
	public CdsServiceResponseJson helloWorld(String theJson) {
		final CdsServiceResponseJson cdsServiceResponseJson = new CdsServiceResponseJson();
		final CdsServiceResponseCardJson cdsServiceResponseCardJson = new CdsServiceResponseCardJson();
		cdsServiceResponseCardJson.setSummary("Hello World!");
		cdsServiceResponseCardJson.setIndicator(CdsServiceIndicatorEnum.WARNING);
		cdsServiceResponseCardJson.setDetail("This is a test.  Do not be alarmed.");
		cdsServiceResponseCardJson.setSource(new CdsServiceResponseCardSourceJson().setLabel("World Greeter"));
		final ResponseExtension extension = new ResponseExtension();
		extension.setTimestamp(new Date());
		extension.setPractitionerSpecialty(CDS_HOOKS_EXTENSION_VALUE_PRACTITIONER_SPECIALITY);
		cdsServiceResponseCardJson.setExtension(extension);
		cdsServiceResponseJson.addCard(cdsServiceResponseCardJson);
		return cdsServiceResponseJson;
	}

	@CdsServiceFeedback(TEST_HOOK_WORLD_ID)
	public CdsServiceFeedbackJson feedback(CdsServiceFeedbackJson theFeedback) {
		myPointcutLatch.call(theFeedback);
		theFeedback.setAcceptedSuggestions(List.of(new CdsServiceAcceptedSuggestionJson().setId(UUID.randomUUID().toString())));
		return theFeedback;
	}

	@CdsService(value = TEST_HOOK_UNIVERSE_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = "Patient/{{context.patientId}}"),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_MEDS_KEY, query = "MedicationRequest?patient={{context.patientId}}")
		},
		extension = """
        {
   				"example-config-item": "example-value"
   		}
		""",
		extensionClass = RequestExtension.class)
	public CdsServiceResponseJson helloUniverse(CdsServiceRequestJson theCdsServiceRequestJson) {
		final CdsServiceResponseJson cdsServiceResponseJson = new CdsServiceResponseJson();
		final CdsServiceResponseCardJson cdsServiceResponseCardJson = new CdsServiceResponseCardJson();
		cdsServiceResponseCardJson.setSummary("Hello Universe!");
		cdsServiceResponseCardJson.setIndicator(CdsServiceIndicatorEnum.CRITICAL);
		cdsServiceResponseCardJson.setDetail("This is a test.  Do not be alarmed.");
		cdsServiceResponseCardJson.setSource(new CdsServiceResponseCardSourceJson().setLabel("World Greeter"));
		cdsServiceResponseCardJson.setExtension(theCdsServiceRequestJson.getExtension());
		cdsServiceResponseJson.addCard(cdsServiceResponseCardJson);
		return cdsServiceResponseJson;
	}

	@CdsService(value = TEST_HOOK_PLAYBACK_ID,
		hook = TEST_HOOK,
		title = TEST_HOOK_TITLE,
		description = TEST_HOOK_DESCRIPTION,
		prefetch = {
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_PATIENT_KEY, query = "Patient/{{context.patientId}}"),
			@CdsServicePrefetch(value = TEST_HOOK_PREFETCH_MEDS_KEY, query = "MedicationRequest?patient={{context.patientId}}")
		},
		extension = """
		{
			"example-client-conformance": "http://hooks.example.org/fhir/102/Conformance/patientview"
		}
		""",
		extensionClass = ExampleConfigExtension.class)
	public CdsServiceResponseJson playback(CdsServiceRequestJson theCdsServiceRequestJson) {
		final CdsServiceResponseJson cdsServiceResponseJson = new CdsServiceResponseJson();
		final CdsServiceResponseCardJson cdsServiceResponseCardJson = new CdsServiceResponseCardJson();
		cdsServiceResponseCardJson.setSummary("FhirServer: " + theCdsServiceRequestJson.getFhirServer() +
			" Hook: " + theCdsServiceRequestJson.getHook() +
			" Hook Instance: " + theCdsServiceRequestJson.getHookInstance());
		cdsServiceResponseCardJson.setIndicator(CdsServiceIndicatorEnum.CRITICAL);
		cdsServiceResponseCardJson.setDetail("This is a test.  Do not be alarmed.");
		cdsServiceResponseCardJson.setSource(new CdsServiceResponseCardSourceJson().setLabel("World Greeter"));
		cdsServiceResponseJson.addCard(cdsServiceResponseCardJson);
		return cdsServiceResponseJson;
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
