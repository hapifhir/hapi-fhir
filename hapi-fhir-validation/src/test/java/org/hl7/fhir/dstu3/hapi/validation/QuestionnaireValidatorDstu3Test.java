package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import org.hamcrest.Matchers;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionnaireValidatorDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(QuestionnaireValidatorDstu3Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;

	@BeforeEach
	public void before() {


		IValidationSupport myValSupport = mock(IValidationSupport.class);
		when(myValSupport.getFhirContext()).thenReturn(ourCtx);

		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		ValidationSupportChain validationSupport = new ValidationSupportChain(myDefaultValidationSupport, myValSupport, new InMemoryTerminologyServerValidationSupport(ourCtx));
		myInstanceVal = new FhirInstanceValidator(validationSupport);

		myVal.registerValidatorModule(myInstanceVal);
	}

	@Test
	public void testQuestionnaireWithPredefinedExtensionDomainsForCoding() {
		String[] extensionDomainsToTest = new String[] {
			"http://example.org/questionnaire-color-control-1",
			"https://example.org/questionnaire-color-control-2",
			"http://acme.com/questionnaire-color-control-3",
			"https://acme.com/questionnaire-color-control-4",
			"http://nema.org/questionnaire-color-control-5",
			"https://nema.org/questionnaire-color-control-6",
			"http://hl7.org/fhir/StructureDefinition/structuredefinition-expression",
		};
		for (String extensionDomainToTest : extensionDomainsToTest) {
			Questionnaire q = new Questionnaire();
			q.setStatus(PublicationStatus.ACTIVE)
				.addItem()
				.setLinkId("link0")
				.setType(QuestionnaireItemType.STRING)
				.addExtension()
				.setUrl(extensionDomainToTest)
				.setValue(new CodeType("text-box"));

			ValidationResult errors = myVal.validateWithResult(q);
			ourLog.info(errors.toString());
			assertThat(errors.isSuccessful(), Matchers.is(true));
			assertThat(errors.getMessages().stream().filter(t->t.getSeverity().ordinal() > ResultSeverityEnum.INFORMATION.ordinal()).collect(Collectors.toList()), Matchers.empty());
		}

	}

	@Test
	public void testQuestionnaireWithPredefinedExtensionDomainsForCodeableConcept() {
		String[] extensionDomainsToTest = new String[] {
			"http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl",
		};
		for (String extensionDomainToTest : extensionDomainsToTest) {
			Questionnaire q = new Questionnaire();
			q.setStatus(PublicationStatus.ACTIVE)
				.addItem()
				.setLinkId("link0")
				.setType(QuestionnaireItemType.STRING)
				.addExtension()
				.setUrl(extensionDomainToTest)
				.setValue(new CodeableConcept().addCoding(new Coding("http://hl7.org/fhir/questionnaire-item-control", "text-box", null)));

			ValidationResult errors = myVal.validateWithResult(q);
			ourLog.info(errors.toString());
			assertThat(errors.isSuccessful(), Matchers.is(true));
			assertThat(errors.getMessages(), Matchers.empty());
		}
		for (String extensionDomainToTest : extensionDomainsToTest) {
			Questionnaire q = new Questionnaire();
			q.setStatus(PublicationStatus.ACTIVE)
				.addItem()
				.setLinkId("link0")
				.setType(QuestionnaireItemType.STRING)
				.addExtension()
				.setUrl(extensionDomainToTest)
				.setValue(new CodeableConcept().addCoding(new Coding(null, "text-box", null)));

			ValidationResult errors = myVal.validateWithResult(q);
			ourLog.info(errors.toString());
			assertThat(errors.isSuccessful(), Matchers.is(true));
			assertThat(errors.getMessages().get(0).getMessage(), containsString("and a coding should come from this value set unless it has no suitable code (note that the validator cannot judge what is suitable) (codes = null#text-box)"));
		}
	}

	@Test
	public void testQuestionnaireWithCustomExtensionDomain() {
		Questionnaire q = new Questionnaire();
		String extensionUrl = "http://my.own.domain/StructureDefinition/";
		q.setStatus(PublicationStatus.ACTIVE)
			.addItem()
				.setLinkId("link0")
				.setType(QuestionnaireItemType.STRING)
				.addExtension()
					.setUrl(extensionUrl + "questionnaire-itemControl")
					.setValue(new CodeType("text-box"));

		ValidationResult errors = myVal.validateWithResult(q);

		ourLog.info(errors.toString());
		assertThat(errors.isSuccessful(), Matchers.is(true));
		assertThat(errors.getMessages(), Matchers.hasSize(1));
		assertEquals(errors.getMessages().get(0).getSeverity(), ResultSeverityEnum.INFORMATION);
		assertThat(errors.getMessages().get(0).getMessage(), Matchers.startsWith("Unknown extension " + extensionUrl));

		myInstanceVal.setCustomExtensionDomains(Collections.singletonList(extensionUrl));
		errors = myVal.validateWithResult(q);

		ourLog.info(errors.toString());
		assertThat(errors.isSuccessful(), Matchers.is(true));
		assertThat(errors.getMessages(), Matchers.empty());
	}

	@AfterAll
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.randomizeLocaleAndTimezone();
	}
}
