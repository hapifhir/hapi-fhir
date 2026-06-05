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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.CodeSystem;

public class ImportTerminologyMetadataAttachmentJson implements IModelJson {

	public static final String ATTACHMENT_FILENAME = "metadata.json";

	@JsonProperty("codeSystemXml")
	private String myCodeSystemXml;

	@JsonProperty("codeSystemStagingVersionId")
	private String myCodeSystemStagingVersionId;

	@JsonIgnore
	private CodeSystem myCodeSystemParsed;

	public CodeSystem getCodeSystem() {
		if (myCodeSystemParsed == null && getCodeSystemXml() != null) {
			myCodeSystemParsed =
					FhirContext.forR4Cached().newXmlParser().parseResource(CodeSystem.class, getCodeSystemXml());
		}
		return myCodeSystemParsed;
	}

	public void setCodeSystem(@Nonnull CodeSystem theCodeSystem) {
		setCodeSystemXml(FhirContext.forR4Cached().newXmlParser().encodeResourceToString(theCodeSystem));
		myCodeSystemParsed = theCodeSystem;
	}

	public String getCodeSystemXml() {
		return myCodeSystemXml;
	}

	public void setCodeSystemXml(String theCodeSystemXml) {
		myCodeSystemXml = theCodeSystemXml;
		myCodeSystemParsed = null;
	}

	public String getCodeSystemStagingVersionId() {
		return myCodeSystemStagingVersionId;
	}

	public void setCodeSystemStagingVersionId(String theCodeSystemStagingVersionId) {
		myCodeSystemStagingVersionId = theCodeSystemStagingVersionId;
	}
}
