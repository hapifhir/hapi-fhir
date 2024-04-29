/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.api;

public enum CdsResolutionStrategyEnum {
	/**
	 * There are no means to fetch missing prefetch elements.  A precondition failure should be thrown.
	 */
	NONE,

	/**
	 * Fhir Server details were provided with the request.  Smile CDR will use them to fetch missing resources before
	 * calling the CDS Service
	 */
	FHIR_CLIENT,

	/**
	 * The CDS Hooks service method will be responsible for fetching the missing resources using the FHIR Server
	 * in the request
	 */
	SERVICE,

	/**
	 * Missing prefetch elements will be retrieved directly from the configured Storage Module
	 */
	DAO
}
