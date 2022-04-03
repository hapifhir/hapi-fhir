package ca.uhn.fhir.rest.server.interceptor.auth;

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

import java.util.Collection;

/**
 * @see AuthorizationInterceptor#setFlags(Collection)
 */
public enum AuthorizationFlagsEnum {

	/**
	 * If this flag is set, attempts to perform read operations
	 * (read/search/history) will be matched by the interceptor before
	 * the method handler is called.
	 * <p>
	 * For example, suppose a rule set is in place that only allows read
	 * access to compartment <code>Patient/123</code>. With this flag set,
	 * any attempts
	 * to perform a FHIR read/search/history operation will be permitted
	 * to proceed to the method handler, and responses will be blocked
	 * by the AuthorizationInterceptor if the response contains a resource
	 * that is not in the given compartment.
	 * </p>
	 * <p>
	 * Setting this flag is less secure, since the interceptor can potentially leak
	 * information about the existence of data, but it is useful in some
	 * scenarios.
	 * </p>
	 *
	 * @since This flag has existed since HAPI FHIR 3.5.0. Prior to this
	 * version, this flag was the default and there was no ability to
	 * proactively block compartment read access.
	 */
	DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS,

	ALLOW_PATCH_REQUEST_UNCHALLENGED;
}
