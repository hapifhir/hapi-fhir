package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.springframework.test.context.TestPropertySource;

/**
 * This is a test verifying that we emit the right SQL when operating in new
 * partition mode - Partition IDs are a part of the PKs of entities, and are
 * used in joins etc.
 */
@TestPropertySource(properties = {
	JpaConstants.HAPI_INCLUDE_PARTITION_IDS_IN_PKS + "=true"
})
public class ConditionalIdKeptPartitioningEnabledTest extends BaseConditionalIdJpaR5Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);

		registerPartitionInterceptorAndCreatePartitions();
	}


	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(ConditionalIdKeptPartitioningEnabledTest.this, myPartitionSelectorInterceptor, true, true);
		}
	}


}
