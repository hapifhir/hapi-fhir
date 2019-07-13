package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartRelatedCodeMappingHandler extends BaseLoincHandler implements IRecordHandler {

	public static final String LOINC_SCT_PART_MAP_ID = "loinc-parts-to-snomed-ct";
	public static final String LOINC_SCT_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-snomed-ct";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_ID = "loinc-term-to-rpids";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_URI = "http://loinc.org/cm/loinc-to-rpids";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_NAME = "LOINC Terms to RadLex RPIDs";
	public static final String LOINC_PART_TO_RID_PART_MAP_ID = "loinc-part-to-rids";
	public static final String LOINC_PART_TO_RID_PART_MAP_URI = "http://loinc.org/cm/loinc-to-rids";
	public static final String LOINC_PART_TO_RID_PART_MAP_NAME = "LOINC Parts to RadLex RIDs";
	private static final String LOINC_SCT_PART_MAP_NAME = "LOINC Part Map to SNOMED CT";
	private static final String LOINC_RXNORM_PART_MAP_ID = "loinc-parts-to-rxnorm";
	private static final String LOINC_RXNORM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-rxnorm";
	private static final String LOINC_RXNORM_PART_MAP_NAME = "LOINC Part Map to RxNORM";
	private static final String CM_COPYRIGHT = "This content from LOINC® is copyright © 1995 Regenstrief Institute, Inc. and the LOINC Committee, and available at no cost under the license at https://loinc.org/license/. The LOINC Part File, LOINC/SNOMED CT Expression Association and Map Sets File, RELMA database and associated search index files include SNOMED Clinical Terms (SNOMED CT®) which is used by permission of the International Health Terminology Standards Development Organisation (IHTSDO) under license. All rights are reserved. SNOMED CT® was originally created by The College of American Pathologists. “SNOMED” and “SNOMED CT” are registered trademarks of the IHTSDO. Use of SNOMED CT content is subject to the terms and conditions set forth in the SNOMED CT Affiliate License Agreement.  It is the responsibility of those implementing this product to ensure they are appropriately licensed and for more information on the license, including how to register as an Affiliate Licensee, please refer to http://www.snomed.org/snomed-ct/get-snomed-ct or info@snomed.org. Under the terms of the Affiliate License, use of SNOMED CT in countries that are not IHTSDO Members is subject to reporting and fee payment obligations. However, IHTSDO agrees to waive the requirements to report and pay fees for use of SNOMED CT content included in the LOINC Part Mapping and LOINC Term Associations for purposes that support or enable more effective use of LOINC. This material includes content from the US Edition to SNOMED CT, which is developed and maintained by the U.S. National Library of Medicine and is available to authorized UMLS Metathesaurus Licensees from the UTS Downloads site at https://uts.nlm.nih.gov.";
	private static final String LOINC_PUBCHEM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-pubchem";
	private static final String LOINC_PUBCHEM_PART_MAP_ID = "loinc-parts-to-pubchem";
	private static final String LOINC_PUBCHEM_PART_MAP_NAME = "LOINC Part Map to PubChem";

	public LoincPartRelatedCodeMappingHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps, Properties theUploadProperties) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties);
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String partNumber = trim(theRecord.get("PartNumber"));
		String partName = trim(theRecord.get("PartName"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String extCodeId = trim(theRecord.get("ExtCodeId"));
		// TODO: use hex code for ascii 160
		extCodeId = extCodeId.replace(" ", "");
		String extCodeDisplayName = trim(theRecord.get("ExtCodeDisplayName"));
		String extCodeSystem = trim(theRecord.get("ExtCodeSystem"));
		String mapType = trim(theRecord.get("MapType"));
		String contentOrigin = trim(theRecord.get("ContentOrigin"));
		String extCodeSystemVersion = trim(theRecord.get("ExtCodeSystemVersion"));
		String extCodeSystemCopyrightNotice = trim(theRecord.get("ExtCodeSystemCopyrightNotice"));

		Enumerations.ConceptMapEquivalence equivalence;
		switch (trim(defaultString(mapType))) {
			case "":
			case "Exact":
				// 'equal' is more exact than 'equivalent' in the equivalence codes
				equivalence = Enumerations.ConceptMapEquivalence.EQUAL;
				break;
			case "LOINC broader":
				equivalence = Enumerations.ConceptMapEquivalence.NARROWER;
				break;
			case "LOINC narrower":
				equivalence = Enumerations.ConceptMapEquivalence.WIDER;
				break;
			default:
				throw new InternalErrorException("Unknown MapType '" + mapType + "' for PartNumber: " + partNumber);
		}

		String loincPartMapId;
		String loincPartMapUri;
		String loincPartMapName;
		switch (extCodeSystem) {
			case IHapiTerminologyLoaderSvc.SCT_URI:
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

		addConceptMapEntry(
			new ConceptMapping()
				.setConceptMapId(loincPartMapId)
				.setConceptMapUri(loincPartMapUri)
				.setConceptMapName(loincPartMapName)
				.setSourceCodeSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
				.setSourceCode(partNumber)
				.setSourceDisplay(partName)
				.setTargetCodeSystem(extCodeSystem)
				.setTargetCode(extCodeId)
				.setTargetDisplay(extCodeDisplayName)
				.setTargetCodeSystemVersion(extCodeSystemVersion)
				.setEquivalence(equivalence)
				.setCopyright(extCodeSystemCopyrightNotice),
			CM_COPYRIGHT
		);

	}

}
