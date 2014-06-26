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

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "HFJ_RES_LINK"/*, indexes= {@Index(name="IDX_RL_TPATHRES", columnList= "SRC_PATH,TARGET_RESOURCE_ID")}*/)
@org.hibernate.annotations.Table(appliesTo="HFJ_RES_LINK",indexes= {
		@org.hibernate.annotations.Index(name="IDX_RL_TPATHRES", columnNames= {"SRC_PATH", "TARGET_RESOURCE_ID"})})
public class ResourceLink implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "SRC_PATH", length = 100, nullable = false)
	private String mySourcePath;

	@ManyToOne(optional = false)
	@JoinColumn(name = "SRC_RESOURCE_ID", referencedColumnName="RES_ID")
	private ResourceTable mySourceResource;

	@Column(name = "SRC_RESOURCE_ID", insertable = false, updatable = false)
	private Long mySourceResourcePid;

	@ManyToOne(optional = false)
	@JoinColumn(name = "TARGET_RESOURCE_ID", referencedColumnName="RES_ID")
	private ResourceTable myTargetResource;

	@Column(name = "TARGET_RESOURCE_ID", insertable = false, updatable = false)
	private Long myTargetResourcePid;

	public ResourceLink() {
		//nothing
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("ResourceLink[");
		b.append("path=").append(mySourcePath);
		b.append(", src=").append(mySourceResource.getId());
		b.append(", target=").append(myTargetResource.getId());
		
		b.append("]");
		return b.toString();
	}

	public ResourceLink(String theSourcePath, ResourceTable theSourceResource, ResourceTable theTargetResource) {
		super();
		mySourcePath = theSourcePath;
		mySourceResource = theSourceResource;
		myTargetResource = theTargetResource;
	}

	public String getSourcePath() {
		return mySourcePath;
	}

	public ResourceTable getSourceResource() {
		return mySourceResource;
	}

	public Long getSourceResourcePid() {
		return mySourceResourcePid;
	}

	public ResourceTable getTargetResource() {
		return myTargetResource;
	}

	public Long getTargetResourcePid() {
		return myTargetResourcePid;
	}

	public void setSourcePath(String theSourcePath) {
		mySourcePath = theSourcePath;
	}

	public void setSourceResource(ResourceTable theSourceResource) {
		mySourceResource = theSourceResource;
	}

	public void setSourceResourcePid(Long theSourceResourcePid) {
		mySourceResourcePid = theSourceResourcePid;
	}

	public void setTargetResource(ResourceTable theTargetResource) {
		Validate.notNull(theTargetResource);
		myTargetResource = theTargetResource;
	}

	public void setTargetResourcePid(Long theTargetResourcePid) {
		myTargetResourcePid = theTargetResourcePid;
	}

}
