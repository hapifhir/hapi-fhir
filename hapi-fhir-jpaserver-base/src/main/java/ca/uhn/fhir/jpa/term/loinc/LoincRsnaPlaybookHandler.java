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

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincRsnaPlaybookHandler extends BaseLoincHandler implements IRecordHandler {

	public static final String RSNA_CODES_VS_ID = "loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_URI = "http://loinc.org/vs/loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_NAME = "LOINC/RSNA Radiology Playbook";
	public static final String RID_CS_URI = "http://www.radlex.org";
	/*
	 * About these being the same - Per Dan Vreeman:
	 * We had some discussion about this, and both
	 * RIDs (RadLex clinical terms) and RPIDs (Radlex Playbook Ids)
	 * belong to the same "code system" since they will never collide.
	 * The codesystem uri is "http://www.radlex.org". FYI, that's
	 * now listed on the FHIR page:
	 * https://www.hl7.org/fhir/terminologies-systems.html
	 * -ja
	 */
	public static final String RPID_CS_URI = RID_CS_URI;
	private static final String CM_COPYRIGHT = "This content from LOINC® is copyright © 1995 Regenstrief Institute, Inc. and the LOINC Committee, and available at no cost under the license at https://loinc.org/license/. The LOINC/RSNA Radiology Playbook and the LOINC Part File contain content from RadLex® (http://rsna.org/RadLex.aspx), copyright © 2005-2017, The Radiological Society of North America, Inc., available at no cost under the license at http://www.rsna.org/uploadedFiles/RSNA/Content/Informatics/RadLex_License_Agreement_and_Terms_of_Use_V2_Final.pdf.";
	private final Map<String, TermConcept> myCode2Concept;
	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
	private final Set<String> myCodesInRsnaPlaybookValueSet = new HashSet<>();

	/**
	 * Constructor
	 */
	public LoincRsnaPlaybookHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps, Properties theUploadProperties) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties);
		myCode2Concept = theCode2concept;
		myValueSets = theValueSets;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String longCommonName = trim(theRecord.get("LongCommonName"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partName = trim(theRecord.get("PartName"));
		String partSequenceOrder = trim(theRecord.get("PartSequenceOrder"));
		String rid = trim(theRecord.get("RID"));
		String preferredName = trim(theRecord.get("PreferredName"));
		String rpid = trim(theRecord.get("RPID"));
		String longName = trim(theRecord.get("LongName"));

		// RSNA Codes VS
		ValueSet vs;
		if (!myIdToValueSet.containsKey(RSNA_CODES_VS_ID)) {
			vs = new ValueSet();
			vs.setUrl(RSNA_CODES_VS_URI);
			vs.setId(RSNA_CODES_VS_ID);
			vs.setName(RSNA_CODES_VS_NAME);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			myIdToValueSet.put(RSNA_CODES_VS_ID, vs);
			myValueSets.add(vs);
		} else {
			vs = myIdToValueSet.get(RSNA_CODES_VS_ID);
		}

		if (!myCodesInRsnaPlaybookValueSet.contains(loincNumber)) {
			vs
				.getCompose()
				.getIncludeFirstRep()
				.setSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
				.addConcept()
				.setCode(loincNumber)
				.setDisplay(longCommonName);
			myCodesInRsnaPlaybookValueSet.add(loincNumber);
		}

		String loincCodePropName;
		switch (partTypeName) {
			case "Rad.Anatomic Location.Region Imaged":
				loincCodePropName = "rad-anatomic-location-region-imaged";
				break;
			case "Rad.Anatomic Location.Imaging Focus":
				loincCodePropName = "rad-anatomic-location-imaging-focus";
				break;
			case "Rad.Modality.Modality type":
				loincCodePropName = "rad-modality-modality-type";
				break;
			case "Rad.Modality.Modality subtype":
				loincCodePropName = "rad-modality-modality-subtype";
				break;
			case "Rad.Anatomic Location.Laterality":
				loincCodePropName = "rad-anatomic-location-laterality";
				break;
			case "Rad.Anatomic Location.Laterality.Presence":
				loincCodePropName = "rad-anatomic-location-laterality-presence";
				break;
			case "Rad.Guidance for.Action":
				loincCodePropName = "rad-guidance-for-action";
				break;
			case "Rad.Guidance for.Approach":
				loincCodePropName = "rad-guidance-for-approach";
				break;
			case "Rad.Guidance for.Object":
				loincCodePropName = "rad-guidance-for-object";
				break;
			case "Rad.Guidance for.Presence":
				loincCodePropName = "rad-guidance-for-presence";
				break;
			case "Rad.Maneuver.Maneuver type":
				loincCodePropName = "rad-maneuver-maneuver-type";
				break;
			case "Rad.Pharmaceutical.Route":
				loincCodePropName = "rad-pharmaceutical-route";
				break;
			case "Rad.Pharmaceutical.Substance Given":
				loincCodePropName = "rad-pharmaceutical-substance-given";
				break;
			case "Rad.Reason for Exam":
				loincCodePropName = "rad-reason-for-exam";
				break;
			case "Rad.Subject":
				loincCodePropName = "rad-subject";
				break;
			case "Rad.Timing":
				loincCodePropName = "rad-timing";
				break;
			case "Rad.View.Aggregation":
				loincCodePropName = "rad-view-view-aggregation";
				break;
			case "Rad.View.View type":
				loincCodePropName = "rad-view-view-type";
				break;
			default:
				throw new InternalErrorException("Unknown PartTypeName: " + partTypeName);
		}

		TermConcept code = myCode2Concept.get(loincNumber);
		if (code != null) {
			code.addPropertyCoding(loincCodePropName, IHapiTerminologyLoaderSvc.LOINC_URI, partNumber, partName);
		}

		// LOINC Part -> Radlex RID code mappings
		if (isNotBlank(rid)) {
			addConceptMapEntry(
				new ConceptMapping()
					.setConceptMapId(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID)
					.setConceptMapUri(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_URI)
					.setConceptMapName(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_NAME)
					.setSourceCodeSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
					.setSourceCode(partNumber)
					.setSourceDisplay(partName)
					.setTargetCodeSystem(RID_CS_URI)
					.setTargetCode(rid)
					.setTargetDisplay(preferredName)
					.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL),
				CM_COPYRIGHT);
		}

		// LOINC Term -> Radlex RPID code mappings
		if (isNotBlank(rpid)) {
			addConceptMapEntry(
				new ConceptMapping()
					.setConceptMapId(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_ID)
					.setConceptMapUri(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_URI)
					.setConceptMapName(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_NAME)
					.setSourceCodeSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
					.setSourceCode(loincNumber)
					.setSourceDisplay(longCommonName)
					.setTargetCodeSystem(RPID_CS_URI)
					.setTargetCode(rpid)
					.setTargetDisplay(longName)
					.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL),
				CM_COPYRIGHT);
		}

	}


}
