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
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.*;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincRsnaPlaybookHandler extends BaseLoincHandler implements IZipContentsHandlerCsv {

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
	private static final String CM_COPYRIGHT = "The LOINC/RSNA Radiology Playbook and the LOINC Part File contain content from RadLex® (http://rsna.org/RadLex.aspx), copyright © 2005-2017, The Radiological Society of North America, Inc., available at no cost under the license at http://www.rsna.org/uploadedFiles/RSNA/Content/Informatics/RadLex_License_Agreement_and_Terms_of_Use_V2_Final.pdf.";
	private final Map<String, TermConcept> myCode2Concept;
	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
	private final Set<String> myCodesInRsnaPlaybookValueSet = new HashSet<>();

	/**
	 * Constructor
	 */
	public LoincRsnaPlaybookHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets,
			List<ConceptMap> theConceptMaps, Properties theUploadProperties, String theCopyrightStatement) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties, theCopyrightStatement);
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

		// CodeSystem version from properties file
		String codeSystemVersionId = myUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());

		// ConceptMap version from properties files
		String loincRsnaCmVersion;
		if (codeSystemVersionId != null) {
			loincRsnaCmVersion = myUploadProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-" + codeSystemVersionId;
		} else {
			loincRsnaCmVersion = myUploadProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode());
		}


		// RSNA Codes VS
		ValueSet vs;
		String rsnaCodesValueSetId;
		if (codeSystemVersionId != null) {
			rsnaCodesValueSetId = RSNA_CODES_VS_ID + "-" + codeSystemVersionId;
		} else {
			rsnaCodesValueSetId = RSNA_CODES_VS_ID;
		}
		if (!myIdToValueSet.containsKey(rsnaCodesValueSetId)) {
			vs = new ValueSet();
			vs.setUrl(RSNA_CODES_VS_URI);
			vs.setId(rsnaCodesValueSetId);
			vs.setName(RSNA_CODES_VS_NAME);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			vs.setVersion(codeSystemVersionId);
			myIdToValueSet.put(rsnaCodesValueSetId, vs);
			myValueSets.add(vs);
		} else {
			vs = myIdToValueSet.get(rsnaCodesValueSetId);
		}

		if (!myCodesInRsnaPlaybookValueSet.contains(loincNumber)) {
			vs
				.getCompose()
				.getIncludeFirstRep()
				.setSystem(ITermLoaderSvc.LOINC_URI)
				.setVersion(codeSystemVersionId)
				.addConcept()
				.setCode(loincNumber)
				.setDisplay(longCommonName);
			myCodesInRsnaPlaybookValueSet.add(loincNumber);
		}

		String loincCodePropName;
		switch (partTypeName.toLowerCase()) {
			case "rad.anatomic location.region imaged":
				loincCodePropName = "rad-anatomic-location-region-imaged";
				break;
			case "rad.anatomic location.imaging focus":
				loincCodePropName = "rad-anatomic-location-imaging-focus";
				break;
			case "rad.modality.modality type":
				loincCodePropName = "rad-modality-modality-type";
				break;
			case "rad.modality.modality subtype":
				loincCodePropName = "rad-modality-modality-subtype";
				break;
			case "rad.anatomic location.laterality":
				loincCodePropName = "rad-anatomic-location-laterality";
				break;
			case "rad.anatomic location.laterality.presence":
				loincCodePropName = "rad-anatomic-location-laterality-presence";
				break;
			case "rad.guidance for.action":
				loincCodePropName = "rad-guidance-for-action";
				break;
			case "rad.guidance for.approach":
				loincCodePropName = "rad-guidance-for-approach";
				break;
			case "rad.guidance for.object":
				loincCodePropName = "rad-guidance-for-object";
				break;
			case "rad.guidance for.presence":
				loincCodePropName = "rad-guidance-for-presence";
				break;
			case "rad.maneuver.maneuver type":
				loincCodePropName = "rad-maneuver-maneuver-type";
				break;
			case "rad.pharmaceutical.route":
				loincCodePropName = "rad-pharmaceutical-route";
				break;
			case "rad.pharmaceutical.substance given":
				loincCodePropName = "rad-pharmaceutical-substance-given";
				break;
			case "rad.reason for exam":
				loincCodePropName = "rad-reason-for-exam";
				break;
			case "rad.subject":
				loincCodePropName = "rad-subject";
				break;
			case "rad.timing":
				loincCodePropName = "rad-timing";
				break;
			case "rad.view.aggregation":
				loincCodePropName = "rad-view-view-aggregation";
				break;
			case "rad.view.view type":
				loincCodePropName = "rad-view-view-type";
				break;
			default:
				throw new InternalErrorException(Msg.code(912) + "Unknown PartTypeName: " + partTypeName);
		}

		TermConcept code = myCode2Concept.get(loincNumber);
		if (code != null) {
			code.addPropertyCoding(loincCodePropName, ITermLoaderSvc.LOINC_URI, partNumber, partName);
		}

		String partConceptMapId;
		String termConceptMapId;
		if (codeSystemVersionId != null) {
			partConceptMapId = LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID + "-" + codeSystemVersionId;
			termConceptMapId = LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_ID + "-" + codeSystemVersionId;
		} else {
			partConceptMapId = LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID;
			termConceptMapId = LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_ID;
		}

		// LOINC Part -> Radlex RID code mappings
		if (isNotBlank(rid)) {
			addConceptMapEntry(
				new ConceptMapping()
					.setConceptMapId(partConceptMapId)
					.setConceptMapUri(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_URI)
					.setConceptMapVersion(loincRsnaCmVersion)
					.setConceptMapName(LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_NAME)
					.setSourceCodeSystem(ITermLoaderSvc.LOINC_URI)
					.setSourceCodeSystemVersion(codeSystemVersionId)
					.setSourceCode(partNumber)
					.setSourceDisplay(partName)
					.setTargetCodeSystem(RID_CS_URI)
					.setTargetCode(rid)
					.setTargetDisplay(preferredName)
					.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL)
			,myLoincCopyrightStatement + " " + CM_COPYRIGHT);
		}

		// LOINC Term -> Radlex RPID code mappings
		if (isNotBlank(rpid)) {
			addConceptMapEntry(
				new ConceptMapping()
					.setConceptMapId(termConceptMapId)
					.setConceptMapUri(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_URI)
					.setConceptMapVersion(loincRsnaCmVersion)
					.setConceptMapName(LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_NAME)
					.setSourceCodeSystem(ITermLoaderSvc.LOINC_URI)
					.setSourceCodeSystemVersion(codeSystemVersionId)
					.setSourceCode(loincNumber)
					.setSourceDisplay(longCommonName)
					.setTargetCodeSystem(RPID_CS_URI)
					.setTargetCode(rpid)
					.setTargetDisplay(longName)
					.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL),
				myLoincCopyrightStatement + " " + CM_COPYRIGHT);
		}

	}


}
