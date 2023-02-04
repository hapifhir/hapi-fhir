package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.batch2.jobs.models.BatchResourceId;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.ips.api.IIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.provider.IpsOperationProvider;
import ca.uhn.fhir.jpa.ips.strategy.DefaultIpsGenerationStrategy;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

		assertEquals(37, output.getEntry().size());
		String patientId = findFirstEntryResource(output, Patient.class).getId();
		assertThat(patientId, matchesPattern("urn:uuid:.*"));
		MedicationStatement medicationStatement = findFirstEntryResource(output, MedicationStatement.class);
		assertEquals(patientId, medicationStatement.getSubject().getReference());
		assertNull(medicationStatement.getInformationSource().getReference());
	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> T findFirstEntryResource(Bundle theBundle, Class<T> theType) {
		return (T) theBundle
			.getEntry()
			.stream()
			.filter(t -> theType.isAssignableFrom(t.getResource().getClass()))
			.findFirst()
			.orElseThrow()
			.getResource();
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


}
