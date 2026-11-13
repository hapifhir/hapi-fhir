package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.ContextConfiguration;

/**
 * Runs {@link LargeCodeSystemStorageTimingPostgresIT} with full-text indexing switched off. Run it the same way,
 * with this class's name in {@code -Dtest=}.
 */
// Created by claude-fable-5-2
@Disabled("Performance test — run manually to time the storage of a large CodeSystem resource")
@ContextConfiguration(classes = TestHSearchAddInConfig.NoFT.class)
class LargeCodeSystemStorageTimingPostgresNoFullTextIT extends LargeCodeSystemStorageTimingPostgresIT {}
