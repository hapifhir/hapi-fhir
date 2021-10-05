package org.hl7.fhir.common.hapi.validation.support;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

public class ValidationConstants {

	public static final String LOINC_LOW = "loinc";
	public static final String LOINC_ALL_VALUESET_ID = "loinc-all";
	public static final String LOINC_GENERIC_CODE_SYSTEM_URL = "http://loinc.org";
	public static final String LOINC_GENERIC_VALUESET_URL = LOINC_GENERIC_CODE_SYSTEM_URL + "/vs";
	public static final String LOINC_GENERIC_VALUESET_URL_PLUS_SLASH = LOINC_GENERIC_VALUESET_URL + "/";

	// not to be instantiated
	private ValidationConstants() { }
}
