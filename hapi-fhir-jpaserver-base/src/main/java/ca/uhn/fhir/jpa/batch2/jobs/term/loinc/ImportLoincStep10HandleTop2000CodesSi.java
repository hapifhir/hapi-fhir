package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;

import java.util.List;

import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsSiHandler.TOP_2000_SI_VS_URI;

public class ImportLoincStep10HandleTop2000CodesSi extends ImportLoincStep9HandleTop2000CodesUs {

	/**
	 * Constructor
	 */
	public ImportLoincStep10HandleTop2000CodesSi() {
		super(TOP_2000_SI_VS_ID, TOP_2000_SI_VS_URI, TOP_2000_SI_VS_NAME);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE,
				LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT));
	}
}
