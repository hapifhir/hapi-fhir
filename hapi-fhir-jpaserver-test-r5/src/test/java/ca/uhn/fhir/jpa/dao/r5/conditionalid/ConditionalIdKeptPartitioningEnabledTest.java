package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This is a test verifying that we emit the right SQL when operating in new
 * partition mode - Partition IDs are a part of the PKs of entities, and are
 * used in joins etc.
 */
@TestPropertySource(properties = {
	JpaConstants.HAPI_INCLUDE_PARTITION_IDS_IN_PKS + "=true"
})
public class ConditionalIdKeptPartitioningEnabledTest extends BaseJpaR5Test {

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
		assertFalse(HapiHibernateDialectSettingsService.getLastTrimConditionalIdsFromPrimaryKeysForUnitTest());
	}

	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(ConditionalIdKeptPartitioningEnabledTest.this, myPartitionSelectorInterceptor, true, true);
		}
	}


}
