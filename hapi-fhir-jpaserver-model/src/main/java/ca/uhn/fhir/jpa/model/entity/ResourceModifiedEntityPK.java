package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ResourceModifiedEntityPK implements Serializable {
	@Column(name = "RES_ID", nullable = false)
	private long myResourcePid;

	@Column(name = "RES_VER", nullable = false)
	private long myResourceVersion;

	public Long getResourcePid() {
		return myResourcePid;
	}

	public ResourceModifiedEntityPK setResourcePid(long theResourcePid) {
		myResourcePid = theResourcePid;
		return this;
	}

	public long getResourceVersion() {
		return myResourceVersion;
	}

	public ResourceModifiedEntityPK setResourceVersion(long theResourceVersion) {
		myResourceVersion = theResourceVersion;
		return this;
	}

	public static ResourceModifiedEntityPK with(long theResourcePid, long theResourceVersion){
		return new ResourceModifiedEntityPK().setResourcePid(theResourcePid).setResourceVersion(theResourceVersion);
	}
}
