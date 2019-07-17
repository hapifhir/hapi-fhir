package ca.uhn.fhir.jpa.binstore;

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

	void writeBlob(IIdType theResourceId, String theBlobId, OutputStream theOutputStream) throws IOException;


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

		public Date getPublished() {
			return myPublished;
		}

		@Nonnull
		public String getContentType() {
			return myContentType;
		}

		@Nonnull
		public String getBlobId() {
			return myBlobId;
		}

		public long getBytes() {
			return myBytes;
		}

	}
}
