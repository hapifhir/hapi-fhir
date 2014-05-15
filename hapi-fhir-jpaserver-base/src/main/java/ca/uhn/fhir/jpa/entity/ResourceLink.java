package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RES_LINK")
public class ResourceLink implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long myId;

	@Column(name = "SRC_PATH", length = 100, nullable = false)
	private String mySourcePath;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SRC_RESOURCE_PID", nullable = false, foreignKey = @ForeignKey(name = "FK_RES_LINK_SRC_RES"))
	private BaseResourceTable<?> mySourceResource;

	@Column(name = "SRC_RESOURCE_PID", insertable = false, updatable = false)
	private Long mySourceResourcePid;

	@ManyToOne(optional = false)
	@JoinColumn(name = "TARGET_RESOURCE_PID", nullable = false, foreignKey = @ForeignKey(name = "FK_RES_LINK_TARGET_RES"))
	private BaseResourceTable<?> myTargetResource;

	@Column(name = "TARGET_RESOURCE_PID", insertable = false, updatable = false)
	private Long myTargetResourcePid;

	public ResourceLink() {
		//nothing
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("ResourceLink[");
		b.append("path=").append(mySourcePath);
		b.append(", src=").append(mySourceResource.getIdAsLong());
		b.append(", target=").append(myTargetResource.getIdAsLong());
		
		b.append("]");
		return b.toString();
	}

	public ResourceLink(String theSourcePath, BaseResourceTable<?> theSourceResource, BaseResourceTable<?> theTargetResource) {
		super();
		mySourcePath = theSourcePath;
		mySourceResource = theSourceResource;
		myTargetResource = theTargetResource;
	}

	public String getSourcePath() {
		return mySourcePath;
	}

	public BaseResourceTable<?> getSourceResource() {
		return mySourceResource;
	}

	public Long getSourceResourcePid() {
		return mySourceResourcePid;
	}

	public BaseResourceTable<?> getTargetResource() {
		return myTargetResource;
	}

	public Long getTargetResourcePid() {
		return myTargetResourcePid;
	}

	public void setSourcePath(String theSourcePath) {
		mySourcePath = theSourcePath;
	}

	public void setSourceResource(BaseResourceTable<?> theSourceResource) {
		mySourceResource = theSourceResource;
	}

	public void setSourceResourcePid(Long theSourceResourcePid) {
		mySourceResourcePid = theSourceResourcePid;
	}

	public void setTargetResource(BaseResourceTable<?> theTargetResource) {
		myTargetResource = theTargetResource;
	}

	public void setTargetResourcePid(Long theTargetResourcePid) {
		myTargetResourcePid = theTargetResourcePid;
	}

}
