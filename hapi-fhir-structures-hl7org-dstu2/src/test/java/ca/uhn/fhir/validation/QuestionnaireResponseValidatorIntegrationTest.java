package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.hapi.validation.FhirQuestionnaireResponseValidator;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.IntegerType;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.Questionnaire.AnswerFormat;
import org.hl7.fhir.instance.model.Questionnaire.GroupComponent;
import org.hl7.fhir.instance.model.QuestionnaireResponse;
import org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;

public class QuestionnaireResponseValidatorIntegrationTest {
	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorIntegrationTest.class);
	private IResourceLoader myResourceLoaderMock;
	private FhirValidator myVal;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Before
	public void before() {
		myResourceLoaderMock = mock(IResourceLoader.class);

		FhirQuestionnaireResponseValidator qaVal = new FhirQuestionnaireResponseValidator();
		qaVal.setResourceLoader(myResourceLoaderMock);

		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);
		myVal.registerValidatorModule(qaVal);
	}

	@Test
	public void testAnswerWithWrongType() {
		Questionnaire q = new Questionnaire();
		q.getGroup().addQuestion().setLinkId("link0").setRequired(true).setType(AnswerFormat.BOOLEAN);

		when(myResourceLoaderMock.load(Mockito.eq(Questionnaire.class), Mockito.eq(new IdType("http://example.com/Questionnaire/q1")))).thenReturn(q);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		ValidationResult result = myVal.validateWithResult(qa);
		ourLog.info(result.getMessages().toString());
		assertThat(result.getMessages().toString(), containsString("Answer to question with linkId[link0] found of type [StringType] but this is invalid for question of type [boolean]"));
	}

	@Test
	public void testRequiredOnlyTestedForFinishedQuestionnaires() {
		Questionnaire q = new Questionnaire();
		q.getGroup().addQuestion().setLinkId("link0").setRequired(true).setType(AnswerFormat.BOOLEAN);
		GroupComponent qg = q.getGroup().addGroup().setLinkId("link1").setRequired(true);
		qg.addQuestion().setLinkId("link2").setRequired(false).setType(AnswerFormat.BOOLEAN);

		when(myResourceLoaderMock.load(Mockito.eq(Questionnaire.class), Mockito.eq(new IdType("http://example.com/Questionnaire/q1")))).thenReturn(q);

		// Wrong type
		{
			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new IntegerType(123));

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages().toString(), containsString("Answer to question with linkId[link0] found of type [IntegerType] but this is invalid for question of type [boolean]"));
		}

		// Not populated, no status
		{
			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages().toString(), containsString("myMessage=Missing required group with linkId[link1],mySeverity=information"));
		}

		// Not populated, partial status
		{
			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages().toString(), containsString("myMessage=Missing required group with linkId[link1],mySeverity=information"));
		}

		// Not populated, finished status
		{
			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.setStatus(QuestionnaireResponseStatus.COMPLETED);

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages().toString(), containsString("Missing answer to required question with linkId[link0],mySeverity=error"));
		}
	}

	 @Test
	  public void testGroupWithNoAnswer() throws Exception {
	    Questionnaire q = ourCtx.newJsonParser().parseResource(Questionnaire.class, IOUtils.toString(getClass().getResourceAsStream("/questionnaire-20160712.json")));
	    QuestionnaireResponse qa = ourCtx.newJsonParser().parseResource(QuestionnaireResponse.class, IOUtils.toString(getClass().getResourceAsStream("/questionnaire-20160712-response.json")));

	    when(myResourceLoaderMock.load(Mockito.eq(Questionnaire.class), Mockito.any(IdType.class))).thenReturn(q);
	    ValidationResult errors = myVal.validateWithResult(qa);

	    ourLog.info(errors.toString());
	    assertEquals(true, errors.isSuccessful());
	  }

	
	@Test
	public void testCodedAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.CHOICE).setOptions(new Reference("http://somevalueset/ValueSet/123"));
		when(myResourceLoaderMock.load(Mockito.eq(Questionnaire.class), Mockito.eq(new IdType("http://example.com/Questionnaire/q1")))).thenReturn(q);

		ValueSet options = new ValueSet();
		options.getCodeSystem().setSystem("urn:system").addConcept().setCode("code0");
		when(myResourceLoaderMock.load(Mockito.eq(ValueSet.class), Mockito.eq(new IdType("http://somevalueset/ValueSet/123")))).thenReturn(options);

		QuestionnaireResponse qa;

		// Good code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		ValidationResult result = myVal.validateWithResult(qa);
		assertEquals(result.getMessages().toString(), 0, result.getMessages().size());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		result = myVal.validateWithResult(qa);
		ourLog.info(result.getMessages().toString());
		assertThat(result.getMessages().toString(), containsString("myLocationString=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
		assertThat(result.getMessages().toString(),
				containsString("myMessage=Question with linkId[link0] has answer with system[urn:system] and code[code1] but this is not a valid answer for ValueSet[http://somevalueset/ValueSet/123]"));

		result.toOperationOutcome();
	}

	@Test
	public void testInvalidReference() {
		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("someReference"); // not relative
		ValidationResult result = myVal.validateWithResult(qa);
		assertEquals(result.getMessages().toString(), 1, result.getMessages().size());
		assertThat(result.getMessages().toString(), containsString("Invalid reference 'someReference"));
	}

	@Test
	public void testUnknownValueSet() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.CHOICE).setOptions(new Reference("http://somevalueset/ValueSet/123"));
		when(myResourceLoaderMock.load(Mockito.eq(Questionnaire.class), Mockito.eq(new IdType("http://example.com/Questionnaire/q1")))).thenReturn(q);

		when(myResourceLoaderMock.load(Mockito.eq(ValueSet.class), Mockito.eq(new IdType("http://somevalueset/ValueSet/123")))).thenThrow(new ResourceNotFoundException("Unknown"));

		QuestionnaireResponse qa;

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		ValidationResult result = myVal.validateWithResult(qa);
		assertThat(result.getMessages().toString(), containsString("myMessage=Reference could not be found: http://some"));
	}

	 @Test
	  public void testContainedQuestionnaireAndValueSet() {
	    ValueSet vs = new ValueSet();
	    vs.getCodeSystem().setSystem("urn:system").addConcept().setCode("code1");
	    vs.setId("#VVV");
	    
	    Questionnaire q = new Questionnaire();
	    q.setId("#QQQ");
	    Reference vsRef = new Reference("#VVV");
	    vsRef.setResource(vs);
      q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.CHOICE).setOptions(vsRef);

	    QuestionnaireResponse qa;

	    // Bad code

	    qa = new QuestionnaireResponse();
      qa.getQuestionnaire().setReference("#QQQ");
      qa.getQuestionnaire().setResource(q);
	    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
	    ValidationResult result = myVal.validateWithResult(qa);
	    assertEquals(result.getMessages().toString(), 0, result.getMessages().size());
	  }

	/**
	 * Sample provided by Eric van der Zwan
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSampleQuestionnaire() {
		when(myResourceLoaderMock.load(Mockito.any(Class.class), Mockito.any(IdType.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
				IdType id = (IdType) theInvocation.getArguments()[1];
				String name = "/nice/" + id.getIdPart() + ".xml";
				InputStream in = getClass().getResourceAsStream(name);
				if (in == null) {
					throw new IllegalArgumentException(name);
				}
				InputStreamReader reader = new InputStreamReader(in);
				String body = IOUtils.toString(reader);

				if (Questionnaire.class.equals(theInvocation.getArguments()[0])) {
					return ourCtx.newXmlParser().parseResource(Questionnaire.class, body);
				} else if (ValueSet.class.equals(theInvocation.getArguments()[0])) {
					return ourCtx.newXmlParser().parseResource(ValueSet.class, body);
				} else {
					throw new IllegalArgumentException(id.getValue());
				}
			}
		});

		QuestionnaireResponse qa = ourCtx.newXmlParser().parseResource(QuestionnaireResponse.class, new InputStreamReader(getClass().getResourceAsStream("/nice/answer-1-admission.xml")));

		ValidationResult result = myVal.validateWithResult(qa);
		ourLog.info(result.getMessages().toString());
		assertThat(result.getMessages().toString(), containsString("Answer to question with linkId[partialBSN] found of type [IntegerType] but this is invalid for question of type [string]"));
	}

}
