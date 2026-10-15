package ca.uhn.fhir.jpa.mdm.svc.concurrency;

import ca.uhn.fhir.mdm.dao.IMdmMatchClaimSvc;
import ca.uhn.fhir.mdm.dao.MdmMatchClaimKey;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
import java.util.Map;

/**
 * Wraps the real claim service so that {@link ConcurrencyScheduler} can hold each worker thread at two gates of every
 * MDM attempt:
 * <ul>
 *     <li>before the attempt's first statement (the pre-read of existing claims), so the scheduler decides when the
 *     thread takes its claims, which is the thread's "start";</li>
 *     <li>just before the attempt's transaction commits, so the scheduler decides when it commits, or makes it abort.</li>
 * </ul>
 * Threads the scheduler doesn't know about pass straight through.
 */
// Created by claude-opus-5-5
public class GatingMdmMatchClaimSvc implements IMdmMatchClaimSvc {

	private final IMdmMatchClaimSvc myDelegate;
	private final ConcurrencyScheduler myScheduler;
	private final Object myRegisteredResourceKey = new Object();
	private volatile Runnable myBeforeClaimHook;

	public GatingMdmMatchClaimSvc(IMdmMatchClaimSvc theDelegate, ConcurrencyScheduler theScheduler) {
		myDelegate = theDelegate;
		myScheduler = theScheduler;
	}

	@Nonnull
	@Override
	public Map<MdmMatchClaimKey, Long> findExistingClaims(@Nonnull Collection<MdmMatchClaimKey> theKeys) {
		ConcurrencyScheduler.Worker worker = myScheduler.currentWorker();
		if (worker != null) {
			worker.awaitStartGate();
			// A caller that joins an existing transaction can pre-read more than once in it; gate its commit once
			if (!TransactionSynchronizationManager.hasResource(myRegisteredResourceKey)) {
				TransactionSynchronizationManager.bindResource(myRegisteredResourceKey, Boolean.TRUE);
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void beforeCommit(boolean theReadOnly) {
						worker.awaitCommitGate();
					}

					@Override
					public void afterCompletion(int theStatus) {
						TransactionSynchronizationManager.unbindResourceIfPossible(myRegisteredResourceKey);
						worker.attemptCompleted(theStatus == STATUS_COMMITTED);
					}
				});
			}
		}
		return myDelegate.findExistingClaims(theKeys);
	}

	@Override
	public void claim(
			@Nonnull Collection<MdmMatchClaimKey> theKeys,
			@Nullable IResourcePersistentId<?> theClaimant,
			@Nonnull Map<MdmMatchClaimKey, Long> theTakeoverTokens) {
		ConcurrencyScheduler.Worker worker = myScheduler.currentWorker();
		Runnable beforeClaimHook = myBeforeClaimHook;
		if (beforeClaimHook != null) {
			beforeClaimHook.run();
		}
		try {
			myDelegate.claim(theKeys, theClaimant, theTakeoverTokens);
		} catch (RuntimeException e) {
			if (worker != null) {
				worker.claimFailed();
			}
			throw e;
		}
		if (worker != null) {
			worker.claimed();
		}
	}

	/**
	 * Sets a hook that runs on every thread between its pre-read of existing claims and its claim insert, or clears it
	 * when {@code null}. The hook decides for itself which threads to act on.
	 */
	public void setBeforeClaimHook(@Nullable Runnable theHook) {
		myBeforeClaimHook = theHook;
	}
}
