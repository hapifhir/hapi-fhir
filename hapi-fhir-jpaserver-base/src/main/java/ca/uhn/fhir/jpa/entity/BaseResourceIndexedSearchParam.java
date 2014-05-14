package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseResourceIndexedSearchParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RES_ID")
	private Long myId;
	
	@Column(name="SP_NAME", length=100)
	private String myName;

	@Column(name = "RES_TYPE",length=100,nullable=false)
	private String myResourceType;

	public String getName() {
		return myName;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public abstract BaseResourceTable<?> getResource();

	protected abstract void setResource(BaseResourceTable<?> theResource);

	public void setResource(BaseResourceTable<?> theResource, String theResourceType) {
		setResource(theResource);
		setResourceType(theResourceType);
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	
	
}
