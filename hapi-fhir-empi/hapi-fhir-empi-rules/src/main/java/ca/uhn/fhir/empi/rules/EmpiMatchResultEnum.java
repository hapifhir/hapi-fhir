package ca.uhn.fhir.empi.rules;

/*-
 * #%L
 * hapi-fhir-empi-rules
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
	 * Match weight fell below threshold for a match.
	 */
	NO_MATCH,

	/**
	 * Match weight fell below low threshold and high threshold.  Requires manual review.
	 */
	POSSIBLE_MATCH,

	/**
	 * Match weight was above high threshold for a match
	 */
	MATCH

	// Stored in database as ORDINAL.  Only add new values to bottom!
}
