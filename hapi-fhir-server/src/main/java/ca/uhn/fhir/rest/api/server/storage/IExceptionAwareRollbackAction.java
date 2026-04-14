/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.api.server.storage;

import jakarta.annotation.Nonnull;

/**
 * This class may be supplied as an argument to {@link ca.uhn.fhir.rest.api.server.storage.TransactionDetails#addRollbackUndoAction(Runnable)}
 * in order to supply a rollback action that has the option to modify the exception that is
 * ultimately thrown.
 */
public interface IExceptionAwareRollbackAction extends Runnable {

	/**
	 * Called when the transaction is rolled back, and may modify the exception being
	 * thrown.
	 *
	 * @param theCause The exception that caused the rollback
	 * @return The exception that should be thrown or the same exception if no different exception should be thrown.
	 */
	@Nonnull
	RuntimeException onRollback(@Nonnull RuntimeException theCause);

	// nothing by default since it's assumed the user will
	@Override
	default void run() {}
}
