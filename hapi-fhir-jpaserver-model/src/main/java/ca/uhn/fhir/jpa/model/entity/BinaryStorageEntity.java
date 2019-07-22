package ca.uhn.fhir.jpa.model.entity;

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
		if (myPublished == null) {
			return null;
		}
		return new Date(myPublished.getTime());
	}

	public void setPublished(Date thePublishedDate) {
		myPublished = thePublishedDate;
	}

	public String getHash() {
		return myHash;
	}

	public void setHash(String theHash) {
		myHash = theHash;
	}

	public String getBlobId() {
		return myBlobId;
	}

	public void setBlobId(String theBlobId) {
		myBlobId = theBlobId;
	}

	public String getResourceId() {
		return myResourceId;
	}

	public void setResourceId(String theResourceId) {
		myResourceId = theResourceId;
	}

	public int getSize() {
		return mySize;
	}

	public void setSize(int theSize) {
		mySize = theSize;
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
}
