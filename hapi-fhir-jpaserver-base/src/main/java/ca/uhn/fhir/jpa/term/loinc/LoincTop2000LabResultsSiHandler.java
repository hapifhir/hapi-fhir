package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.entity.TermConcept;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class LoincTop2000LabResultsSiHandler extends BaseLoincTop2000LabResultsHandler {

	public static final String TOP_2000_SI_VS_ID = "top-2000-lab-observations-si";
	public static final String TOP_2000_SI_VS_URI = "http://loinc.org/vs/top-2000-lab-observations-si";
	public static final String TOP_2000_SI_VS_NAME = "Top 2000 Lab Results SI";

	public LoincTop2000LabResultsSiHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps, Properties theUploadProperties, String theCopyrightStatement) {
		super(theCode2concept, theValueSets, TOP_2000_SI_VS_ID, TOP_2000_SI_VS_URI, TOP_2000_SI_VS_NAME,
			theConceptMaps, theUploadProperties, theCopyrightStatement);
	}


}
