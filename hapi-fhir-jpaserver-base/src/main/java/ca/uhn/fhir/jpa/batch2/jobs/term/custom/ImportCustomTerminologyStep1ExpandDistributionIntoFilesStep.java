package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.ICD10CM_URI;

public class ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep
		extends BaseExpandDistributionIntoFilesStep<ImportTerminologyJobParameters, Void> {

	@Override
	protected Void newContextObject() {
		return null;
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
	protected void massageCodeSystem(CodeSystem theCodeSystem) {
		super.massageCodeSystem(theCodeSystem);

		theCodeSystem.setUrl(ICD10CM_URI);
		theCodeSystem.setName("ICD-10-CM");
	}
}
