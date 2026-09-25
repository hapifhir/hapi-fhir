package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderQuestionnaireResponseDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderQuestionnaireResponseDstu3Test.class);
	private static RequestValidatingInterceptor ourValidatingInterceptor;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myServer.unregisterInterceptor(ourValidatingInterceptor);
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

		myRestServer.registerInterceptor(ourValidatingInterceptor);
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
		qr1.getQuestionnaire().setReferenceElement(qId);
		qr1.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr1.addItem().setLinkId("link1").addAnswer().setValue(new DecimalType(123));
		try {
			myClient.create().resource(qr1).execute();
			fail("");
		} catch (UnprocessableEntityException e) {
			assertThat(e.toString()).contains("Answer value must be of the type string");
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
		
		HttpTestResponse response = myServer.fhirRequest("/QuestionnaireResponse").post(input, ca.uhn.fhir.rest.api.Constants.CT_FHIR_XML);
		String responseString = response.getBody();
		ourLog.info("Response: {}", responseString);
		response.assertStatus(201);
		String newIdString = response.getHeader(ca.uhn.fhir.rest.api.Constants.HEADER_LOCATION_LC);
		assertThat(newIdString).startsWith(myServerBase + "/QuestionnaireResponse/");
		final IdType id2 = new IdType(newIdString);

		responseString = myServer.fhirRequest("/QuestionnaireResponse/" + id2.getIdPart() + "?_format=xml&_pretty=true").get().getBody();
		ourLog.info("Response: {}", responseString);
		assertThat(responseString).contains("Exclusion Criteria");
	}

	@Test
	public void testValidateOnNoId() throws Exception {
		String responseString = myServer.fhirRequest("/QuestionnaireResponse/$validate").get().assertStatus(400).getBody();
		ourLog.info("Response: {}", responseString);
		assertThat(responseString).contains("No resource supplied for $validate operation");
	}
	
	
	/**
	 * From a Skype message from Brian Postlethwaite
	 */
	@Test
	public void testValidateQuestionnaireResponseWithNoIdForCreate() throws Exception {
		
		String input = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"mode\",\"valueString\":\"create\"},{\"name\":\"resource\",\"resource\":{\"resourceType\":\"QuestionnaireResponse\",\"questionnaire\":{\"reference\":\"http://fhirtest.uhn.ca/baseDstu2/Questionnaire/MedsCheckEligibility\"},\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">!-- populated from the rendered HTML below --></div>\"},\"status\":\"completed\",\"authored\":\"2017-02-10T00:02:58.098Z\"}}]}";
		String responseString = myServer.fhirRequest("/QuestionnaireResponse/$validate?_pretty=true").post(input, ca.uhn.fhir.rest.api.Constants.CT_JSON).assertStatus(200).getBody();
		ourLog.info("Response: {}", responseString);
	}
	
	/**
	 * From a Skype message from Brian Postlethwaite
	 */
	@Test
	public void testValidateQuestionnaireResponseWithNoIdForUpdate() throws Exception {
		
		String input = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"mode\",\"valueString\":\"update\"},{\"name\":\"resource\",\"resource\":{\"resourceType\":\"QuestionnaireResponse\",\"questionnaire\":{\"reference\":\"http://fhirtest.uhn.ca/baseDstu2/Questionnaire/MedsCheckEligibility\"},\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">!-- populated from the rendered HTML below --></div>\"},\"status\":\"completed\",\"authored\":\"2017-02-10T00:02:58.098Z\"}}]}";
		String responseString = myServer.fhirRequest("/QuestionnaireResponse/$validate?_pretty=true").post(input, ca.uhn.fhir.rest.api.Constants.CT_JSON).assertStatus(422).getBody();
		ourLog.info("Response: {}", responseString);
		assertThat(responseString).contains("Resource has no ID");
	}

	
	
}
