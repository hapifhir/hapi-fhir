package ca.uhn.fhir.jpa.dao.r5.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestR5Config;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.RestfulServerConfigurerExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.VersionEnum;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_EVERYTHING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@ContextConfiguration(classes = {BaseDatabaseVerificationIT.TestConfig.class, TestDaoSearch.Config.class})
public abstract class BaseDatabaseVerificationIT extends BaseJpaTest implements ITestDataBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDatabaseVerificationIT.class);
	private static final String MIGRATION_TABLENAME = "MIGRATIONS";

	@Autowired
	EntityManagerFactory myEntityManagerFactory;

	@Autowired
	JpaEmbeddedDatabase myJpaEmbeddedDatabase;

	@Autowired
	IFhirResourceDaoPatient<Patient> myPatientDao;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	protected ResourceProviderFactory myResourceProviders;

	@Autowired
	private DatabaseBackedPagingProvider myPagingProvider;

	@Autowired
	TestDaoSearch myTestDaoSearch;

	@RegisterExtension
	protected RestfulServerExtension myServer = new RestfulServerExtension(FhirContext.forR5Cached());

	@RegisterExtension
	protected RestfulServerConfigurerExtension myServerConfigurer = new RestfulServerConfigurerExtension(() -> myServer)
		.withServerBeforeAll(s -> {
			s.registerProviders(myResourceProviders.createProviders());
			s.setDefaultResponseEncoding(EncodingEnum.JSON);
			s.setDefaultPrettyPrint(false);
			s.setPagingProvider(myPagingProvider);
		});


	@ParameterizedTest
	@ValueSource(ints = {10, 100000})
	public void testCreateRead(int theSize) {
		String name = StringUtils.leftPad("", theSize, "a");

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addName().setFamily(name);
		IIdType id = myPatientDao.create(patient, new SystemRequestDetails()).getId();

		Patient actual = myPatientDao.read(id, new SystemRequestDetails());
		assertEquals(name, actual.getName().get(0).getFamily());
	}


	@Test
	public void testDelete() {
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType id = myPatientDao.create(patient, new SystemRequestDetails()).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id, new SystemRequestDetails());

		assertThatExceptionOfType(ResourceGoneException.class).isThrownBy(() -> myPatientDao.read(id, new SystemRequestDetails()));
	}


	@Test
	public void testEverything() {
        Set<String> expectedIds = new HashSet<>();
        expectedIds.add(createPatient(withId("A"), withActiveTrue()).toUnqualifiedVersionless().getValue());
        for (int i = 0; i < 25; i++) {
            expectedIds.add(createObservation(withSubject("Patient/A")).toUnqualifiedVersionless().getValue());
        }

        IGenericClient client = myServer.getFhirClient();
		Bundle outcome = client
			.operation()
			.onInstanceVersion(new IdType("Patient/A"))
			.named(OPERATION_EVERYTHING)
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
        List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		while (outcome.getLink("next") != null) {
			outcome = client.loadPage().next(outcome).execute();
			values.addAll(toUnqualifiedVersionlessIdValues(outcome));
		}

		assertThat(values).as(values.toString()).containsExactlyInAnyOrder(expectedIds.toArray(new String[0]));
    }

	@ParameterizedTest
	@CsvSource(textBlock = """
			query string,			Patient?name=smith
			query date,				Observation?date=2021
			query token,			Patient?active=true
			sort string, 			Patient?_sort=name
			sort date, 				Observation?_sort=date
			sort token, 			Patient?_sort=active
			sort chained date, 		Observation?_sort=patient.birthdate
			sort chained string, 	Observation?_sort=patient.name
			sort chained qualified, Patient?_sort=Practitioner:general-practitioner.family
			sort chained token, 	Observation?_sort=patient.active
			""")
	void testSyntaxForVariousQueries(String theMessage, String theQuery) {
		assertDoesNotThrow(()->myTestDaoSearch.searchForBundleProvider(theQuery), theMessage);
	}

	@Configuration
	public static class TestConfig extends TestR5Config {

		@Autowired
		private JpaDatabaseContextConfigParamObject myJpaDatabaseContextConfigParamObject;

		@Override
		@Bean
		public DataSource dataSource() {
			DataSource dataSource = myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDataSource();

			HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(dataSource, myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType(), MIGRATION_TABLENAME);
			HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

			MigrationTaskList tasks = new HapiFhirJpaMigrationTasks(Set.of()).getAllTasks(VersionEnum.values());

			SchemaMigrator schemaMigrator = new SchemaMigrator(
				"HAPI FHIR", MIGRATION_TABLENAME, dataSource, new Properties(), tasks, hapiMigrationStorageSvc);
			schemaMigrator.setDriverType(myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType());

			ourLog.info("About to run migration...");
			schemaMigrator.createMigrationTableIfRequired();
			schemaMigrator.migrate();
			ourLog.info("Migration complete");


			return dataSource;
		}

		@Bean
		public JpaEmbeddedDatabase jpaEmbeddedDatabase(JpaDatabaseContextConfigParamObject theJpaDatabaseContextConfigParamObject) {
			return theJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase();
		}

		@Override
		protected Properties jpaProperties() {
			Properties retVal = super.jpaProperties();
			retVal.put("hibernate.hbm2ddl.auto", "none");
			retVal.put("hibernate.dialect", myJpaDatabaseContextConfigParamObject.getDialect());
			return retVal;
		}

	}

	public static class JpaDatabaseContextConfigParamObject {
		final JpaEmbeddedDatabase myJpaEmbeddedDatabase;
		final String myDialect;

		public JpaDatabaseContextConfigParamObject(JpaEmbeddedDatabase theJpaEmbeddedDatabase, String theDialect) {
			myJpaEmbeddedDatabase = theJpaEmbeddedDatabase;
			myDialect = theDialect;
		}

		public JpaEmbeddedDatabase getJpaEmbeddedDatabase() {
			return myJpaEmbeddedDatabase;
		}

		public String getDialect() {
			return myDialect;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource)).create(theResource, new SystemRequestDetails()).getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource)).update(theResource, new SystemRequestDetails()).getId();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

}


