package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionnaireResponseValidatorR4Test {
	public static final String ID_ICC_QUESTIONNAIRE_SETUP = "Questionnaire/profile";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorR4Test.class);
	private static final String CODE_ICC_SCHOOLTYPE_PT = "PT";
	private static final String ID_VS_SCHOOLTYPE = "ValueSet/schooltype";
	private static final String SYSTEMURI_ICC_SCHOOLTYPE = "http://ehealthinnovation/icc/ns/schooltype";
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forR4();
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;
	private IValidationSupport myValSupport;
	private HapiWorkerContext myWorkerCtx;

	@Before
	public void before() {
		myValSupport = mock(IValidationSupport.class);
		// new DefaultProfileValidationSupport();
		myWorkerCtx = new HapiWorkerContext(ourCtx, myValSupport);

		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		ValidationSupportChain validationSupport = new ValidationSupportChain(myValSupport, myDefaultValidationSupport);
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
		when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq("http://codesystems.com/system"))).thenReturn(codeSystem);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

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
		answerValues[14] = new Reference("http://example.com/Questionnaire/q1");
		answerValues[15] = new Quantity(42);


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

			when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class),
				eq(qa.getQuestionnaire()))).thenReturn(q);

			ValidationResult errors = myVal.validateWithResult(qa);

			ourLog.info(errors.toString());
			assertThat("index[" + i + "]: " + errors.toString(), errors.getMessages(), empty());
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

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaireElement().getValue()))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer value must be of type boolean"));
	}

	@Test
	public void testCodedAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setAnswerValueSet("http://somevalueset");
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq("http://example.com/Questionnaire/q1"))).thenReturn(q);

		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system"))).thenReturn(true);
		when(myValSupport.isCodeSystemSupported(any(), eq("http://codesystems.com/system2"))).thenReturn(true);
		when(myValSupport.validateCode(any(), eq("http://codesystems.com/system"), eq("code0"), any()))
			.thenReturn(new IValidationSupport.CodeValidationResult(new CodeSystem.ConceptDefinitionComponent().setCode("code0")));
		when(myValSupport.validateCode(any(), eq("http://codesystems.com/system"), eq("code1"), any()))
			.thenReturn(new IValidationSupport.CodeValidationResult(ValidationMessage.IssueSeverity.ERROR, "Unknown code"));

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl("http://codesystems.com/system");
		codeSystem.addConcept().setCode("code0");
		when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq("http://codesystems.com/system"))).thenReturn(codeSystem);

		CodeSystem codeSystem2 = new CodeSystem();
		codeSystem2.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem2.setUrl("http://codesystems.com/system2");
		codeSystem2.addConcept().setCode("code2");
		when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq("http://codesystems.com/system2"))).thenReturn(codeSystem2);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		options.getCompose().addInclude().setSystem("http://codesystems.com/system2").addConcept().setCode("code2");
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

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
		assertEquals(errors.toString(), 0, errors.getMessages().size());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Unknown code for 'http://codesystems.com/system#code1' - QuestionnaireResponse.item[0].answer[0].value.ofType(Coding)"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system2").setCode("code3"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Validation failed for 'http://codesystems.com/system2#code3'"));
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

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Element 'QuestionnaireResponse.item[0].linkId': minimum required = 1, but only found 0"));
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

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
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
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response found for required item with id = 'link0'"));
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
			.setType(QuestionnaireItemType.CHOICE)
			.setAnswerValueSet(valueSetRef);

		item1.addItem().setLinkId("link11")
			.setType(QuestionnaireItemType.TEXT);

		Questionnaire q = new Questionnaire();
		q.addItem(item1);
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(questionnaireRef)))
			.thenReturn(q);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl(codeSystemUrl);
		codeSystem.addConcept().setCode(codeValue);
		when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq(codeSystemUrl)))
			.thenReturn(codeSystem);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem(codeSystemUrl).addConcept().setCode(codeValue);
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq(valueSetRef)))
			.thenReturn(options);
		when(myValSupport.validateCode(any(FhirContext.class), eq(codeSystemUrl), eq(codeValue), any(String.class)))
			.thenReturn(new CodeValidationResult(new ConceptDefinitionComponent(new CodeType(codeValue))));

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
			.addItem().setLinkId("link11");

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
			.setType(QuestionnaireItemType.OPENCHOICE)
			.setAnswerValueSet(valueSetRef);

		item1.addItem().setLinkId("link11")
			.setType(QuestionnaireItemType.TEXT);

		Questionnaire q = new Questionnaire();
		q.addItem(item1);
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(questionnaireRef)))
			.thenReturn(q);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl(codeSystemUrl);
		codeSystem.addConcept().setCode(codeValue);
		when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq(codeSystemUrl)))
			.thenReturn(codeSystem);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem(codeSystemUrl).addConcept().setCode(codeValue);
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq(valueSetRef)))
			.thenReturn(options);
		when(myValSupport.validateCode(any(FhirContext.class), eq(codeSystemUrl), eq(codeValue), any(String.class)))
			.thenReturn(new CodeValidationResult(new ConceptDefinitionComponent(new CodeType(codeValue))));

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
			.addItem().setLinkId("link11");

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
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(questionnaireRef)))
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
			.addItem().setLinkId("link11");

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
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(reference)))
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
		item.setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.OPENCHOICE).setAnswerValueSet("http://somevalueset");
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(questionnaireRef))).thenReturn(q);

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem.setUrl("http://codesystems.com/system");
		codeSystem.addConcept().setCode("code0");
		when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq("http://codesystems.com/system"))).thenReturn(codeSystem);

		CodeSystem codeSystem2 = new CodeSystem();
		codeSystem2.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem2.setUrl("http://codesystems.com/system2");
		codeSystem2.addConcept().setCode("code2");
		when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq("http://codesystems.com/system2"))).thenReturn(codeSystem2);

		ValueSet options = new ValueSet();
		options.getCompose().addInclude().setSystem("http://codesystems.com/system").addConcept().setCode("code0");
		options.getCompose().addInclude().setSystem("http://codesystems.com/system2").addConcept().setCode("code2");
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);

		when(myValSupport.validateCode(any(FhirContext.class), eq("http://codesystems.com/system"), eq("code0"), any(String.class))).thenReturn(new CodeValidationResult(new ConceptDefinitionComponent(new CodeType("code0"))));

		QuestionnaireResponse qa;
		ValidationResult errors;

