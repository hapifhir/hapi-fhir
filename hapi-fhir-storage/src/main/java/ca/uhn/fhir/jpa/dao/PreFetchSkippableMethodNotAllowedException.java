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

import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;

/**
 * A {@link MethodNotAllowedException} raised while determining a transaction entry's partition before
 * pre-fetch, indicating the failure only reflects entry content that is not resolvable yet (an unresolved
 * reference, an id the server has not assigned). The transaction processor may skip such failures and retry
 * partition resolution once pre-fetch and the after-prefetch hooks have resolved the entries.
 */
// Created by Claude Fable 5
public class PreFetchSkippableMethodNotAllowedException extends MethodNotAllowedException {

	public PreFetchSkippableMethodNotAllowedException(String theMessage) {
		super(theMessage);
	}
}
