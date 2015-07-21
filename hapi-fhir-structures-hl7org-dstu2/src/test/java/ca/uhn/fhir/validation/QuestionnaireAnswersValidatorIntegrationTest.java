package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.IdType;
import org.hl7.fhir.instance.model.IntegerType;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.Questionnaire.GroupComponent;
import org.hl7.fhir.instance.model.QuestionnaireAnswers;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.Questionnaire.AnswerFormat;
import org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionnaireAnswersStatus;
import org.hl7.fhir.instance.utils.WorkerContext;
import org.hl7.fhir.instance.validation.QuestionnaireAnswersValidator;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class QuestionnaireAnswersValidatorIntegrationTest {
	private static final FhirContext ourCtx = FhirContext.forDstu2Hl7Org();

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireAnswersValidatorIntegrationTest.class);

	private IResourceLoader myResourceLoaderMock;

	private FhirValidator myVal;

	@Before
	public void before() {
		myResourceLoaderMock = mock(IResourceLoader.class);

		FhirQuestionnaireAnswersValidator qaVal = new FhirQuestionnaireAnswersValidator();
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

		QuestionnaireAnswers qa = new QuestionnaireAnswers();
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
			QuestionnaireAnswers qa = new QuestionnaireAnswers();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new IntegerType(123));

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages().toString(), containsString("Answer to question with linkId[link0] found of type [IntegerType] but this is invalid for question of type [boolean]"));
		}

		// Not populated, no status
		{
			QuestionnaireAnswers qa = new QuestionnaireAnswers();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages(), empty());
		}

		// Not populated, partial status
		{
			QuestionnaireAnswers qa = new QuestionnaireAnswers();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.setStatus(QuestionnaireAnswersStatus.INPROGRESS);

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages(), empty());
		}

		// Not populated, finished status
		{
			QuestionnaireAnswers qa = new QuestionnaireAnswers();
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.setStatus(QuestionnaireAnswersStatus.COMPLETED);

			ValidationResult result = myVal.validateWithResult(qa);
			ourLog.info(result.getMessages().toString());
			assertThat(result.getMessages().toString(), containsString("Missing answer to required question with linkId[link0]"));
		}
}

	@Test
	public void testCodedAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.CHOICE).setOptions(new Reference("http://somevalueset/ValueSet/123"));
		when(myResourceLoaderMock.load(Mockito.eq(Questionnaire.class), Mockito.eq(new IdType("http://example.com/Questionnaire/q1")))).thenReturn(q);

		ValueSet options = new ValueSet();
		options.getDefine().setSystem("urn:system").addConcept().setCode("code0");
		when(myResourceLoaderMock.load(Mockito.eq(ValueSet.class), Mockito.eq(new IdType("http://somevalueset/ValueSet/123")))).thenReturn(options);

		QuestionnaireAnswers qa;

		// Good code

		qa = new QuestionnaireAnswers();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		ValidationResult result = myVal.validateWithResult(qa);
		assertEquals(result.getMessages().toString(), 0, result.getMessages().size());

		// Bad code

		qa = new QuestionnaireAnswers();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		result = myVal.validateWithResult(qa);
		ourLog.info(result.getMessages().toString());
		assertThat(result.getMessages().toString(), containsString("myLocationString=QuestionnaireAnswers.group(0).question(0).answer(0)"));
		assertThat(result.getMessages().toString(),
				containsString("myMessage=Question with linkId[link0] has answer with system[urn:system] and code[code1] but this is not a valid answer for ValueSet[http://somevalueset/ValueSet/123]"));

		result.toOperationOutcome();
	}

	@Test
	public void testUnknownValueSet() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.CHOICE).setOptions(new Reference("http://somevalueset/ValueSet/123"));
		when(myResourceLoaderMock.load(Mockito.eq(Questionnaire.class), Mockito.eq(new IdType("http://example.com/Questionnaire/q1")))).thenReturn(q);

		when(myResourceLoaderMock.load(Mockito.eq(ValueSet.class), Mockito.eq(new IdType("http://somevalueset/ValueSet/123")))).thenThrow(new ResourceNotFoundException("Unknown"));

		QuestionnaireAnswers qa;

		// Bad code

		qa = new QuestionnaireAnswers();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		ValidationResult result = myVal.validateWithResult(qa);
		assertThat(result.getMessages().toString(), containsString("myMessage=Reference could not be found: http://some"));
	}

}
