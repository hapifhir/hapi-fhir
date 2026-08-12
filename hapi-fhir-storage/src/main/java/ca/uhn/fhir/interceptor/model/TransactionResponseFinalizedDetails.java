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
package ca.uhn.fhir.interceptor.model;

import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;

// Created by Claude Fable 5
/**
 * This object is used as a method parameter for interceptor hook methods implementing the
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_TRANSACTION_RESPONSE_FINALIZED} pointcut. It carries the
 * finalized response bundle — aggregated across sub-transactions when processing was split — so hooks can inspect
 * or adjust the complete response before it is returned to the caller.
 *
 * @since 8.11.20
 */
public class TransactionResponseFinalizedDetails {

	private final IBaseBundle myResponseBundle;
	private final ITransactionProcessorVersionAdapter<IBaseBundle, IBase> myVersionAdapter;

	/**
	 * Constructor
	 */
	public TransactionResponseFinalizedDetails(
			@Nonnull IBaseBundle theResponseBundle,
			@Nonnull ITransactionProcessorVersionAdapter<IBaseBundle, IBase> theVersionAdapter) {
		myResponseBundle = theResponseBundle;
		myVersionAdapter = theVersionAdapter;
	}

	/**
	 * Provides the finalized response bundle. Hooks may adjust its entries (for example to remove entries
	 * corresponding to request entries they injected) before the response is returned to the caller.
	 */
	public IBaseBundle getResponseBundle() {
		return myResponseBundle;
	}

	/**
	 * Provides the version adapter for reading and mutating the response bundle's entries in a
	 * FHIR-version-agnostic way.
	 */
	public ITransactionProcessorVersionAdapter<IBaseBundle, IBase> getVersionAdapter() {
		return myVersionAdapter;
	}
}
