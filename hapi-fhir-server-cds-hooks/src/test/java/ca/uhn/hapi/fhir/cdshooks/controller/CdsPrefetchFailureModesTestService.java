package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.hapi.fhir.cdshooks.api.CdsPrefetchFailureMode;
import ca.uhn.hapi.fhir.cdshooks.api.CdsService;
import ca.uhn.hapi.fhir.cdshooks.api.CdsServicePrefetch;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;

public class CdsPrefetchFailureModesTestService {

	@CdsService(value = "CdsPrefetchFailureModesTestService",
		hook = "prefetch-failure-modes-test-hook",
		description = "a test CDS hook for testing prefetch failure modes",
		prefetch = {
			@CdsServicePrefetch(
				value = "failureModeFailPatient",
				query = "Patient/{{context.failureModeFailPatientId}}",
				failureMode = CdsPrefetchFailureMode.FAIL
				),
			@CdsServicePrefetch(
				value = "failureModeOmitPatient",
				query = "Patient/{{context.failureModeOmitPatientId}}",
				failureMode = CdsPrefetchFailureMode.OMIT
			),
			@CdsServicePrefetch(
				value = "failureModeOperationOutcomePatient",
				query = "Patient/{{context.failureModeOperationOutcomePatientId}}",
				failureMode = CdsPrefetchFailureMode.OPERATION_OUTCOME
			)
		})
	public CdsServiceResponseJson serveWithFailureModes(String theJson) {

		CdsServiceResponseJson response = new CdsServiceResponseJson();
		return response;
	}
}
