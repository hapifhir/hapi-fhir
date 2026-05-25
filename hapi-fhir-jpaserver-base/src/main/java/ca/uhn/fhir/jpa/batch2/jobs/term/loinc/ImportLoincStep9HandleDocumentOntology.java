package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.DOCUMENT_ONTOLOGY_CODES_VS_ID;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.DOCUMENT_ONTOLOGY_CODES_VS_NAME;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.DOCUMENT_ONTOLOGY_CODES_VS_URI;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep9HandleDocumentOntology
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep9HandleDocumentOntology.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep9HandleDocumentOntology.class);

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
				LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE,
				LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportLoincJobParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {
		String loincNumber = trim(theRecord.get("LoincNumber"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partSequenceOrder = trim(theRecord.get("PartSequenceOrder"));
		String partName = trim(theRecord.get("PartName"));

		IValidationSupport.LookupCodeResult outcome = lookupPreExistingConcept(theJobMetadata, loincNumber);
		if (outcome == null || !outcome.isFound()) {
			return;
		}

		// Document Ontology Codes VS
		ValueSet vs = getOrAddValueSet(
				theStepExecutionDetails,
				theJobMetadata,
                theData,
				theContext,
				DOCUMENT_ONTOLOGY_CODES_VS_ID,
				DOCUMENT_ONTOLOGY_CODES_VS_URI,
				DOCUMENT_ONTOLOGY_CODES_VS_NAME,
				null);
		addCodeAsIncludeToValueSet(vs, ITermLoaderSvc.LOINC_URI, loincNumber, null);

		// Part Properties
		String loincCodePropName =
				switch (partTypeName) {
					case "Document.Kind" -> "document-kind";
					case "Document.Role" -> "document-role";
					case "Document.Setting" -> "document-setting";
					case "Document.SubjectMatterDomain" -> "document-subject-matter-domain";
					case "Document.TypeOfService" -> "document-type-of-service";
					default -> throw new InternalErrorException(
							Msg.code(917) + "Unknown PartTypeName: " + partTypeName);
				};

		ourLog.debug("Adding coding property: {} to concept.code {}", loincCodePropName, partNumber);

		CodeSystem.ConceptDefinitionComponent concept =
				getOrAddConcept(theContext, theCodeSystemToPopulate, loincNumber);
		concept.addProperty()
				.setCode(loincCodePropName)
				.setValue(new Coding(ITermLoaderSvc.LOINC_URI, partNumber, partName));
	}
}
