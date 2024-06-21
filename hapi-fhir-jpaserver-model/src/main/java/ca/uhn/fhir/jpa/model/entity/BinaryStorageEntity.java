/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.Length;

import java.sql.Blob;
import java.util.Date;

import static java.util.Objects.nonNull;

@Entity
@Table(name = "HFJ_BINARY_STORAGE_BLOB")
public class BinaryStorageEntity {

	@Id
	@Column(name = "BLOB_ID", length = 200, nullable = false)
	// N.B GGG: Note that the `content id` is the same as the `externalized binary id`.
	private String myContentId;

	@Column(name = "RESOURCE_ID", length = 100, nullable = false)
	private String myResourceId;

	@Column(name = "BLOB_SIZE", nullable = false)
	private long mySize;

	@Column(name = "CONTENT_TYPE", nullable = false, length = 100)
	private String myContentType;

	/**
	 * @deprecated
	 */
	@Deprecated(since = "7.2.0")
	@Lob // TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Column(name = "BLOB_DATA", nullable = true, insertable = true, updatable = false)
	private Blob myBlob;

	@Column(name = "STORAGE_CONTENT_BIN", nullable = true, length = Length.LONG32)
	private byte[] myStorageContentBin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISHED_DATE", nullable = false)
	private Date myPublished;

	@Column(name = "BLOB_HASH", length = 128, nullable = true)
	private String myHash;

	public Date getPublished() {
		return new Date(myPublished.getTime());
	}

	public void setPublished(Date thePublishedDate) {
		myPublished = thePublishedDate;
	}

	public String getHash() {
		return myHash;
	}

	public void setContentId(String theContentId) {
		myContentId = theContentId;
	}

	public void setResourceId(String theResourceId) {
		myResourceId = theResourceId;
	}

	public long getSize() {
		return mySize;
	}

	public String getContentType() {
		return myContentType;
	}

	public void setContentType(String theContentType) {
		myContentType = theContentType;
	}

	public Blob getBlob() {
		return myBlob;
	}

	public void setBlob(Blob theBlob) {
		myBlob = theBlob;
	}

	public String getContentId() {
		return myContentId;
	}

	public void setSize(long theSize) {
		mySize = theSize;
	}

	public void setHash(String theHash) {
		myHash = theHash;
	}

	public byte[] getStorageContentBin() {
		return myStorageContentBin;
	}

	public BinaryStorageEntity setStorageContentBin(byte[] theStorageContentBin) {
		myStorageContentBin = theStorageContentBin;
		return this;
	}

	public boolean hasStorageContent() {
		return nonNull(myStorageContentBin);
	}

	public boolean hasBlob() {
		return nonNull(myBlob);
	}
}
