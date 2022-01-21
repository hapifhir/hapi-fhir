package ca.uhn.fhir.rest.server.interceptor.consent;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public enum ConsentOperationStatusEnum {

	/**
	 * The requested operation cannot proceed, and an operation outcome suitable for
	 * the user is available
	 */
	REJECT,

	/**
	 * The requested operation is allowed to proceed, but the engine will review each
	 * resource before sending to the client
	 */
	PROCEED,

	/**
	 * The engine has nothing to say about the operation  (same as proceed, but the
	 * host application need not consult the engine - can use more efficient
	 * counting/caching methods)
	 */
	AUTHORIZED,

}
