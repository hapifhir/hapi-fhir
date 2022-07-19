package ca.uhn.fhir.util;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

public enum VersionEnum {
	V0_1,
	V0_2,
	V0_3,
	V0_4,
	V0_5,
	V0_6,
	V0_7,
	V0_8,
	V0_9,

	V1_0,
	V1_1,
	V1_2,
	V1_3,
	V1_4,
	V1_5,
	V1_6,

	V2_0,
	V2_1,
	V2_2,
	V2_3,
	V2_4,
	V2_5,
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
	V4_0_3,
	V4_1_0,
	V4_2_0,
	@Deprecated
	V4_3_0, // 4.3.0 was renamed to 5.0.0 during the cycle
	V5_0_0,
	V5_0_1,
	V5_0_2,
	V5_1_0,
	V5_2_0,
	V5_2_1,
	V5_3_0,
	V5_3_1,
	V5_3_2,
	V5_3_3,
	V5_4_0,
	V5_4_1,
	V5_4_2,
	V5_5_0,
	V5_5_1,
	V5_5_2,
	V5_5_3,
	V5_5_4,
	V5_5_5,
	V5_5_6,
	V5_5_7,
	V5_6_0,
	V5_6_1,
	V5_6_2,
	V5_6_3,
	V5_7_0,
	V5_7_1,
	V5_7_2,
	V6_0_0,
	V6_0_1,
	V6_0_2,
	V6_0_3,
	V6_0_4,
	V6_1_0
	;

	public static VersionEnum latestVersion() {
		VersionEnum[] values = VersionEnum.values();
		return values[values.length - 1];
	}
}
