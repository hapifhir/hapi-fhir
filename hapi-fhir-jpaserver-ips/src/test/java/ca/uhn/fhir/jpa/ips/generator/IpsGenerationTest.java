package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.provider.IpsOperationProvider;
import ca.uhn.fhir.jpa.ips.strategy.DefaultIpsGenerationStrategy;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {IpsGenerationTest.IpsConfig.class})
public class IpsGenerationTest extends BaseResourceProviderR4Test {

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
		validateDocument(outcome);
		assertEquals(117, output.getEntry().size());
		String patientId = findFirstEntryResource(output, Patient.class, 1).getId();
		assertThat(patientId, matchesPattern("urn:uuid:.*"));
		MedicationStatement medicationStatement = findFirstEntryResource(output, MedicationStatement.class, 2);
		assertEquals(patientId, medicationStatement.getSubject().getReference());
		assertNull(medicationStatement.getInformationSource().getReference());
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
		validateDocument(outcome);
		assertEquals(7, output.getEntry().size());
		String patientId = findFirstEntryResource(output, Patient.class, 1).getId();
		assertThat(patientId, matchesPattern("urn:uuid:.*"));
		assertEquals(patientId, findEntryResource(output, Condition.class, 0, 2).getSubject().getReference());
		assertEquals(patientId, findEntryResource(output, Condition.class, 1, 2).getSubject().getReference());
	}

	private void validateDocument(Bundle theOutcome) {
		FhirValidator validator = myFhirContext.newValidator();
		validator.registerValidatorModule(new FhirInstanceValidator(myFhirContext));
		ValidationResult validation = validator.validateWithResult(theOutcome);
		assertTrue(validation.isSuccessful(), () -> myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(validation.toOperationOutcome()));
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

}
