package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hamcrest.Matchers;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.MarkdownType;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemEnableWhenComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemOptionComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.TimeType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.BOOLEAN;
import static org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType.CHOICE;
import static org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class QuestionnaireResponseValidatorDstu3Test {
	private static final String QUESTIONNAIRE_URL = "http://example.com/Questionnaire/q1";
	private static final IdType ID_ICC_QUESTIONNAIRE_SETUP = new IdType("Questionnaire/profile");
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorDstu3Test.class);
	private static final String CODE_ICC_SCHOOLTYPE_PT = "PT";
	private static final IdType ID_VS_SCHOOLTYPE = new IdType("ValueSet/schooltype");
	private static final String SYSTEMURI_ICC_SCHOOLTYPE = "http://ehealthinnovation/icc/ns/schooltype";
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;
	private IValidationSupport myValSupport;

	@BeforeEach
	public void before() {
		myValSupport = mock(IValidationSupport.class);
		when(myValSupport.getFhirContext()).thenReturn(ourCtx);

		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		ValidationSupportChain validationSupport = new ValidationSupportChain(myDefaultValidationSupport, myValSupport, new InMemoryTerminologyServerValidationSupport(ourCtx), new CommonCodeSystemsTerminologyService(ourCtx));
		myInstanceVal = new FhirInstanceValidator(validationSupport);

		myVal.registerValidatorModule(myInstanceVal);

	}

	private ValidationResult stripBindingHasNoSourceMessage(ValidationResult theErrors) {
		List<SingleValidationMessage> messages = new ArrayList<>(theErrors.getMessages());
		for (int i = 0; i < messages.size(); i++) {
			if (messages.get(i).getMessage().contains("has no source, so can't")) {
				messages.remove(i);
				i--;
			}
		}

		return new ValidationResult(ourCtx, messages);
	}

	@Test
	public void testAnswerWithCorrectType() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl("http://codesystems.com/system");
		codeSystem.addConcept().setCode("code0");

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");

		int itemCnt = 16;
		QuestionnaireItemType[] questionnaireItemTypes = new QuestionnaireItemType[itemCnt];
		questionnaireItemTypes[0] = QuestionnaireItemType.BOOLEAN;
		questionnaireItemTypes[1] = QuestionnaireItemType.DECIMAL;
		questionnaireItemTypes[2] = QuestionnaireItemType.INTEGER;
		questionnaireItemTypes[3] = QuestionnaireItemType.DATE;
		questionnaireItemTypes[4] = QuestionnaireItemType.DATETIME;
		questionnaireItemTypes[5] = QuestionnaireItemType.TIME;
		questionnaireItemTypes[6] = QuestionnaireItemType.STRING;
		questionnaireItemTypes[7] = QuestionnaireItemType.TEXT;
		questionnaireItemTypes[8] = QuestionnaireItemType.TEXT;
		questionnaireItemTypes[9] = QuestionnaireItemType.URL;
		questionnaireItemTypes[10] = QuestionnaireItemType.CHOICE;
		questionnaireItemTypes[11] = QuestionnaireItemType.OPENCHOICE;
		questionnaireItemTypes[12] = QuestionnaireItemType.OPENCHOICE;
		questionnaireItemTypes[13] = QuestionnaireItemType.ATTACHMENT;
		questionnaireItemTypes[14] = QuestionnaireItemType.REFERENCE;
		questionnaireItemTypes[15] = QuestionnaireItemType.QUANTITY;

		Type[] answerValues = new Type[itemCnt];
		answerValues[0] = new BooleanType(true);
		answerValues[1] = new DecimalType(42.0);
		answerValues[2] = new IntegerType(42);
		answerValues[3] = new DateType(new Date());
		answerValues[4] = new DateTimeType(new Date());
		answerValues[5] = new TimeType("04:47:12");
		answerValues[6] = new StringType("some text");
		answerValues[7] = new StringType("some text");
		answerValues[8] = new MarkdownType("some text");
		answerValues[9] = new UriType("http://example.com");
		answerValues[10] = new Coding().setSystem("http://codesystems.com/system").setCode("code0");
		answerValues[11] = new Coding().setSystem("http://codesystems.com/system").setCode("code0");
		answerValues[12] = new StringType("some value");
		answerValues[13] = new Attachment().setData("some data".getBytes()).setContentType("txt");
		answerValues[14] = new Reference(QUESTIONNAIRE_URL);
		answerValues[15] = new Quantity(42);

		for (int i = 0; i < itemCnt; i++) {
			before();

			if (questionnaireItemTypes[i] == null) continue;
			String linkId = "link" + i;

			reset(myValSupport);
			Questionnaire q = new Questionnaire();
			when(myValSupport.fetchResource(eq(Questionnaire.class),
				eq(QUESTIONNAIRE_URL))).thenReturn(q);
			when(myValSupport.fetchCodeSystem(eq("http://codesystems.com/system"))).thenReturn(codeSystem);
			when(myValSupport.fetchResource(eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);
			when(myValSupport.validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), nullable(ValueSet.class)))
				.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));

			q.getItem().clear();
			QuestionnaireItemComponent questionnaireItemComponent =
				q.addItem().setLinkId(linkId).setRequired(true).setType(questionnaireItemTypes[i]);
			if (i == 10 || i == 11) {
				questionnaireItemComponent.setOptions(new Reference("http://somevalueset"));
			} else if (i == 12) {
				questionnaireItemComponent.setOption(
					Collections.singletonList(new QuestionnaireItemOptionComponent(new StringType("some value"))));
			}

			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
			qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
			qa.addItem().setLinkId(linkId).addAnswer().setValue(answerValues[i]);

			ValidationResult errors = myVal.validateWithResult(qa);

			assertThat("index[" + i + "]: " + errors.toString(), errors.getMessages(), empty());
		}
	}

	@Test
	public void testAnswerWithWrongType() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer value must be of type boolean"));
	}

	@Test
	public void testCodedAnswer() {
		String questionnaireRef = QUESTIONNAIRE_URL;

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(new Reference("http://somevalueset"));
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(QUESTIONNAIRE_URL))).thenReturn(q);

		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system"))).thenReturn(true);
		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system2"))).thenReturn(true);
		when(myValSupport.validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), nullable(ValueSet.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));
		when(myValSupport.validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code1"), any(), nullable(ValueSet.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setSeverityCode(ValidationMessage.IssueSeverity.ERROR.toCode()).setMessage("Unknown code"));

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl("http://codesystems.com/system");
		codeSystem.addConcept().setCode("code0");
		when(myValSupport.fetchCodeSystem(eq("http://codesystems.com/system"))).thenReturn(codeSystem);

		CodeSystem codeSystem2 = new CodeSystem();
		codeSystem2.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem2.setUrl("http://codesystems.com/system2");
		codeSystem2.addConcept().setCode("code2");
		when(myValSupport.fetchCodeSystem(eq("http://codesystems.com/system2"))).thenReturn(codeSystem2);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		options.getCompose().addInclude().setSystem("http://codesystems.com/system2").addConcept().setCode("code2");
		when(myValSupport.fetchResource(eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

		when(myValSupport.validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode(CODE_ICC_SCHOOLTYPE_PT));
		when(myValSupport.validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code1"), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setSeverityCode("warning").setMessage("Unknown code: http://codesystems.com/system / code1"));


		QuestionnaireResponse qa;
		ValidationResult errors;

		// Good code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code0"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		assertEquals(0, errors.getMessages().size(), errors.toString());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Unknown code: http://codesystems.com/system / code1 for 'http://codesystems.com/system#code1'"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		// Unhandled system

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system2").setCode("code3"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Unknown code 'http://codesystems.com/system2#code3'"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

	}

	@Test
	public void testGroupWithNoLinkIdInQuestionnaireResponse() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qGroup = q.addItem().setType(QuestionnaireItemType.GROUP);
		qGroup.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		QuestionnaireResponseItemComponent qaGroup = qa.addItem();
		qaGroup.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.linkId: minimum required = 1, but only found 0"));
	}

	@Test
	public void testMissingAnswerInNestedStructureIsReported() {
		Questionnaire q = new Questionnaire();
		q.addItem().setType(QuestionnaireItemType.GROUP).setRequired(true)
			.addItem().setType(QuestionnaireItemType.GROUP).setRequired(true)
			.addItem().setType(QuestionnaireItemType.BOOLEAN).setLinkId("link0").setRequired(true);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qa);

		assertThat(errors.toString(), Matchers.not(containsString("No issues")));
	}

	@Test
	public void testGroupMarkedAsRequiredIsOk() {
		Questionnaire q = new Questionnaire();
		q.addItem().setType(QuestionnaireItemType.GROUP).setRequired(true).setLinkId("group")
			.addItem().setType(QuestionnaireItemType.BOOLEAN).setLinkId("child").setRequired(true);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("group")
			.addItem().setLinkId("child").addAnswer().setValue(new BooleanType(true));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qa);

		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testItemWithNoType() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qItem = q.addItem();
		qItem.setLinkId("link0");

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		QuestionnaireResponseItemComponent qaItem = qa.addItem().setLinkId("link0");
		qaItem.addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Definition for item link0 does not contain a type"));
		assertEquals(1, errors.getMessages().size());
	}

	@Test
	public void testMissingRequiredQuestion() {

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response answer found for required item 'link0'"));
	}

	@Test
	public void testEnableWhenWithHasAnswerTrueDisablesQuestionWhenNoAnswerIsPresent() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING).addEnableWhen().setQuestion("link0").setHasAnswer(true);


		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testRequiredQuestionQuantityWithEnableWhenHidesQuestionHasAnswerTrue() {

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.QUANTITY);

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING).addEnableWhen().setQuestion("link0").setHasAnswer(true);
		q.addItem(item1);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);

		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testRequiredQuestionWithEnableWhenHidesQuestion() {

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.STRING);

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1").setType(QuestionnaireItemType.STRING).setRequired(true);
		q.addItem(item1);
		QuestionnaireItemEnableWhenComponent enable = new QuestionnaireItemEnableWhenComponent();
		item1.addEnableWhen(enable);
		enable.setQuestion("link0");
		enable.setHasAnswer(true);
		enable.setAnswer(new Quantity().setValue(1L));

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testRequiredQuestionQuantityWithEnableWhenHidesQuestionValue() {

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.QUANTITY);

		//link1 question is enabled when link0 has answer
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING);
		q.addItem(item1);
		QuestionnaireItemEnableWhenComponent enable = new QuestionnaireItemEnableWhenComponent();
		item1.addEnableWhen(enable);
		enable.setQuestion("link0");
		enable.setAnswer(new Quantity().setValue(2L));

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Quantity().setValue(1L));

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testRequiredQuestionQuantityWithEnableWhenEnablesQuestionValue() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.QUANTITY);

		//link1 question is enabled when link0 has answer
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1").setType(QuestionnaireItemType.STRING).setRequired(true);
		q.addItem(item1);
		QuestionnaireItemEnableWhenComponent enable = new QuestionnaireItemEnableWhenComponent();
		item1.addEnableWhen(enable);
		enable.setQuestion("link0");
		enable.setAnswer(new Quantity().setValue(1L));

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Quantity().setValue(1L));

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString(" No response answer found for required item 'link1'"));
	}

	@Test
	public void testRequiredQuestionCodingWithEnableWhenEnablesQuestionValue() {
		Questionnaire q = new Questionnaire();
		q.addItem()
			.setLinkId("link0")
			.setRequired(false)
			.setType(CHOICE)
			.addOption(new Questionnaire.QuestionnaireItemOptionComponent().setValue(new Coding("http://foo", "YES", null)))
			.addOption(new Questionnaire.QuestionnaireItemOptionComponent().setValue(new Coding("http://foo", "NO", null)));

		q.addItem()
			.setLinkId("link1")
			.setType(QuestionnaireItemType.STRING)
			.setRequired(true)
			.addEnableWhen()
			.setQuestion("link0")
			.setAnswer(new Coding("http://foo", "YES", null));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(QUESTIONNAIRE_URL))).thenReturn(q);
		QuestionnaireResponse qa;
		ValidationResult errors;

		// link0 has an answer, but it's the wrong one - No answer to link1 provided (good)
		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding("http://foo", "NO", null));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));

		// link0 has an answer, but it's the wrong one - An answer to link1 provided (bad)
		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding("http://foo", "NO", null));
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("HELLO"));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Item has answer, even though it is not enabled (item id = 'link1')"));

		// link0 has an answer, and it's the right one
		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding("http://foo", "YES", null));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response answer found for required item 'link1'"));
	}

	@Test
	public void testRequiredQuestionWithEnableWhenHasAnswerTrueWithAnswer() {

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.STRING);

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING);
		q.addItem(item1);
		QuestionnaireItemEnableWhenComponent enable = new QuestionnaireItemEnableWhenComponent();
		item1.addEnableWhen(enable);
		enable.setQuestion("link0");
		enable.setHasAnswer(true);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("BAR"));

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testRequiredQuestionWithEnableWheHidesRequiredQuestionnHasAnswerFalse() {

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.STRING);

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING);
		q.addItem(item1);
		QuestionnaireItemEnableWhenComponent enable = new QuestionnaireItemEnableWhenComponent();
		item1.addEnableWhen(enable);
		enable.setQuestion("link0");
		enable.setHasAnswer(false);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);

		// link1 should be disabled, because the enableWhen enables it when link0 doesn't haven an answer
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testGivenQuestionIsNotEnabledWithEnableWhenAnswersAreReportedAsErrors() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link2").setRequired(false).setType(QuestionnaireItemType.STRING).addEnableWhen().setQuestion("link0").setHasAnswer(true);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr.getQuestionnaire().setReference(QUESTIONNAIRE_URL);

		qr.addItem().setLinkId("link2").addAnswer().setValue(new StringType("FOO"));

		String reference = qr.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qr);

		assertThat(errors.toString(), Matchers.not(containsString("No issues")));
	}

	@Test
	public void testGivenQuestionIsNotEnabledWithEnableWhenButHasItemsWithoutAnswersAreOk() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link2").setRequired(false).setType(QuestionnaireItemType.STRING).addEnableWhen().setQuestion("link0").setHasAnswer(true);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr.getQuestionnaire().setReference(QUESTIONNAIRE_URL);

		qr.addItem().setLinkId("link2");

		String reference = qr.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qr);

		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testGivenQuestionIsNotEnabledWithEnableWhenButHasItemsWithoutAnswersAreOk2() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.STRING);
		q.addItem().setLinkId("link1").setRequired(false).setType(QuestionnaireItemType.STRING);

		q.addItem()
			.setLinkId("link2")
			.setRequired(true)
			.setType(QuestionnaireItemType.STRING)
			.addEnableWhen().setQuestion("link1").setHasAnswer(true);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qr.addItem().setLinkId("link0");

		qr.addItem().setLinkId("link1").addAnswer().setValue(new StringType("Answer"));

		qr.addItem().setLinkId("link2");

		String reference = qr.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);

		// Without an answer
		ValidationResult errors = myVal.validateWithResult(qr);
		assertThat(errors.toString(), containsString("No response answer found for required item 'link2'"));

		// With an answer
		qr.getItem().get(2).addAnswer().setValue(new StringType("AAA"));
		errors = myVal.validateWithResult(qr);
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testGivenQuestionnaireResponseHasSiblingItemsWhenTheyShouldBeChildItems() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent item = q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.GROUP);
		item.addItem().setLinkId("link1").setRequired(true).setType(QuestionnaireItemType.STRING);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qr.addItem().setLinkId("link0").setText("Text");
		qr.addItem().setLinkId("link1").addAnswer().setValue(new StringType("Answer"));
		String reference = qr.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qr);
		assertThat(errors.toString(), Matchers.not(containsString("No issues")));
		assertTrue(
			errors.getMessages().stream().filter(vm -> vm.getMessage().contains("Structural Error"))
				.anyMatch(vm -> vm.getMessage().contains("link1")), "Must contain structural error about misplaced link1 item");
	}

	@Test
	public void testAnswerIsValueCodingWithExtensionInside() {
		Questionnaire q = new Questionnaire();
		Coding qcoding = new Coding();
		qcoding.setSystem("http://foo");
		qcoding.setCode("1293");
		q.addItem().setLinkId("1B").setRequired(true).setType(CHOICE).addOption().setValue(qcoding);
		q.addItem().setLinkId("2B").setType(BOOLEAN).addEnableWhen().setQuestion("1B").setAnswer(qcoding);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(COMPLETED);
		qr.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		QuestionnaireResponseItemComponent qrItem = qr.addItem().setLinkId("1B");
		Coding coding = new Coding();
		coding.setSystem("http://foo");
		coding.setCode("1293");
		QuestionnaireResponseItemAnswerComponent answer = qrItem.addAnswer();
		answer.setValue(coding);
		coding.addExtension("http://hl7.org/fhir/StructureDefinition/iso21090-CO-value", new DecimalType("1.0"));
		qr.addItem().setLinkId("2B").addAnswer().setValue(new BooleanType(true));

		String reference = qr.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qr);
		assertThat(errors.toString(), containsString("No issues"));

	}

	@Test
	public void testChoiceItemsEnableWhenHasNoSystemYetAnswerHasSystem() {
		Questionnaire q = new Questionnaire();
		Coding qcoding = new Coding();
		qcoding.setCode("male");
		qcoding.setSystem("http://hl7.org/fhir/administrative-gender");
		q.addItem().setLinkId("1B").setRequired(true).setType(CHOICE).addOption().setValue(qcoding);
		Coding enablewhenCoding = new Coding();
		enablewhenCoding.setCode("male");
		q.addItem().setLinkId("2B").setType(BOOLEAN).addEnableWhen().setQuestion("1B").setAnswer(enablewhenCoding);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(COMPLETED);
		qr.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		QuestionnaireResponseItemComponent qrItem = qr.addItem().setLinkId("1B");
		Coding coding = new Coding();
		coding.setCode("male");
		coding.setSystem("http://hl7.org/fhir/administrative-gender");
		QuestionnaireResponseItemAnswerComponent answer = qrItem.addAnswer();
		answer.setValue(coding);
		qr.addItem().setLinkId("2B").addAnswer().setValue(new BooleanType(true));

		String reference = qr.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qr);
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testEmbeddedItemInChoice() {
		String questionnaireRef = QUESTIONNAIRE_URL;
		String valueSetRef = "http://somevalueset";
		String codeSystemUrl = "http://codesystems.com/system";
		String codeValue = "code0";

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1")
			.setType(QuestionnaireItemType.CHOICE)
			.setOptions(new Reference(valueSetRef));

		item1.addItem().setLinkId("link11")
			.setType(QuestionnaireItemType.TEXT);

		Questionnaire q = new Questionnaire();
		q.addItem(item1);
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(questionnaireRef)))
			.thenReturn(q);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl(codeSystemUrl);
		codeSystem.addConcept().setCode(codeValue);
		when(myValSupport.fetchCodeSystem(eq(codeSystemUrl))).thenReturn(codeSystem);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem(codeSystemUrl).addConcept().setCode(codeValue);
		when(myValSupport.fetchResource(eq(ValueSet.class), eq(valueSetRef)))
			.thenReturn(options);
		when(myValSupport.validateCode(any(), any(), eq(codeSystemUrl), eq(codeValue), any(String.class), anyString()))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode(codeValue));

		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);
		String qXml = xmlParser.encodeResourceToString(q);
		ourLog.info(qXml);

		// create the response
		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link1")
			.addAnswer()
			.addItem().setLinkId("link11");

		String rXml = xmlParser.encodeResourceToString(qa);
		ourLog.info(rXml);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());
	}

	@Test
	public void testEmbeddedItemInOpenChoice() {
		String questionnaireRef = QUESTIONNAIRE_URL;
		String valueSetRef = "http://somevalueset";
		String codeSystemUrl = "http://codesystems.com/system";
		String codeValue = "code0";

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1")
			.setType(QuestionnaireItemType.OPENCHOICE)
			.setOptions(new Reference(valueSetRef));

		item1.addItem().setLinkId("link11")
			.setType(QuestionnaireItemType.TEXT);

		Questionnaire q = new Questionnaire();
		q.addItem(item1);
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(questionnaireRef)))
			.thenReturn(q);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl(codeSystemUrl);
		codeSystem.addConcept().setCode(codeValue);
		when(myValSupport.fetchCodeSystem(eq(codeSystemUrl)))
			.thenReturn(codeSystem);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem(codeSystemUrl).addConcept().setCode(codeValue);
		when(myValSupport.fetchResource(eq(ValueSet.class), eq(valueSetRef)))
			.thenReturn(options);
		when(myValSupport.validateCode(any(), any(), eq(codeSystemUrl), eq(codeValue), any(String.class), anyString()))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode(codeValue));

		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);
		String qXml = xmlParser.encodeResourceToString(q);
		ourLog.info(qXml);

		// create the response
		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link1")
			.addAnswer()
			.addItem().setLinkId("link11");

		String rXml = xmlParser.encodeResourceToString(qa);
		ourLog.info(rXml);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());
	}

	@Test
	public void testEmbeddedItemInString() {
		String questionnaireRef = QUESTIONNAIRE_URL;

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1")
			.setType(QuestionnaireItemType.TEXT);

		item1.addItem().setLinkId("link11")
			.setType(QuestionnaireItemType.TEXT);

		Questionnaire q = new Questionnaire();
		q.addItem(item1);
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(questionnaireRef)))
			.thenReturn(q);

		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);
		String qXml = xmlParser.encodeResourceToString(q);
		ourLog.info(qXml);

		// create the response
		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link1")
			.addAnswer()
			.addItem().setLinkId("link11");

		String rXml = xmlParser.encodeResourceToString(qa);
		ourLog.info(rXml);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());
	}

	@Test
	public void testValidateQuestionnaireResponseWithValueSetChoiceAnswer() {
		/*
		 * Create CodeSystem
		 */
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl(SYSTEMURI_ICC_SCHOOLTYPE);
		codeSystem.addConcept().setCode(CODE_ICC_SCHOOLTYPE_PT);

		/*
		 * Create valueset
		 */
		ValueSet iccSchoolTypeVs = new ValueSet();
		iccSchoolTypeVs.setId(ID_VS_SCHOOLTYPE);
		iccSchoolTypeVs.getCompose().getIncludeFirstRep().setSystem(SYSTEMURI_ICC_SCHOOLTYPE);
		iccSchoolTypeVs
			.getCompose()
			.getIncludeFirstRep()
			.addConcept()
			.setCode(CODE_ICC_SCHOOLTYPE_PT)
			.setDisplay("Part Time");

		/*
		 * Create Questionnaire
		 */
		Questionnaire questionnaire = new Questionnaire();
		{
			questionnaire.setId(ID_ICC_QUESTIONNAIRE_SETUP);

			Questionnaire.QuestionnaireItemComponent basicGroup = questionnaire.addItem();
			basicGroup.setLinkId("basic");
			basicGroup.setType(Questionnaire.QuestionnaireItemType.GROUP);
			basicGroup
				.addItem()
				.setLinkId("schoolType")
				.setType(Questionnaire.QuestionnaireItemType.CHOICE)
				.setOptions(new Reference(ID_VS_SCHOOLTYPE))
				.setRequired(true);
		}

		/*
		 * Create response
		 */
		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		questionnaireResponse.setQuestionnaire(new Reference(ID_ICC_QUESTIONNAIRE_SETUP));
		questionnaireResponse.getSubject().setReference("Patient/123");

		QuestionnaireResponse.QuestionnaireResponseItemComponent basicGroup = questionnaireResponse
			.addItem();
		basicGroup.setLinkId("basic");
		basicGroup
			.addItem()
			.setLinkId("schoolType")
			.addAnswer()
			.setValue(new Coding(SYSTEMURI_ICC_SCHOOLTYPE, CODE_ICC_SCHOOLTYPE_PT, ""));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(questionnaireResponse.getQuestionnaire().getReference()))).thenReturn(questionnaire);
		when(myValSupport.fetchResource(eq(ValueSet.class), eq(ID_VS_SCHOOLTYPE.getValue()))).thenReturn(iccSchoolTypeVs);
		when(myValSupport.validateCodeInValueSet(any(), any(), eq(SYSTEMURI_ICC_SCHOOLTYPE), eq(CODE_ICC_SCHOOLTYPE_PT), any(), nullable(ValueSet.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode(CODE_ICC_SCHOOLTYPE_PT));
		when(myValSupport.fetchCodeSystem(eq(SYSTEMURI_ICC_SCHOOLTYPE))).thenReturn(codeSystem);

		ValidationResult errors = myVal.validateWithResult(questionnaireResponse);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}

	@Test
	public void testMissingRequiredAnswer() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0")
			.setType(QuestionnaireItemType.STRING)
			.setRequired(true);

		String reference = QUESTIONNAIRE_URL;
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference)))
			.thenReturn(q);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getQuestionnaire().setReference(reference);
		qa.addItem().setLinkId("link0");
		qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);

		ValidationResult errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), hasSize(1));
		assertEquals(ResultSeverityEnum.WARNING, errors.getMessages().get(0).getSeverity());

		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);

		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), hasSize(1));
		assertEquals(ResultSeverityEnum.ERROR, errors.getMessages().get(0).getSeverity());
	}

	@Test
	public void testOpenchoiceAnswer() {
		String questionnaireRef = QUESTIONNAIRE_URL;

		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent item = q.addItem();
		item.setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.OPENCHOICE).setOptions(new Reference("http://somevalueset"));
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(questionnaireRef))).thenReturn(q);

		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system"))).thenReturn(true);
		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system2"))).thenReturn(true);
		when(myValSupport.validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));
		when(myValSupport.validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code1"), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setSeverityCode(ValidationMessage.IssueSeverity.ERROR.toCode()).setMessage("Unknown code"));

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl("http://codesystems.com/system");
		codeSystem.addConcept().setCode("code0");
		when(myValSupport.fetchCodeSystem(eq("http://codesystems.com/system"))).thenReturn(codeSystem);

		CodeSystem codeSystem2 = new CodeSystem();
		codeSystem2.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem2.setUrl("http://codesystems.com/system2");
		codeSystem2.addConcept().setCode("code2");
		when(myValSupport.fetchCodeSystem(eq("http://codesystems.com/system2"))).thenReturn(codeSystem2);

		ValueSet options = new ValueSet();
		options.setUrl("http://somevalueset");
		options.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		options.getCompose().addInclude().setSystem("http://codesystems.com/system2").addConcept().setCode("code2");
		when(myValSupport.fetchResource(eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

		when(myValSupport.isValueSetSupported(any(), eq("http://somevalueset"))).thenReturn(true);

		when(myValSupport.validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), any(IBaseResource.class))).thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));

		QuestionnaireResponse qa;
		ValidationResult errors;

		// Good code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code0"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		assertEquals(0, errors.getMessages().size(), errors.toString());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Unknown code for 'http://codesystems.com/system#code1'"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		// Partial code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem(null).setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		assertEquals(2, errors.getMessages().size());
		assertThat(errors.getMessages().get(0).getMessage(), containsString("A code with no system has no defined meaning. A system should be provided"));
		assertThat(errors.getMessages().get(0).getLocationString(), containsString("QuestionnaireResponse.item[0].answer[0]"));
		assertThat(errors.getMessages().get(1).getMessage(), containsString("The value provided (null::code1) is not in the options value set in the questionnaire"));
		assertThat(errors.getMessages().get(1).getLocationString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertEquals(2, errors.getMessages().size());
		assertThat(errors.getMessages().get(0).getMessage(), containsString("A code with no system has no defined meaning. A system should be provided"));
		assertThat(errors.getMessages().get(0).getLocationString(), containsString("QuestionnaireResponse.item[0].answer[0]"));
		assertThat(errors.getMessages().get(1).getMessage(), containsString("The value provided (null::code1) is not in the options value set in the questionnaire"));
		assertThat(errors.getMessages().get(1).getLocationString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://system").setCode(null));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("The value provided (http://system::null) is not in the options value set in the questionnaire"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		// Wrong type

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new IntegerType(123));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Cannot validate integer answer option because no option list is provided"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		// String answer

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("Hello"));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		List<SingleValidationMessage> warningsAndErrors = errors
			.getMessages()
			.stream()
			.filter(t -> t.getSeverity().ordinal() > ResultSeverityEnum.INFORMATION.ordinal())
			.collect(Collectors.toList());
		assertThat(warningsAndErrors, is(empty()));

		// Missing String answer

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setDisplay(""));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response answer found for required item 'link0'"));
	}

	@Test
	public void testUnexpectedAnswer() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString(" - QuestionnaireResponse"));
		assertThat(errors.toString(), containsString("LinkId 'link1' not found in questionnaire"));
	}

	@Test
	public void testUnexpectedGroup() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(QUESTIONNAIRE_URL);
		qa.addItem().setLinkId("link1").addItem().setLinkId("link2");

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString(" - QuestionnaireResponse"));
		assertThat(errors.toString(), containsString("LinkId 'link1' not found in questionnaire"));
	}

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu3();
	}

	@AfterAll
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.randomizeLocaleAndTimezone();
	}

}
