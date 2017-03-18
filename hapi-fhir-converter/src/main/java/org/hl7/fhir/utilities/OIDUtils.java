package org.hl7.fhir.utilities;

/*
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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


public class OIDUtils {

	/*
  2.16.840.1.113883.3.72.5.2 - NIST owns this
  2.16.840.1.113883.4.6 - National Provider Identifier
  2.16.840.1.113883.6.21 - UB92
  2.16.840.1.113883.6.69 - NDC
	 */

	public static String getUriForOid(String r) {
		if (r.equals("2.16.840.1.113883.6.96"))
			return "http://snomed.info/sct";
		if (r.equals("2.16.840.1.113883.6.1"))
			return "http://loinc.org";
		if (r.equals("2.16.840.1.113883.6.8"))
			return "http://unitsofmeasure.org";
		if (r.equals("2.16.840.1.113883.6.3"))
			return "http://hl7.org/fhir/sid/icd-10";
		if (r.equals("2.16.840.1.113883.6.42"))
			return "http://hl7.org/fhir/sid/icd-9";
		if (r.equals("2.16.840.1.113883.6.104"))
			return "http://hl7.org/fhir/sid/icd-9";
		if (r.equals("2.16.840.1.113883.6.103"))
			return "http://hl7.org/fhir/sid/icd-9"; //todo: confirm this		
		if (r.equals("2.16.840.1.113883.6.73"))
			return "http://hl7.org/fhir/sid/atc";
		if (r.equals("2.16.840.1.113883.3.26.1.1"))
			return "http://ncimeta.nci.nih.gov";
		if (r.equals("2.16.840.1.113883.3.26.1.1.1"))
			return "http://ncimeta.nci.nih.gov";
		if (r.equals("2.16.840.1.113883.6.88"))
			return "http://www.nlm.nih.gov/research/umls/rxnorm"; // todo: confirm this

		if (r.equals("2.16.840.1.113883.5.1008"))
			return "http://hl7.org/fhir/v3/NullFlavor";
		if (r.equals("2.16.840.1.113883.5.111"))
			return "http://hl7.org/fhir/v3/RoleCode";
		if (r.equals("2.16.840.1.113883.5.4"))
			return "http://hl7.org/fhir/v3/ActCode";
		if (r.equals("2.16.840.1.113883.5.8"))
			return "http://hl7.org/fhir/v3/ActReason";
		if (r.equals("2.16.840.1.113883.5.83"))
			return "http://hl7.org/fhir/v3/ObservationInterpretation";
		if (r.equals("2.16.840.1.113883.6.238"))
			return "http://hl7.org/fhir/v3/Race";

		if (r.equals("2.16.840.1.113883.6.59"))
			return "http://hl7.org/fhir/sid/cvx";
		if (r.equals("2.16.840.1.113883.12.292"))
			return "http://hl7.org/fhir/sid/cvx";

		if (r.equals("2.16.840.1.113883.6.12"))
			return "http://www.ama-assn.org/go/cpt";

		if (r.startsWith("2.16.840.1.113883.12."))
			return "http://hl7.org/fhir/sid/v2-"+r.substring(21);
		return null;
	}

}
