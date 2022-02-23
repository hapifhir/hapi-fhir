package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

public enum MdmMatchResultEnum {

	/**
	 * Manually confirmed to not be a match.
	 */
	NO_MATCH,

	/**
	 * Enough of a match to warrant manual review.
	 */
	POSSIBLE_MATCH,

	/**
	 * Strong enough match to consider matched.
	 */
	MATCH,

	/**
	 * Link between two Golden Records resources indicating they may be duplicates.
	 */
	POSSIBLE_DUPLICATE,

	/**
	 * Link between Golden Record and Source Resource pointing to the Golden Record for that Source Resource
	 */
	GOLDEN_RECORD,

	/**
	 * Link between two Golden Resources resulting from a merge. One golden resource is deactivated. The inactive golden
	 * resource points to the active golden resource after the merge. The source resource points to the inactive golden
	 * resource after the merge.
	 */
	REDIRECT
	// Stored in database as ORDINAL.  Only add new values to bottom!
}