//		// Good code
//
//		qa = new QuestionnaireResponse();
//		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
//		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
//		qa.getQuestionnaireElement().setValue(questionnaireRef);
//		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code0"));
//		errors = myVal.validateWithResult(qa);
//		errors = stripBindingHasNoSourceMessage(errors);
//		assertEquals(errors.getMessages().toString(), 0, errors.getMessages().size());
//
//		// Bad code
//
//		qa = new QuestionnaireResponse();
//		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
//		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
//		qa.getQuestionnaireElement().setValue(questionnaireRef);
//		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code1"));
//		errors = myVal.validateWithResult(qa);
//		errors = stripBindingHasNoSourceMessage(errors);
//		ourLog.info(errors.toString());
//		assertThat(errors.toString(), containsString("The value provided (http://codesystems.com/system::code1) is not in the options value set in the questionnaire"));
//		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));
//
//		// Partial code
//
//		qa = new QuestionnaireResponse();
//		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
//		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
//		qa.getQuestionnaireElement().setValue(questionnaireRef);
//		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem(null).setCode("code1"));
//		errors = myVal.validateWithResult(qa);
//		errors = stripBindingHasNoSourceMessage(errors);
//		ourLog.info(errors.toString());
//		assertThat(errors.toString(), containsString("The value provided (null::code1) is not in the options value set in the questionnaire"));
//		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));
//
//		qa = new QuestionnaireResponse();
//		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
//		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
//		qa.getQuestionnaireElement().setValue(questionnaireRef);
//		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("").setCode("code1"));
//		errors = myVal.validateWithResult(qa);
//		errors = stripBindingHasNoSourceMessage(errors);
//		ourLog.info(errors.toString());
//		assertThat(errors.toString(), containsString("The value provided (null::code1) is not in the options value set in the questionnaire"));
//		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));
//
//		// System only in known codesystem
//		qa = new QuestionnaireResponse();
//		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
//		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
//		qa.getQuestionnaireElement().setValue(questionnaireRef);
//		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode(null));
//		errors = myVal.validateWithResult(qa);
//		ourLog.info(errors.toString());
//		assertThat(errors.toString(), containsString("The value provided (http://codesystems.com/system::null) is not in the options value set in the questionnaire"));
//		assertThat(errors.toString(), containsString("QuestionnaireResponse.item[0].answer[0]"));

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://system").setCode(null));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		// This is set in InstanceValidator#validateAnswerCode
		assertThat(errors.toString(), containsString("INFORMATION - Code http://system/null was not validated because the code system is not present"));
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
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setDisplay("Hello"));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());

		// Missing String answer

		qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setDisplay(""));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response found for required item with id = 'link0'"));

	}

	@Test
	public void testUnexpectedAnswer() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString(" - QuestionnaireResponse"));
		assertThat(errors.toString(), containsString("LinkId \"link1\" not found in questionnaire"));
	}

	@Test
	public void testUnexpectedGroup() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaireElement().setValue("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addItem().setLinkId("link2");

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString(" - QuestionnaireResponse"));
		assertThat(errors.toString(), containsString("LinkId \"link1\" not found in questionnaire"));
	}

	@Test
	public void testValidateQuestionnaireResponseWithValueSetChoiceAnswer() {
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
				.setAnswerValueSet(ID_VS_SCHOOLTYPE)
				.setRequired(true);
		}

		/*
		 * Create response
		 */
		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		qa.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qa.setQuestionnaire(ID_ICC_QUESTIONNAIRE_SETUP);
		qa.getSubject().setReference("Patient/123");

		QuestionnaireResponse.QuestionnaireResponseItemComponent basicGroup = qa
			.addItem();
		basicGroup.setLinkId("basic");
		basicGroup
			.addItem()
			.setLinkId("schoolType")
			.addAnswer()
			.setValue(new Coding(SYSTEMURI_ICC_SCHOOLTYPE, CODE_ICC_SCHOOLTYPE_PT, ""));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire()))).thenReturn(questionnaire);
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq(ID_VS_SCHOOLTYPE))).thenReturn(iccSchoolTypeVs);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No issues"));
	}


	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
