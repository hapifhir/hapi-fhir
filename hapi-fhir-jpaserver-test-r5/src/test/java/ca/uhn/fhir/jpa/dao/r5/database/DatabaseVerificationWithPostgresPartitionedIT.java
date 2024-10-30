package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.embedded.PostgresEmbeddedDatabase;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.util.TestPartitionSelectorInterceptor;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.INCLUDE_STAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {
	DatabaseVerificationWithPostgresPartitionedIT.TestConfig.class,
	BaseDatabaseVerificationIT.TestConfig.class,
	TestDaoSearch.Config.class
})
@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class DatabaseVerificationWithPostgresPartitionedIT extends BaseJpaTest implements ITestDataBuilder {
	private final TestPartitionSelectorInterceptor myPartitionInterceptor = new TestPartitionSelectorInterceptor();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private IPartitionLookupSvc myPartitionLookupSvc;
	@Autowired
	private IFhirResourceDaoPatient myPatientDao;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myPartitionInterceptor);
	}

	@Test
	public void testSearchWithIncludeStar() {
		// Setup
		createPartition(1);
		createPartition(2);

		myPartitionInterceptor.setNextPartitionId(1);
		IIdType orgId1 = createOrganization(withName("ORG 1")).toUnqualifiedVersionless();
		IIdType id1 = createPatient(withFamily("Simpson"), withGiven("Homer"), withOrganization(orgId1)).toUnqualifiedVersionless();
		myPartitionInterceptor.setNextPartitionId(2);
		IIdType orgId2 = createOrganization(withName("ORG 2")).toUnqualifiedVersionless();
		createPatient(withFamily("Griffon"), withGiven("Peter"), withOrganization(orgId2));

		logAllResources();

		// Test
		myPartitionInterceptor.setNextPartitionId(1);
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.addInclude(new Include(INCLUDE_STAR));
		IBundleProvider outcome = myPatientDao.search(params, new SystemRequestDetails());

		// Verify
		List<String> actualIds = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(actualIds).asList().containsExactly(id1.getValue(), orgId1.getValue());
	}

	private void createPartition(int partitionId) {
		PartitionEntity partition = new PartitionEntity();
		partition.setId(partitionId);
		partition.setName("PARTITION_" + partitionId);
		myPartitionLookupSvc.createPartition(partition, new SystemRequestDetails());
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource)).create(theResource, new SystemRequestDetails()).getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		return myDaoRegistry.getResourceDao(myFhirContext.getResourceType(theResource)).update(theResource, new SystemRequestDetails()).getId();
	}

	@Nonnull
	private static PostgresEmbeddedDatabase createPartitionedPostgresDatabase() {
		PostgresEmbeddedDatabase retVal = new PostgresEmbeddedDatabase();

		String initScript = ClasspathUtil.loadResource("/schema/postgres-partitioned.sql");
		retVal.executeSqlAsBatch(initScript);

		return retVal;
	}

	@Configuration
	public static class TestConfig {

		@Bean
		public BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject jpaDatabaseParamObject() {
			return new BaseDatabaseVerificationIT.JpaDatabaseContextConfigParamObject(
				createPartitionedPostgresDatabase(),
				HapiFhirPostgresDialect.class.getName()
			);
		}

		@Bean
		public PartitionSettingsConfigurer partitionSettingsPrimary(PartitionSettings thePartitionSettings) {
			return new PartitionSettingsConfigurer(thePartitionSettings);
		}

	}

	public static class PartitionSettingsConfigurer {

		public PartitionSettingsConfigurer(PartitionSettings thePartitionSettings) {
			assert thePartitionSettings != null;

			thePartitionSettings.setPartitioningEnabled(true);
			thePartitionSettings.setPartitionIdsInPrimaryKeys(true);
			thePartitionSettings.setDefaultPartitionId(0);
		}
	}

}
