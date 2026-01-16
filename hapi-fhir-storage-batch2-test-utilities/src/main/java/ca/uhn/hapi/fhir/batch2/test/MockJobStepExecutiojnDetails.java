package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobInstance;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;

public class MockJobStepExecutiojnDetails implements IJobStepExecutionServices {
	@Override
	public SystemRequestDetails newRequestDetails(IJobInstance theJobInstance) {
		return new SystemRequestDetails();
	}
}
