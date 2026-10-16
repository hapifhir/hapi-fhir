package ca.uhn.fhir.jpa.dao.mdm;

import ca.uhn.fhir.jpa.dao.data.IMdmMatchClaimJpaRepository;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.dao.MdmMatchClaimKey;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.LockAcquisitionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

/**
 * How {@link MdmMatchClaimSvcJpaImpl#claim} reports a failed claim insert. A database can end a claim insert that is
 * waiting on another transaction's claim with a lock failure rather than a duplicate key, and every such outcome must
 * reach MDM as a retriable claim conflict.
 */
@ExtendWith(MockitoExtension.class)
// Created by claude-opus-5-5
class MdmMatchClaimSvcJpaImplTest {

	private static final MdmMatchClaimKey KEY = MdmMatchClaimKey.forSourcePid("Patient", JpaPid.fromId(1L));

	@Mock
	private EntityManager myEntityManager;

	@Mock
	private IMdmMatchClaimJpaRepository myRepository;

	@Mock
	private PlatformTransactionManager myTxManager;

	private MdmMatchClaimSvcJpaImpl mySvc;

	@BeforeEach
	void beforeStartTransaction() {
		TransactionSynchronizationManager.initSynchronization();
		TransactionSynchronizationManager.setActualTransactionActive(true);
		mySvc = new MdmMatchClaimSvcJpaImpl(myEntityManager, myRepository, myTxManager);
	}

	@AfterEach
	void afterEndTransaction() {
		TransactionSynchronizationManager.clear();
	}

	static Stream<RuntimeException> claimConflicts() {
		return Stream.of(
				new ConstraintViolationException("duplicate key", new SQLException("duplicate key"), "PK"),
				new LockTimeoutException("lock wait timed out"),
				new LockAcquisitionException("deadlock detected", new SQLException("deadlock detected")));
	}

	@ParameterizedTest
	@MethodSource("claimConflicts")
	void claim_whenTheInsertConflictsOrTimesOut_reportsAClaimConflict(RuntimeException theFailure) {
		doThrow(theFailure).when(myEntityManager).flush();

		assertThatThrownBy(() -> mySvc.claim(List.of(KEY), null, Map.of()))
				.isInstanceOf(ResourceVersionConflictException.class)
				.hasMessageContaining("HAPI-3062")
				.hasCause(theFailure);
	}

	@Test
	void claim_whenTheInsertFailsForAnotherReason_rethrowsTheFailure() {
		PersistenceException failure = new PersistenceException("connection lost");
		doThrow(failure).when(myEntityManager).flush();

		assertThatThrownBy(() -> mySvc.claim(List.of(KEY), null, Map.of())).isSameAs(failure);
	}
}
