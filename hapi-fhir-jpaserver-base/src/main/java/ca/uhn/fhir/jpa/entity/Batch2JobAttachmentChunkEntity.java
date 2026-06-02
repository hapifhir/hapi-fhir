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

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.Length;

import java.io.Serializable;
import java.util.Objects;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

/**
 * Subsequent data chunk for {@link Batch2JobAttachmentEntity}
 */
@Entity
@Table(name = "BT2_JOB_ATTACHMENT_CHUNK")
public class Batch2JobAttachmentChunkEntity implements Serializable {

	@EmbeddedId
	private ChunkPk myId;

	@Column(name = "ATTACHMENT_DATA", length = Length.LONG32, nullable = false)
	private byte[] myAttachmentData;

	@SuppressWarnings("unused")
	@ManyToOne
	@JoinColumns(value = {
		@JoinColumn(name = "JOB_INSTANCE_ID", referencedColumnName = "JOB_INSTANCE_ID", nullable = false, insertable = false, updatable = false),
		@JoinColumn(name = "ATTACHMENT_ID", referencedColumnName = "ATTACHMENT_ID", nullable = false, insertable = false, updatable = false)
	}, foreignKey = @ForeignKey(name = "FK_BT2_JOB_ATTCHNK_PARENT"))
	private Batch2JobAttachmentEntity myParent;

	/**
	 * Constructor
	 */
	public Batch2JobAttachmentChunkEntity() {
		// nothing
	}

	public byte[] getData() {
		return myAttachmentData;
	}

	public void setData(byte[] theData) {
		myAttachmentData = theData;
	}

	public ChunkPk getId() {
		return myId;
	}

	public void setId(ChunkPk theId) {
		myId = theId;
	}

	@Embeddable
	public static class ChunkPk {

		@Column(name = "JOB_INSTANCE_ID", length = ID_MAX_LENGTH)
		private String myJobInstanceId;

		@Column(name = "ATTACHMENT_ID", length = ID_MAX_LENGTH)
		private String myAttachmentId;

		@Column(name = "CHUNK_INDEX", length = ID_MAX_LENGTH)
		private int myChunkIndex;

		/**
		 * Constructor
		 */
		public ChunkPk() {
			// nothing
		}

		/**
		 * Constructor
		 */
		public ChunkPk(Batch2JobAttachmentEntity.AttachmentPk theId, int theChunkIndex) {
			myJobInstanceId = theId.getJobInstanceId();
			myAttachmentId = theId.getAttachmentId();
			myChunkIndex = theChunkIndex;
		}

		@Override
		public boolean equals(Object theO) {
			return (theO instanceof ChunkPk that)
					&& Objects.equals(myJobInstanceId, that.myJobInstanceId)
					&& Objects.equals(myAttachmentId, that.myAttachmentId)
					&& myChunkIndex == that.myChunkIndex;
		}

		@Override
		public int hashCode() {
			return Objects.hash(myJobInstanceId, myAttachmentId, myChunkIndex);
		}

		public String getAttachmentId() {
			return myAttachmentId;
		}
	}
}
