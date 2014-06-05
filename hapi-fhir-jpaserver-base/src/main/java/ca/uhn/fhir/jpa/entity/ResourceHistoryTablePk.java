package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ResourceHistoryTablePk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PID")
	private Long myId;

	@Column(name = "RES_TYPE", length = 30, nullable = false)
	private String myResourceType;

	@Column(name = "VERSION", nullable = false)
	private Long myVersion;

	public ResourceHistoryTablePk() {
		// nothing
	}

	public ResourceHistoryTablePk(String theResourceType, Long theResourceId, Long theVersion) {
		super();
		myResourceType = theResourceType;
		myId = theResourceId;
		myVersion = theVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myId == null) ? 0 : myId.hashCode());
		result = prime * result + ((myResourceType == null) ? 0 : myResourceType.hashCode());
		result = prime * result + ((myVersion == null) ? 0 : myVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceHistoryTablePk other = (ResourceHistoryTablePk) obj;
		if (myId == null) {
			if (other.myId != null)
				return false;
		} else if (!myId.equals(other.myId))
			return false;
		if (myResourceType == null) {
			if (other.myResourceType != null)
				return false;
		} else if (!myResourceType.equals(other.myResourceType))
			return false;
		if (myVersion == null) {
			if (other.myVersion != null)
				return false;
		} else if (!myVersion.equals(other.myVersion))
			return false;
		return true;
	}

	public Long getId() {
		return myId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Long getVersion() {
		return myVersion;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public void setVersion(Long theVersion) {
		myVersion = theVersion;
	}

}
