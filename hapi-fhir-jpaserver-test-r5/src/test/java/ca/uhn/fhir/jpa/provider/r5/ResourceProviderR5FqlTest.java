package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderR5FqlTest extends BaseResourceProviderR5Test {

	@Test
	public void testFqlQuery() {

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

		// Test
		String outcome = myServer.fhirRequest("/" + HfqlConstants.HFQL_EXECUTE)
			.post(request)
			.assertStatus(200)
			.getBody();

		// Verify
		assertThat(outcome).contains("0,Simpson0,Homer");
		assertThat(outcome).contains("1,Simpson1,Homer");

	}

}
