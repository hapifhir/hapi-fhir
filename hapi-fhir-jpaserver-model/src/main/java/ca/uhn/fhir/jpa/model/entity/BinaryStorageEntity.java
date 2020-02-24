package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "HFJ_BINARY_STORAGE_BLOB")
public class BinaryStorageEntity {

	@Id
	@Column(name = "BLOB_ID", length = 200, nullable = false)
	private String myBlobId;
	@Column(name = "RESOURCE_ID", length = 100, nullable = false)
	private String myResourceId;
	@Column(name = "BLOB_SIZE", nullable = true)
	private int mySize;
	@Column(name = "CONTENT_TYPE", nullable = false, length = 100)
	private String myBlobContentType;
	@Lob
	@Column(name = "BLOB_DATA", nullable = false, insertable = true, updatable = false)
	private Blob myBlob;
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

	public void setBlobId(String theBlobId) {
		myBlobId = theBlobId;
	}

	public void setResourceId(String theResourceId) {
		myResourceId = theResourceId;
	}

	public int getSize() {
		return mySize;
	}

	public String getBlobContentType() {
		return myBlobContentType;
	}

	public void setBlobContentType(String theBlobContentType) {
		myBlobContentType = theBlobContentType;
	}

	public Blob getBlob() {
		return myBlob;
	}

	public void setBlob(Blob theBlob) {
		myBlob = theBlob;
	}

	public String getBlobId() {
		return myBlobId;
	}
}
