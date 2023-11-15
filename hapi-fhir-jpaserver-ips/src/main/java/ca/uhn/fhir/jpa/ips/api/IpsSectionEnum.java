/*-
 * #%L
 * HAPI FHIR JPA Server - International Patient Summary (IPS)
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.ips.api;

public enum IpsSectionEnum {
	ALLERGY_INTOLERANCE,
	MEDICATION_SUMMARY,
	PROBLEM_LIST,
	IMMUNIZATIONS,
	PROCEDURES,
	MEDICAL_DEVICES,
	DIAGNOSTIC_RESULTS,
	VITAL_SIGNS,
	ILLNESS_HISTORY,
	PREGNANCY,
	SOCIAL_HISTORY,
	FUNCTIONAL_STATUS,
	PLAN_OF_CARE,
	ADVANCE_DIRECTIVES
}
