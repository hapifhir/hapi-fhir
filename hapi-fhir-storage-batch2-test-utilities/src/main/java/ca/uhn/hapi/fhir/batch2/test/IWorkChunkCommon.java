package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;

public interface IWorkChunkCommon extends WorkChunkTestConstants {

	/**
	 * Returns the concrete class that is implementing this stuff.
	 * Used primarily for structure
	 */
	ITestFixture getTestManager();

	default JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setReport("TEST");
		return instance;
	}
}
