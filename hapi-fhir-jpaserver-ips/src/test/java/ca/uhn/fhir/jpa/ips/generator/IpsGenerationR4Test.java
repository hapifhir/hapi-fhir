package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.provider.IpsOperationProvider;
import ca.uhn.fhir.jpa.ips.strategy.DefaultIpsGenerationStrategy;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This test uses a complete R4 JPA server as a backend and wires the
 * {@link IpsOperationProvider} into the REST server to test the end-to-end
 * IPS generation flow.
 */
@ContextConfiguration(classes = {IpsGenerationR4Test.IpsConfig.class})
public class IpsGenerationR4Test extends BaseResourceProviderR4Test {

	@Autowired
	private IpsOperationProvider myIpsOperationProvider;

	@BeforeEach
	public void beforeEach() {
		myServer.withServer(t -> t.registerProvider(myIpsOperationProvider));
	}

	@AfterEach
	public void afterEach() {
		myServer.withServer(t -> t.unregisterProvider(myIpsOperationProvider));
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC);
	}


	@Test
	public void testGenerateLargePatientSummary() {
		Bundle sourceData = ClasspathUtil.loadCompressedResource(myFhirContext, Bundle.class, "/large-patient-everything.json.gz");
		sourceData.setType(Bundle.BundleType.TRANSACTION);
		for (Bundle.BundleEntryComponent nextEntry : sourceData.getEntry()) {
			nextEntry.getRequest().setMethod(Bundle.HTTPVerb.PUT);
			nextEntry.getRequest().setUrl(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		Bundle outcome = mySystemDao.transaction(mySrd, sourceData);
		ourLog.info("Created {} resources", outcome.getEntry().size());

		Bundle output = myClient
			.operation()
			.onInstance("Patient/f15d2419-fbff-464a-826d-0afe8f095771")
			.named(JpaConstants.OPERATION_SUMMARY)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Verify
		validateDocument(output);
		assertEquals(117, output.getEntry().size());
		String patientId = findFirstEntryResource(output, Patient.class, 1).getId();
		assertThat(patientId, matchesPattern("urn:uuid:.*"));
		MedicationStatement medicationStatement = findFirstEntryResource(output, MedicationStatement.class, 2);
		assertEquals(patientId, medicationStatement.getSubject().getReference());
		assertNull(medicationStatement.getInformationSource().getReference());

		List<String> sectionTitles = extractSectionTitles(output);
		assertThat(sectionTitles.toString(), sectionTitles, contains("Allergies and Intolerances", "Medication List", "Problem List", "History of Immunizations", "Diagnostic Results"));
	}

	@Test
	public void testGenerateLargePatientSummary2() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		Bundle sourceData = ClasspathUtil.loadCompressedResource(myFhirContext, Bundle.class, "/large-patient-everything-2.json.gz");
		sourceData.setType(Bundle.BundleType.TRANSACTION);
		for (Bundle.BundleEntryComponent nextEntry : sourceData.getEntry()) {
			nextEntry.getRequest().setMethod(Bundle.HTTPVerb.PUT);
			nextEntry.getRequest().setUrl(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		Bundle outcome = mySystemDao.transaction(mySrd, sourceData);
		ourLog.info("Created {} resources", outcome.getEntry().size());

		Bundle output = myClient
			.operation()
			.onInstance("Patient/11439250")
			.named(JpaConstants.OPERATION_SUMMARY)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Verify
		assertEquals(74, output.getEntry().size());
	}

	@Test
	public void testGenerateLargePatientSummary3() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		Bundle sourceData = ClasspathUtil.loadCompressedResource(myFhirContext, Bundle.class, "/large-patient-everything-3.json.gz");
		sourceData.setType(Bundle.BundleType.TRANSACTION);
		for (Bundle.BundleEntryComponent nextEntry : sourceData.getEntry()) {
			nextEntry.getRequest().setMethod(Bundle.HTTPVerb.PUT);
			nextEntry.getRequest().setUrl(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		Bundle outcome = mySystemDao.transaction(mySrd, sourceData);
		ourLog.info("Created {} resources", outcome.getEntry().size());

		Bundle output = myClient
			.operation()
			.onInstance("Patient/nl-core-Patient-01")
			.named(JpaConstants.OPERATION_SUMMARY)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Verify
		assertEquals(80, output.getEntry().size());
	}

	@Test
	public void testGenerateTinyPatientSummary() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		Bundle sourceData = ClasspathUtil.loadCompressedResource(myFhirContext, Bundle.class, "/tiny-patient-everything.json.gz");
		sourceData.setType(Bundle.BundleType.TRANSACTION);
		for (Bundle.BundleEntryComponent nextEntry : sourceData.getEntry()) {
			nextEntry.getRequest().setMethod(Bundle.HTTPVerb.PUT);
			nextEntry.getRequest().setUrl(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		Bundle outcome = mySystemDao.transaction(mySrd, sourceData);
		ourLog.info("Created {} resources", outcome.getEntry().size());

		Bundle output = myClient
			.operation()
			.onInstance("Patient/5342998")
			.named(JpaConstants.OPERATION_SUMMARY)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Verify
		validateDocument(output);
		assertEquals(7, output.getEntry().size());
		String patientId = findFirstEntryResource(output, Patient.class, 1).getId();
		assertThat(patientId, matchesPattern("urn:uuid:.*"));
		assertEquals(patientId, findEntryResource(output, Condition.class, 0, 2).getSubject().getReference());
		assertEquals(patientId, findEntryResource(output, Condition.class, 1, 2).getSubject().getReference());

		List<String> sectionTitles = extractSectionTitles(output);
		assertThat(sectionTitles.toString(), sectionTitles, contains("Allergies and Intolerances", "Medication List", "Problem List"));
	}

	/**
	 * Default strategy should order immunizations alphabetically
	 */
	@Test
	public void testImmunizationOrder() {
		// Setup

		createPatient(withId("PT1"), withFamily("Simpson"), withGiven("Homer"));

		// Create some immunizations out of order
		Immunization i;
		i = new Immunization();
		i.setPatient(new Reference("Patient/PT1"));
		i.setOccurrence(new DateTimeType("2010-01-01T00:00:00Z"));
		i.setVaccineCode(new CodeableConcept().setText("Vax 2010"));
		myImmunizationDao.create(i, mySrd);
		i = new Immunization();
		i.setPatient(new Reference("Patient/PT1"));
		i.setOccurrence(new DateTimeType("2005-01-01T00:00:00Z"));
		i.setVaccineCode(new CodeableConcept().setText("Vax 2005"));
		myImmunizationDao.create(i, mySrd);
		i = new Immunization();
		i.setPatient(new Reference("Patient/PT1"));
		i.setOccurrence(new DateTimeType("2015-01-01T00:00:00Z"));
		i.setVaccineCode(new CodeableConcept().setText("Vax 2015"));
		myImmunizationDao.create(i, mySrd);

		// Test

		Bundle output = myClient
			.operation()
			.onInstance("Patient/PT1")
			.named(JpaConstants.OPERATION_SUMMARY)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
		ourLog.info("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		Composition composition = findCompositionSectionByDisplay(output, "History of Immunization Narrative");
		// Should be newest first
		assertThat(composition.getText().getDivAsString(), stringContainsInOrder(
			"Vax 2015", "Vax 2010", "Vax 2005"
		));

		List<String> resourceDates = output
			.getEntry()
			.stream()
			.filter(t -> t.getResource() instanceof Immunization)
			.map(t -> (Immunization) t.getResource())
			.map(t -> t.getOccurrenceDateTimeType().getValueAsString().substring(0, 4))
			.collect(Collectors.toList());
		assertThat(resourceDates, contains("2015", "2010", "2005"));
	}


	@Nonnull
	private static Composition findCompositionSectionByDisplay(Bundle output, String theDisplay) {
		Composition composition = (Composition) output.getEntry().get(0).getResource();
		Composition.SectionComponent section = composition
			.getSection()
			.stream()
			.filter(t -> t.getCode().getCoding().get(0).getDisplay().equals(theDisplay))
			.findFirst()
			.orElseThrow();
		return composition;
	}


	@Nonnull
	private static List<String> extractSectionTitles(Bundle outcome) {
		Composition composition = (Composition) outcome.getEntry().get(0).getResource();
		List<String> sectionTitles = composition
			.getSection()
			.stream()
			.map(Composition.SectionComponent::getTitle)
			.toList();
		return sectionTitles;
	}

	private void validateDocument(Bundle theOutcome) {
		FhirValidator validator = myFhirContext.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(myFhirContext);
		instanceValidator.setValidationSupport(new ValidationSupportChain(new IpsTerminologySvc(), myFhirContext.getValidationSupport()));
		validator.registerValidatorModule(instanceValidator);
		ValidationResult validation = validator.validateWithResult(theOutcome);
		assertTrue(validation.isSuccessful(), () -> myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(validation.toOperationOutcome()));

		// Make sure that all refs have been replaced with UUIDs
		List<ResourceReferenceInfo> references = myFhirContext.newTerser().getAllResourceReferences(theOutcome);
		for (IBaseResource next : myFhirContext.newTerser().getAllEmbeddedResources(theOutcome, true)) {
			references.addAll(myFhirContext.newTerser().getAllResourceReferences(next));
		}
		for (ResourceReferenceInfo next : references) {
			if (!next.getResourceReference().getReferenceElement().getValue().startsWith("urn:uuid:")) {
				fail(next.getName());
			}
		}
	}

	@Configuration
	public static class IpsConfig {

		@Bean
		public IIpsGenerationStrategy ipsGenerationStrategy() {
			return new DefaultIpsGenerationStrategy();
		}

		@Bean
		public IIpsGeneratorSvc ipsGeneratorSvc(FhirContext theFhirContext, IIpsGenerationStrategy theGenerationStrategy, DaoRegistry theDaoRegistry) {
			return new IpsGeneratorSvcImpl(theFhirContext, theGenerationStrategy, theDaoRegistry);
		}

		@Bean
		public IpsOperationProvider ipsOperationProvider(IIpsGeneratorSvc theIpsGeneratorSvc) {
			return new IpsOperationProvider(theIpsGeneratorSvc);
		}


	}

	@SuppressWarnings("unchecked")
	private static <T extends IBaseResource> T findFirstEntryResource(Bundle theBundle, Class<T> theType, int theExpectedCount) {
		return findEntryResource(theBundle, theType, 0, theExpectedCount);
	}

	@SuppressWarnings("unchecked")
	static <T extends IBaseResource> T findEntryResource(Bundle theBundle, Class<T> theType, int index, int theExpectedCount) {
		List<Resource> resources = theBundle
			.getEntry()
			.stream()
			.map(Bundle.BundleEntryComponent::getResource)
			.filter(r -> theType.isAssignableFrom(r.getClass()))
			.toList();
		assertEquals(theExpectedCount, resources.size());
		return (T) resources.get(index);
	}

	/**
	 * This is a little fake terminology server that hardcodes the IPS terminology
	 * needed to validate these documents. This way we don't need to depend on a huge
	 * package.
	 */
	private class IpsTerminologySvc implements IValidationSupport {
		@Override
		public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
			return true;
		}

		@Nullable
		@Override
		public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
			if ("http://loinc.org".equals(theCodeSystem)) {
				if ("60591-5".equals(theCode)) {
					return new CodeValidationResult().setCode(theCode);
				}
			}
			if ("http://snomed.info/sct".equals(theCodeSystem)) {
				if ("14657009".equals(theCode) || "255604002".equals(theCode)) {
					return new CodeValidationResult().setCode(theCode);
				}
			}
			return null;
		}

		@Nullable
		@Override
		public IBaseResource fetchCodeSystem(String theSystem) {
			if ("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips".equals(theSystem)) {
				CodeSystem cs = new CodeSystem();
				cs.setUrl("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips");
				cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
				cs.addConcept().setCode("no-allergy-info");
				cs.addConcept().setCode("no-medication-info");
				cs.addConcept().setCode("no-known-allergies");
				return cs;
			}
			return null;
		}

		@Override
		public FhirContext getFhirContext() {
			return myFhirContext;
		}
	}
}
