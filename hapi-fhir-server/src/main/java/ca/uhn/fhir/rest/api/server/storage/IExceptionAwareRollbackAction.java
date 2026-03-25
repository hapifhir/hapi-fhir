package ca.uhn.fhir.rest.api.server.storage;

import jakarta.annotation.Nonnull;

/**
 * This class may be supplied as an argument to {@link ca.uhn.fhir.rest.api.server.storage.TransactionDetails#addRollbackUndoAction(Runnable)}
 * in order to supply a rollback action that has the option to modify the exception that is
 * ultimately thrown.
 */
public interface IExceptionAwareRollbackAction extends Runnable {

	/**
	 * Called when the transaction is rolled back
	 *
	 * @param theCause The exception that caused the rollback (note that this isn't necessarily the root cause
	 */
	void onRollback(@Nonnull Exception theCause);

	// nothing by default since it's assumed the user will
	@Override
	default void run() {}
}
