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
package ca.uhn.fhir.context.support;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * Thrown by an {@link IValidationSupport} module that could not answer a request at all, for example because the
 * remote terminology server it relies on failed or could not be reached. It says nothing about the resource or
 * code that was asked about.
 * <p>
 * The validation support chain treats it as "ask the next module" and does not cache the result, so the module is
 * asked again once it has recovered. Called outside a chain, it is an HTTP 500 whose message names what failed.
 * </p>
 *
 * @since 9.2.0
 */
// Created by Claude Opus 5.5
public class ValidationSupportUnavailableException extends InternalErrorException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param theMessage what could not be answered, and why
	 * @param theCause   the failure that kept the module from answering
	 */
	public ValidationSupportUnavailableException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}
}
