package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemEnableWhenComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SchematronValidationDstu3QuestionnaireTest {
	private static final Logger ourLog = LoggerFactory.getLogger(SchematronValidationDstu3QuestionnaireTest.class);

	private static FhirContext ourCtx = FhirContext.forDstu3();

	private static int linkIdCnt = 1;

	@Test
	public void enableWhenWithAnswer() {
		Questionnaire resource = new Questionnaire();
		resource.setStatus(Enumerations.PublicationStatus.ACTIVE);

		QuestionnaireItemComponent child1 = createItem(QuestionnaireItemType.GROUP);
		resource.addItem(child1);

		QuestionnaireItemEnableWhenComponent enableWhen = new QuestionnaireItemEnableWhenComponent();
		enableWhen.setQuestion("q1");
		enableWhen.setAnswer(new StringType("a value"));
		child1.addEnableWhen(enableWhen);

		QuestionnaireItemComponent child21 = createItem(QuestionnaireItemType.STRING);
		child1.addItem(child21);

		String inputXml = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource);
		ourLog.info(inputXml);

		ValidationResult result = validateSchematron(resource);
		assertTrue(result.isSuccessful());
	}

	@Test
	public void enableWhenWithHasAnswer() {
		Questionnaire resource = new Questionnaire();
		resource.setStatus(Enumerations.PublicationStatus.ACTIVE);

		QuestionnaireItemComponent child1 = createItem(QuestionnaireItemType.GROUP);
		resource.addItem(child1);

		QuestionnaireItemEnableWhenComponent enableWhen = new QuestionnaireItemEnableWhenComponent();
		enableWhen.setQuestion("q1");
		enableWhen.setHasAnswer(true);
		child1.addEnableWhen(enableWhen);

		QuestionnaireItemComponent child21 = createItem(QuestionnaireItemType.STRING);
		child1.addItem(child21);

		String inputXml = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource);
		ourLog.info(inputXml);

		ValidationResult result = validateSchematron(resource);
		assertTrue(result.isSuccessful());
	}

	@Test
	public void enableWhenWithHasAnswerAndAnswer() {
		Questionnaire resource = new Questionnaire();
		resource.setStatus(Enumerations.PublicationStatus.ACTIVE);

		QuestionnaireItemComponent child1 = createItem(QuestionnaireItemType.GROUP);
		resource.addItem(child1);

		QuestionnaireItemEnableWhenComponent enableWhen = new QuestionnaireItemEnableWhenComponent();
		enableWhen.setQuestion("q1");
		enableWhen.setAnswer(new StringType("a value"));
		enableWhen.setHasAnswer(true);
		child1.addEnableWhen(enableWhen);

		QuestionnaireItemComponent child21 = createItem(QuestionnaireItemType.STRING);
		child1.addItem(child21);

		String inputXml = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource);
		ourLog.info(inputXml);

		ValidationResult result = validateSchematron(resource);
		assertFalse(result.isSuccessful());
		assertEquals(1, result.getMessages().size());
		assertThat(result.getMessages().get(0).getMessage(), containsString("que-7"));
	}

	private QuestionnaireItemComponent createItem(QuestionnaireItemType type) {
		QuestionnaireItemComponent item = new QuestionnaireItemComponent();
		item.setLinkId("id-" + linkIdCnt++);
		item.setType(type);
		return item;
	}

	private ValidationResult validateSchematron(Questionnaire resource) {
		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(false);
		val.setValidateAgainstStandardSchematron(true);
		ValidationResult result = val.validateWithResult(resource);

		String outcomeXml = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(outcomeXml);
		return result;
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
