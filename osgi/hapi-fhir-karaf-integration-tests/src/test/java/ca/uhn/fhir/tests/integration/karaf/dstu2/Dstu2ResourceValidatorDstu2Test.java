package ca.uhn.fhir.tests.integration.karaf.dstu2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ConditionVerificationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.tests.integration.karaf.ValidationConstants;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SchemaBaseValidator;
import ca.uhn.fhir.validation.ValidationFailureException;
import ca.uhn.fhir.validation.ValidationResult;
import ca.uhn.fhir.validation.schematron.SchematronBaseValidator;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.HAPI_FHIR_VALIDATION_DSTU2;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.KARAF;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.WRAP;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;


/**
 * Useful docs about this test: https://ops4j1.jira.com/wiki/display/paxexam/FAQ
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class Dstu2ResourceValidatorDstu2Test {

	private FhirContext ourCtx = FhirContext.forDstu2();
	private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Dstu2ResourceValidatorDstu2Test.class);

   @Configuration
   public Option[] config() throws IOException {
      return options(
      	KARAF.option(),
			WRAP.option(),
			HAPI_FHIR_VALIDATION_DSTU2.option(),
			mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.hamcrest").versionAsInProject(),
			when(false)
         	.useOptions(
            	debugConfiguration("5005", true))
      );
   }

	private FhirValidator createFhirValidator() {
		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(ValidationConstants.SCHEMATRON_ENABLED);
		return val;
	}

	private String logOperationOutcome(ValidationResult result) {
		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(encoded);
		return encoded;
	}

	/**
	 * See issue #50
	 */
	@Test()
	public void testOutOfBoundsDate() {
		Patient p = new Patient();
		p.setBirthDate(new DateDt("2000-12-31"));

		// Put in an invalid date
		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new StrictErrorHandler());

		String encoded = parser.setPrettyPrint(true).encodeResourceToString(p).replace("2000-12-31", "2000-15-31");
		ourLog.info(encoded);

		assertThat(encoded, StringContains.containsString("2000-15-31"));

		ValidationResult result = ourCtx.newValidator().validateWithResult(encoded);
		String resultString = parser.setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(resultString);

		assertEquals(2, ((OperationOutcome)result.toOperationOutcome()).getIssue().size());
		assertThat(resultString, StringContains.containsString("cvc-pattern-valid"));

		try {
			parser.parseResource(encoded);
			fail();
		} catch (DataFormatException e) {
			assertEquals("DataFormatException at [[row,col {unknown-source}]: [2,4]]: Invalid attribute value \"2000-15-31\": Invalid date/time format: \"2000-15-31\"", e.getMessage());
		}
	}

	@Test
	public void testSchemaBundleValidatorIsSuccessful() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("bundle-example.json"), StandardCharsets.UTF_8);
		Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, res);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b));

		FhirValidator val = createFhirValidator();

		ValidationResult result = val.validateWithResult(b);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();

		assertTrue(result.toString(), result.isSuccessful());
		assertNotNull(operationOutcome);
		assertEquals(1, operationOutcome.getIssue().size());
	}

	/**
	 * Make sure that the elements that appear in all resources (meta, language, extension, etc)
	 * all appear in the correct order
	 */
	@Test
	public void testValidateResourceWithResourceElements() {
		TestPatientFor327 patient = new TestPatientFor327();
		patient.setBirthDate(new Date(), TemporalPrecisionEnum.DAY);
		patient.setId("123");
		patient.getText().setDiv("<div>FOO</div>");
		patient.getText().setStatus(NarrativeStatusEnum.GENERATED);
		patient.getLanguage().setValue("en");
		patient.addUndeclaredExtension(true, "http://foo").setValue(new StringDt("MOD"));
		ResourceMetadataKeyEnum.UPDATED.put(patient, new InstantDt(new Date()));

		List<ResourceReferenceDt> conditions = new ArrayList<ResourceReferenceDt>();
		Condition condition = new Condition();
		condition.getPatient().setReference("Patient/123");
		condition.addBodySite().setText("BODY SITE");
		condition.getCode().setText("CODE");
		condition.setVerificationStatus(ConditionVerificationStatusEnum.CONFIRMED);
		conditions.add(new ResourceReferenceDt(condition));
		patient.setCondition(conditions);
		patient.addIdentifier().setSystem("http://foo").setValue("123");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		FhirValidator val = createFhirValidator();
		ValidationResult result = val.validateWithResult(encoded);

		String messageString = logOperationOutcome(result);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("No issues"));

	}

	/**
	 * See
	 * https://groups.google.com/d/msgid/hapi-fhir/a266083f-6454-4cf0-a431-c6500f052bea%40googlegroups.com?utm_medium=
	 * email&utm_source=footer
	 */
	@Test
	public void testValidateWithExtensionsJson() {
		PatientProfileDstu2 myPatient = new PatientProfileDstu2();
		myPatient.setColorPrimary(new CodeableConceptDt("http://example.com#animalColor", "furry-grey"));
		myPatient.setColorSecondary(new CodeableConceptDt("http://example.com#animalColor", "furry-white"));
		myPatient.setOwningOrganization(new ResourceReferenceDt("Organization/2.25.79433498044103547197447759549862032393"));
		myPatient.addName().addFamily("FamilyName");
		myPatient.addUndeclaredExtension(new ExtensionDt().setUrl("http://foo.com/example").setValue(new StringDt("String Extension")));

		IParser p = FhirContext.forDstu2().newJsonParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(myPatient);
		ourLog.info(messageString);

		//@formatter:off
		assertThat(messageString, stringContainsInOrder(
			"meta",
			"String Extension",
			"Organization/2.25.79433498044103547197447759549862032393",
			"furry-grey",
			"furry-white",
			"FamilyName"
		));
		assertThat(messageString, not(stringContainsInOrder(
			"extension",
			"meta"
		)));
		//@formatter:on

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		if (ValidationConstants.SCHEMATRON_ENABLED) {
			val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
		}

		ValidationResult result = val.validateWithResult(messageString);

		logOperationOutcome(result);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("valueReference"));
		assertThat(messageString, not(containsString("valueResource")));
	}

	private Matcher<? super String> stringContainsInOrder(java.lang.String... substrings) {
		return Matchers.stringContainsInOrder(Arrays.asList(substrings));
	}

	/**
	 * See
	 * https://groups.google.com/d/msgid/hapi-fhir/a266083f-6454-4cf0-a431-c6500f052bea%40googlegroups.com?utm_medium=
	 * email&utm_source=footer
	 */
	@Test
	public void testValidateWithExtensionsXml() {
		PatientProfileDstu2 myPatient = new PatientProfileDstu2();
		myPatient.setColorPrimary(new CodeableConceptDt("http://example.com#animalColor", "furry-grey"));
		myPatient.setColorSecondary(new CodeableConceptDt("http://example.com#animalColor", "furry-white"));
		myPatient.setOwningOrganization(new ResourceReferenceDt("Organization/2.25.79433498044103547197447759549862032393"));
		myPatient.addName().addFamily("FamilyName");
		myPatient.addUndeclaredExtension(new ExtensionDt().setUrl("http://foo.com/example").setValue(new StringDt("String Extension")));

		IParser p = FhirContext.forDstu2().newXmlParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(myPatient);
		ourLog.info(messageString);

		//@formatter:off
		assertThat(messageString, stringContainsInOrder(
			"meta",
			"Organization/2.25.79433498044103547197447759549862032393",
			"furry-grey",
			"furry-white",
			"String Extension",
			"FamilyName"
		));
		assertThat(messageString, not(stringContainsInOrder(
			"extension",
			"meta"
		)));
		assertThat(messageString, containsString("url=\"http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#animal-colorSecondary\""));
		assertThat(messageString, containsString("url=\"http://foo.com/example\""));
		//@formatter:on

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		if (ValidationConstants.SCHEMATRON_ENABLED) {
			val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
		}

		ValidationResult result = val.validateWithResult(messageString);

		logOperationOutcome(result);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("valueReference"));
		assertThat(messageString, not(containsString("valueResource")));
	}

	@ResourceDef(name = "Patient")
	public static class TestPatientFor327 extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(name = "testCondition")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "testCondition", definedLocally = true, isModifier = false)
		private List<ResourceReferenceDt> testConditions = null;

		public List<ResourceReferenceDt> getConditions() {
			return this.testConditions;
		}

		public void setCondition(List<ResourceReferenceDt> ref) {
			this.testConditions = ref;
		}
	}
}
