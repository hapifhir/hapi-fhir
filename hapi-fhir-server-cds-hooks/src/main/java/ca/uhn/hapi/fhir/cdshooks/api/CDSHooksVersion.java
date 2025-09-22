/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import jakarta.annotation.Nullable;

public enum CDSHooksVersion {
	V_1_1,
	V_2_0;

	/**
	 * Using V_1_1 as the default for not breaking existing implementations
	 */
	public static final CDSHooksVersion DEFAULT = V_1_1;

	public static CDSHooksVersion getOrDefault(@Nullable CDSHooksVersion theVersion) {
		if (theVersion == null) {
			return DEFAULT;
		}
		return theVersion;
	}
}
