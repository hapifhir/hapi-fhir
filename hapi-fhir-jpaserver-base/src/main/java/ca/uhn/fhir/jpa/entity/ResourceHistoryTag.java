package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_HISTORY_TAG")
public class ResourceHistoryTag extends BaseTag implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	@Column(name = "PID")
	private Long myId;
	
	@ManyToOne()
	@JoinColumn(name="RES_VER_PID", referencedColumnName="PID", nullable=false)
	private ResourceHistoryTable myResourceHistory;

	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable=false)
	private String myResourceType;

	@Column(name="RES_ID", nullable=false)
	private Long myResourceId;

	public String getResourceType() {
		return myResourceType;
	}


	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}


	public Long getResourceId() {
		return myResourceId;
	}


	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}


	public ResourceHistoryTag() {
	}
	

	public ResourceHistoryTag(ResourceHistoryTable theResourceHistoryTable, TagDefinition theTag) {
		myResourceHistory=theResourceHistoryTable;
		setTag(theTag);
		setResourceId(theResourceHistoryTable.getResourceId());
	}

	public ResourceHistoryTable getResourceHistory() {
		return myResourceHistory;
	}

	public void setResource(ResourceHistoryTable theResourceHistory) {
		myResourceHistory = theResourceHistory;
	}

}
