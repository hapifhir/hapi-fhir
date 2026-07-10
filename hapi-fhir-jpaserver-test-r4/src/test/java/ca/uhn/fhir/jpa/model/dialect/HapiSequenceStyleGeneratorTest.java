package ca.uhn.fhir.jpa.model.dialect;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.hapi.fhir.sql.hibernatesvc.IdSequencePoolingStrategy;
import org.hibernate.id.enhanced.StandardOptimizerDescriptor;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-4-8
/**
 * Unit tests for {@link HapiSequenceStyleGenerator}: the optimizer selected based on the per-thread
 * id-pooling setting, and the guard that prevents the reserved {@link JpaConstants#NO_MORE_PID} value from
 * being persisted as a resource id.
 */
class HapiSequenceStyleGeneratorTest {

	@Test
	void perThreadPool_selectsThreadLocalPooledOptimizer() {
		assertThat(HapiSequenceStyleGenerator.determineOptimizerExternalName(IdSequencePoolingStrategy.PER_THREAD_POOL))
				.isEqualTo(StandardOptimizerDescriptor.POOLED_LOTL.getExternalName());
	}

	@Test
	void sharedPool_selectsLegacyPooledOptimizer() {
		assertThat(HapiSequenceStyleGenerator.determineOptimizerExternalName(IdSequencePoolingStrategy.SHARED_POOL))
				.isEqualTo(StandardOptimizerDescriptor.POOLED.getExternalName());
	}

	@Test
	void generateNonReservedValue_whenNotReserved_returnsValue() {
		Deque<Long> values = new ArrayDeque<>(List.of(123L));
		assertThat(HapiSequenceStyleGenerator.generateNonReservedValue(values::poll)).isEqualTo(123L);
	}

	@Test
	void generateNonReservedValue_retriesOnce_whenFirstValueIsReserved() {
		Deque<Long> values = new ArrayDeque<>(List.of(JpaConstants.NO_MORE_PID, 456L));
		assertThat(HapiSequenceStyleGenerator.generateNonReservedValue(values::poll)).isEqualTo(456L);
	}

	@Test
	void generateNonReservedValue_throws_whenReservedValueReturnedTwice() {
		Deque<Long> values = new ArrayDeque<>(List.of(JpaConstants.NO_MORE_PID, JpaConstants.NO_MORE_PID));
		assertThatThrownBy(() -> HapiSequenceStyleGenerator.generateNonReservedValue(values::poll))
				.isInstanceOf(InternalErrorException.class)
				.hasMessageContaining("HAPI-2791")
				.hasMessageContaining("Resource ID generator provided illegal value: -1 / -1");
	}
}
