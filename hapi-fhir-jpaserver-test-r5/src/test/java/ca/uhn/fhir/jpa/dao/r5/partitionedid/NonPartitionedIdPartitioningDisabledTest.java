package ca.uhn.fhir.jpa.dao.r5.partitionedid;

import org.junit.jupiter.api.Nested;

/**
 * This is a test verifying that we emit the right SQL for HAPI FHIR running in
 * full legacy mode - No partitioning, no partition IDs in PKs.
 */
public class NonPartitionedIdPartitioningDisabledTest extends BasePartitionedIdJpaR5Test {

	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(NonPartitionedIdPartitioningDisabledTest.this, new PartitionSelectorInterceptor(), false, false);
		}
	}

}
