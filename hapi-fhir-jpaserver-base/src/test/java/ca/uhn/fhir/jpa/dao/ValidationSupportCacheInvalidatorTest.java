package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.support.IValidationSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// Created by Claude Opus 5.5
@ExtendWith(MockitoExtension.class)
class ValidationSupportCacheInvalidatorTest {

	@Mock
	private IValidationSupport myValidationSupport;

	@AfterEach
	void afterEach() {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.clearSynchronization();
		}
	}

	@Test
	void invalidateCachesAfterCommit_noTransaction_invalidatesStraightAway() {
		// Test
		ValidationSupportCacheInvalidator.invalidateCachesAfterCommit(myValidationSupport);

		// Verify
		verify(myValidationSupport).invalidateCaches();
	}

	@Test
	void invalidateCachesAfterCommit_twoWritesInATransaction_invalidatesOnceAfterCommitBeforeOtherWork() {
		// Setup: other after-commit work, such as starting a pre-expansion job, registered before the first write
		TransactionSynchronizationManager.initSynchronization();
		TransactionSynchronization otherWork = mock(TransactionSynchronization.class);
		TransactionSynchronizationManager.registerSynchronization(otherWork);

		// Test
		ValidationSupportCacheInvalidator.invalidateCachesAfterCommit(myValidationSupport);
		ValidationSupportCacheInvalidator.invalidateCachesAfterCommit(myValidationSupport);

		// Verify: nothing before the commit, then one invalidation ahead of the other work
		verify(myValidationSupport, never()).invalidateCaches();
		List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
		assertThat(synchronizations).hasSize(2);
		synchronizations.forEach(TransactionSynchronization::afterCommit);
		InOrder order = inOrder(myValidationSupport, otherWork);
		order.verify(myValidationSupport, times(1)).invalidateCaches();
		order.verify(otherWork).afterCommit();
	}
}
