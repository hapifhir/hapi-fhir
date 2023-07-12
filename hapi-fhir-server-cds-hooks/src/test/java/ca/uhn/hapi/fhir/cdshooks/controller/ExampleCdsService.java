package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.hapi.fhir.cdshooks.api.CdsService;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServiceFeedback;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServicePrefetch;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceFeedbackJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceIndicatorEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardSourceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import org.hl7.fhir.r4.model.Patient;

public class ExampleCdsService {
	@CdsService(value = "example-service",
		hook = "patient-view",
		title = "Greet a patient",
		description = "This service says hello to a patient",
		prefetch = {
			@CdsServicePrefetch(value = "patient", query = "Patient/{{context.patientId}}")
		})
	public CdsServiceResponseJson exampleService(CdsServiceRequestJson theCdsRequest) {
		Patient patient = (Patient) theCdsRequest.getPrefetch("patient");
		CdsServiceResponseJson response = new CdsServiceResponseJson();
		CdsServiceResponseCardJson card = new CdsServiceResponseCardJson();
		card.setSummary("Hello " + patient.getNameFirstRep().getNameAsSingleString());
		card.setIndicator(CdsServiceIndicatorEnum.INFO);
		CdsServiceResponseCardSourceJson source = new CdsServiceResponseCardSourceJson();
		source.setLabel("Smile CDR");
		card.setSource(source);
		response.addCard(card);
		return response;
	}

	@CdsServiceFeedback("example-service")
	public String exampleServiceFeedback(CdsServiceFeedbackJson theFeedback) {
		return "{\"message\": \"Thank you for your feedback dated " + theFeedback.getOutcomeTimestamp() + "!\"}";
	}
}
