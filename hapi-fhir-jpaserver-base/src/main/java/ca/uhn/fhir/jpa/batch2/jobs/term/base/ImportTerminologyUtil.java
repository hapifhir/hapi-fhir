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
			StepExecutionDetails<? extends ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		ImportTerminologyJobParameters jobParameters = theStepExecutionDetails.getParameters();
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
