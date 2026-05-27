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
package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseTerminologyImportParameters;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Properties;

public class ImportLoincJobParameters extends BaseTerminologyImportParameters {

	@JsonIgnore
	private Properties myJobProperties;

	public Properties getJobProperties() {
		return myJobProperties;
	}

	public void setJobProperties(Properties theJobProperties) {
		myJobProperties = theJobProperties;
	}
}
