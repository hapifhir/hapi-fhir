/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.constant;

public class CareCapsConstants {
	private CareCapsConstants() {}

	public static final String CARE_GAPS_REPORT_PROFILE =
			"http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/indv-measurereport-deqm";
	public static final String CARE_GAPS_BUNDLE_PROFILE =
			"http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/gaps-bundle-deqm";
	public static final String CARE_GAPS_COMPOSITION_PROFILE =
			"http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/gaps-composition-deqm";
	public static final String CARE_GAPS_DETECTED_ISSUE_PROFILE =
			"http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/gaps-detectedissue-deqm";
	public static final String CARE_GAPS_GAP_STATUS_EXTENSION =
			"http://hl7.org/fhir/us/davinci-deqm/StructureDefinition/extension-gapStatus";
	public static final String CARE_GAPS_GAP_STATUS_SYSTEM =
			"http://hl7.org/fhir/us/davinci-deqm/CodeSystem/gaps-status";
}
