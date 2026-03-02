package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

/**
 * This is a test verifying that we emit the right SQL when running in
 * legacy partition mode with DEFAULT partition value of null (the default if
 * not configured otherwise) - Partition IDs are in use, but they aren't
 * included in primary keys or joins.
 */
public class DbpmDisabledPartitioningEnabledNullDefaultPartitionTest extends BaseDbpmResourceProviderR5Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(null);

		registerPartitionInterceptorAndCreatePartitions();
	}

	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(DbpmDisabledPartitioningEnabledNullDefaultPartitionTest.this, myPartitionSelectorInterceptor, true, false);
		}
	}


}
