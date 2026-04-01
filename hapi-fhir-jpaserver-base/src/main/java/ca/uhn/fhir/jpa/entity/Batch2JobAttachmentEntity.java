package ca.uhn.fhir.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.Length;

import java.util.Objects;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

@Entity
@Table(name = "BT2_JOB_ATTACHMENT", indexes = {
	@jakarta.persistence.Index(name = "IDX_BT2JA_INST_ID", columnList = "JOB_INSTANCE_ID")
})
public class Batch2JobAttachmentEntity {

	@EmbeddedId
	private Batch2WorkChunkAttachmentEntityPk myId;
	@Column(name = "FILENAME", length = ID_MAX_LENGTH, nullable = true)
	private String myFilename;
	@Enumerated(jakarta.persistence.EnumType.STRING)
	@Column(name = "CMP_STATUS", length = 10, nullable = false)
	private CompressionEnum myCompressedStatus;
	@ManyToOne
	@JoinColumn(name = "JOB_INSTANCE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	private Batch2JobInstanceEntity myJobInstance;
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
	 * Constructor
	 */
	public Batch2JobAttachmentEntity(String theInstanceId, String theAttachmentId) {
		myId = new Batch2WorkChunkAttachmentEntityPk(theInstanceId, theAttachmentId);
	}

	public String getFilename() {
		return myFilename;
	}

	public void setFilename(String theFilename) {
		myFilename = theFilename;
	}

	public byte[] getData() {
		return myAttachmentData;
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
	}


	public enum CompressionEnum {
		/**
		 * Reordering is ok
		 */
		NONE,
		GZIP
	}


}
