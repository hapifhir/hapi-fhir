package ca.uhn.fhir.jpa.provider.dstu3;

import com.google.common.base.Charsets;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ObservationLastnDstu3Test extends BaseResourceProviderDstu3Test {

	/**
	 * See #3986. Verifies that supplying the max parameter in the $lastn does not cause a validation error.
	 */
	@Test
	public void testSupplyingMaxToTheLastNOPerationDoesNotCauseAValidationError() throws Exception {
		String outcome = executeApiCall(myServerBase + "/Observation/$lastn?max=1");
		assertThat(outcome).doesNotContain("HAPI-524");
	}

	private String executeApiCall(String theUrl) throws IOException {
		String outcome;
		HttpGet get = new HttpGet(theUrl);
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			outcome = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
		} finally {
			IOUtils.closeQuietly(resp);
		}

		return outcome;
	}
}
