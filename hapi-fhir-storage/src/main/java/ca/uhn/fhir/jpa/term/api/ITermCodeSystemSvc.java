package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.term.models.DeleteLinksPropertiesAndDesignationsResult;

import java.util.Iterator;

public interface ITermCodeSystemSvc {

	/**
	 * Gets an iterator for all code system version PIDs
	 * @param thePid
	 * @return
	 */
	Iterator<Long> getAllCodeSystemVersionForCodeSystemPid(long thePid);

	/**
	 * Deletes all child links, concept properties and concept designations for the
	 * provided code system PID
	 * @param theCodeSystemVersionPid
	 * @return
	 */
	DeleteLinksPropertiesAndDesignationsResult deleteLinksPropertiesAndDesignationsByCodeSystemVersionPID(long theCodeSystemVersionPid);

	/**
	 * see BatchTermConceptsDeleteWriter
	 * @param theVersionPid
	 */
	void deleteCodeSystemConceptsByVersion(long theVersionPid);

	/**
	 * see BatchTermCodeSystemVersionDeleteWriter
	 * @param theVersionPid
	 */
	void deleteCodeSystemVersion(long theVersionPid);

	/**
	 * see TermCodeSystemDeleteTasklet
	 * @param thePid
	 */
	void deleteCodeSystem(long thePid);
}
