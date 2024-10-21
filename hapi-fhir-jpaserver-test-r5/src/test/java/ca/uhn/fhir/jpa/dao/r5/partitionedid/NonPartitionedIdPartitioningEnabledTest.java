package ca.uhn.fhir.jpa.dao.r5.partitionedid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

/**
 * This is a test verifying that we emit the right SQL when running in
 * legacy partition mode - Partition IDs are in use, but they aren't
 * included in primary keys or joins.
 */
public class NonPartitionedIdPartitioningEnabledTest extends BasePartitionedIdJpaR5Test {

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
			super(NonPartitionedIdPartitioningEnabledTest.this, myPartitionSelectorInterceptor, true, false);
		}
	}


}
