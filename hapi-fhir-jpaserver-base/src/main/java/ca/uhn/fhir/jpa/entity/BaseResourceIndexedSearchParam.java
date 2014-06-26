package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SP_ID")
	private Long myId;

	@Column(name = "SP_NAME", length = 100, nullable=false)
	private String myParamName;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RES_ID", referencedColumnName="RES_ID")
	private ResourceTable myResource;

	@Column(name = "RES_TYPE", nullable=false)
	private String myResourceType;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theName) {
		myParamName = theName;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
		myResourceType = theResource.getResourceType();
	}

}
