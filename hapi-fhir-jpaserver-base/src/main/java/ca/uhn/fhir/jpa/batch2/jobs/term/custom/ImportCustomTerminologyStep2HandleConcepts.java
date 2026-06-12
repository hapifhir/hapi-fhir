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
package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyModeEnum;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.CUSTOM_CONCEPTS_FILE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportCustomTerminologyStep2HandleConcepts
		extends BaseImportTerminologyFileCsvStep<
				ImportTerminologyJobParameters, BaseImportTerminologyFileStep.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportCustomTerminologyStep2HandleConcepts.class);

	public static final String CODE = "CODE";
	public static final String DISPLAY = "DISPLAY";

	static final String FILENAME_COMPLETE_CONCEPTS_JSON_FILENAME = "complete-concepts.json";
	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();

	@Nonnull
	@Override
	public List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS, t -> t.endsWith(CUSTOM_CONCEPTS_FILE)));
	}

	/**
	 * It's valid to just supply properties or hierarchy without any concept definitions
	 */
	@Override
	public boolean mustFindFile() {
		return false;
	}

	@Override
	protected void processAttachment(
			@Nonnull
					StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson>
							theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			BaseImportTerminologyFileStep.MyBaseContext theContext,
			AttachmentDetails attachment,
			ImportTerminologyJobParameters jobParameters,
			CodeSystem codeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {

		if (FILENAME_COMPLETE_CONCEPTS_JSON_FILENAME.equals(theSourceFilename)) {
			ourLog.info("Handling complete concepts file directly for storage of inline concepts");
			try (InputStream inputStream = attachment.getInputStream()) {
				try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
					CodeSystem canonicalCodeSystem =
							myCanonicalFhirContext.newJsonParser().parseResource(CodeSystem.class, reader);
					if (jobParameters.getMode() == ImportTerminologyModeEnum.SNAPSHOT) {
						canonicalCodeSystem.setVersion(theJobMetadata.getCodeSystemStagingVersionId());
					}

					storeConceptsToDb(theStepExecutionDetails, theContext, canonicalCodeSystem);
				}
			} catch (IOException theE) {
				throw new RuntimeException(theE);
			}
		} else {
			super.processAttachment(
					theStepExecutionDetails,
					theJobMetadata,
					theContext,
					attachment,
					jobParameters,
					codeSystemToPopulate,
					theData,
					theSourceFilename);
		}
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportTerminologyJobParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {
		String code = trim(theRecord.get(CODE));
		if (isNotBlank(code)) {
			String display = trim(theRecord.get(DISPLAY));

			CodeSystem.ConceptDefinitionComponent concept = getOrAddConcept(theContext, code);
			concept.setDisplay(display);
		}
	}

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}
}
