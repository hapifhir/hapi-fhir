/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.Validate;
import org.hibernate.Length;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

@Entity
@Table(
		name = "BT2_JOB_ATTACHMENT",
		indexes = {
			@Index(name = "IDX_BT2JA_INST_ID", columnList = "JOB_INSTANCE_ID"),
			@Index(name = "IDX_BT2JA_INST_ID_AND_FN", columnList = "JOB_INSTANCE_ID, FILENAME", unique = true)
		})
public class Batch2JobAttachmentEntity implements Serializable {
	private static final int FILENAME_MAX_LENGTH = 300;

	@EmbeddedId
	private Batch2WorkChunkAttachmentEntityPk myId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "JOB_INSTANCE_ID",
			referencedColumnName = "ID",
			insertable = false,
			updatable = false,
			foreignKey = @ForeignKey(name = "FK_BT2JA_INSTANCE"))
	private Batch2JobInstanceEntity myJobInstance;

	@Column(name = "FILENAME", length = FILENAME_MAX_LENGTH, nullable = false)
	private String myFilename;

	@Column(name = "CONTENT_TYPE", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private AttachmentContentTypeEnum myContentType;

	@Column(name = "CMP_STATUS", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private CompressionEnum myCompressedStatus;

	@Column(name = "ATTACHMENT_DATA", length = Length.LONG32, nullable = false)
	private byte[] myAttachmentData;

	@Column(name = "ATTACHMENT_LENGTH_CMP", nullable = false)
	private long myAttachmentLengthCompressed;

	@Column(name = "ATTACHMENT_LENGTH_UC", nullable = false)
	private long myAttachmentLengthUncompressed;

	/**
	 * Constructor
	 */
	public Batch2JobAttachmentEntity() {
		// nothing
	}

	/**
	 * Constructor - Creates a new instance for the given instance ID with a randomly generated attachment ID.
	 */
	public Batch2JobAttachmentEntity(String theInstanceId) {
		myId = new Batch2WorkChunkAttachmentEntityPk(
				theInstanceId, UUID.randomUUID().toString());
	}

	public long getAttachmentLengthCompressed() {
		return myAttachmentLengthCompressed;
	}

	public void setAttachmentLengthCompressed(long theAttachmentLengthCompressed) {
		myAttachmentLengthCompressed = theAttachmentLengthCompressed;
	}

	public long getAttachmentLengthUncompressed() {
		return myAttachmentLengthUncompressed;
	}

	public void setAttachmentLengthUncompressed(long theAttachmentLengthUncompressed) {
		myAttachmentLengthUncompressed = theAttachmentLengthUncompressed;
	}

	public CompressionEnum getCompressedStatus() {
		return myCompressedStatus;
	}

	public void setCompressedStatus(CompressionEnum theCompressedStatus) {
		myCompressedStatus = theCompressedStatus;
	}

	public String getFilename() {
		return myFilename;
	}

	public void setFilename(String theFilename) {
		Validate.isTrue(theFilename == null || theFilename.length() <= FILENAME_MAX_LENGTH, "Filename can not exceed length %d: %s", FILENAME_MAX_LENGTH, theFilename);
		myFilename = theFilename;
	}

	public byte[] getData() {
		return myAttachmentData;
	}

	public void setData(byte[] theData) {
		myAttachmentData = theData;
	}

	public AttachmentContentTypeEnum getContentType() {
		return myContentType;
	}

	public void setContentType(AttachmentContentTypeEnum theContentType) {
		myContentType = theContentType;
	}

	public Batch2WorkChunkAttachmentEntityPk getId() {
		return myId;
	}

	public enum CompressionEnum {
		/**
		 * Do not re-order!
		 */
		NONE,
		GZIP
	}

	@Embeddable
	public static class Batch2WorkChunkAttachmentEntityPk {

		@Column(name = "JOB_INSTANCE_ID", length = ID_MAX_LENGTH)
		private String myJobInstanceId;

		@Column(name = "ATTACHMENT_ID", length = ID_MAX_LENGTH)
		private String myAttachmentId;

		/**
		 * Constructor
		 */
		public Batch2WorkChunkAttachmentEntityPk() {
			// nothing
		}

		/**
		 * Constructor
		 */
		public Batch2WorkChunkAttachmentEntityPk(String theInstanceId, String theAttachmentId) {
			myJobInstanceId = theInstanceId;
			myAttachmentId = theAttachmentId;
		}

		@Override
		public boolean equals(Object theO) {
			return (theO instanceof Batch2WorkChunkAttachmentEntityPk that)
					&& Objects.equals(myJobInstanceId, that.myJobInstanceId)
					&& Objects.equals(myAttachmentId, that.myAttachmentId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myJobInstanceId, myAttachmentId);
		}

		public String getAttachmentId() {
			return myAttachmentId;
		}
	}
}
