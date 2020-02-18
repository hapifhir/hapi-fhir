package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class QuestionnaireValidatorR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(QuestionnaireValidatorR4Test.class);
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forR4();
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;

	@Before
	public void before() {
		IValidationSupport myValSupport = mock(IValidationSupport.class);

		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		ValidationSupportChain validationSupport = new ValidationSupportChain(myDefaultValidationSupport, myValSupport);
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
			Questionnaire q = minimalValidQuestionnaire();
			q.addItem()
				.setLinkId("link0")
				.setType(QuestionnaireItemType.STRING)
				.addExtension()
				.setUrl(extensionDomainToTest)
				.setValue(new Coding(null, "text-box", null));

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
			Questionnaire q = minimalValidQuestionnaire();
			q.addItem()
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
	}

	@Test
	public void testQuestionnaireWithCustomExtensionDomain() {
		String extensionUrl = "http://my.own.domain/StructureDefinition/";
		Questionnaire q = minimalValidQuestionnaire();
		q.addItem()
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

		myInstanceVal.setCustomExtensionDomains(extensionUrl);
		errors = myVal.validateWithResult(q);

		ourLog.info(errors.toString());
		assertThat(errors.isSuccessful(), Matchers.is(true));
		assertThat(errors.getMessages(), Matchers.empty());
	}

	private Questionnaire minimalValidQuestionnaire() {
		Narrative n = new Narrative().setStatus(NarrativeStatus.GENERATED);
		n.setDivAsString("simple example");
		Questionnaire q = new Questionnaire();
		q.setText(n);
		q.setName("SomeName");
		q.setStatus(PublicationStatus.ACTIVE);
		return q;
	}

	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
