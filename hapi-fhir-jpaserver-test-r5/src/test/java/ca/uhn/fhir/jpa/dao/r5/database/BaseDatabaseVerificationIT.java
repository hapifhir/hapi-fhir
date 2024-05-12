package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.fql.provider.HfqlRestProvider;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.jpa.provider.DiffProvider;
import ca.uhn.fhir.jpa.provider.JpaCapabilityStatementProvider;
import ca.uhn.fhir.jpa.provider.ProcessMessageProvider;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestR5Config;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
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
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_EVERYTHING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@ContextConfiguration(classes = {BaseDatabaseVerificationIT.TestConfig.class})
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

		assertThrows(ResourceGoneException.class, () -> myPatientDao.read(id, new SystemRequestDetails()));
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

        assertThat(values.toString(), values, containsInAnyOrder(expectedIds.toArray(new String[0])));
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
		private JpaEmbeddedDatabase myJpaEmbeddedDatabase;
		private String myDialect;

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

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource)).create(theResource, new SystemRequestDetails()).getId();
	}

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


