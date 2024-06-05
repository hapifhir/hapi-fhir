package ca.uhn.fhir.jpa.provider.r5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderR5FqlTest extends BaseResourceProviderR5Test {

	@Test
	public void testFqlQuery() throws IOException {

		// Setup
		for (int i = 0; i < 20; i++) {
			createPatient(withActiveTrue(), withIdentifier("foo", "bar"), withFamily("Simpson" + i), withGiven("Homer"));
		}

		String select = """
			select name[0].family, name[0].given[0]
			from Patient
			where id in search_match('identifier', 'foo|bar')
			""";
		Parameters request = new Parameters();
		request.addParameter(HfqlConstants.PARAM_ACTION, new StringType(HfqlConstants.PARAM_ACTION_SEARCH));
		request.addParameter(HfqlConstants.PARAM_QUERY, new StringType(select));
		request.addParameter(HfqlConstants.PARAM_LIMIT, new IntegerType(100));
		request.addParameter(HfqlConstants.PARAM_FETCH_SIZE, new IntegerType(5));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + HfqlConstants.HFQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(myFhirContext, request));

		// Test
		try (CloseableHttpResponse response = ourHttpClient.execute(fetch)) {

			// Verify
			assertEquals(200, response.getStatusLine().getStatusCode());
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(outcome).contains("0,Simpson0,Homer");
			assertThat(outcome).contains("1,Simpson1,Homer");
		}

	}

}
