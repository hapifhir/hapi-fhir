package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hl7.fhir.dstu3.hapi.validation.FhirQuestionnaireResponseValidator;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

public class QuestionnaireResponseValidatorIntegrationDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorIntegrationDstu3Test.class);

	private IValidationSupport myResourceLoaderMock;
	private FhirValidator myVal;

	@Before
	public void before() {
		myResourceLoaderMock = mock(IValidationSupport.class);

		FhirQuestionnaireResponseValidator qaVal = new FhirQuestionnaireResponseValidator();
		qaVal.setValidationSupport(myResourceLoaderMock);

		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);
		myVal.registerValidatorModule(qaVal);
	}

	@Test
	public void testAnswerWithWrongType() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		when(myResourceLoaderMock.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(("http://example.com/Questionnaire/q1")))).thenReturn(q);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		ValidationResult result = myVal.validateWithResult(qa);
		ourLog.info(result.getMessages().toString());
		assertThat(result.getMessages().toString(), containsString("Answer to question with linkId[link0] found of type [StringType] but this is invalid for question of type [boolean]"));
	}

	@Test
	public void testRequiredOnlyTestedForFinishedQuestionnaires() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);
		QuestionnaireItemComponent qg = q.addItem().setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.GROUP);
		qg.addItem().setLinkId("link2").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		when(myResourceLoaderMock.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(("http://example.com/Questionnaire/q1")))).thenReturn(q);

		// Wrong type
		{
			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.addItem().setLinkId("link0").addAnswer().setValue(new IntegerType(123));

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
			assertThat(result.getMessages().toString(), containsString("Missing required group with linkId[link1],mySeverity=error"));
		}
	}

	@Test
	public void testCodedAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(new Reference("http://somevalueset/ValueSet/123"));
		when(myResourceLoaderMock.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(("http://example.com/Questionnaire/q1")))).thenReturn(q);

		ValueSet options = new ValueSet();
		options.getCodeSystem().setSystem("urn:system").addConcept().setCode("code0");
		when(myResourceLoaderMock.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq(("http://somevalueset/ValueSet/123")))).thenReturn(options);

		QuestionnaireResponse qa;

		// Good code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		ValidationResult result = myVal.validateWithResult(qa);
		assertEquals(result.getMessages().toString(), 0, result.getMessages().size());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		result = myVal.validateWithResult(qa);
		ourLog.info(result.getMessages().toString());
		assertThat(result.getMessages().toString(), containsString("myLocationString=//QuestionnaireResponse/item(0)/answer(0)"));
		assertThat(result.getMessages().toString(), containsString("myMessage=Question with linkId[link0] has answer with system[urn:system] and code[code1] but this is not a valid answer for ValueSet[http://somevalueset/ValueSet/123]"));

		result.toOperationOutcome();
	}

	@Test
	public void testInvalidReference() {
		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("someReference"); // not relative
		ValidationResult result = myVal.validateWithResult(qa);
		assertEquals(result.getMessages().toString(), 1, result.getMessages().size());
		assertThat(result.getMessages().toString(), containsString("Questionnaire someReference is not found"));
	}

	@Test
	public void testUnknownValueSet() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(new Reference("http://somevalueset/ValueSet/123"));
		when(myResourceLoaderMock.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(("http://example.com/Questionnaire/q1")))).thenReturn(q);

		when(myResourceLoaderMock.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq(("http://somevalueset/ValueSet/123")))).thenReturn(null);

		QuestionnaireResponse qa;

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		ValidationResult result = myVal.validateWithResult(qa);
		assertThat(result.getMessages().toString(), containsString("but this ValueSet can not be found"));
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
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(vsRef);

		QuestionnaireResponse qa;

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("#QQQ");
		qa.getQuestionnaire().setResource(q);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		ValidationResult result = myVal.validateWithResult(qa);
		assertEquals(result.getMessages().toString(), 0, result.getMessages().size());
	}

}
