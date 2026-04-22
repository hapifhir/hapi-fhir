package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import ca.uhn.fhir.jpa.util.TestPartitionSelectorInterceptor;
import org.junit.jupiter.api.Nested;

/**
 * This is a test verifying that we emit the right SQL for HAPI FHIR running in
 * full legacy mode - No partitioning, no partition IDs in PKs.
 */
public class DbpmDisabledPartitioningDisabledTest extends BaseDbpmResourceProviderR5Test {

	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(DbpmDisabledPartitioningDisabledTest.this, new TestPartitionSelectorInterceptor(), false, false);
		}
	}

}
