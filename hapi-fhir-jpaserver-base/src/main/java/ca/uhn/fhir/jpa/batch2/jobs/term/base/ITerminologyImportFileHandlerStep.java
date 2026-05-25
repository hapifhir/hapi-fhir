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
		CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS,
		/**
		 * The file is a CSV file with a header row. It should be split into chunks, with
		 * the header repeated in each chunk.
		 */
		CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS
	}

	record FileHandlingInstructions(FileHandlingType fileHandlingType) {}
}
