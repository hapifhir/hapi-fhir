package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is a test verifying that we emit the right SQL when running in
 * legacy partition mode - Partition IDs are in use, but they aren't
 * included in primary keys or joins.
 */
public class ConditionalIdFilteredPartitioningEnabledTest extends BaseJpaR5Test {

	public static final int PARTITION_1 = 1;
	public static final int PARTITION_2 = 2;
	private final PartitionSelectorInterceptor myPartitionSelectorInterceptor = new PartitionSelectorInterceptor();

	@Autowired
	private IPartitionLookupSvc myPartitionConfigSvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);

		assertFalse(myInterceptorRegistry.hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_READ), ()->myInterceptorRegistry.getAllRegisteredInterceptors().toString());
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
			super(ConditionalIdFilteredPartitioningEnabledTest.this, myPartitionSelectorInterceptor, true, false);
		}
	}


}
