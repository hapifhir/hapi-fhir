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

public class TerminologyConstants {

	/** Non-instantiable */
	private TerminologyConstants() {}

	public static final String FILENAME_LOINC_UPLOAD_PROPERTIES_FILE = "loincupload.properties";
	public static final String FILENAME_LOINC_DISTRIBUTION_FILE = "loinc.zip";
	public static final String FILENAME_SNOMED_CT_DISTRIBUTION_FILE = "snomedct.zip";
}
