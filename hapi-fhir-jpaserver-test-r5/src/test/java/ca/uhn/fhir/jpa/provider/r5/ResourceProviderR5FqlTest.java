package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderR5FqlTest extends BaseResourceProviderR5Test {

	@Test
	public void testFqlQuery() throws IOException {

		// Setup
		for (int i = 0; i < 20; i++) {
			createPatient(withActiveTrue(), withIdentifier("foo", "bar"), withFamily("Simpson" + i), withGiven("Homer"));
		}

		String select = """
			from Patient
			search identifier = 'foo|bar'
			select name.family, name.given
			""";
		Parameters request = new Parameters();
		request.addParameter(FqlRestProvider.PARAM_QUERY, new StringType(select));
		request.addParameter(FqlRestProvider.PARAM_LIMIT, new IntegerType(100));
		request.addParameter(FqlRestProvider.PARAM_FETCH_SIZE, new IntegerType(5));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + FqlConstants.FQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(myFhirContext, request));

		// Test
		try (CloseableHttpResponse response = ourHttpClient.execute(fetch)) {

			// Verify
			String expected = """
				1
				the-search-id,100,"{""selectClauses"":[{""clause"":""name.family"",""alias"":""name.family""},{""clause"":""name.given"",""alias"":""name.given""}],""whereClauses"":[],""searchClauses"":[{""left"":""identifier"",""operator"":""EQUALS"",""right"":[""'foo|bar'""]}],""fromResourceName"":""Patient""}"
				"",name.family,name.given
				0,Simpson0,Homer
				1,Simpson1,Homer
				2,Simpson2,Homer
				3,Simpson3,Homer
				4,Simpson4,Homer
				""";
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String searchId = extractSearchId(outcome);
			assertEquals(expected, outcome.replace(searchId, "the-search-id"));
			assertEquals(200, response.getStatusLine().getStatusCode());
		}

	}

	private static String extractSearchId(String outcome) {
		String secondLine = outcome.split("\\n")[1];
		String searchId = secondLine.split(",")[0];
		return searchId;
	}

}
