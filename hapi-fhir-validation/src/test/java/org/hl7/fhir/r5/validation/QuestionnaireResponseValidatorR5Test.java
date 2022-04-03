package org.hl7.fhir.r5.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r5.model.Attachment;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DataType;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.DateType;
import org.hl7.fhir.r5.model.DecimalType;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.MarkdownType;
import org.hl7.fhir.r5.model.Narrative;
import org.hl7.fhir.r5.model.Quantity;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.hl7.fhir.r5.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.r5.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.TimeType;
import org.hl7.fhir.r5.model.UriType;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionnaireResponseValidatorR5Test {
	public static final String ID_ICC_QUESTIONNAIRE_SETUP = "http://example.com/Questionnaire/profile";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorR5Test.class);
	private static final String CODE_ICC_SCHOOLTYPE_PT = "PT";
	private static final String ID_VS_SCHOOLTYPE = "ValueSet/schooltype";
	private static final String SYSTEMURI_ICC_SCHOOLTYPE = "http://ehealthinnovation/icc/ns/schooltype";
	private static final FhirContext ourCtx = FhirContext.forR5Cached();
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;
	private IValidationSupport myValSupport;
	private HapiWorkerContext myWorkerCtx;

	@BeforeEach
	public void before() {
		myValSupport = mock(IValidationSupport.class);
		when(myValSupport.getFhirContext()).thenReturn(ourCtx);

		myWorkerCtx = new HapiWorkerContext(ourCtx, myValSupport);

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
		when(myValSupport.fetchCodeSystem(eq("http://codesystems.com/system"))).thenReturn(codeSystem);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		when(myValSupport.fetchResource(eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

		when(myValSupport.validateCode(any(), any(), any(), any(), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setSeverity(IValidationSupport.IssueSeverity.ERROR).setMessage("Unknown code"));
		when(myValSupport.validateCodeInValueSet(any(), any(), any(), any(), any(), nullable(ValueSet.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));

		int itemCnt = 15;
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
		questionnaireItemTypes[10] = QuestionnaireItemType.CODING;
		questionnaireItemTypes[11] = QuestionnaireItemType.CODING;
		questionnaireItemTypes[12] = QuestionnaireItemType.ATTACHMENT;
		questionnaireItemTypes[13] = QuestionnaireItemType.REFERENCE;
		questionnaireItemTypes[14] = QuestionnaireItemType.QUANTITY;

		DataType[] answerValues = new DataType[itemCnt];
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
		answerValues[12] = new Attachment().setData("some data".getBytes()).setContentType("txt");
		answerValues[13] = new Reference("http://example.com/Questionnaire/q1");
		answerValues[14] = new Quantity(42);


		for (int i = 0; i < itemCnt; i++) {
			ourLog.info("Testing item {}: {}", i, questionnaireItemTypes[i]);

			Questionnaire q = new Questionnaire();
			if (questionnaireItemTypes[i] == null) continue;
			String linkId = "link" + i;
			QuestionnaireItemComponent questionnaireItemComponent =
				q.addItem().setLinkId(linkId).setRequired(true).setType(questionnaireItemTypes[i]);
			if (i == 10 || i == 11) {
				questionnaireItemComponent.setAnswerValueSet("http://somevalueset");
			} else if (i == 12) {
				questionnaireItemComponent.setAnswerOption(
					Arrays.asList(new Questionnaire.QuestionnaireItemAnswerOptionComponent(new StringType("some value"))));
			}

			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
			qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
			qa.setQuestionnaire("http://example.com/Questionnaire/q" + i);
			qa.addItem().setLinkId(linkId).addAnswer().setValue(answerValues[i]);

			when(myValSupport.fetchResource(eq(Questionnaire.class),
				eq(qa.getQuestionnaire()))).thenReturn(q);
			when(myValSupport.validateCode(any(), any(), any(), any(), any(), nullable(String.class)))
				.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));

			ValidationResult errors = myVal.validateWithResult(qa);

			ourLog.info(errors.toString());
			assertThat("index[" + i + "]: " + errors, errors.getMessages(), empty());
		}
	}

	@Test
	public void testAnswerWithWrongType() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaireElement().getValue()))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer value must be of type boolean"));
	}

	@Test
	public void testCodedAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CODING).setAnswerValueSet("http://somevalueset");
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq("http://example.com/Questionnaire/q1"))).thenReturn(q);

		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system"))).thenReturn(true);
		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system2"))).thenReturn(true);
		when(myValSupport.validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));
		when(myValSupport.validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code1"), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setSeverity(IValidationSupport.IssueSeverity.ERROR).setMessage("Unknown code"));
		when(myValSupport.validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code0"), any(), nullable(ValueSet.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));
		when(myValSupport.validateCodeInValueSet(any(), any(), eq("http://codesystems.com/system"), eq("code1"), any(), nullable(ValueSet.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setSeverity(IValidationSupport.IssueSeverity.ERROR).setMessage("Unknown code"));

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

		QuestionnaireResponse qa;
		ValidationResult errors;

		// Good code

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code0"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		assertEquals(0, errors.getMessages().size(), errors.toString());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Unknown code for 'http://codesystems.com/system#code1'"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system2").setCode("code3"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Unknown code 'http://codesystems.com/system2#code3' for 'http://codesystems.com/system2#code3'"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

	}

	@Test
	public void testGroupWithNoLinkIdInQuestionnaireResponse() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qGroup = q.addItem().setType(QuestionnaireItemType.GROUP);
		qGroup.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		QuestionnaireResponseItemComponent qaGroup = qa.addItem();
		qaGroup.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.linkId: minimum required = 1, but only found 0"));
	}

	@Test
	public void testItemWithNoType() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qItem = q.addItem();
		qItem.setLinkId("link0");

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		QuestionnaireResponseItemComponent qaItem = qa.addItem().setLinkId("link0");
		qaItem.addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
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
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		String reference = qa.getQuestionnaire();
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response answer found for required item 'link0'"));
	}

	@Test
	public void testEmbeddedItemInChoice() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";
		String valueSetRef = "http://somevalueset";
		String codeSystemUrl = "http://codesystems.com/system";
		String codeValue = "code0";

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1")
			.setType(QuestionnaireItemType.CODING)
			.setAnswerValueSet(valueSetRef);

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
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
		qa.setQuestionnaire(questionnaireRef);
		qa.addItem()
			.setLinkId("link1")
			.addAnswer()
			.setValue(new Coding(codeSystemUrl, codeValue, null))
			.addItem()
			.setLinkId("link11")
			.addAnswer()
			.setValue(new StringType("foo"));

		String rXml = xmlParser.encodeResourceToString(qa);
		ourLog.info(rXml);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());
	}

	@Test
	public void testEmbeddedItemInOpenChoice() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";
		String valueSetRef = "http://somevalueset";
		String codeSystemUrl = "http://codesystems.com/system";
		String codeValue = "code0";

		// create the questionnaire
		QuestionnaireItemComponent item1 = new QuestionnaireItemComponent();
		item1.setLinkId("link1")
			.setType(QuestionnaireItemType.CODING)
			.setAnswerValueSet(valueSetRef);

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
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
		qa.setQuestionnaire(questionnaireRef);
		qa.addItem()
			.setLinkId("link1")
			.addAnswer()
			.setValue(new Coding(codeSystemUrl, codeValue, null))
			.addItem()
			.setLinkId("link11")
			.addAnswer()
			.setValue(new StringType("foo"));

		String rXml = xmlParser.encodeResourceToString(qa);
		ourLog.info(rXml);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());
	}

	@Test
	public void testEmbeddedItemInString() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

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
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
		qa.setQuestionnaire(questionnaireRef);
		qa.addItem().setLinkId("link1")
			.addAnswer()
			.setValue(new StringType("FOO"))
			.addItem()
			.setLinkId("link11")
			.addAnswer()
			.setValue(new StringType("BAR"));

		String rXml = xmlParser.encodeResourceToString(qa);
		ourLog.info(rXml);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());
	}

	@Test
	public void testMissingRequiredAnswer() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0")
			.setType(QuestionnaireItemType.STRING)
			.setRequired(true);

		String reference = "http://example.com/Questionnaire/q1";
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(reference)))
			.thenReturn(q);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setQuestionnaire(reference);
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
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent item = q.addItem();
		item.setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.CODING).setAnswerValueSet("http://somevalueset").setAnswerConstraint(Questionnaire.QuestionnaireAnswerConstraint.OPTIONSORTYPE);
		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(questionnaireRef))).thenReturn(q);

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
			.thenReturn(new IValidationSupport.CodeValidationResult().setCode("code0"));
		when(myValSupport.validateCode(any(), any(), eq("http://codesystems.com/system"), eq("code1"), any(), nullable(String.class)))
			.thenReturn(new IValidationSupport.CodeValidationResult().setSeverity(IValidationSupport.IssueSeverity.ERROR).setMessage("Unknown code"));

		QuestionnaireResponse qa;
		ValidationResult errors;

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://system").setCode(null));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		// This is set in InstanceValidator#validateAnswerCode
		assertThat(errors.toString(), containsString(" The value provided (http://system::null) is not in the options value set"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		// Wrong type

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new IntegerType(123));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Cannot validate integer answer option because no option list is provided"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		// String answer

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("Hello"));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.getMessages().stream().filter(t->t.getSeverity().ordinal() > ResultSeverityEnum.INFORMATION.ordinal()).collect(Collectors.toList()), empty());

		// Missing String answer

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
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
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
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
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addItem().setLinkId("link2");

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString(" - QuestionnaireResponse"));
		assertThat(errors.toString(), containsString("LinkId 'link1' not found in questionnaire"));
	}

	@Test
	public void testValidateQuestionnaireResponseWithValueSetChoiceAnswer() {
		/*
		 * Create CodeSystem
		 */
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
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

			QuestionnaireItemComponent basicGroup = questionnaire.addItem();
			basicGroup.setLinkId("basic");
			basicGroup.setType(QuestionnaireItemType.GROUP);
			basicGroup
				.addItem()
				.setLinkId("schoolType")
				.setType(QuestionnaireItemType.CODING)
				.setAnswerValueSet(ID_VS_SCHOOLTYPE)
				.setRequired(true);
		}

		/*
		 * Create response
		 */
		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.setQuestionnaire(ID_ICC_QUESTIONNAIRE_SETUP);
		qa.getSubject().setReference("Patient/123");

		QuestionnaireResponseItemComponent basicGroup = qa
			.addItem();
		basicGroup.setLinkId("basic");
		basicGroup
			.addItem()
			.setLinkId("schoolType")
			.addAnswer()
			.setValue(new Coding(SYSTEMURI_ICC_SCHOOLTYPE, CODE_ICC_SCHOOLTYPE_PT, ""));

		when(myValSupport.fetchResource(eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(questionnaire);
		when(myValSupport.fetchResource(eq(ValueSet.class), eq(ID_VS_SCHOOLTYPE))).thenReturn(iccSchoolTypeVs);
		when(myValSupport.validateCodeInValueSet(any(), any(), any(), any(), any(), any(ValueSet.class))).thenReturn(new IValidationSupport.CodeValidationResult().setCode(CODE_ICC_SCHOOLTYPE_PT));
		when(myValSupport.fetchCodeSystem(eq(SYSTEMURI_ICC_SCHOOLTYPE))).thenReturn(codeSystem);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}


	@AfterAll
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
	}


}
