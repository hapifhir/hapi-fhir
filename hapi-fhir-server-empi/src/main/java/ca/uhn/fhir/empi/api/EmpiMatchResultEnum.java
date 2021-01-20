package ca.uhn.fhir.empi.api;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

public enum EmpiMatchResultEnum {
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
	 * Link between two Person resources indicating they may be duplicates.
	 */
	POSSIBLE_DUPLICATE,

	/**
	 * Link between Person and Target pointing to the Golden Record for that Person
	 */

	GOLDEN_RECORD,

	/**
	 * Link between two Person resources resulting from a merge.  The Person points to the active person after the merge
	 * and the Target points to the inactive person after the merge.
	 */

	REDIRECT
	// Stored in database as ORDINAL.  Only add new values to bottom!
}
