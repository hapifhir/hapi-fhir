package ca.uhn.fhir.jpa.search.reindex;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public interface IResourceReindexingSvc {

	/**
	 * Marks all indexes as needing fresh indexing
	 *
	 * @return Returns the job ID
	 */
	Long markAllResourcesForReindexing();

	/**
	 * Marks all indexes of the given type as needing fresh indexing
	 *
	 * @return Returns the job ID
	 */
	Long markAllResourcesForReindexing(String theType);

	/**
	 * @return Returns null if the system did not attempt to perform a pass because one was
	 * already proceeding. Otherwise, returns the number of resources affected.
	 */
	Integer runReindexingPass();

	/**
	 * Does the same thing as {@link #runReindexingPass()} but makes sure to perform at
	 * least one pass even if one is half finished
	 */
	int forceReindexingPass();

	/**
	 * Cancels all running and future reindexing jobs. This is mainly intended
	 * to be used by unit tests.
	 */
	void cancelAndPurgeAllJobs();

	int countReindexJobs();
}
