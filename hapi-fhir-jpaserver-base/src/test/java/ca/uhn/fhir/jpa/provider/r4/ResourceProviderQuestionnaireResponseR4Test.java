package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderQuestionnaireResponseR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderQuestionnaireResponseR4Test.class);
	private static RequestValidatingInterceptor ourValidatingInterceptor;

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
		String baseUrl = "https://hapi.fhir.org/baseR4/";

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setUrl(baseUrl + "Questionnaire/my-questionnaire");
		questionnaire.setId("my-questionnaire");

		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		questionnaireResponse.setQuestionnaire(questionnaire.getUrl());
		questionnaireResponse.setId("my-questionnaire-response");

		myQuestionnaireDao.update(questionnaire);
		myQuestionnaireResponseDao.update(questionnaireResponse);

		SearchParameterMap spMap = new SearchParameterMap();
		spMap.setLoadSynchronous(true);
		spMap.addInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
		spMap.add("_id", new TokenParam("my-questionnaire-response"));
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
			assertThat(myFhirContext.newJsonParser().encodeResourceToString(e.getOperationOutcome()), containsString("Answer value must be of type string"));
		}
	}

	/**
	 * Test added to verify <a href="https://github.com/hapifhir/hapi-fhir/issues/2843">https://github.com/hapifhir/hapi-fhir/issues/2843</a>
	 */
	@Test
	public void testSearch_withIncludeQuestionnaire_shouldReturnWithCanonicalReferencedQuestionnaire() {
		String questionnaireCanonicalUrl = "https://hapi.fhir.org/baseR4/Questionnaire/xl-54127-6-hapi";
		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setId("xl-54127-6-hapi");
		questionnaire.setUrl(questionnaireCanonicalUrl);
		questionnaire.addIdentifier(new Identifier().setSystem("http://loinc.org").setValue("54127-6"));
		questionnaire.setName("US Surgeon General family health portrait");
		questionnaire.setTitle(questionnaire.getName());
		questionnaire.setStatus(Enumerations.PublicationStatus.DRAFT);
		questionnaire.addSubjectType("Patient").addSubjectType("Person");
		questionnaire.addCode(new Coding("http://loinc.org", "54127-6", questionnaire.getName()));
		questionnaire.addItem().setLinkId("/54126-8")
			.setType(QuestionnaireItemType.GROUP)
			.setRequired(false)
			.setText("My health history")
			.addCode(new Coding("http://loinc.org", "54126-8", "My health history"));
		IIdType qIdType = myQuestionnaireDao.create(questionnaire, mySrd).getId().toUnqualifiedVersionless();

		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		questionnaireResponse.setId("xl-54127-6-hapi-response");
		questionnaireResponse.setQuestionnaire(questionnaireCanonicalUrl);
		questionnaireResponse.setStatus(QuestionnaireResponseStatus.COMPLETED);
		questionnaireResponse.addItem().setLinkId("/54126-8")
			.setText("My health history")
			.addItem().setLinkId("/54126-8/54125-0").setText("Name").addAnswer().setValue(new StringType("TAMBRA AGARWAL"))
			.addItem().setLinkId("/54126-8/21112-8").setText("Birth Date").addAnswer().setValue(new DateType("1994-01-01"));
		IIdType qrIdType = myQuestionnaireResponseDao.create(questionnaireResponse, mySrd).getId().toUnqualifiedVersionless();

		// Search
		Bundle results = myClient.search()
			.byUrl("QuestionnaireResponse?_id=" + qrIdType.toUnqualifiedVersionless()  + "&_include=QuestionnaireResponse:questionnaire")
			.returnBundle(Bundle.class)
			.execute();
		assertThat(results.getEntry().size(), is(equalTo(2)));

		List<String> expectedIds = new ArrayList<>();
		expectedIds.add(qrIdType.getValue());
		expectedIds.add(qIdType.getValue());

		List<String> actualIds = results.getEntry().stream()
			.map(Bundle.BundleEntryComponent::getResource)
			.map(resource -> resource.getIdElement().toUnqualifiedVersionless().toString())
			.collect(Collectors.toList());
		assertEquals(expectedIds, actualIds);
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
