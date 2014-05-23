package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ResourceHistoryTablePk implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="PID")
	private Long myId;
	
	@Column(name="RES_TYPE", length=30, nullable=false)
	private String myResourceType;

	@Column(name="VERSION", nullable=false)
	private Long myVersion;

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
