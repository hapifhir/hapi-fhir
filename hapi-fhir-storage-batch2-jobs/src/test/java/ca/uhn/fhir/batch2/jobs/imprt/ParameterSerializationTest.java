package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterSerializationTest {

	@Test
	public void testBatchJobParametersSuccessfullySerializeAllFields() {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		BulkImportJobParameters parameters = new BulkImportJobParameters();
		parameters.addNdJsonUrl("myurl");
		parameters.setHttpBasicCredentials("username:password");
		startRequest.setParameters(parameters);

		BulkImportJobParameters readBackParameters = startRequest.getParameters(BulkImportJobParameters.class);

		assertEquals("username:password", readBackParameters.getHttpBasicCredentials());
	}
}
