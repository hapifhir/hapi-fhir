package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.hapi.fhir.sql.hibernatesvc.HapiHibernateDialectSettingsService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is a test verifying that we emit the right SQL for HAPI FHIR running in
 * full legacy mode - No partitioning, no partition IDs in PKs.
 */
public class ConditionalIdFilteredPartitioningDisabledTest extends BaseJpaR5Test {

	@Test
	public void testTrimConditionalIdsFromPrimaryKeys() {
		assertTrue(HapiHibernateDialectSettingsService.getLastTrimConditionalIdsFromPrimaryKeysForUnitTest());
	}

	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(ConditionalIdFilteredPartitioningDisabledTest.this, new PartitionSelectorInterceptor(), false, false);
		}
	}

}
