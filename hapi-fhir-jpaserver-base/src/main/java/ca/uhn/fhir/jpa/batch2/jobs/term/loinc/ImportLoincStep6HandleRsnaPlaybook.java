package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincPartRelatedCodeMappingHandler;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.CM_COPYRIGHT;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.RID_CS_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.RPID_CS_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.RSNA_CODES_VS_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.RSNA_CODES_VS_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.RSNA_CODES_VS_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

// FIXME: make sure we don't expand ValueSets until status = active
public class ImportLoincStep6HandleRsnaPlaybook extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep6HandleRsnaPlaybook.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep6HandleRsnaPlaybook.class);

	@Override
	protected MyContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNameDefault() {
		return LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNamePropertyFileKey() {
		return LOINC_RSNA_PLAYBOOK_FILE;
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
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
		String codeSystemVersionId = theData.getLoincCodeSystem().getVersion();

		// ConceptMap version from properties files
		String loincRsnaCmVersion;
		if (codeSystemVersionId != null) {
			loincRsnaCmVersion =
				theJobParameters.getProperties().getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-" + codeSystemVersionId;
		} else {
			loincRsnaCmVersion = theJobParameters.getProperties().getProperty(LOINC_CONCEPTMAP_VERSION.getCode());
		}

		// RSNA Codes VS
		ValueSet vs = getValueSet(theJobParameters, theData, theContext, RSNA_CODES_VS_ID, RSNA_CODES_VS_URI, RSNA_CODES_VS_NAME, null);

		if (!theContext.getCodesInRsnaPlaybookValueSet().contains(loincNumber)) {
			vs.getCompose()
				.getIncludeFirstRep()
				.setSystem(ITermLoaderSvc.LOINC_URI)
				.setVersion(codeSystemVersionId)
				.addConcept()
				.setCode(loincNumber)
				.setDisplay(longCommonName);
			theContext.getCodesInRsnaPlaybookValueSet().add(loincNumber);
		}

		String loincCodePropName = switch (partTypeName.toLowerCase()) {
			case "rad.anatomic location.region imaged" -> "rad-anatomic-location-region-imaged";
			case "rad.anatomic location.imaging focus" -> "rad-anatomic-location-imaging-focus";
			case "rad.modality.modality type" -> "rad-modality-modality-type";
			case "rad.modality.modality subtype" -> "rad-modality-modality-subtype";
			case "rad.anatomic location.laterality" -> "rad-anatomic-location-laterality";
			case "rad.anatomic location.laterality.presence" -> "rad-anatomic-location-laterality-presence";
			case "rad.guidance for.action" -> "rad-guidance-for-action";
			case "rad.guidance for.approach" -> "rad-guidance-for-approach";
			case "rad.guidance for.object" -> "rad-guidance-for-object";
			case "rad.guidance for.presence" -> "rad-guidance-for-presence";
			case "rad.maneuver.maneuver type" -> "rad-maneuver-maneuver-type";
			case "rad.pharmaceutical.route" -> "rad-pharmaceutical-route";
			case "rad.pharmaceutical.substance given" -> "rad-pharmaceutical-substance-given";
			case "rad.reason for exam" -> "rad-reason-for-exam";
			case "rad.subject" -> "rad-subject";
			case "rad.timing" -> "rad-timing";
			case "rad.view.aggregation" -> "rad-view-view-aggregation";
			case "rad.view.view type" -> "rad-view-view-type";
			default -> throw new InternalErrorException(Msg.code(912) + "Unknown PartTypeName: " + partTypeName);
		};

		CodeSystem.ConceptDefinitionComponent code = getOrAddConcept(theContext, theCodeSystemToPopulate, loincNumber);
		code.addProperty()
			.setCode(loincCodePropName)
			.setValue(new Coding(ITermLoaderSvc.LOINC_URI, partNumber, partName));

		String partConceptMapId;
		String termConceptMapId;
		if (codeSystemVersionId != null) {
			partConceptMapId =
				LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID + "-" + codeSystemVersionId;
			termConceptMapId =
				LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_ID + "-" + codeSystemVersionId;
		} else {
			partConceptMapId = LoincPartRelatedCodeMappingHandler.LOINC_PART_TO_RID_PART_MAP_ID;
			termConceptMapId = LoincPartRelatedCodeMappingHandler.LOINC_TERM_TO_RPID_PART_MAP_ID;
		}

		// LOINC Part -> Radlex RID code mappings
		if (isNotBlank(rid)) {
			addConceptMapEntry(
				theData,
				theContext,
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
					.setCopyright(CM_COPYRIGHT)
				);
		}

		// LOINC Term -> Radlex RPID code mappings
		if (isNotBlank(rpid)) {
			addConceptMapEntry(
				theData,
				theContext,
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
					.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL)
					.setCopyright(CM_COPYRIGHT)
			);
		}
	}


	protected static class MyContext extends MyBaseContext {

		private final Set<String> myCodesInRsnaPlaybookValueSet = new HashSet<>();

		public Set<String> getCodesInRsnaPlaybookValueSet() {
			return myCodesInRsnaPlaybookValueSet;
		}
	}

}
