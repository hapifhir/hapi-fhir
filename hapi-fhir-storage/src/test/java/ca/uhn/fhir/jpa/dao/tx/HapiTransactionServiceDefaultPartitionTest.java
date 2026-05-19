// Created by claude-opus-4-7
package ca.uhn.fhir.jpa.dao.tx;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

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

	@Test
	void executeWithDefaultPartitionInContext_whenDefaultPartitionIdConfigured_threadLocalSeesConfiguredId() {
		PartitionSettings settings = new PartitionSettings();
		settings.setDefaultPartitionId(7);
		HapiTransactionService svc = new HapiTransactionService();
		svc.setPartitionSettingsForUnitTest(settings);

		AtomicReference<RequestPartitionId> insideCallback = new AtomicReference<>();
		svc.executeWithDefaultPartitionInContext(() -> {
			insideCallback.set(HapiTransactionService.getRequestPartitionAssociatedWithThread());
			return null;
		});

		assertThat(insideCallback.get()).isNotNull();
		assertThat(insideCallback.get().getFirstPartitionIdOrNull()).isEqualTo(7);
	}

	@Test
	void executeWithDefaultPartitionInContext_whenSettingsUnconfigured_threadLocalSeesNullPartition() {
		PartitionSettings settings = new PartitionSettings();
		HapiTransactionService svc = new HapiTransactionService();
		svc.setPartitionSettingsForUnitTest(settings);

		AtomicReference<RequestPartitionId> insideCallback = new AtomicReference<>();
		svc.executeWithDefaultPartitionInContext(() -> {
			insideCallback.set(HapiTransactionService.getRequestPartitionAssociatedWithThread());
			return null;
		});

		assertThat(insideCallback.get()).isNotNull();
		assertThat(insideCallback.get().getFirstPartitionIdOrNull()).isNull();
	}

	@Test
	void executeWithDefaultPartitionInContext_restoresPreviousThreadLocalAfterCallback() {
		PartitionSettings settings = new PartitionSettings();
		settings.setDefaultPartitionId(7);
		HapiTransactionService svc = new HapiTransactionService();
		svc.setPartitionSettingsForUnitTest(settings);

		assertThat(HapiTransactionService.getRequestPartitionAssociatedWithThread()).isNull();
		svc.executeWithDefaultPartitionInContext(() -> null);
		assertThat(HapiTransactionService.getRequestPartitionAssociatedWithThread()).isNull();
	}

	private static RequestPartitionId partitionIdOnSystemRequestDefault(PartitionSettings theSettings) {
		HapiTransactionService svc = new HapiTransactionService();
		svc.setPartitionSettingsForUnitTest(theSettings);
		HapiTransactionService.ExecutionBuilder builder =
				(HapiTransactionService.ExecutionBuilder) svc.withSystemRequestOnDefaultPartition();
		return builder.getRequestPartitionIdForTesting();
	}
}
