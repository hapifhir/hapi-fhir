package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.springframework.test.context.TestPropertySource;

/**
 * Runs the {@link PartitionedIdRepositoryFactoryBean} trial without database partition mode, where
 * {@code PARTITION_ID} isn't part of the key.
 */
// Created by claude-fable-5-2
@TestPropertySource(properties = {JpaConstants.HAPI_DATABASE_PARTITION_MODE + "=false"})
public class PartitionedIdRepositoryFactoryDbpmDisabledTest extends PartitionedIdRepositoryFactoryDbpmEnabledTest {

	@Override
	protected boolean isDatabasePartitionMode() {
		return false;
	}
}
