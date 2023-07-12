/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.util.JsonDateDeserializer;
import ca.uhn.fhir.rest.server.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import javax.annotation.Nonnull;

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

	@Nonnull
	public Date getStart() {
		return myStart;
	}

	public ChunkRangeJson setStart(@Nonnull Date theStart) {
		myStart = theStart;
		return this;
	}

	@Nonnull
	public Date getEnd() {
		return myEnd;
	}

	public ChunkRangeJson setEnd(@Nonnull Date theEnd) {
		myEnd = theEnd;
		return this;
	}
}
