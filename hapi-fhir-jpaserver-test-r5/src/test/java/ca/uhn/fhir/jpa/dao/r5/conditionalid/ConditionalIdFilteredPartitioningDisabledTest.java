package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import org.junit.jupiter.api.Nested;

/**
 * This is a test verifying that we emit the right SQL for HAPI FHIR running in
 * full legacy mode - No partitioning, no partition IDs in PKs.
 */
public class ConditionalIdFilteredPartitioningDisabledTest extends BaseConditionalIdJpaR5Test {

	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(ConditionalIdFilteredPartitioningDisabledTest.this, new PartitionSelectorInterceptor(), false, false);
		}
	}

}
