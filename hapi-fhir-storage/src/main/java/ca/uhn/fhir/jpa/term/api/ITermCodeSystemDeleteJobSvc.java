package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.term.models.CodeSystemConceptsDeleteResult;

import java.util.Iterator;

public interface ITermCodeSystemDeleteJobSvc {

	/**
	 * Gets an iterator for all code system version PIDs
	 * @param thePid
	 * @return
	 */
	Iterator<Long> getAllCodeSystemVersionForCodeSystemPid(long thePid);

	/**
	 * Deletes all metadata associated with a code system version
	 * Specific metadata deleted:
	 * * concept links
	 * * concept properties
	 * * concept designations
	 * * concepts
	 * @param theVersionPid - the version id of the code system to delete
	 * @return - a wrapper for the delete results of each of the deletes (if desired)
	 */
	CodeSystemConceptsDeleteResult deleteCodeSystemConceptsByCodeSystemVersionPid(long theVersionPid);

	/**
	 * Deletes a Code System Version
	 * NB: it is expected that any concepts related to the Code System Version are
	 * deleted first.
	 * @param theVersionPid - the code system version pid for the version to delete
	 */
	void deleteCodeSystemVersion(long theVersionPid);

	/**
	 * Deletes a code system.
	 * NB: it is expected that all code system versions are deleted first.
	 * @param thePid - the code system pid
	 */
	void deleteCodeSystem(long thePid);

	/**
	 * Notifies that the job has completed (or errored out).
	 * @param theJobId - the job id
	 */
	void notifyJobComplete(String theJobId);
}
