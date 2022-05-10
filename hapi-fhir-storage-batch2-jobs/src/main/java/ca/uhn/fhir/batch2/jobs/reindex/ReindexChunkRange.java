package ca.uhn.fhir.batch2.jobs.reindex;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.util.JsonDateDeserializer;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public class ReindexChunkRange implements IModelJson {

	@Nullable
	@JsonProperty("url")
	private String myUrl;

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
	public String getUrl() {
		return myUrl;
	}

	public void setUrl(@Nullable String theUrl) {
		myUrl = theUrl;
	}

	@Nonnull
	public Date getStart() {
		return myStart;
	}

	public ReindexChunkRange setStart(@Nonnull Date theStart) {
		myStart = theStart;
		return this;
	}

	@Nonnull
	public Date getEnd() {
		return myEnd;
	}

	public ReindexChunkRange setEnd(@Nonnull Date theEnd) {
		myEnd = theEnd;
		return this;
	}

}
