package ca.uhn.fhir.jpa.binstore;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.hash.HashingInputStream;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public interface IBinaryStorageSvc {

	/**
	 * Gets the maximum number of bytes that can be stored in a single binary
	 * file by this service. The default is {@link Integer#MAX_VALUE}
	 */
	int getMaximumBinarySize();

	/**
	 * Sets the maximum number of bytes that can be stored in a single binary
	 * file by this service. The default is {@link Integer#MAX_VALUE}
	 *
	 * @param theMaximumBinarySize The maximum size
	 */
	void setMaximumBinarySize(int theMaximumBinarySize);

	/**
	 * Gets the minimum number of bytes that will be stored. Binary content smaller
	 * * than this threshold may be inlined even if a binary storage service
	 * * is active.
	 */
	int getMinimumBinarySize();

	/**
	 * Sets the minimum number of bytes that will be stored. Binary content smaller
	 * than this threshold may be inlined even if a binary storage service
	 * is active.
	 */
	void setMinimumBinarySize(int theMinimumBinarySize);

	/**
	 * Give the storage service the ability to veto items from storage
	 *
	 * @param theSize        How large is the item
	 * @param theResourceId  What is the resource ID it will be associated with
	 * @param theContentType What is the content type
	 * @return <code>true</code> if the storage service should store the item
	 */
	boolean shouldStoreBlob(long theSize, IIdType theResourceId, String theContentType);

	/**
	 * Store a new binary blob
	 *
	 * @param theResourceId  The resource ID that owns this blob. Note that it should not be possible to retrieve a blob without both the resource ID and the blob ID being correct.
	 * @param theContentType The content type to associate with this blob
	 * @param theInputStream An InputStream to read from. This method should close the stream when it has been fully consumed.
	 * @return Returns details about the stored data
	 */
	StoredDetails storeBlob(IIdType theResourceId, String theContentType, InputStream theInputStream) throws IOException;

	StoredDetails fetchBlobDetails(IIdType theResourceId, String theBlobId) throws IOException;

	/**
	 * @return Returns <code>true</code> if the blob was found and written, of <code>false</code> if the blob was not found (i.e. it was expunged or the ID was invalid)
	 */
	boolean writeBlob(IIdType theResourceId, String theBlobId, OutputStream theOutputStream) throws IOException;

	void expungeBlob(IIdType theResourceId, String theBlobId);


	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
	class StoredDetails {

		@JsonProperty("blobId")
		private String myBlobId;
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
		public StoredDetails(@Nonnull String theBlobId, long theBytes, @Nonnull String theContentType, HashingInputStream theIs, Date thePublished) {
			myBlobId = theBlobId;
			myBytes = theBytes;
			myContentType = theContentType;
			myHash = theIs.hash().toString();
			myPublished = thePublished;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
				.append("blobId", myBlobId)
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
		public String getBlobId() {
			return myBlobId;
		}

		public StoredDetails setBlobId(String theBlobId) {
			myBlobId = theBlobId;
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
}
