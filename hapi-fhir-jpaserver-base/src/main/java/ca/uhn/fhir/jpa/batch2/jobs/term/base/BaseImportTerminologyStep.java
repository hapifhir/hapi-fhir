/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
