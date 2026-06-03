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
import ca.uhn.fhir.batch2.api.AttachmentDetails;
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

/**
 * This entity represents the first chunk of a Batch2 job attachment.
 * See {@link ca.uhn.fhir.batch2.api.IJobPersistence#storeNewAttachment(String, AttachmentDetails)}
 * for a description of what attachments are used for.
 * <p>
 * If the overall number of bytes is too big to reasonably store in a single entity, we put further data
 * chunks of the attachment into {@link Batch2JobAttachmentChunkEntity}.
 * </p>
 */
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
	private AttachmentPk myId;

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

	@Column(name = "ATTACHMENT_DATA", length = Length.LONG32, nullable = false, updatable = false)
	private byte[] myAttachmentData;

	@Column(name = "ATTACHMENT_LENGTH_CMP", nullable = false)
	private long myAttachmentLengthCompressed;

	@Column(name = "ATTACHMENT_LENGTH_UC", nullable = false)
	private long myAttachmentLengthUncompressed;

	/**
	 * If non-null, there are additional chunks of data for this attachment
	 * in {@link Batch2JobAttachmentChunkEntity}. Attachment chunks
	 * are numbered from 0 to this value.
	 */
	@Column(name = "EXTRA_CHUNK_IDX", nullable = true)
	private Integer myExtraChunkMaximumIndex;

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
		myId = new AttachmentPk(theInstanceId, UUID.randomUUID().toString());
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

	/**
	 * If non-null, there are additional chunks of data for this attachment
	 * in {@link Batch2JobAttachmentChunkEntity}. Attachment chunks
	 * are numbered from 0 to this value.
	 */
	public Integer getExtraChunkMaximumIndex() {
		return myExtraChunkMaximumIndex;
	}

	/**
	 * If non-null, there are additional chunks of data for this attachment
	 * in {@link Batch2JobAttachmentChunkEntity}. Attachment chunks
	 * are numbered from 0 to this value.
	 * <p>
	 * This method is intended to be private, and the max index should only ever
	 * be modified by {@link #newChunkEntity(byte[])}.
	 * </p>
	 */
	private void setExtraChunkMaximumIndex(int theExtraChunkMaximumIndex) {
		if (myExtraChunkMaximumIndex == null) {
			Validate.isTrue(theExtraChunkMaximumIndex == 0, "Numbering must be null, 0, 1, 2... Current value: null");
		} else {
			Validate.isTrue(
					theExtraChunkMaximumIndex == myExtraChunkMaximumIndex + 1,
					"Numbering must be null, 0, 1, 2... Current value: %d, new value: %s",
					myExtraChunkMaximumIndex,
					theExtraChunkMaximumIndex);
		}
		myExtraChunkMaximumIndex = theExtraChunkMaximumIndex;
	}

	public String getFilename() {
		return myFilename;
	}

	public void setFilename(String theFilename) {
		Validate.isTrue(
				theFilename == null || theFilename.length() <= FILENAME_MAX_LENGTH,
				"Filename can not exceed length %d: %s",
				FILENAME_MAX_LENGTH,
				theFilename);
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

	public AttachmentPk getId() {
		return myId;
	}

	public void incrementAttachmentLengthCompressed(int theLength) {
		myAttachmentLengthCompressed += theLength;
	}

	public void incrementAttachmentLengthUncompressed(int theCount) {
		myAttachmentLengthUncompressed += theCount;
	}

	/**
	 * Creates a new chunk entity for this attachment. The returned entity should
	 * be persisted immediately.
	 */
	public Batch2JobAttachmentChunkEntity newChunkEntity(byte[] theBytes) {
		int chunkIndex;
		if (myExtraChunkMaximumIndex == null) {
			chunkIndex = 0;
		} else {
			chunkIndex = myExtraChunkMaximumIndex + 1;
		}
		setExtraChunkMaximumIndex(chunkIndex);

		Batch2JobAttachmentChunkEntity retVal = new Batch2JobAttachmentChunkEntity();
		retVal.setId(new Batch2JobAttachmentChunkEntity.ChunkPk(getId(), chunkIndex));
		retVal.setData(theBytes);

		return retVal;
	}

	public enum CompressionEnum {
		/**
		 * Do not re-order!
		 */
		NONE,
		GZIP
	}

	@Embeddable
	public static class AttachmentPk {

		@Column(name = "JOB_INSTANCE_ID", length = ID_MAX_LENGTH)
		private String myJobInstanceId;

		@Column(name = "ATTACHMENT_ID", length = ID_MAX_LENGTH)
		private String myAttachmentId;

		/**
		 * Constructor
		 */
		public AttachmentPk() {
			// nothing
		}

		/**
		 * Constructor
		 */
		public AttachmentPk(String theInstanceId, String theAttachmentId) {
			myJobInstanceId = theInstanceId;
			myAttachmentId = theAttachmentId;
		}

		@Override
		public boolean equals(Object theO) {
			return (theO instanceof AttachmentPk that)
					&& Objects.equals(myJobInstanceId, that.myJobInstanceId)
					&& Objects.equals(myAttachmentId, that.myAttachmentId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myJobInstanceId, myAttachmentId);
		}

		public String getJobInstanceId() {
			return myJobInstanceId;
		}

		public String getAttachmentId() {
			return myAttachmentId;
		}
	}
}
