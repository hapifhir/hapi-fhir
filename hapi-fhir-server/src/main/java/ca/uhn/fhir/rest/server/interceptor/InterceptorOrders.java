package ca.uhn.fhir.rest.server.interceptor;

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

public class InterceptorOrders {

	public static final int SERVE_MEDIA_RESOURCE_RAW_INTERCEPTOR = 1000;
	public static final int RESPONSE_HIGHLIGHTER_INTERCEPTOR = 10000;
	public static final int RESPONSE_SIZE_CAPTURING_INTERCEPTOR_COMPLETED = -1;

	public static final int RESPONSE_TERMINOLOGY_TRANSLATION_INTERCEPTOR = 100;
	public static final int RESPONSE_TERMINOLOGY_DISPLAY_POPULATION_INTERCEPTOR = 110;

	/**
	 * Non instantiable
	 */
	private InterceptorOrders() {
		// nothing
	}

}
