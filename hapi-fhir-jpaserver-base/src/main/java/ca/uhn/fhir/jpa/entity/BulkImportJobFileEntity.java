package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imp.model.JobFileRowProcessingModeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Entity
@Table(name = "HFJ_BLK_IMPORT_JOBFILE", indexes = {
		  @Index(name = "IDX_BLKIM_JOBFILE_JOBID", columnList = "JOB_PID")
})
public class BulkImportJobFileEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKIMJOBFILE_PID")
	@SequenceGenerator(name = "SEQ_BLKIMJOBFILE_PID", sequenceName = "SEQ_BLKIMJOBFILE_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne
	@JoinColumn(name = "JOB_PID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name = "FK_BLKIMJOBFILE_JOB"))
	private BulkImportJobEntity myJob;

	@Column(name = "ROW_PROCESSING_MODE", length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private JobFileRowProcessingModeEnum myRowProcessingMode;

	@Column(name = "FILE_SEQ", nullable = false)
	private int myFileSequence;

	@Lob
	@Column(name = "JOB_CONTENTS", nullable = false)
	private byte[] myContents;

	public BulkImportJobEntity getJob() {
		return myJob;
	}

	public void setJob(BulkImportJobEntity theJob) {
		myJob = theJob;
	}

	public JobFileRowProcessingModeEnum getRowProcessingMode() {
		return myRowProcessingMode;
	}

	public void setRowProcessingMode(JobFileRowProcessingModeEnum theRowProcessingMode) {
		myRowProcessingMode = theRowProcessingMode;
	}

	public int getFileSequence() {
		return myFileSequence;
	}

	public void setFileSequence(int theFileSequence) {
		myFileSequence = theFileSequence;
	}

	public String getContents() {
		return new String(myContents, StandardCharsets.UTF_8);
	}

	public void setContents(String theContents) {
		myContents = theContents.getBytes(StandardCharsets.UTF_8);
	}


	public BulkImportJobFileJson toJson() {
		return new BulkImportJobFileJson()
			.setContents(getContents())
			.setProcessingMode(getRowProcessingMode());
	}
}
