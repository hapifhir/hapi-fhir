/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.binary.api;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.util.JsonDateDeserializer;
import ca.uhn.fhir.rest.server.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.hash.HashingInputStream;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class StoredDetails implements IModelJson {

	@JsonProperty("binaryContentId")
	private String myBinaryContentId;

	@JsonProperty("bytes")
	private long myBytes;

	@JsonProperty("contentType")
	private String myContentType;

	@JsonProperty("hash")
	private String myHash;

	@JsonProperty("published")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myPublished;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unused")
	public StoredDetails() {
		super();
	}

	/**
	 * Constructor
	 */
	public StoredDetails(
			@Nonnull String theBinaryContentId,
			long theBytes,
			@Nonnull String theContentType,
			HashingInputStream theIs,
			Date thePublished) {
		myBinaryContentId = theBinaryContentId;
		myBytes = theBytes;
		myContentType = theContentType;
		myHash = theIs.hash().toString();
		myPublished = thePublished;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("binaryContentId", myBinaryContentId)
				.append("bytes", myBytes)
				.append("contentType", myContentType)
				.append("hash", myHash)
				.append("published", myPublished)
				.toString();
	}

	public String getHash() {
		return myHash;
	}

	public StoredDetails setHash(String theHash) {
		myHash = theHash;
		return this;
	}

	public Date getPublished() {
		return myPublished;
	}

	public StoredDetails setPublished(Date thePublished) {
		myPublished = thePublished;
		return this;
	}

	@Nonnull
	public String getContentType() {
		return myContentType;
	}

	public StoredDetails setContentType(String theContentType) {
		myContentType = theContentType;
		return this;
	}

	@Nonnull
	public String getBinaryContentId() {
		return myBinaryContentId;
	}

	public StoredDetails setBinaryContentId(String theBinaryContentId) {
		myBinaryContentId = theBinaryContentId;
		return this;
	}

	public long getBytes() {
		return myBytes;
	}

	public StoredDetails setBytes(long theBytes) {
		myBytes = theBytes;
		return this;
	}
}
