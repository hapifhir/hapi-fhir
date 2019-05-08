package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.model.entity.ForcedId;

import javax.persistence.*;

@Entity
@Table(name = "HFJ_BLK_EXPORT_COLFILE")
public class BulkExportCollectionFileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_BLKEXCOLFILE_PID")
	@SequenceGenerator(name = "SEQ_BLKEXCOLFILE_PID", sequenceName = "SEQ_BLKEXCOLFILE_PID")
	@Column(name = "PID")
	private Long myId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_PID", referencedColumnName = "PID", nullable = false)
	private BulkExportCollectionEntity myCollection;
	@Column(name = "RES_ID", length = ForcedId.MAX_FORCED_ID_LENGTH, nullable = false)
	private String myResourceId;

	public void setCollection(BulkExportCollectionEntity theCollection) {
		myCollection = theCollection;
	}

	public void setResource(String theResourceId) {
		myResourceId = theResourceId;
	}

	public String getResourceId() {
		return myResourceId;
	}
}
