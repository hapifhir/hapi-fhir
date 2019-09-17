package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "HFJ_BLK_EXPORT_COLLECTION")
public class BulkExportCollectionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKEXCOL_PID")
	@SequenceGenerator(name = "SEQ_BLKEXCOL_PID", sequenceName = "SEQ_BLKEXCOL_PID")
	@Column(name = "PID")
	private Long myId;
	@ManyToOne
	@JoinColumn(name = "JOB_PID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name="FK_BLKEXCOL_JOB"))
	private BulkExportJobEntity myJob;
	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;
	@Column(name = "TYPE_FILTER", length = 1000, nullable = true)
	private String myFilter;
	@Version
	@Column(name = "OPTLOCK", nullable = false)
	private int myVersion;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myCollection")
	private Collection<BulkExportCollectionFileEntity> myFiles;

	public void setJob(BulkExportJobEntity theJob) {
		myJob = theJob;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getFilter() {
		return myFilter;
	}

	public void setFilter(String theFilter) {
		myFilter = theFilter;
	}

	public int getVersion() {
		return myVersion;
	}

	public void setVersion(int theVersion) {
		myVersion = theVersion;
	}

	public Collection<BulkExportCollectionFileEntity> getFiles() {
		if (myFiles == null) {
			myFiles = new ArrayList<>();
		}
		return myFiles;
	}
}
