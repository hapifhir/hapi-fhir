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
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.hl7.fhir.r4.model.CodeSystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newCsvParser;

public abstract class BaseImportTerminologyFileCsvStep<
				PT extends ImportTerminologyJobParameters, CT extends BaseImportTerminologyFileCsvStep.MyBaseContext>
		extends BaseImportTerminologyFileStep<PT, CT> {

	protected void processAttachment(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theContext,
			AttachmentDetails attachment,
			PT jobParameters,
			CodeSystem codeSystemToPopulate,
			TerminologyFileSetJson theData,
			String sourceFilename) {
		try (InputStream inputStream = attachment.getInputStream()) {
			InputStreamReader reader = new InputStreamReader(
					BOMInputStream.builder().setInputStream(inputStream).get(), StandardCharsets.UTF_8);

			/*
			 * Even if the source files use a delimiter other than comma, the expand step (step 1)
			 * splits the files up and rewrites them as CSV using a comma delimiter.
			 */
			CSVParser csvReader = newCsvParser(',', reader);
			for (CSVRecord record : csvReader.getRecords()) {
				handleRecord(
						theStepExecutionDetails,
						theJobMetadata,
						jobParameters,
						theContext,
						record,
						codeSystemToPopulate,
						theData,
						sourceFilename);
			}

		} catch (IOException e) {
			throw new JobExecutionFailedException(
					Msg.code(2941) + "Failed to read file attachment: " + e.getMessage(), e);
		}
	}

	protected abstract void handleRecord(
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			PT theJobParameters,
			CT theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename);
}
