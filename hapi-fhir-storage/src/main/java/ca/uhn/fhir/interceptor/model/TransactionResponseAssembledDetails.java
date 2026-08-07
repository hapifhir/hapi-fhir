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
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_TRANSACTION_RESPONSE_ASSEMBLED} pointcut. It carries the
 * fully assembled response bundle, so hooks can inspect or adjust the response entries before the response is
 * returned to the caller.
 *
 * @since FIXME-TG update after version bump
 */
public class TransactionResponseAssembledDetails {

	private final IBaseBundle myResponseBundle;
	private final ITransactionProcessorVersionAdapter<IBaseBundle, IBase> myVersionAdapter;

	/**
	 * Constructor
	 */
	public TransactionResponseAssembledDetails(
			@Nonnull IBaseBundle theResponseBundle,
			@Nonnull ITransactionProcessorVersionAdapter<IBaseBundle, IBase> theVersionAdapter) {
		myResponseBundle = theResponseBundle;
		myVersionAdapter = theVersionAdapter;
	}

	/**
	 * Provides the assembled response bundle. Hooks may adjust its entries (for example to correct an operation
	 * outcome) before the response is returned to the caller.
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
