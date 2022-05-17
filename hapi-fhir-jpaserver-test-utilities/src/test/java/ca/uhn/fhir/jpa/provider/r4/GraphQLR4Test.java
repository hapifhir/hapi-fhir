package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.util.FileUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Patient;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.provider.GraphQLR4ProviderTest.DATA_PREFIX;
import static ca.uhn.fhir.jpa.provider.GraphQLR4ProviderTest.DATA_SUFFIX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class GraphQLR4Test extends BaseResourceProviderR4Test {
	public static final String INTROSPECTION_QUERY = "{\"query\":\"\\n    query IntrospectionQuery {\\n      __schema {\\n        queryType { name }\\n        mutationType { name }\\n        subscriptionType { name }\\n        types {\\n          ...FullType\\n        }\\n        directives {\\n          name\\n          description\\n          locations\\n          args {\\n            ...InputValue\\n          }\\n        }\\n      }\\n    }\\n\\n    fragment FullType on __Type {\\n      kind\\n      name\\n      description\\n      fields(includeDeprecated: true) {\\n        name\\n        description\\n        args {\\n          ...InputValue\\n        }\\n        type {\\n          ...TypeRef\\n        }\\n        isDeprecated\\n        deprecationReason\\n      }\\n      inputFields {\\n        ...InputValue\\n      }\\n      interfaces {\\n        ...TypeRef\\n      }\\n      enumValues(includeDeprecated: true) {\\n        name\\n        description\\n        isDeprecated\\n        deprecationReason\\n      }\\n      possibleTypes {\\n        ...TypeRef\\n      }\\n    }\\n\\n    fragment InputValue on __InputValue {\\n      name\\n      description\\n      type { ...TypeRef }\\n      defaultValue\\n    }\\n\\n    fragment TypeRef on __Type {\\n      kind\\n      name\\n      ofType {\\n        kind\\n        name\\n        ofType {\\n          kind\\n          name\\n          ofType {\\n            kind\\n            name\\n            ofType {\\n              kind\\n              name\\n              ofType {\\n                kind\\n                name\\n                ofType {\\n                  kind\\n                  name\\n                  ofType {\\n                    kind\\n                    name\\n                  }\\n                }\\n              }\\n            }\\n          }\\n        }\\n      }\\n    }\\n  \",\"operationName\":\"IntrospectionQuery\"}";
	private Logger ourLog = LoggerFactory.getLogger(GraphQLR4Test.class);
	private IIdType myPatientId0;

	@Test
	public void testInstance_Read_Patient() throws IOException {
		initTestPatients();

		String query = "{name{family,given}}";
		HttpGet httpGet = new HttpGet(ourServerBase + "/Patient/" + myPatientId0.getIdPart() + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			@Language("json")
			String expected = """
				{
				  "name":[{
				    "family":"FAM",
				    "given":["GIVEN1","GIVEN2"]
				  },{
				    "given":["GivenOnly1","GivenOnly2"]
				  }]
				}""";
			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + expected + DATA_SUFFIX), TestUtil.stripWhitespace(resp));
		}

	}

	@Test
	public void testInstance_Patient_Birthdate() throws IOException {
		initTestPatients();

		String query = "{birthDate}";
		HttpGet httpGet = new HttpGet(ourServerBase + "/Patient/" + myPatientId0.getIdPart() + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			@Language("json")
			String expected = """
            {
			    "birthDate": "1965-08-09"
				}""";
			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + expected + DATA_SUFFIX), TestUtil.stripWhitespace(resp));
		}

	}

	@Test
	public void testType_Introspect_Patient() throws IOException {
		initTestPatients();

		String uri = ourServerBase + "/Patient/$graphql";
		HttpPost httpGet = new HttpPost(uri);
		httpGet.setEntity(new StringEntity(INTROSPECTION_QUERY, ContentType.APPLICATION_JSON));

		// Repeat a couple of times to make sure it doesn't fail after the first one. At one point
		// the generator polluted the structure userdata and failed the second time
		for (int i = 0; i < 3; i++) {
			try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
				String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				ourLog.info(resp);
				assertEquals(200, response.getStatusLine().getStatusCode());
				assertThat(resp, containsString("{\"kind\":\"OBJECT\",\"name\":\"Patient\","));
				assertThat(resp, not(containsString("{\"kind\":\"OBJECT\",\"name\":\"Observation\",")));
				assertThat(resp, not(containsString("\"name\":\"Observation\",\"args\":[{\"name\":\"id\"")));
				assertThat(resp, not(containsString("\"name\":\"ObservationList\",\"args\":[{\"name\":\"_filter\"")));
				assertThat(resp, not(containsString("\"name\":\"ObservationConnection\",\"fields\":[{\"name\":\"count\"")));
				assertThat(resp, containsString("\"name\":\"Patient\",\"args\":[{\"name\":\"id\""));
				assertThat(resp, containsString("\"name\":\"PatientList\",\"args\":[{\"name\":\"_filter\""));
			}
		}
	}

	@Test
	public void testType_Introspect_Observation() throws IOException {
		initTestPatients();

		String uri = ourServerBase + "/Observation/$graphql";
		HttpPost httpGet = new HttpPost(uri);
		httpGet.setEntity(new StringEntity(INTROSPECTION_QUERY, ContentType.APPLICATION_JSON));

		// Repeat a couple of times to make sure it doesn't fail after the first one. At one point
		// the generator polluted the structure userdata and failed the second time
		for (int i = 0; i < 3; i++) {
			try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
				String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				ourLog.info(resp);
				assertEquals(200, response.getStatusLine().getStatusCode());
				assertThat(resp, not(containsString("{\"kind\":\"OBJECT\",\"name\":\"Patient\",")));
				assertThat(resp, containsString("{\"kind\":\"OBJECT\",\"name\":\"Observation\","));
				assertThat(resp, not(containsString("{\"kind\":\"OBJECT\",\"name\":\"Query\",\"fields\":[{\"name\":\"PatientList\"")));
				assertThat(resp, containsString("\"name\":\"Observation\",\"args\":[{\"name\":\"id\""));
				assertThat(resp, containsString("\"name\":\"ObservationList\",\"args\":[{\"name\":\"_filter\""));
				assertThat(resp, containsString("\"name\":\"ObservationConnection\",\"fields\":[{\"name\":\"count\""));
				assertThat(resp, not(containsString("\"name\":\"Patient\",\"args\":[{\"name\":\"id\"")));
				assertThat(resp, not(containsString("\"name\":\"PatientList\",\"args\":[{\"name\":\"_filter\"")));
			}
		}
	}

	@Test
	public void testRoot_Introspect() throws IOException {
		initTestPatients();

		String uri = ourServerBase + "/$graphql";
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(new StringEntity(INTROSPECTION_QUERY, ContentType.APPLICATION_JSON));

		// Repeat a couple of times to make sure it doesn't fail after the first one. At one point
		// the generator polluted the structure userdata and failed the second time
		for (int i = 0; i < 3; i++) {
			try (CloseableHttpResponse response = ourHttpClient.execute(httpPost)) {
				String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				ourLog.info("Response has size: {}", FileUtil.formatFileSize(resp.length()));
				assertEquals(200, response.getStatusLine().getStatusCode());
				assertThat(resp, containsString("{\"kind\":\"OBJECT\",\"name\":\"Patient\","));
				assertThat(resp, containsString("{\"kind\":\"OBJECT\",\"name\":\"Observation\","));
				assertThat(resp, containsString("\"name\":\"Observation\",\"args\":[{\"name\":\"id\""));
				assertThat(resp, containsString("\"name\":\"ObservationList\",\"args\":[{\"name\":\"_filter\""));
				assertThat(resp, containsString("\"name\":\"ObservationConnection\",\"fields\":[{\"name\":\"count\""));
				assertThat(resp, containsString("\"name\":\"Patient\",\"args\":[{\"name\":\"id\""));
				assertThat(resp, containsString("\"name\":\"PatientList\",\"args\":[{\"name\":\"_filter\""));
			}
		}
	}

	@Test
	public void testRoot_Read_Patient() throws IOException {
		initTestPatients();

		String query = "{Patient(id:\"" + myPatientId0.getIdPart() + "\"){name{family,given}}}";
		HttpGet httpGet = new HttpGet(ourServerBase + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);

			@Language("json")
			String expected = """
				{
				"Patient":{
				"name":[{
				"family":"FAM",
				"given":["GIVEN1","GIVEN2"]
				},{
				"given":["GivenOnly1","GivenOnly2"]
				}]
				}
				}""";
			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX +
				expected +
				DATA_SUFFIX), TestUtil.stripWhitespace(resp));
		}

	}


	@Test
	public void testRoot_Search_Patient() throws IOException {
		initTestPatients();

		String query = "{PatientList(given:\"given\"){name{family,given}}}";
		HttpGet httpGet = new HttpGet(ourServerBase + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			@Language("json")
			String expected = """
				{
				  "PatientList":[{
				    "name":[{
				      "family":"FAM",
				      "given":["GIVEN1","GIVEN2"]
				    },{
				      "given":["GivenOnly1","GivenOnly2"]
				    }]
				  },{
				    "name":[{
				      "given":["GivenOnlyB1","GivenOnlyB2"]
				    }]
				  }]
				}""";
			assertEquals(TestUtil.stripWhitespace(DATA_PREFIX + expected + DATA_SUFFIX), TestUtil.stripWhitespace(resp));
		}

	}

	@Test
	public void testRoot_Search_Observation() throws IOException {
		initTestPatients();

		String query = "{ObservationList(date: \"2022\") {id}}";
		HttpGet httpGet = new HttpGet(ourServerBase + "/$graphql?query=" + UrlUtil.escapeUrlParam(query));

		myCaptureQueriesListener.clear();
		try (CloseableHttpResponse response = ourHttpClient.execute(httpGet)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
		}
		myCaptureQueriesListener.logSelectQueries();
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
		p.setBirthDateElement(new DateType("1965-08-09"));
		myPatientId0 = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addName()
			.addGiven("GivenOnlyB1")
			.addGiven("GivenOnlyB2");
		myClient.create().resource(p).execute();
	}


}
