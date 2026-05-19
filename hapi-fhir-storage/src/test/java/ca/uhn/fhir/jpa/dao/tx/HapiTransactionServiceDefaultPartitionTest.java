// Created by claude-opus-4-7
package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Locks in the GL-8692 fix for {@link HapiTransactionService#withSystemRequestOnDefaultPartition()}.
 *
 * <p>Before the fix, the method always routed system requests through a {@code null} partition ID,
 * ignoring {@link PartitionSettings#getDefaultPartitionId()}. This caused cross-partition writes
 * and wrong cache keys whenever an operator configured a non-null default partition.
 *
 * <p>After the fix, the method routes through {@code myPartitionSettings.getDefaultRequestPartitionId()},
 * so a configured default ID is honored. The unconfigured (null) case is preserved.
 */
class HapiTransactionServiceDefaultPartitionTest {

	@Test
	void withSystemRequestOnDefaultPartition_whenDefaultPartitionIdConfigured_routesThroughConfiguredId() {
		PartitionSettings settings = new PartitionSettings();
		settings.setDefaultPartitionId(7);

		RequestPartitionId partition = partitionIdOnSystemRequestDefault(settings);

		assertThat(partition.getFirstPartitionIdOrNull()).isEqualTo(7);
	}

	@Test
	void withSystemRequestOnDefaultPartition_whenSettingsUnconfigured_preservesNullPartition() {
		PartitionSettings settings = new PartitionSettings();

		RequestPartitionId partition = partitionIdOnSystemRequestDefault(settings);

		assertThat(partition.getFirstPartitionIdOrNull()).isNull();
	}

	private static RequestPartitionId partitionIdOnSystemRequestDefault(PartitionSettings theSettings) {
		HapiTransactionService svc = new HapiTransactionService();
		svc.setPartitionSettingsForUnitTest(theSettings);
		HapiTransactionService.ExecutionBuilder builder =
				(HapiTransactionService.ExecutionBuilder) svc.withSystemRequestOnDefaultPartition();
		return builder.getRequestPartitionIdForTesting();
	}
}
