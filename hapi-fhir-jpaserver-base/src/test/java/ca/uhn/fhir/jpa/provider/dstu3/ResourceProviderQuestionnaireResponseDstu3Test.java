package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;

public class ResourceProviderQuestionnaireResponseDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderQuestionnaireResponseDstu3Test.class);
	private static RequestValidatingInterceptor ourValidatingInterceptor;

	@AfterClass
	public static void afterClassClearContext() {
		ourRestServer.unregisterInterceptor(ourValidatingInterceptor);
		ourValidatingInterceptor = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Override
	@Before
	public void before() throws Exception {
		super.before();

		if (ourValidatingInterceptor == null) {
			ourValidatingInterceptor = new RequestValidatingInterceptor();
			ourValidatingInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);

			Collection<IValidatorModule> validators = myAppCtx.getBeansOfType(IValidatorModule.class).values();
			for (IValidatorModule next : validators) {
				ourValidatingInterceptor.addValidatorModule(next);
			}
			ourRestServer.registerInterceptor(ourValidatingInterceptor);
		}
	}

	
	
	@Test
	public void testCreateWithLocalReference() {
		Patient pt1 = new Patient();
		pt1.addName().addFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Questionnaire q1 = new Questionnaire();
		q1.addItem().setLinkId("link1").setType(QuestionnaireItemType.STRING);
		IIdType qId = myQuestionnaireDao.create(q1, mySrd).getId().toUnqualifiedVersionless();
		
		QuestionnaireResponse qr1 = new QuestionnaireResponse();
		qr1.getQuestionnaire().setReferenceElement(qId);
		qr1.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr1.addItem().setLinkId("link1").addAnswer().setValue(new DecimalType(123));
		try {
			ourClient.create().resource(qr1).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.toString(), containsString("Answer value must be of type string"));
		}
	}
	
	@Test
	public void testCreateWithAbsoluteReference() {
		Patient pt1 = new Patient();
		pt1.addName().addFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Questionnaire q1 = new Questionnaire();
		q1.addItem().setLinkId("link1").setType(QuestionnaireItemType.STRING);
		IIdType qId = myQuestionnaireDao.create(q1, mySrd).getId().toUnqualifiedVersionless();
		
		QuestionnaireResponse qr1 = new QuestionnaireResponse();
		qr1.getQuestionnaire().setReferenceElement(qId.withServerBase("http://example.com", "Questionnaire"));
		qr1.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr1.addItem().setLinkId("link1").addAnswer().setValue(new DecimalType(123));
		try {
			ourClient.create().resource(qr1).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.toString(), containsString("Answer value must be of type string"));
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
		post.setEntity(new StringEntity(input, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		final IdType id2;
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent());
			ourLog.info("Response: {}", responseString);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/QuestionnaireResponse/"));
			id2 = new IdType(newIdString);
		} finally {
			IOUtils.closeQuietly(response);
		}

		HttpGet get = new HttpGet(ourServerBase + "/QuestionnaireResponse/" + id2.getIdPart() + "?_format=xml&_pretty=true");
		response = ourHttpClient.execute(get);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent());
			ourLog.info("Response: {}", responseString);
			assertThat(responseString, containsString("Exclusion Criteria"));
		} finally {
			IOUtils.closeQuietly(response);
		}
		
		
		
	}


}
