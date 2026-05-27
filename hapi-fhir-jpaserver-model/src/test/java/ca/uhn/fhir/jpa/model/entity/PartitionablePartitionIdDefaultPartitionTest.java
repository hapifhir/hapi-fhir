package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-4-7
/**
 * Locks in the behavior of the {@link PartitionablePartitionId#toRequestPartitionId(PartitionablePartitionId, ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings)}
 * overload introduced to replace the deprecated single-arg helper that always resolved a {@code null} input to a
 * {@code null} partition (GL-8692). The overload must honor a configured non-null default partition ID, while staying
 * behavior-preserving (partition {@code null}) when no default is configured.
 */
public class PartitionablePartitionIdDefaultPartitionTest {

	@Test
	public void toRequestPartitionId_nullInputWithConfiguredDefault_resolvesToConfiguredPartition() {
		PartitionSettings settings = new PartitionSettings().setDefaultPartitionId(7);

		RequestPartitionId result = PartitionablePartitionId.toRequestPartitionId(null, settings);

		assertThat(result.getFirstPartitionIdOrNull()).isEqualTo(7);
	}

	@Test
	public void toRequestPartitionId_nullInputWithDefaultSettings_resolvesToNullPartition() {
		PartitionSettings settings = new PartitionSettings();

		RequestPartitionId result = PartitionablePartitionId.toRequestPartitionId(null, settings);

		assertThat(result.getFirstPartitionIdOrNull()).isNull();
	}

	@Test
	public void toRequestPartitionId_nonNullInput_passesThroughRegardlessOfSettings() {
		PartitionSettings settings = new PartitionSettings().setDefaultPartitionId(7);
		LocalDate partitionDate = LocalDate.of(2026, 5, 26);
		PartitionablePartitionId input = PartitionablePartitionId.with(3, partitionDate);

		RequestPartitionId result = PartitionablePartitionId.toRequestPartitionId(input, settings);

		// A non-null input must resolve to its own partition, never the configured default.
		assertThat(result.getFirstPartitionIdOrNull()).isEqualTo(3);
		assertThat(result.getPartitionDate()).isEqualTo(partitionDate);
	}
}
