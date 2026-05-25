package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class BaseImportTerminologyStep {

	@Autowired
	protected IJobPersistence myJobPersistence;

	/**
	 * Constructor
	 */
	public BaseImportTerminologyStep() {
		super();
	}

	/**
	 * Constructor
	 */
	public BaseImportTerminologyStep(@Nonnull IJobPersistence theJobPersistence) {
		Validate.notNull(theJobPersistence, "theJobPersistence must not be null");
		myJobPersistence = theJobPersistence;
	}

	protected ImportTerminologyMetadataAttachmentJson getJobMetadata(String jobInstanceId) {
		AttachmentDetails jobMetadataAttachment = myJobPersistence.fetchAttachmentByFilename(
				jobInstanceId, ImportTerminologyMetadataAttachmentJson.ATTACHMENT_FILENAME);
		ImportTerminologyMetadataAttachmentJson jobMetadata;
		try {
			jobMetadata = JsonUtil.deserialize(
					jobMetadataAttachment.getInputStream(), ImportTerminologyMetadataAttachmentJson.class);
		} catch (IOException e) {
			throw new JobExecutionFailedException(Msg.code(2940) + "Failed to retrieve job metadata attachment: " + e);
		}
		return jobMetadata;
	}
}
