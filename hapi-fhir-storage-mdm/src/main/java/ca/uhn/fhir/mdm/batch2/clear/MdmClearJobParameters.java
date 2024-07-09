/*-
 * #%L
 * hapi-fhir-storage-mdm
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.batch2.clear;

import ca.uhn.fhir.batch2.jobs.parameters.JobParameters;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

public class MdmClearJobParameters extends JobParameters {
	@JsonProperty("resourceType")
	@Nonnull
	private List<@Pattern(regexp = "^[A-Z][A-Za-z]+$", message = "If populated, must be a valid resource type'") String>
			myResourceNames;

	public List<String> getResourceNames() {
		if (myResourceNames == null) {
			myResourceNames = new ArrayList<>();
		}
		return myResourceNames;
	}

	public MdmClearJobParameters addResourceType(@Nonnull String theResourceName) {
		Validate.notNull(theResourceName);
		getResourceNames().add(theResourceName);
		return this;
	}

	public void setResourceNames(@Nonnull List<String> theResourceNames) {
		myResourceNames = theResourceNames;
	}
}
