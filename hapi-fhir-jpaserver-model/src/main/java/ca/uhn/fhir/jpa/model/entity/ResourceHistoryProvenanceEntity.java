package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.rest.api.Constants;

import javax.persistence.*;

@Table(name = "HFJ_RES_VER_PROV")
@Entity
public class ResourceHistoryProvenanceEntity {

	public static final int SOURCE_URI_LENGTH = 100;

	@Id
	@Column(name = "RES_VER_PID")
	private Long myId;
	@OneToOne()
	@JoinColumn(name = "RES_VER_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_RESVERPROV_RESVER_PID"), nullable = false, insertable = false, updatable = false)
	@MapsId
	private ResourceHistoryTable myResourceHistoryTable;
	@OneToOne()
	@JoinColumn(name = "RES_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_RESVERPROV_RES_PID"), nullable = false)
	private ResourceTable myResourceTable;
	@Column(name = "SOURCE_URI", length = SOURCE_URI_LENGTH, nullable = true)
	private String mySourceUri;
	@Column(name = "REQUEST_ID", length = Constants.REQUEST_ID_LENGTH, nullable = true)
	private String myRequestId;

	public ResourceTable getResourceTable() {
		return myResourceTable;
	}

	public void setResourceTable(ResourceTable theResourceTable) {
		myResourceTable = theResourceTable;
	}

	public ResourceHistoryTable getResourceHistoryTable() {
		return myResourceHistoryTable;
	}

	public void setResourceHistoryTable(ResourceHistoryTable theResourceHistoryTable) {
		myResourceHistoryTable = theResourceHistoryTable;
	}

	public String getSourceUri() {
		return mySourceUri;
	}

	public void setSourceUri(String theSourceUri) {
		mySourceUri = theSourceUri;
	}

	public String getRequestId() {
		return myRequestId;
	}

	public void setRequestId(String theRequestId) {
		myRequestId = theRequestId;
	}

}
