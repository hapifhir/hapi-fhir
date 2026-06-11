/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;

// Created by Claude Opus 4.6 (1M context)
/**
 * Thrown when a partitioned transaction commits some sub-bundles successfully before a later
 * sub-bundle fails. Carries the response entries for the sub-bundles that did commit (one inner
 * list per committed sub-bundle), so callers (e.g., the merge service) can drive rollback of the
 * committed work. How entries are distributed across sub-bundles is decided by the
 * {@code STORAGE_TRANSACTION_PRE_PARTITION} interceptor, not by this class.
 */
public class PartitionedTransactionPartialFailureException extends BaseServerResponseException {

	private final List<List<IBase>> myResponseEntriesPerSubBundle;

	public PartitionedTransactionPartialFailureException(
			String theMessage, List<List<IBase>> theResponseEntriesPerSubBundle, Throwable theCause) {
		super(deriveStatusCode(theCause), theMessage, theCause);
		myResponseEntriesPerSubBundle = theResponseEntriesPerSubBundle;
	}

	private static int deriveStatusCode(Throwable theCause) {
		if (theCause instanceof BaseServerResponseException serverException) {
			return serverException.getStatusCode();
		}
		return 500;
	}

	/**
	 * Returns the response entries from sub-bundles that committed successfully before the failure.
	 * Each inner list contains the Bundle entry components for one committed sub-bundle, in execution order.
	 */
	public List<List<IBase>> getCommittedResponseEntriesPerSubBundle() {
		return myResponseEntriesPerSubBundle;
	}
}
