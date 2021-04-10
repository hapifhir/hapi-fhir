package ca.uhn.fhir.jpa.bulk.imp.api;

import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum;

import javax.annotation.Nonnull;
import java.util.List;

public interface IBulkDataImportSvc {

	/**
	 * Create a new job in {@link ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum#STAGING STAGING} state (meaning it won't yet be worked on and can be added to)
	 */
	String createNewJob(@Nonnull List<BulkImportJobFileJson> theInitialFiles);

	/**
	 * Add more files to a job in {@link ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum#STAGING STAGING} state
	 *
	 * @param theJobId The job ID
	 * @param theFiles The files to add to the job
	 */
	void addFilesToJob(String theJobId, List<BulkImportJobFileJson> theFiles);

	/**
	 * Move a job from {@link ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum#STAGING STAGING}
	 * state to {@link ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum#READY READY}
	 * state, meaning that is is a candidate to be picked up for processing
	 *
	 * @param theJobId The job ID
	 */
	void markJobAsReadyForActivation(String theJobId);

	/**
	 * This method is intended to be called from the job scheduler, and will begin execution on
	 * the next job in status {@link ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum#READY READY}
	 */
	void activateNextReadyJob();

	/**
	 * Updates the job status for the given job
	 */
	void setJobToStatus(String theJobId, BulkImportJobStatusEnum theStatus);

	/**
	 * Gets the number of files available for a given Job ID
	 *
	 * @param theJobId The job ID
	 * @return The file count
	 */
	int getFileCount(String theJobId);

	/**
	 * Fetch a given file by job ID
	 *
	 * @param theJobId     The job ID
	 * @param theFileIndex The index of the file within the job
	 * @return The file
	 */
	BulkImportJobFileJson fetchFile(String theJobId, int theFileIndex);
}
