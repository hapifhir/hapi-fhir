/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.api;

public enum MdmModeEnum {
	/**
	 * Normal MDM processing that creates Golden Resources and maintains links
	 */
	MATCH_AND_LINK,
	/**
	 * in MATCH_ONLY mode, MDM operations are disabled and no MDM processing occurs. This is useful if, for example,
	 * you want to use the Patient/$match operation without having the overhead of creating links and golden resources.
	 */
	MATCH_ONLY

	// TODO KHS possible future mode MATCH_AND_MERGE where incoming matched resources have their data merged into the
	// golden resource and then deleted with all references updated to point to the golden resource
}
