package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class QuestionnaireValidatorDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(QuestionnaireValidatorDstu3Test.class);
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;

	@Before
	public void before() {
		IValidationSupport myValSupport = mock(IValidationSupport.class);

		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		ValidationSupportChain validationSupport = new ValidationSupportChain(myValSupport, myDefaultValidationSupport);
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
				.setValue(new Coding(null, "text-box", null));

			ValidationResult errors = myVal.validateWithResult(q);
			ourLog.info(errors.toString());
			assertThat(errors.isSuccessful(), Matchers.is(true));
			assertThat(errors.getMessages(), Matchers.empty());
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
				.setValue(new CodeableConcept().addCoding(new Coding(null, "text-box", null)));

			ValidationResult errors = myVal.validateWithResult(q);
			ourLog.info(errors.toString());
			assertThat(errors.isSuccessful(), Matchers.is(true));
			assertThat(errors.getMessages(), Matchers.empty());
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
					.setValue(new Coding(null, "text-box", null));

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

	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
