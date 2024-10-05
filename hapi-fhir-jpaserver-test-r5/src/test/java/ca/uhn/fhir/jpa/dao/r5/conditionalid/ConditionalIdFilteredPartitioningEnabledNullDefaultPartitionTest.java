package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static ca.uhn.fhir.jpa.dao.r5.conditionalid.ConditionalIdKeptPartitioningEnabledTest.PARTITION_1;
import static ca.uhn.fhir.jpa.dao.r5.conditionalid.ConditionalIdKeptPartitioningEnabledTest.PARTITION_2;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is a test verifying that we emit the right SQL when running in
 * legacy partition mode with DEFAULT partition value of null (the default if
 * not configured otherwise) - Partition IDs are in use, but they aren't
 * included in primary keys or joins.
 */
public class ConditionalIdFilteredPartitioningEnabledNullDefaultPartitionTest extends BaseJpaR5Test {

	private final PartitionSelectorInterceptor myPartitionSelectorInterceptor = new PartitionSelectorInterceptor();

	@Autowired
	private IPartitionLookupSvc myPartitionConfigSvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(null);

		myInterceptorRegistry.registerInterceptor(myPartitionSelectorInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PARTITION_1).setName("Partition_1"), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(PARTITION_2).setName("Partition_2"), null);
	}


	@Override
	@AfterEach
	protected void afterResetInterceptors() {
		super.afterResetInterceptors();
		myPartitionSettings.setPartitioningEnabled(false);
		myInterceptorRegistry.unregisterInterceptor(myPartitionSelectorInterceptor);
	}

	@Test
	public void testTrimConditionalIdsFromPrimaryKeys() {
		assertTrue(HapiHibernateDialectSettingsService.getLastTrimConditionalIdsFromPrimaryKeysForUnitTest());
	}

	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(ConditionalIdFilteredPartitioningEnabledNullDefaultPartitionTest.this, myPartitionSelectorInterceptor, true, false);
		}
	}


}
