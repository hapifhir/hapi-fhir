package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
public class ParameterSerializationTest {

	@Test
	public void test_serialize_for_batch() {
		BulkImportJobParameters parameters = new BulkImportJobParameters();
		parameters.addNdJsonUrl("myurl");
		parameters.setHttpBasicCredentials("username:password");
		String defaultSerialization = JsonUtil.serializeOrInvalidRequest(parameters);

		assertThat(defaultSerialization, is(not(containsString("username:password"))));
		String batchJobSerialization = JsonUtil.serializeForBatchJob(parameters);
		assertThat(batchJobSerialization, is(containsString("username:password")));
	}
}
