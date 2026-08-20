/*-
 * #%L
 * HAPI FHIR - Core Library
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

import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;

// Created by Claude Opus 4.8
/**
 * This object is used as a method parameter for interceptor hook methods implementing the
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_TRANSACTION_WRITE_AFTER_PREFETCH} pointcut. It carries the
 * list of bundle entries that are about to be written, so hooks can inspect or mutate them (for example, to
 * resolve references to concrete IDs using data resolved during the pre-fetch).
 *
 * @since 8.11.15
 */
public class TransactionWriteAfterPrefetchDetails {

	private final List<IBase> myEntries;

	/**
	 * Constructor
	 */
	public TransactionWriteAfterPrefetchDetails(List<IBase> theEntries) {
		myEntries = theEntries;
	}

	/**
	 * Provides the list of bundle entries being processed, in processing order. Hooks may mutate the entries (for
	 * example to resolve references to concrete IDs) using data resolved during the pre-fetch.
	 */
	public List<IBase> getEntries() {
		return myEntries;
	}
}
