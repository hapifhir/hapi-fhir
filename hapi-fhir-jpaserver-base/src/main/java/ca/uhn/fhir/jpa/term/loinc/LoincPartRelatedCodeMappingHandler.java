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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartRelatedCodeMappingHandler extends BaseLoincHandler implements IZipContentsHandlerCsv {

	public static final String LOINC_SCT_PART_MAP_ID = "loinc-parts-to-snomed-ct";
	public static final String LOINC_SCT_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-snomed-ct";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_ID = "loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_URI = "http://loinc.org/cm/loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_NAME = "LOINC Terms to RadLex RPIDs";
	public static final String LOINC_PART_TO_RID_PART_MAP_ID = "loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_NAME = "LOINC Parts to RadLex RIDs";
	private static final String LOINC_SCT_PART_MAP_NAME = "LOINC Part Map to SNOMED CT";
	private static final String LOINC_RXNORM_PART_MAP_ID = "loinc-parts-to-rxnorm";
	private static final String LOINC_RXNORM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-rxnorm";
	private static final String LOINC_RXNORM_PART_MAP_NAME = "LOINC Part Map to RxNORM";

	private static final String LOINC_PUBCHEM_PART_MAP_ID = "loinc-parts-to-pubchem";
	private static final String LOINC_PUBCHEM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-pubchem";
	private static final String LOINC_PUBCHEM_PART_MAP_NAME = "LOINC Part Map to PubChem";

	private static final String CM_COPYRIGHT = "The LOINC Part File, LOINC/SNOMED CT Expression Association and Map Sets File, RELMA database and associated search index files include SNOMED Clinical Terms (SNOMED CT®) which is used by permission of the International Health Terminology Standards Development Organisation (IHTSDO) under license. All rights are reserved. SNOMED CT® was originally created by The College of American Pathologists. “SNOMED” and “SNOMED CT” are registered trademarks of the IHTSDO. Use of SNOMED CT content is subject to the terms and conditions set forth in the SNOMED CT Affiliate License Agreement.  It is the responsibility of those implementing this product to ensure they are appropriately licensed and for more information on the license, including how to register as an Affiliate Licensee, please refer to http://www.snomed.org/snomed-ct/get-snomed-ct or info@snomed.org. Under the terms of the Affiliate License, use of SNOMED CT in countries that are not IHTSDO Members is subject to reporting and fee payment obligations. However, IHTSDO agrees to waive the requirements to report and pay fees for use of SNOMED CT content included in the LOINC Part Mapping and LOINC Term Associations for purposes that support or enable more effective use of LOINC. This material includes content from the US Edition to SNOMED CT, which is developed and maintained by the U.S. National Library of Medicine and is available to authorized UMLS Metathesaurus Licensees from the UTS Downloads site at https://uts.nlm.nih.gov.";

	public LoincPartRelatedCodeMappingHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps, Properties theUploadProperties, String theCopyrightStatement) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties, theCopyrightStatement);
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String partNumber = trim(theRecord.get("PartNumber"));
		String partName = trim(theRecord.get("PartName"));
		String extCodeId = trim(theRecord.get("ExtCodeId"));
		// TODO: use hex code for ascii 160
		extCodeId = extCodeId.replace(" ", "");
		String extCodeDisplayName = trim(theRecord.get("ExtCodeDisplayName"));
		String extCodeSystem = trim(theRecord.get("ExtCodeSystem"));
		String mapType = trim(theRecord.get("Equivalence"));
		String extCodeSystemVersion = trim(theRecord.get("ExtCodeSystemVersion"));
		String extCodeSystemCopyrightNotice = trim(theRecord.get("ExtCodeSystemCopyrightNotice"));

		// CodeSystem version from properties file
		String codeSystemVersionId = myUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());

		// ConceptMap version from properties files
		String loincPartMapVersion;
		if (codeSystemVersionId != null) {
			loincPartMapVersion = myUploadProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-" + codeSystemVersionId;
		} else {
			loincPartMapVersion = myUploadProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode());
		}
		
		Enumerations.ConceptMapEquivalence equivalence;
		switch (trim(defaultString(mapType))) {
			case "":
			case "equivalent":
				// 'equal' is more exact than 'equivalent' in the equivalence codes
				equivalence = Enumerations.ConceptMapEquivalence.EQUAL;
				break;
			case "narrower":
				equivalence = Enumerations.ConceptMapEquivalence.NARROWER;
				break;
			case "wider":
				equivalence = Enumerations.ConceptMapEquivalence.WIDER;
				break;
			case "relatedto":
				equivalence = Enumerations.ConceptMapEquivalence.RELATEDTO;
				break;
			default:
				throw new InternalErrorException(Msg.code(916) + "Unknown equivalence '" + mapType + "' for PartNumber: " + partNumber);
		}

		String loincPartMapId;
		String loincPartMapUri;
		String loincPartMapName;
		switch (extCodeSystem) {
			case ITermLoaderSvc.SCT_URI:
				loincPartMapId = LOINC_SCT_PART_MAP_ID;
				loincPartMapUri = LOINC_SCT_PART_MAP_URI;
				loincPartMapName = LOINC_SCT_PART_MAP_NAME;
				break;
			case "http://www.nlm.nih.gov/research/umls/rxnorm":
				loincPartMapId = LOINC_RXNORM_PART_MAP_ID;
				loincPartMapUri = LOINC_RXNORM_PART_MAP_URI;
				loincPartMapName = LOINC_RXNORM_PART_MAP_NAME;
				break;
			case "http://www.radlex.org":
				loincPartMapId = LOINC_PART_TO_RID_PART_MAP_ID;
				loincPartMapUri = LOINC_PART_TO_RID_PART_MAP_URI;
				loincPartMapName = LOINC_PART_TO_RID_PART_MAP_NAME;
				break;
			case "http://pubchem.ncbi.nlm.nih.gov":
				loincPartMapId = LOINC_PUBCHEM_PART_MAP_ID;
				loincPartMapUri = LOINC_PUBCHEM_PART_MAP_URI;
				loincPartMapName = LOINC_PUBCHEM_PART_MAP_NAME;
				break;
			default:
				loincPartMapId = extCodeSystem.replaceAll("[^a-zA-Z]", "");
				loincPartMapUri = extCodeSystem;
				loincPartMapName = "Unknown Mapping";
				break;
		}
		String conceptMapId;
		if (codeSystemVersionId != null) {
			conceptMapId = loincPartMapId + "-" + codeSystemVersionId;
		} else {
			conceptMapId = loincPartMapId;
		}

		addConceptMapEntry(
			new ConceptMapping()
				.setConceptMapId(conceptMapId)
				.setConceptMapUri(loincPartMapUri)
				.setConceptMapVersion(loincPartMapVersion)
				.setConceptMapName(loincPartMapName)
				.setSourceCodeSystem(ITermLoaderSvc.LOINC_URI)
				.setSourceCodeSystemVersion(codeSystemVersionId)
				.setSourceCode(partNumber)
				.setSourceDisplay(partName)
				.setTargetCodeSystem(extCodeSystem)
				.setTargetCode(extCodeId)
				.setTargetDisplay(extCodeDisplayName)
				.setTargetCodeSystemVersion(extCodeSystemVersion)
				.setEquivalence(equivalence)
				.setCopyright(extCodeSystemCopyrightNotice),
			myLoincCopyrightStatement + " " + CM_COPYRIGHT
		);

	}

}
