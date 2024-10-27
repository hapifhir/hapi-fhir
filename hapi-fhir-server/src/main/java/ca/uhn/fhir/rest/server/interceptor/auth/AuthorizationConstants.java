/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.auth;

public class AuthorizationConstants {

	public static final int ORDER_CONSENT_INTERCEPTOR = 100;
	public static final int ORDER_BINARY_SECURITY_INTERCEPTOR = 150;
	public static final int ORDER_AUTH_INTERCEPTOR = 200;
	public static final int ORDER_CONVERTER_INTERCEPTOR = 300;

	private AuthorizationConstants() {
		super();
	}
}
