/*-
 * #%L
 * hapi-fhir-storage-mdm
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MdmChunkRangeJson extends ChunkRangeJson {
	@Nonnull
	@JsonProperty("resourceType")
	private String myResourceType;

	@Nonnull
	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(@Nullable String theResourceType) {
		myResourceType = theResourceType;
	}
}
