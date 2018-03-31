package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
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

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartRelatedCodeMappingHandler extends BaseHandler implements IRecordHandler {

	public static final String LOINC_PART_MAP_ID = "LOINC-PART-MAP";
	public static final String LOINC_PART_MAP_URI = "http://loinc.org/fhir/loinc-part-map";
	public static final String LOINC_PART_MAP_NAME = "LOINC Part Map";
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final List<ConceptMap> myConceptMaps;

	public LoincPartRelatedCodeMappingHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		super(theCode2concept, theValueSets, theConceptMaps);
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myConceptMaps = theConceptMaps;
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
		switch (mapType) {
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
				throw new InternalErrorException("Unknown MapType: " + mapType);
		}

		addConceptMapEntry(
			new ConceptMapping()
				.setConceptMapId(LOINC_PART_MAP_ID)
				.setConceptMapUri(LOINC_PART_MAP_URI)
				.setConceptMapName(LOINC_PART_MAP_NAME)
				.setSourceCodeSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
				.setSourceCode(partNumber)
				.setSourceDisplay(partName)
				.setTargetCodeSystem(extCodeSystem)
				.setTargetCode(extCodeId)
				.setTargetDisplay(extCodeDisplayName)
				.setTargetCodeSystemVersion(extCodeSystemVersion)
				.setEquivalence(equivalence)
				.setCopyright(extCodeSystemCopyrightNotice));

	}

}
