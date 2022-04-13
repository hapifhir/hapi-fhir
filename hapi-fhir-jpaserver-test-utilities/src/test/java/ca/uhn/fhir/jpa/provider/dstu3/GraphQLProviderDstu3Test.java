package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.provider.GraphQLR4ProviderTest.DATA_PREFIX;
import static ca.uhn.fhir.jpa.provider.GraphQLR4ProviderTest.DATA_SUFFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphQLProviderDstu3Test extends BaseResourceProviderDstu3Test {
	private Logger ourLog = LoggerFactory.getLogger(GraphQLProviderDstu3Test.class);
	private IIdType myPatientId0;

	@Test
	public void testInstanceSimpleRead() throws IOException {
		initTestPatients();

		String query = "{name{family,given}}";
		HttpGet httpGet = new HttpGet(ourServerBase + "/Patient/" + myPatientId0.getIdPart() + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX +"{\n" +
				"  \"name\":[{\n" +
				"    \"family\":\"FAM\",\n" +
				"    \"given\":[\"GIVEN1\",\"GIVEN2\"]\n" +
				"  },{\n" +
				"    \"given\":[\"GivenOnly1\",\"GivenOnly2\"]\n" +
				"  }]\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(resp));
		}

	}

	@Test
	public void testSystemSimpleSearch() throws IOException {
		initTestPatients();

		String query = "{PatientList(given:\"given\"){name{family,given}}}";
		HttpGet httpGet = new HttpGet(ourServerBase + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX +"{\n" +
				"  \"PatientList\":[{\n" +
				"    \"name\":[{\n" +
				"      \"family\":\"FAM\",\n" +
				"      \"given\":[\"GIVEN1\",\"GIVEN2\"]\n" +
				"    },{\n" +
				"      \"given\":[\"GivenOnly1\",\"GivenOnly2\"]\n" +
				"    }]\n" +
				"  },{\n" +
				"    \"name\":[{\n" +
				"      \"given\":[\"GivenOnlyB1\",\"GivenOnlyB2\"]\n" +
				"    }]\n" +
				"  }]\n" +
				"}" + DATA_SUFFIX), TestUtil.stripWhitespace(resp));
		}

	}

	private void initTestPatients() {
		Patient p = new Patient();
		p.addName()
			.setFamily("FAM")
			.addGiven("GIVEN1")
			.addGiven("GIVEN2");
		p.addName()
			.addGiven("GivenOnly1")
			.addGiven("GivenOnly2");
		myPatientId0 = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addName()
			.addGiven("GivenOnlyB1")
			.addGiven("GivenOnlyB2");
		ourClient.create().resource(p).execute();
	}


}
