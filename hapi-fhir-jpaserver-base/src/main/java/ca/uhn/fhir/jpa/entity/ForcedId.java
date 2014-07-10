package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity()
@Table(name = "HFJ_FORCED_ID", uniqueConstraints = { @UniqueConstraint(name = "IDX_FORCEDID", columnNames = { "FORCED_ID" }) })
@NamedQueries(@NamedQuery(name = "Q_GET_FORCED_ID", query = "SELECT f FROM ForcedId f WHERE myForcedId = :ID"))
public class ForcedId {

	public static final int MAX_FORCED_ID_LENGTH = 100;

	@Column(name = "FORCED_ID", nullable = false, length = MAX_FORCED_ID_LENGTH, updatable = false)
	private String myForcedId;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "PID")
	private Long myId;

	@JoinColumn(name = "RESOURCE_PID", nullable = false, updatable = false)
	@OneToOne()
	private ResourceTable myResource;

	@Column(name = "RESOURCE_PID", nullable = false, updatable = false, insertable=false)
	private Long myResourcePid;

	public String getForcedId() {
		return myForcedId;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public Long getResourcePid() {
		if (myResourcePid==null) {
			return myResource.getId();
		}
		return myResourcePid;
	}

	public void setForcedId(String theForcedId) {
		myForcedId = theForcedId;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
	}

	public void setResourcePid(ResourceTable theResourcePid) {
		myResource = theResourcePid;
	}

}
