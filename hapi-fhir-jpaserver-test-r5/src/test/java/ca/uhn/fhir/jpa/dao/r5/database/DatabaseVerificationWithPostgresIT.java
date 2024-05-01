package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {
	DatabaseVerificationWithPostgresIT.TestConfig.class,
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class
})
public class DatabaseVerificationWithPostgresIT extends BaseDatabaseVerificationIT {

	@Configuration
	public static class TestConfig {
		@Bean
		public JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new JpaDatabaseContextConfigParamObject(
				new PostgresEmbeddedDatabase(),
				HapiFhirPostgresDialect.class.getName()
			);
		}
	}

	@Autowired
	DaoTestDataBuilder myDataBuilder;
	@Autowired
	TestDaoSearch myTestDaoSearch;


	@Test
	void testChainedSort() {

		IIdType practitionerId = myDataBuilder.createPractitioner(myDataBuilder.withFamily("Jones"));

		String id1 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithy")).getIdPart();
		String id2 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithwick")).getIdPart();
		String id3 = myDataBuilder.createPatient(
			myDataBuilder.withFamily("Smith"),
			myDataBuilder.withReference("generalPractitioner", practitionerId)).getIdPart();

		IBundleProvider iBundleProvider = myTestDaoSearch.searchForBundleProvider("Patient?_total=ACCURATE&_sort=Practitioner:general-practitioner.family");
		assertEquals(3, iBundleProvider.size());
		List<IBaseResource> allResources = iBundleProvider.getAllResources();
		assertEquals(3, iBundleProvider.size());
		assertEquals(3, allResources.size());
	}


}
