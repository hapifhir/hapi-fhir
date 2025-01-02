/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.primitive.IdDt;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

public class FhirIdJson implements IModelJson {

	@JsonProperty("type")
	private String myResourceType;

	@JsonProperty("id")
	private String myFhirId;

	// Jackson needs an empty constructor
	public FhirIdJson() {}

	public FhirIdJson(String theResourceType, String theFhirId) {
		myResourceType = theResourceType;
		myFhirId = theFhirId;
	}

	public FhirIdJson(IIdType theFhirId) {
		myResourceType = theFhirId.getResourceType();
		myFhirId = theFhirId.getIdPart();
	}

	@Override
	public String toString() {
		return myResourceType + "/" + myFhirId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public FhirIdJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getFhirId() {
		return myFhirId;
	}

	public FhirIdJson setFhirId(String theFhirId) {
		myFhirId = theFhirId;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		FhirIdJson id = (FhirIdJson) theO;

		return new EqualsBuilder()
				.append(myResourceType, id.myResourceType)
				.append(myFhirId, id.myFhirId)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(myResourceType)
				.append(myFhirId)
				.toHashCode();
	}

	public IdDt asIdDt() {
		return new IdDt(myResourceType, myFhirId);
	}
}
