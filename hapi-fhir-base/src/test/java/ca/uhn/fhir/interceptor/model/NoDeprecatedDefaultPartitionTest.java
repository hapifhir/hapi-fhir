// Created by claude-opus-4-7
package ca.uhn.fhir.interceptor.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Regression guard for GL-8692: locks in the removal of the deprecated
 * {@code RequestPartitionId.defaultPartition()} and {@code defaultPartition(LocalDate)} overloads.
 * The supported replacements are {@link RequestPartitionId#defaultPartition(IDefaultPartitionSettings)}
 * and {@link RequestPartitionId#fromPartitionId(Integer)} (for an explicit null/default partition).
 */
public class NoDeprecatedDefaultPartitionTest {

	@Test
	void deprecatedDefaultPartitionMethodsAreRemoved() throws NoSuchMethodException {
		// The settings-based overload remains the supported form...
		assertThat(RequestPartitionId.class.getMethod("defaultPartition", IDefaultPartitionSettings.class))
				.isNotNull();
		// ...while the two deprecated overloads must stay gone (GL-8692 Stage 8).
		assertThatThrownBy(() -> RequestPartitionId.class.getMethod("defaultPartition"))
				.isInstanceOf(NoSuchMethodException.class);
		assertThatThrownBy(() -> RequestPartitionId.class.getMethod("defaultPartition", LocalDate.class))
				.isInstanceOf(NoSuchMethodException.class);
	}
}
