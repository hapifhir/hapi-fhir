package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.migrate.HapiMigrationStorageSvc;
import ca.uhn.fhir.jpa.migrate.MigrationTaskList;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestR5Config;
import ca.uhn.fhir.jpa.util.TestPartitionSelectorInterceptor;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.server.RestfulServerConfigurerExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.VersionEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.Set;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.HAPI_DATABASE_PARTITION_MODE;

@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@ContextConfiguration(classes = {BaseDatabasePartitionModeIT.TestConfig.class, TestDaoSearch.Config.class})
@TestPropertySource(properties = {
	HAPI_DATABASE_PARTITION_MODE + "=true"
})
public abstract class BaseDatabasePartitionModeIT extends BaseJpaTest implements ITestDataBuilder, TuplePredicateSearchTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDatabasePartitionModeIT.class);
	private static final String MIGRATION_TABLENAME = "MIGRATIONS";
	private static final int PARTITION_ID = 1;

	@Autowired
	IFhirResourceDaoPatient<Patient> myPatientDao;

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	protected DaoRegistry myDaoRegistry;

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

	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;

	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;

	private final TestPartitionSelectorInterceptor myPartitionInterceptor = new TestPartitionSelectorInterceptor();

	@BeforeEach
	void beforeEach() {
		myExpungeEverythingService.expungeEverything(new SystemRequestDetails());
		myPartitionSettings.setDatabasePartitionMode(true);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);
		createPartitionIfAbsent(PARTITION_ID);
		myPartitionInterceptor.setNextPartitionId(PARTITION_ID);
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);
	}

	private void createPartitionIfAbsent(int thePartitionId) {
		try {
			myPartitionLookupSvc.getPartitionById(thePartitionId);
		} catch (ResourceNotFoundException e) {
			myPartitionLookupSvc.createPartition(
				new PartitionEntity().setId(thePartitionId).setName("PARTITION_" + thePartitionId), null);
		}
	}

	protected SystemRequestDetails requestDetailsWithPartition() {
		SystemRequestDetails details = new SystemRequestDetails();
		details.setRequestPartitionId(RequestPartitionId.fromPartitionId(PARTITION_ID));
		return details;
	}

	@Override
	public TuplePredicateSearchTest.Context getTuplePredicateSearchTestContext() {
		return new TuplePredicateSearchTest.Context(
			myStorageSettings,
			myServer
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource))
			.create(theResource, requestDetailsWithPartition()).getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource))
			.update(theResource, requestDetailsWithPartition()).getId();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Configuration
	public static class TestConfig extends TestR5Config {

		@Autowired
		private JpaDatabaseContextConfigParamObject myJpaDatabaseContextConfigParamObject;

		@Override
		@Bean
		public DataSource dataSource() {
			DataSource dataSource = myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDataSource();

			HapiMigrationDao hapiMigrationDao = new HapiMigrationDao(
				dataSource,
				myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType(),
				MIGRATION_TABLENAME);
			HapiMigrationStorageSvc hapiMigrationStorageSvc = new HapiMigrationStorageSvc(hapiMigrationDao);

			Set<String> flags = Set.of(HapiFhirJpaMigrationTasks.FlagEnum.DB_PARTITION_MODE.getCommandLineValue());

			MigrationTaskList tasks = new HapiFhirJpaMigrationTasks(flags).getAllTasks(VersionEnum.values());

			SchemaMigrator schemaMigrator = new SchemaMigrator(
				"HAPI FHIR", MIGRATION_TABLENAME, dataSource, new Properties(), tasks, hapiMigrationStorageSvc);
			schemaMigrator.setDriverType(
				myJpaDatabaseContextConfigParamObject.getJpaEmbeddedDatabase().getDriverType());

			ourLog.info("About to run migration...");
			schemaMigrator.createMigrationTableIfRequired();
			schemaMigrator.migrate();
			ourLog.info("Migration complete");

			return dataSource;
		}

		@Bean
		public JpaEmbeddedDatabase jpaEmbeddedDatabase(
				JpaDatabaseContextConfigParamObject theJpaDatabaseContextConfigParamObject) {
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
}
