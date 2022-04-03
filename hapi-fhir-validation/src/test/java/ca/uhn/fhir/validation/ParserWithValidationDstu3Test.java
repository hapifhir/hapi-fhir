package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.ActivityDefinition;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserWithValidationDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ParserWithValidationDstu3Test.class);

	private static FhirContext ourCtx = FhirContext.forDstu3();

	@Test
	public void testActivityDefinitionElementsOrder() {
		final String origContent = "{\"resourceType\":\"ActivityDefinition\",\"id\":\"x1\",\"url\":\"http://testing.org\",\"status\":\"draft\",\"timingDateTime\":\"2011-02-03\"}";
		final IParser parser = ourCtx.newJsonParser();
		IValidationSupport validationSupport = getValidationSupport();

		// verify that InstanceValidator likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, origContent, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		ActivityDefinition fhirObj = parser.parseResource(ActivityDefinition.class, origContent);
		String content = parser.encodeResourceToString(fhirObj);
		ourLog.info("Serialized form: {}", content);

		// verify that InstanceValidator still likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, content, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		// verify that the original and newly serialized match
		assertEquals(origContent, content);
	}

	/**
	 * See #683
	 */
	@Test
	public void testChildOrderWithChoiceTypeXml() {
		final String origContent = "<ActivityDefinition xmlns=\"http://hl7.org/fhir\"><id value=\"x1\"/><url value=\"http://testing.org\"/><status value=\"draft\"/><timingDateTime value=\"2011-02-03\"/></ActivityDefinition>";
		final IParser parser = ourCtx.newXmlParser();
		IValidationSupport validationSupport = getValidationSupport();

		// verify that InstanceValidator likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, origContent, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		ActivityDefinition fhirObj = parser.parseResource(ActivityDefinition.class, origContent);
		String content = parser.encodeResourceToString(fhirObj);
		ourLog.info("Serialized form: {}", content);

		// verify that InstanceValidator still likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, content, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		// verify that the original and newly serialized match
		assertEquals(origContent, content);
	}

	@Test
	public void testConceptMapElementsOrder() {
		final String origContent = "{\"resourceType\":\"ConceptMap\",\"id\":\"x1\",\"url\":\"http://testing.org\",\"status\":\"draft\",\"sourceUri\":\"http://y1\"}";
		final IParser parser = ourCtx.newJsonParser();
		IValidationSupport validationSupport = getValidationSupport();

		// verify that InstanceValidator likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, origContent, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		ConceptMap fhirObj = parser.parseResource(ConceptMap.class, origContent);
		String content = parser.encodeResourceToString(fhirObj);
		ourLog.info("Serialized form: {}", content);

		// verify that InstanceValidator still likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, content, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		// verify that the original and newly serialized match
		assertEquals(origContent, content);
	}

	private IValidationSupport getValidationSupport() {
		return new ValidationSupportChain(new DefaultProfileValidationSupport(ourCtx), new InMemoryTerminologyServerValidationSupport(ourCtx));
	}

	@Test
	public void testConceptMapElementsOrderXml() {
		final String origContent = "<ConceptMap xmlns=\"http://hl7.org/fhir\"><id value=\"x1\"/><url value=\"http://testing.org\"/><status value=\"draft\"/><sourceUri value=\"http://url1\"/></ConceptMap>";
		final IParser parser = ourCtx.newXmlParser();
		IValidationSupport validationSupport = getValidationSupport();

		// verify that InstanceValidator likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, origContent, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		ConceptMap fhirObj = parser.parseResource(ConceptMap.class, origContent);
		String content = parser.encodeResourceToString(fhirObj);
		ourLog.info("Serialized form: {}", content);

		// verify that InstanceValidator still likes the format
		{
			IValidationContext<IBaseResource> validationCtx = ValidationContext.forText(ourCtx, content, null);
			new FhirInstanceValidator(validationSupport).validateResource(validationCtx);
			ValidationResult result = validationCtx.toResult();
			for (SingleValidationMessage msg : result.getMessages()) {
				ourLog.info("{}", msg);
			}
			assertEquals(0, result.getMessages().size());
		}

		// verify that the original and newly serialized match
		assertEquals(origContent, content);
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
