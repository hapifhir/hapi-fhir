package ca.uhn.fhir.rest.client.interceptor;

/*-
 * #%L
 * HAPI FHIR - Client Framework
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

public interface InterceptorOrders {

	int LOGGING_INTERCEPTOR_REQUEST = -2;
	int URL_TENANT_SELECTION_INTERCEPTOR_REQUEST = 100;
	int CAPTURING_INTERCEPTOR_REQUEST = 1000;

	int CAPTURING_INTERCEPTOR_RESPONSE = -1;
	int LOGGING_INTERCEPTOR_RESPONSE = 1001;
}
