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

public enum ImportTerminologyModeEnum {

	/**
	 * Specified codes are added to the existing codes.
	 */
	ADD,
	/**
	 * Specified codes are removed from the existing codes.
	 */
	REMOVE,
	/**
	 * Existing codes are removed and replaced with the new codes.
	 * This is the default mode.
	 */
	SNAPSHOT
}
