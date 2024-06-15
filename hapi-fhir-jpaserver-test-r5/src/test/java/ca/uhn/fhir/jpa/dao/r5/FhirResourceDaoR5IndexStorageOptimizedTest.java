package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.search.reindex.InstanceReindexServiceImplR5Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

/**
 * R5 Test cases with enabled {@link StorageSettings#isIndexStorageOptimized()}
 */
public class FhirResourceDaoR5IndexStorageOptimizedTest {

	@Nested
	public class IndexStorageOptimizedFhirSystemDaoTransactionR5Test extends FhirSystemDaoTransactionR5Test {
		@BeforeEach
		public void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}

		@AfterEach
		public void cleanUp() {
			myStorageSettings.setIndexStorageOptimized(false);
		}
	}

	@Nested
	public class IndexStorageOptimizedInstanceReindexServiceImplR5Test extends InstanceReindexServiceImplR5Test {
		@BeforeEach
		public void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}

		@AfterEach
		public void cleanUp() {
			myStorageSettings.setIndexStorageOptimized(false);
		}
	}

	@Nested
	public class IndexStorageOptimizedUpliftedRefchainsAndChainedSortingR5Test extends UpliftedRefchainsAndChainedSortingR5Test {
		@BeforeEach
		public void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}

		@AfterEach
		public void cleanUp() {
			myStorageSettings.setIndexStorageOptimized(false);
		}
	}
}
