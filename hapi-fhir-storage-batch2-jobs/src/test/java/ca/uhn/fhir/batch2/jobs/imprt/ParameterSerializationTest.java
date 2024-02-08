package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
public class ParameterSerializationTest {

	@Test
	public void testBatchJobParametersSuccessfullySerializeAllFields() {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		BulkImportJobParameters parameters = new BulkImportJobParameters();
		parameters.addNdJsonUrl("myurl");
		parameters.setHttpBasicCredentials("username:password");
		startRequest.setParameters(parameters);

		BulkImportJobParameters readBackParameters = startRequest.getParameters(BulkImportJobParameters.class);

		assertThat(readBackParameters.getHttpBasicCredentials(), is(equalTo("username:password")));
	}
}
