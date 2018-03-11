package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartRelatedCodeMappingHandler implements IRecordHandler {

	public static final String LOINC_TO_SNOMED_CM_ID = "LOINC_TO_SNOMED_CM";
	private static final Logger ourLog = LoggerFactory.getLogger(LoincPartRelatedCodeMappingHandler.class);
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final List<ConceptMap> myConceptMaps;

	public LoincPartRelatedCodeMappingHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, List<ConceptMap> theConceptMaps) {
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

		ConceptMap conceptMap;
		if (extCodeSystem.equals(IHapiTerminologyLoaderSvc.SCT_URL)) {
			conceptMap = findOrAddCodeSystem(LOINC_TO_SNOMED_CM_ID, "http://loinc.org/loinc-to-snomed", extCodeSystem, extCodeSystemCopyrightNotice);
		} else {
			throw new InternalErrorException("Unknown external code system ID: " + extCodeSystem);
		}


		ConceptMap.ConceptMapGroupComponent group = null;
		for (ConceptMap.ConceptMapGroupComponent next : conceptMap.getGroup()) {
			if (next.getTarget().equals(extCodeSystem)) {
				if (defaultIfBlank(next.getTargetVersion(), "").equals(defaultIfBlank(extCodeSystemVersion, ""))) {
					group = next;
					break;
				}
			}
		}

		if (group == null) {
			group = conceptMap.addGroup();
			group.setSource(IHapiTerminologyLoaderSvc.LOINC_URL);
			group.setTarget(extCodeSystem);
			group.setTargetVersion(defaultIfBlank(extCodeSystemVersion, null));
		}

		ConceptMap.SourceElementComponent element = null;
		for (ConceptMap.SourceElementComponent next : group.getElement()) {
			if (next.getCode().equals(partNumber)) {
				element = next;
				break;
			}
		}

		if (element == null) {
			element = group
				.addElement()
				.setCode(partNumber)
				.setDisplay(partName);
		}

		ConceptMap.TargetElementComponent target = element
			.addTarget()
			.setCode(extCodeId)
			.setDisplay(extCodeDisplayName);

		switch (mapType) {
			case "Exact":
				// 'equal' is more exact than 'equivalent' in the equivalence codes
				target.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL);
				break;
			case "LOINC broader":
				target.setEquivalence(Enumerations.ConceptMapEquivalence.NARROWER);
				break;
			case "LOINC narrower":
				target.setEquivalence(Enumerations.ConceptMapEquivalence.WIDER);
				break;
			default:
				throw new InternalErrorException("Unknown MapType: " + mapType);
		}


	}

	private ConceptMap findOrAddCodeSystem(String theId, String theUri, String theTargetCodeSystem, String theTargetCopyright) {
		for (ConceptMap next : myConceptMaps) {
			if (next.getId().equals(theId)) {
				return next;
			}
		}

		ConceptMap cm = new ConceptMap();
		cm.setId(theId);
		cm.setUrl(theUri);
		cm.setSource(new CanonicalType(IHapiTerminologyLoaderSvc.LOINC_URL));
		cm.setTarget(new CanonicalType(theTargetCodeSystem));
		cm.setCopyright(theTargetCopyright);
		myConceptMaps.add(cm);
		return cm;
	}
}
