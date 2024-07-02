/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.util.JsonDateDeserializer;
import ca.uhn.fhir.rest.server.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Date;

public class ChunkRangeJson implements IModelJson {
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("start")
	@Nonnull
	private Date myStart;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("end")
	@Nonnull
	private Date myEnd;

	@Nullable
	@JsonProperty("url")
	private String myUrl;

	@JsonProperty("resourceType")
	private String myResourceType;

	@Nullable
	@JsonProperty("partitionId")
	private RequestPartitionId myPartitionId;

	public ChunkRangeJson() {}

	public ChunkRangeJson(@Nonnull Date theStart, @Nonnull Date theEnd) {
		this.myStart = theStart;
		this.myEnd = theEnd;
	}

	@Nonnull
	public Date getStart() {
		return myStart;
	}

	@Nonnull
	public Date getEnd() {
		return myEnd;
	}

	@Nullable
	public String getUrl() {
		return myUrl;
	}

	public ChunkRangeJson setUrl(@Nullable String theUrl) {
		myUrl = theUrl;
		return this;
	}

	@Nonnull
	public String getResourceType() {
		return myResourceType;
	}

	public ChunkRangeJson setResourceType(@Nullable String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	@Nullable
	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public ChunkRangeJson setPartitionId(@Nullable RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
		return this;
	}
}
