package ca.uhn.fhir.jpa.provider.r4;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.jena.base.Sys;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.*; import static org.hamcrest.MatcherAssert.assertThat;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.springframework.beans.factory.annotation.Autowired;

public class ResourceProviderQuestionnaireResponseR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderQuestionnaireResponseR4Test.class);
	private static RequestValidatingInterceptor ourValidatingInterceptor;


	@Autowired
	MatchUrlService myMatchUrlService;

	@AfterAll
	public static void afterClassClearContext() {
		ourRestServer.unregisterInterceptor(ourValidatingInterceptor);
		ourValidatingInterceptor = null;
	}


	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		if (ourValidatingInterceptor == null) {
			ourValidatingInterceptor = new RequestValidatingInterceptor();
			ourValidatingInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);

			Collection<IValidatorModule> validators = myAppCtx.getBeansOfType(IValidatorModule.class).values();
			for (IValidatorModule next : validators) {
				ourValidatingInterceptor.addValidatorModule(next);
			}
		}

		ourRestServer.getInterceptorService().registerInterceptor(ourValidatingInterceptor);
	}

	


	@Test

	public void testCreateWithNonLocalReferenceWorksWithIncludes() {

		myModelConfig.setTreatBaseUrlsAsLocal(Collections.singleton("https://hapi.fhir.org/baseR4/"));

		String q = "{\n" +
			"  \"resourceType\": \"Questionnaire\",\n" +
			"  \"id\": \"xl-54127-6-hapi\",\n" +
			"  \"url\": \"https://hapi.fhir.org/baseR4/Questionnaire/xl-54127-6-hapi\",\n" +
			"  \"meta\": {\n" +
			"    \"versionId\": \"1\",\n" +
			"    \"lastUpdated\": \"2021-07-11T05:23:45.000-04:00\",\n" +
			"    \"source\": \"#se9YJtmM96g7kGYo\",\n" +
			"    \"profile\": [ \"http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire|2.7\" ],\n" +
			"    \"tag\": [ {\n" +
			"      \"code\": \"lformsVersion: 25.0.0\"\n" +
			"    } ]\n" +
			"  },\n" +
			"  \"extension\": [ {\n" +
			"    \"url\": \"http://hl7.org/fhir/StructureDefinition/questionnaire-launchContext\",\n" +
			"    \"extension\": [ {\n" +
			"      \"url\": \"name\",\n" +
			"      \"valueId\": \"patient\"\n" +
			"    }, {\n" +
			"      \"url\": \"type\",\n" +
			"      \"valueCode\": \"Patient\"\n" +
			"    }, {\n" +
			"      \"url\": \"descripton\",\n" +
			"      \"valueString\": \"For filling in patient information as the subject for the form\"\n" +
			"    } ]\n" +
			"  } ],\n" +
			"  \"identifier\": [ {\n" +
			"    \"system\": \"http://loinc.org\",\n" +
			"    \"value\": \"54127-6\"\n" +
			"  } ],\n" +
			"  \"name\": \"US Surgeon General family health portrait\",\n" +
			"  \"title\": \"US Surgeon General family health portrait\",\n" +
			"  \"status\": \"draft\",\n" +
			"  \"subjectType\": [ \"Patient\", \"Person\" ],\n" +
			"  \"date\": \"2018-11-05T16:54:56-05:00\",\n" +
			"  \"code\": [ {\n" +
			"    \"system\": \"http://loinc.org\",\n" +
			"    \"code\": \"54127-6\",\n" +
			"    \"display\": \"US Surgeon General family health portrait\"\n" +
			"  } ],\n" +
			"  \"item\": [ {\n" +
			"    \"linkId\": \"/54126-8\",\n" +
			"    \"code\": [ {\n" +
			"      \"system\": \"http://loinc.org\",\n" +
			"      \"code\": \"54126-8\",\n" +
			"      \"display\": \"My health history\"\n" +
			"    } ],\n" +
			"    \"text\": \"My health history\",\n" +
			"    \"type\": \"group\",\n" +
			"    \"required\": false\n" +
			"  } ]\n" +
			"}\n";

		String qr = "{\n" +
			"  \"resourceType\": \"QuestionnaireResponse\",\n" +
			"  \"id\": \"xl-5770809-hapi\",\n" +
			"  \"meta\": {\n" +
			"    \"versionId\": \"1\",\n" +
			"    \"lastUpdated\": \"2021-07-15T14:26:27.000-04:00\",\n" +
			"    \"source\": \"#5w9ykUMceXVLTyAa\",\n" +
			"    \"profile\": [ \"http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaireresponse|2.7\" ],\n" +
			"    \"tag\": [ {\n" +
			"      \"code\": \"lformsVersion: 29.1.0\"\n" +
			"    } ]\n" +
			"  },\n" +
//			"  \"questionnaire\": \"Questionnaire/xl-54127-6-hapi\",\n" +
			"  \"questionnaire\": \"https://hapi.fhir.org/baseR4/Questionnaire/xl-54127-6-hapi\",\n" +
			"  \"status\": \"completed\",\n" +
			"  \"authored\": \"2021-07-15T18:26:27.707Z\",\n" +
			"  \"item\": [ {\n" +
			"    \"linkId\": \"/54126-8\",\n" +
			"    \"text\": \"My health history\",\n" +
			"    \"item\": [ {\n" +
			"      \"linkId\": \"/54126-8/54125-0\",\n" +
			"      \"text\": \"Name\",\n" +
			"      \"answer\": [ {\n" +
			"        \"valueString\": \"TAMBRA AGARWAL\"\n" +
			"      } ]\n" +
			"    }, {\n" +
			"      \"linkId\": \"/54126-8/21112-8\",\n" +
			"      \"text\": \"Birth Date\",\n" +
			"      \"answer\": [ {\n" +
			"        \"valueDate\": \"2094-01-01\"\n" +
			"      } ]\n" +
			"    } ]\n" +
			"  } ]\n" +
			"}\n";

		Questionnaire questionnaire = myFhirCtx.newJsonParser().parseResource(Questionnaire.class, q);
		QuestionnaireResponse questionnaireResponse = myFhirCtx.newJsonParser().parseResource(QuestionnaireResponse.class, qr);

		myQuestionnaireDao.update(questionnaire);
		myQuestionnaireResponseDao.update(questionnaireResponse);
		RuntimeResourceDefinition questionnaireResponse1 = myFhirCtx.getResourceDefinition("QuestionnaireResponse");

		SearchParameterMap spMap = new SearchParameterMap();
		spMap.setLoadSynchronous(true);
		spMap.addInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
		spMap.add("_id", new TokenParam("xl-5770809-hapi"));
		IBundleProvider search = myQuestionnaireResponseDao.search(spMap);
		assertThat(search.size(), is(equalTo(2)));
	}

	@SuppressWarnings("unused")
	@Test
	public void testCreateWithLocalReference() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Questionnaire q1 = new Questionnaire();
		q1.addItem().setLinkId("link1").setType(QuestionnaireItemType.STRING);
		IIdType qId = myQuestionnaireDao.create(q1, mySrd).getId().toUnqualifiedVersionless();
		
		QuestionnaireResponse qr1 = new QuestionnaireResponse();
		qr1.setQuestionnaire(qId.getValue());
		qr1.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr1.addItem().setLinkId("link1").addAnswer().setValue(new DecimalType(123));
		try {
			myClient.create().resource(qr1).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(myFhirCtx.newJsonParser().encodeResourceToString(e.getOperationOutcome()), containsString("Answer value must be of type string"));
		}
	}
	
	@Test
	public void testSaveQuestionnaire() throws Exception {
		String input = "<QuestionnaireResponse xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <status value=\"completed\"/>\n" + 
				"    <authored value=\"2016-05-03T13:05:20-04:00\"/>\n" + 
				"    <item>\n" + 
				"        <linkId value=\"breast-feeding-intention\"/>\n" + 
				"        <text value=\"Breast Feeding Intention:\"/>\n" + 
				"        <answer>\n" + 
				"            <valueCoding>\n" + 
				"                <system value=\"http://example.org/codesystem-breastfeeding-intention\"/>\n" + 
				"                <code value=\"true\"/>\n" + 
				"                <display value=\"Mother wants to provide formula exclusively\"/>\n" + 
				"            </valueCoding>\n" + 
				"        </answer>\n" + 
				"    </item>\n" + 
				"    <item>\n" + 
				"        <linkId value=\"breast-feeding-education\"/>\n" + 
				"        <text value=\"Answer if not exclusive BM:\"/>\n" + 
				"        <answer>\n" + 
				"            <valueCoding>\n" + 
				"                <system value=\"http://example.org/codesystem-breastfeeding-education\"/>\n" + 
				"                <code value=\"true\"/>\n" + 
				"                <display value=\"Mother not given comprehensive education per protocol\"/>\n" + 
				"            </valueCoding>\n" + 
				"        </answer>\n" + 
				"    </item>\n" + 
				"    <item>\n" + 
				"        <linkId value=\"breast-feeding-exclusion\"/>\n" + 
				"        <text value=\"Exclusion Criteria:\"/>\n" + 
				"        <answer>\n" + 
				"            <valueCoding>\n" + 
				"                <system value=\"http://example.org/codesystem-breastfeeding-exclusion\"/>\n" + 
				"                <code value=\"true\"/>\n" + 
				"                <display\n" + 
				"                    value=\"Maternal use of drugs of abuse, antimetabolites, chemotherapeutic agents, or radioisotopes\"\n" + 
				"                />\n" + 
				"            </valueCoding>\n" + 
				"        </answer>\n" + 
				"    </item>\n" + 
				"</QuestionnaireResponse>";
		
		HttpPost post = new HttpPost(ourServerBase + "/QuestionnaireResponse");
		post.setEntity(new StringEntity(input, ContentType.create(ca.uhn.fhir.rest.api.Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		final IdType id2;
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(ca.uhn.fhir.rest.api.Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/QuestionnaireResponse/"));
			id2 = new IdType(newIdString);
		} finally {
			IOUtils.closeQuietly(response);
		}

		HttpGet get = new HttpGet(ourServerBase + "/QuestionnaireResponse/" + id2.getIdPart() + "?_format=xml&_pretty=true");
		response = ourHttpClient.execute(get);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertThat(responseString, containsString("Exclusion Criteria"));
		} finally {
			IOUtils.closeQuietly(response);
		}
		
		
		
	}

	@Test
	public void testValidateOnNoId() throws Exception {
		HttpGet get = new HttpGet(ourServerBase + "/QuestionnaireResponse/$validate");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertThat(responseString, containsString("No resource supplied for $validate operation"));
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response);
		}
		
	}
	
	
	/**
	 * From a Skype message from Brian Postlethwaite
	 */
	@Test
	public void testValidateQuestionnaireResponseWithNoIdForCreate() throws Exception {
		
		String input = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"mode\",\"valueString\":\"create\"},{\"name\":\"resource\",\"resource\":{\"resourceType\":\"QuestionnaireResponse\",\"questionnaire\":\"http://fhirtest.uhn.ca/baseDstu2/Questionnaire/MedsCheckEligibility\",\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">!-- populated from the rendered HTML below --></div>\"},\"status\":\"completed\",\"authored\":\"2017-02-10T00:02:58.098Z\"}}]}";
		HttpPost post = new HttpPost(ourServerBase + "/QuestionnaireResponse/$validate?_pretty=true");
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response);
		}
		
	}
	
	/**
	 * From a Skype message from Brian Postlethwaite
	 */
	@Test
	public void testValidateQuestionnaireResponseWithNoIdForUpdate() throws Exception {
		
		String input = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"mode\",\"valueString\":\"update\"},{\"name\":\"resource\",\"resource\":{\"resourceType\":\"QuestionnaireResponse\",\"questionnaire\":\"http://fhirtest.uhn.ca/baseDstu2/Questionnaire/MedsCheckEligibility\",\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">!-- populated from the rendered HTML below --></div>\"},\"status\":\"completed\",\"authored\":\"2017-02-10T00:02:58.098Z\"}}]}";
		HttpPost post = new HttpPost(ourServerBase + "/QuestionnaireResponse/$validate?_pretty=true");
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertThat(responseString, containsString("Resource has no ID"));
			assertEquals(422, response.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(response);
		}
		
	}

	
	
}
