package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemOptionComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class QuestionnaireResponseValidatorDstu3Test {
	public static final IdType ID_ICC_QUESTIONNAIRE_SETUP = new IdType("Questionnaire/profile");
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorDstu3Test.class);
	private static final String CODE_ICC_SCHOOLTYPE_PT = "PT";
	private static final IdType ID_VS_SCHOOLTYPE = new IdType("ValueSet/schooltype");
	private static final String SYSTEMURI_ICC_SCHOOLTYPE = "http://ehealthinnovation/icc/ns/schooltype";
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;
	private IValidationSupport myValSupport;
	private IWorkerContext myWorkerCtx;

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
		List<SingleValidationMessage> messages = new ArrayList<SingleValidationMessage>(theErrors.getMessages());
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
		answerValues[14] = new Reference("http://example.com/Questionnaire/q1");
		answerValues[15] = new Quantity(42);

		for (int i = 0; i < itemCnt; i++) {
			if (questionnaireItemTypes[i] == null) continue;
			String linkId = "link" + i;

			reset(myValSupport);
			Questionnaire q = new Questionnaire();
			when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class),
				eq("http://example.com/Questionnaire/q1"))).thenReturn(q);
			when(myValSupport.fetchCodeSystem(any(FhirContext.class), eq("http://codesystems.com/system"))).thenReturn(codeSystem);
			when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq("http://somevalueset"))).thenReturn(options);
			myInstanceVal.flushCaches();

			q.getItem().clear();
			QuestionnaireItemComponent questionnaireItemComponent =
				q.addItem().setLinkId(linkId).setRequired(true).setType(questionnaireItemTypes[i]);
			if (i == 10 || i == 11) {
				questionnaireItemComponent.setOptions(new Reference("http://somevalueset"));
			} else if (i == 12) {
				questionnaireItemComponent.setOption(
					Arrays.asList(new QuestionnaireItemOptionComponent(new StringType("some value"))));
			}

			QuestionnaireResponse qa = new QuestionnaireResponse();
			qa.setStatus(QuestionnaireResponseStatus.INPROGRESS);
			qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
			qa.addItem().setLinkId(linkId).addAnswer().setValue(answerValues[i]);

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
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);

		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Answer value must be of type boolean"));
	}

	@Test
	public void testCodedAnswer() {
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.CHOICE).setOptions(new Reference("http://somevalueset"));
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq("http://example.com/Questionnaire/q1"))).thenReturn(q);

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
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code0"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		assertEquals(errors.toString(), 0, errors.getMessages().size());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("The value provided (http://codesystems.com/system::code1) is not in the options value set in the questionnaire"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.answer"));

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system2").setCode("code3"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("The value provided (http://codesystems.com/system2::code3) is not in the options value set in the questionnaire"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.answer"));

	}

	@Test
	public void testGroupWithNoLinkIdInQuestionnaireResponse() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qGroup = q.addItem().setType(QuestionnaireItemType.GROUP);
		qGroup.addItem().setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		QuestionnaireResponseItemComponent qaGroup = qa.addItem();
		qaGroup.addItem().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No LinkId, so can't be validated"));
	}

	@Test
	public void testItemWithNoType() {
		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent qGroup = q.addItem();
		qGroup.setLinkId("link0");
		qGroup.addItem().setLinkId("link1").setType(QuestionnaireItemType.STRING);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		QuestionnaireResponseItemComponent qaGroup = qa.addItem().setLinkId("link0");
		qaGroup.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
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
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		String reference = qa.getQuestionnaire().getReference();
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(reference))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response found for required item link0"));
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
			.setOptions(new Reference(valueSetRef));

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
		String questionnaireRef = "http://example.com/Questionnaire/q1";
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

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(questionnaireResponse.getQuestionnaire().getReference()))).thenReturn(questionnaire);
		when(myValSupport.fetchResource(any(FhirContext.class), eq(ValueSet.class), eq(ID_VS_SCHOOLTYPE.getValue()))).thenReturn(iccSchoolTypeVs);
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

		String reference = "http://example.com/Questionnaire/q1";
		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(reference)))
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
		String questionnaireRef = "http://example.com/Questionnaire/q1";

		Questionnaire q = new Questionnaire();
		QuestionnaireItemComponent item = q.addItem();
		item.setLinkId("link0").setRequired(true).setType(QuestionnaireItemType.OPENCHOICE).setOptions(new Reference("http://somevalueset"));
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

		// Good code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code0"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		assertEquals(errors.toString(), 0, errors.getMessages().size());

		// Bad code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://codesystems.com/system").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("The value provided (http://codesystems.com/system::code1) is not in the options value set in the questionnaire"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.answer"));

		// Partial code

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem(null).setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("The value provided (null::code1) is not in the options value set in the questionnaire"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.answer"));

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("").setCode("code1"));
		errors = myVal.validateWithResult(qa);
		errors = stripBindingHasNoSourceMessage(errors);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("The value provided (null::code1) is not in the options value set in the questionnaire"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.answer"));

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("http://system").setCode(null));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("The value provided (http://system::null) is not in the options value set in the questionnaire"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.answer"));

		// Wrong type

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new IntegerType(123));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("Cannot validate integer answer option because no option list is provided"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item.answer"));

		// String answer

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setDisplay("Hello"));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.getMessages(), empty());

		// Missing String answer

		qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference(questionnaireRef);
		qa.addItem().setLinkId("link0").addAnswer().setValue(new Coding().setDisplay(""));
		errors = myVal.validateWithResult(qa);
		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString("No response answer found for required item link0"));
		assertThat(errors.toString(), containsString("QuestionnaireResponse.item"));

	}

	@Test
	public void testUnexpectedAnswer() {
		Questionnaire q = new Questionnaire();
		q.addItem().setLinkId("link0").setRequired(false).setType(QuestionnaireItemType.BOOLEAN);

		QuestionnaireResponse qa = new QuestionnaireResponse();
		qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
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
		qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
		qa.addItem().setLinkId("link1").addItem().setLinkId("link2");

		when(myValSupport.fetchResource(any(FhirContext.class), eq(Questionnaire.class), eq(qa.getQuestionnaire().getReference()))).thenReturn(q);
		ValidationResult errors = myVal.validateWithResult(qa);

		ourLog.info(errors.toString());
		assertThat(errors.toString(), containsString(" - QuestionnaireResponse"));
		assertThat(errors.toString(), containsString("LinkId \"link1\" not found in questionnaire"));
	}

	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
