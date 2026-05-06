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
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep10HandleTop2000CodesSi extends ImportLoincStep9HandleTop2000CodesUs {

	/**
	 * Constructor
	 */
	public ImportLoincStep10HandleTop2000CodesSi() {
		super(
			TOP_2000_SI_VS_ID,
			TOP_2000_SI_VS_URI,
			TOP_2000_SI_VS_NAME
		);
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNameDefault() {
		return LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT;
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNamePropertyFileKey() {
		return LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE;
	}

}
