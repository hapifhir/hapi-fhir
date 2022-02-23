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
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincIeeeMedicalDeviceCodeHandler extends BaseLoincHandler implements IZipContentsHandlerCsv {

	public static final String LOINC_IEEE_CM_ID = "loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_URI = "http://loinc.org/cm/loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_NAME = "LOINC/IEEE Device Code Mappings";
	private static final String CM_COPYRIGHT = "The LOINC/IEEE Medical Device Code Mapping Table contains content from IEEE (http://ieee.org), copyright © 2017 IEEE.";

	/**
	 * Constructor
	 */
	public LoincIeeeMedicalDeviceCodeHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps, Properties theUploadProperties, String theCopyrightStatement) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties, theCopyrightStatement);
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String codeSystemVersionId = myUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		String loincIeeeCmVersion;
		if (codeSystemVersionId != null) {
			loincIeeeCmVersion =  myUploadProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-" + codeSystemVersionId;
		} else {
			loincIeeeCmVersion = myUploadProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode());
		}
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String longCommonName = trim(theRecord.get("LOINC_LONG_COMMON_NAME"));
		String ieeeCode = trim(theRecord.get("IEEE_CF_CODE10"));
		String ieeeDisplayName = trim(theRecord.get("IEEE_REFID"));

		// LOINC Part -> IEEE 11073:10101 Mappings
		String sourceCodeSystemUri = ITermLoaderSvc.LOINC_URI;
		String targetCodeSystemUri = ITermLoaderSvc.IEEE_11073_10101_URI;
		String conceptMapId;
		if (codeSystemVersionId != null) {
			conceptMapId = LOINC_IEEE_CM_ID + "-" + codeSystemVersionId;
		} else {
			conceptMapId = LOINC_IEEE_CM_ID;
		}
		addConceptMapEntry(
			new ConceptMapping()
				.setConceptMapId(conceptMapId)
				.setConceptMapUri(LOINC_IEEE_CM_URI)
				.setConceptMapVersion(loincIeeeCmVersion)
				.setConceptMapName(LOINC_IEEE_CM_NAME)
				.setSourceCodeSystem(sourceCodeSystemUri)
				.setSourceCodeSystemVersion(codeSystemVersionId)
				.setSourceCode(loincNumber)
				.setSourceDisplay(longCommonName)
				.setTargetCodeSystem(targetCodeSystemUri)
				.setTargetCode(ieeeCode)
				.setTargetDisplay(ieeeDisplayName)
				.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL),
			myLoincCopyrightStatement + " " + CM_COPYRIGHT);
	}


}
