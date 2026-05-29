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

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface ITerminologyImportFileHandlerStep<
				PT extends BaseTerminologyImportParameters, IT extends IModelJson, OT extends IModelJson>
		extends IJobStepWorker<PT, IT, OT> {

	@Nonnull
	Optional<FileHandlingInstructions> canHandleFile(
			StepExecutionDetails<PT, VoidModel> theStepExecutionDetails, PT theJobParameters, String theFileName);

	enum FileHandlingType {
		CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS,
		CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
		TSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS
	}

	record FileHandlingInstructions(FileHandlingType fileHandlingType) {}
}
