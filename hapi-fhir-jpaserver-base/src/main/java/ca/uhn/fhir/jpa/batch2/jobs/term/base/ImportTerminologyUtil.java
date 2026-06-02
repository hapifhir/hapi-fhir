package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.util.Properties;

public class ImportTerminologyUtil {

	private ImportTerminologyUtil() {
		// nothing
	}

	public static Properties getJobProperties(
			IJobPersistence theJobPersistence,
			StepExecutionDetails<? extends TerminologyImportParameters, ?> theStepExecutionDetails) {
		TerminologyImportParameters jobParameters = theStepExecutionDetails.getParameters();
		Properties retVal = jobParameters.getJobProperties();
		if (retVal == null) {
			String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
			retVal = new Properties();
			try {
				String filename = LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode();
				AttachmentDetails attachment = theJobPersistence.fetchAttachmentByFilename(instanceId, filename);
				retVal.load(attachment.getInputStream());
			} catch (ResourceNotFoundException | IOException e) {
				// no properties file was provided
			}

			jobParameters.setJobProperties(retVal);
		}
		return retVal;
	}
}
