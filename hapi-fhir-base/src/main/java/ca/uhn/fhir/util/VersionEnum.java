package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public enum VersionEnum {
	V2_5_0,
	V3_0_0,
	V3_1_0,
	V3_2_0,
	V3_3_0,
	V3_4_0,
	V3_5_0,
	V3_6_0,
	V3_7_0,
	V3_8_0,
	V4_0_0,
	V4_1_0;

	public static VersionEnum latestVersion() {
		VersionEnum[] values = VersionEnum.values();
		return values[values.length - 1];
	}
}
