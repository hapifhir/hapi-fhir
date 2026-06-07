package ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10cm;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.ImportIcdJobAppCtx;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;

/**
 * @see ImportIcdJobAppCtx#importIcd10CmStep1ExpandDistributionIntoFiles()
 */
public class ImportIcd10CmStep1ExpandDistributionIntoFilesStep
		extends BaseExpandDistributionIntoFilesStep<ImportTerminologyJobParameters, Void> {

	@Override
	protected Void newContextObject() {
		return null;
	}

	@Override
	protected boolean isIndividualFileAttachmentsSupported() {
		return true;
	}

	@Nonnull
	@Override
	protected String getDistributionFileName() {
		return TerminologyConstants.FILENAME_ICD10CM_DISTRIBUTION_FILE;
	}

	@Nonnull
	@Override
	protected String getCodeSystemIdRoot() {
		return "icd-10-cm";
	}

	@Override
	protected void massageCodeSystem(
			CodeSystem theCodeSystem,
			Void theContext,
			StepExecutionDetails<ImportTerminologyJobParameters, VoidModel> theStepExecutionDetails) {
		super.massageCodeSystem(theCodeSystem, theContext, theStepExecutionDetails);

		theCodeSystem.setName("ICD-10-CM");
	}
}
