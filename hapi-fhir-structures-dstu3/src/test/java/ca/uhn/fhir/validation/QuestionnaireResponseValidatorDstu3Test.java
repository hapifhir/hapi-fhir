package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.hapi.validation.HapiWorkerContext;
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
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.dstu3.utils.IWorkerContext;
import org.hl7.fhir.dstu3.validation.QuestionnaireResponseValidator;
import org.hl7.fhir.dstu3.validation.ValidationMessage;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

public class QuestionnaireResponseValidatorDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3();

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorDstu3Test.class);
	private QuestionnaireResponseValidator myVal;

	private IWorkerContext myWorkerCtx;

	private IValidationSupport myValSupport;

	@Before
	public void before() {
		myValSupport = mock(IValidationSupport.class);
		// new DefaultProfileValidationSupport();
		myWorkerCtx = new HapiWorkerContext(ourCtx, myValSupport);
		myVal = new QuestionnaireResponseValidator(myWorkerCtx);
	}

	@Test
	public void testAnswerWithWrongType() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);

		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer to question with linkId[link0] found of type [StringType] but this is invalid for question of type [boolean]"));
	}

	@Test
	public void testCodedAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(new Reference("http://somevalueset"));
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq("http://example.com/Questionnaire/q1"))).thenReturn(q);

		ValueSet options = new ValueSet();
		options.getCodeSystem().setSystem("urn:system").addConcept().setCode("code0");
		options.getCompose().addInclude().setSystem("urn:system2").addConcept().setCode("code2");
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

		QuestionnaireResponse qa;
		List<ValidationMessage> errors;

		// Good code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		assertEquals(errors.toString(), 0, errors.size());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));
		assertThat(errors.toString(), containsString("message=Question with linkId[link0] has answer with system[urn:system] and code[code1] but this is not a valid answer for ValueSet[http://somevalueset]"));

		qa = new QuestionnaireResponse();

		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system2").setCode("code3"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));
		assertThat(errors.toString(), containsString("message=Question with linkId[link0] has answer with system[urn:system2] and code[code3] but this is not a valid answer for ValueSet[http://somevalueset]"));

	}

	@Test
	public void testOpenchoiceAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.OPENCHOICE).setOptions(new Reference("http://somevalueset"));
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(questionnaireRef))).thenReturn(q);

		ValueSet options = new ValueSet();
		options.getCodeSystem().setSystem("urn:system").addConcept().setCode("code0");
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

		QuestionnaireResponse qa;
		List<ValidationMessage> errors;

		// Good code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		assertEquals(errors.toString(), 0, errors.size());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("message=Question with linkId[link0] has answer with system[urn:system] and code[code1] but this is not a valid answer for ValueSet[http://somevalueset]"));
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));

		// Partial code

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem(null).setCode("code1"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer to question with linkId[link0] has a coding, but this coding does not contain a code and system (both must be present, or neither as the question allows OPENCHOICE)"));
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("").setCode("code1"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer to question with linkId[link0] has a coding, but this coding does not contain a code and system (both must be present, or neither as the question allows OPENCHOICE)"));
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("system").setCode(null));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer to question with linkId[link0] has a coding, but this coding does not contain a code and system (both must be present, or neither as the question allows OPENCHOICE)"));
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("system").setCode(null));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer to question with linkId[link0] has a coding, but this coding does not contain a code and system (both must be present, or neither as the question allows OPENCHOICE)"));
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));

		// Wrong type

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new IntegerType(123));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("message=Answer to question with linkId[link0] found of type [IntegerType] but this is invalid for question of type [open-choice]"));
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));

		// String answer

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("Hello"));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors, empty());

		// Missing String answer

		qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType(""));
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer to question with linkId[link0] has no value but this item is required"));
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)/answer(0)"));

	}

	@Test
	public void testGroupWithNoLinkIdInQuestionnaireResponse() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qGroup = q.addItem().setType(QuestionnaireItemType.GROUP);
		qGroup.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		QuestionnaireResponseItemComponent qaGroup = qa.addItem();
		qaGroup.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Questionnaire definition contains group with no linkId"));
	}

	@Test
	public void testMissingRequiredQuestion() {

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Missing required question with linkId[link0]"));
	}

	@Test
	public void testItemWithNoType() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qGroup = q.addItem();
		qGroup.setLinkId("link0");
		qGroup.addItem().setLinkId("link1").setType(QuestionnaireItemType.STRING);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		QuestionnaireResponseItemComponent qaGroup = qa.addItem().setLinkId("link0");
		qaGroup.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("definition contains item with no type"));
		assertEquals(1, errors.size());
	}

	@Test
	public void testUnexpectedAnswer() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)"));
		assertThat(errors.toString(), containsString("message=Item with linkId[link1] found at this position, but this item does not exist at this position in Questionnaire"));
	}

	@Test
	public void testUnexpectedGroup() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addItem().setLinkId("link2");

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/item(0)"));
		assertThat(errors.toString(), containsString("Item with linkId[link1] found at this position, but this item does not exist at this position in Questionnaire"));
	}

	// @Test
	public void validateHealthConnexExample() throws Exception {
		String input = IOUtils.toString(QuestionnaireResponseValidatorDstu3Test.class.getResourceAsStream("/questionnaireanswers-0f431c50ddbe4fff8e0dd6b7323625fc.xml"));

		QuestionnaireResponse qa = ourCtx.newXmlParser().parseResource(QuestionnaireResponse.class, input);
		ArrayList<ValidationMessage> errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		assertEquals(errors.toString(), 0, errors.size());

		/*
		 * Now change a coded value
		 */
		//@formatter:off
		input = input.replaceAll(
			"<answer>\n" +
			"					<valueCoding>\n" + 
			"						<system value=\"f69573b8-cb63-4d31-85a4-23ac784735ab\"/>\n" + 
			"						<code value=\"2\"/>\n" +
			"						<display value=\"Once/twice\"/>\n" + 
			"					</valueCoding>\n" + 
			"				</answer>",
			"<answer>\n" + 
			"					<valueCoding>\n" + 
			"						<system value=\"f69573b8-cb63-4d31-85a4-23ac784735ab\"/>\n" + 
			"						<code value=\"GGG\"/>\n" +
			"						<display value=\"Once/twice\"/>\n" + 
			"					</valueCoding>\n" + 
			"				</answer>");
		assertThat(input, containsString("GGG"));
		//@formatter:on

		qa = ourCtx.newXmlParser().parseResource(QuestionnaireResponse.class, input);
		errors = new ArrayList<ValidationMessage>();
		myVal.validate(errors, qa);
		assertEquals(errors.toString(), 10, errors.size());
	}

}
