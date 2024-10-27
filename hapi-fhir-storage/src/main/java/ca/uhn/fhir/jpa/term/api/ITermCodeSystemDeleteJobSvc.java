/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
