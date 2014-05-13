package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "IDX_SP_TOKEN")
public class ResourceIndexedSearchParamToken extends BaseResourceIndexedSearchParam<String> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RESOURCE_PID", nullable = false)
	private BaseResourceTable<?> myResource;

	@Column(name = "RESOURCE_PID", insertable=false, updatable=false)
	private Long myResourcePid;

	@Column(name = "SP_VALUE", nullable = true, length=100)
	public String myValue;

	@Column(name = "SP_SYSTEM", nullable = true, length=100)
	public String mySystem;

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public ResourceIndexedSearchParamToken(String theName, String theSystem, String theValue) {
		setName(theName);
		setSystem(theSystem);
		setValue(theValue);
	}

	public BaseResourceTable<?> getResource() {
		return myResource;
	}

	public String getValue() {
		return myValue;
	}

	public void setResource(BaseResourceTable<?> theResource) {
		myResource = theResource;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}

}
