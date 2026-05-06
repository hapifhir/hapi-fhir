package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.uhn.fhir.jpa.term.loinc.LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincDocumentOntologyHandler.DOCUMENT_ONTOLOGY_CODES_VS_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.*;
import static org.apache.commons.lang3.StringUtils.*;

public class ImportLoincStep8HandleDocumentOntology extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep8HandleDocumentOntology.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep8HandleDocumentOntology.class);

	@Override
	protected MyContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNameDefault() {
		return LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNamePropertyFileKey() {
		return LOINC_DOCUMENT_ONTOLOGY_FILE;
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
		String loincNumber = trim(theRecord.get("LoincNumber"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partSequenceOrder = trim(theRecord.get("PartSequenceOrder"));
		String partName = trim(theRecord.get("PartName"));

		// Document Ontology Codes VS
		ValueSet vs = getValueSet(theJobParameters, theData, theContext, DOCUMENT_ONTOLOGY_CODES_VS_ID, DOCUMENT_ONTOLOGY_CODES_VS_URI, DOCUMENT_ONTOLOGY_CODES_VS_NAME, null);
		addCodeAsIncludeToValueSet(vs, ITermLoaderSvc.LOINC_URI, loincNumber, null);

		// Part Properties
		String loincCodePropName = switch (partTypeName) {
			case "Document.Kind" -> "document-kind";
			case "Document.Role" -> "document-role";
			case "Document.Setting" -> "document-setting";
			case "Document.SubjectMatterDomain" -> "document-subject-matter-domain";
			case "Document.TypeOfService" -> "document-type-of-service";
			default -> throw new InternalErrorException(Msg.code(917) + "Unknown PartTypeName: " + partTypeName);
		};

		ourLog.debug("Adding coding property: {} to concept.code {}", loincCodePropName, partNumber);

		CodeSystem.ConceptDefinitionComponent concept = getOrAddConcept(theContext, theCodeSystemToPopulate, loincNumber);
		concept.addProperty()
			.setCode(loincCodePropName)
			.setValue(new Coding(ITermLoaderSvc.LOINC_URI, partNumber, partName));
	}

	protected static class MyContext extends MyBaseContext {
		// nothing
	}

}
