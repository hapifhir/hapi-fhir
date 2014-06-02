package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_RES_TAG")
public class ResourceTag extends BaseTag {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(cascade = {})
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID")
	private ResourceTable myResource;

	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN,nullable=false)
	private String myResourceType;

	@Column(name="RES_ID", insertable=false,updatable=false)
	private Long myResourceId;
	
	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public ResourceTag() {
	}

	public ResourceTag(ResourceTable theResourceTable, TagDefinition theTag) {
		myResource = theResourceTable;
		setTag(theTag);
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

}
