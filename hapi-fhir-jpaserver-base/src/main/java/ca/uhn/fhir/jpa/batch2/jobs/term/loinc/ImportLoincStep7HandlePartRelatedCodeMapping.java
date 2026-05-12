package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;

import java.util.List;

import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_PUBCHEM_PART_MAP_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_PUBCHEM_PART_MAP_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_PUBCHEM_PART_MAP_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_RXNORM_PART_MAP_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_RXNORM_PART_MAP_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_RXNORM_PART_MAP_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_SCT_PART_MAP_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_SCT_PART_MAP_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler.LOINC_SCT_PART_MAP_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.CM_COPYRIGHT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep7HandlePartRelatedCodeMapping
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess() {
		return List.of(new LoincFileNameSpecification(
				LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE,
				LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
			LoincJobImportParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			ImportLoincFileSetJson theData) {
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
		String codeSystemVersionId = theData.getLoincCodeSystem().getVersion();

		// ConceptMap version from properties files
		String loincPartMapVersion;
		if (codeSystemVersionId != null) {
			loincPartMapVersion = theJobParameters.getProperties().getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-"
					+ codeSystemVersionId;
		} else {
			loincPartMapVersion = theJobParameters.getProperties().getProperty(LOINC_CONCEPTMAP_VERSION.getCode());
		}

		Enumerations.ConceptMapEquivalence equivalence =
				switch (trim(defaultString(mapType))) {
					case "", "equivalent" ->
					// 'equal' is more exact than 'equivalent' in the equivalence codes
					Enumerations.ConceptMapEquivalence.EQUAL;
					case "narrower" -> Enumerations.ConceptMapEquivalence.NARROWER;
					case "wider" -> Enumerations.ConceptMapEquivalence.WIDER;
					case "relatedto" -> Enumerations.ConceptMapEquivalence.RELATEDTO;
					default -> throw new InternalErrorException(
							Msg.code(916) + "Unknown equivalence '" + mapType + "' for PartNumber: " + partNumber);
				};

		String loincPartMapId;
		String loincPartMapUri;
		String loincPartMapName;
		switch (extCodeSystem) {
			case ITermLoaderSvc.SCT_URI -> {
				loincPartMapId = LOINC_SCT_PART_MAP_ID;
				loincPartMapUri = LOINC_SCT_PART_MAP_URI;
				loincPartMapName = LOINC_SCT_PART_MAP_NAME;
			}
			case "http://www.nlm.nih.gov/research/umls/rxnorm" -> {
				loincPartMapId = LOINC_RXNORM_PART_MAP_ID;
				loincPartMapUri = LOINC_RXNORM_PART_MAP_URI;
				loincPartMapName = LOINC_RXNORM_PART_MAP_NAME;
			}
			case "http://www.radlex.org" -> {
				loincPartMapId = LOINC_PART_TO_RID_PART_MAP_ID;
				loincPartMapUri = LOINC_PART_TO_RID_PART_MAP_URI;
				loincPartMapName = LOINC_PART_TO_RID_PART_MAP_NAME;
			}
			case "http://pubchem.ncbi.nlm.nih.gov" -> {
				loincPartMapId = LOINC_PUBCHEM_PART_MAP_ID;
				loincPartMapUri = LOINC_PUBCHEM_PART_MAP_URI;
				loincPartMapName = LOINC_PUBCHEM_PART_MAP_NAME;
			}
			default -> {
				loincPartMapId = extCodeSystem.replaceAll("[^a-zA-Z]", "");
				loincPartMapUri = extCodeSystem;
				loincPartMapName = "Unknown Mapping";
			}
		}

		String conceptMapVersion = theData.getLoincCodeSystem().getVersion();
		if (isNotBlank(conceptMapVersion)) {
			loincPartMapId += "-" + conceptMapVersion;
		}

		if (isBlank(extCodeSystemCopyrightNotice)) {
			extCodeSystemCopyrightNotice = CM_COPYRIGHT;
		}

		addConceptMapEntry(
				theData,
				theContext,
				new ConceptMapping()
						.setConceptMapId(loincPartMapId)
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
						.setCopyright(extCodeSystemCopyrightNotice));
	}

}
