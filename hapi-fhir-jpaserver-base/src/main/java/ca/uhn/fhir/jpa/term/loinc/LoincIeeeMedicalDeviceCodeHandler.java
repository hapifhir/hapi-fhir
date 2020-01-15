package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincIeeeMedicalDeviceCodeHandler extends BaseLoincHandler implements IRecordHandler {

	public static final String LOINC_IEEE_CM_ID = "loinc-to-ieee-device-codes";
	public static final String LOINC_IEEE_CM_URI = "http://loinc.org/cm/loinc-to-ieee-device-codes";
	public static final String LOINC_IEEE_CM_NAME = "LOINC/IEEE Device Code Mappings";
	private static final String CM_COPYRIGHT = "This content from LOINC® is copyright © 1995 Regenstrief Institute, Inc. and the LOINC Committee, and available at no cost under the license at https://loinc.org/license/. The LOINC/IEEE Medical Device Code Mapping Table contains content from IEEE (http://ieee.org), copyright © 2017 IEEE.";

	/**
	 * Constructor
	 */
	public LoincIeeeMedicalDeviceCodeHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps, Properties theUploadProperties) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties);
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String longCommonName = trim(theRecord.get("LOINC_LONG_COMMON_NAME"));
		String ieeeCode = trim(theRecord.get("IEEE_CF_CODE10"));
		String ieeeDisplayName = trim(theRecord.get("IEEE_REFID"));

		// LOINC Part -> IEEE 11073:10101 Mappings
		String sourceCodeSystemUri = ITermLoaderSvc.LOINC_URI;
		String targetCodeSystemUri = ITermLoaderSvc.IEEE_11073_10101_URI;
		addConceptMapEntry(
			new ConceptMapping()
				.setConceptMapId(LOINC_IEEE_CM_ID)
				.setConceptMapUri(LOINC_IEEE_CM_URI)
				.setConceptMapName(LOINC_IEEE_CM_NAME)
				.setSourceCodeSystem(sourceCodeSystemUri)
				.setSourceCode(loincNumber)
				.setSourceDisplay(longCommonName)
				.setTargetCodeSystem(targetCodeSystemUri)
				.setTargetCode(ieeeCode)
				.setTargetDisplay(ieeeDisplayName)
				.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL),
			CM_COPYRIGHT);

	}


}
