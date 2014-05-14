package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "IDX_SP_STRING")
public class ResourceIndexedSearchParamString extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCE_PID", nullable = false)
	private BaseResourceTable<?> myResource;

	@Column(name = "RESOURCE_PID", insertable=false, updatable=false)
	private Long myResourcePid;

	@Column(name = "SP_VALUE", length = 100, nullable = true)
	public String myValue;

	public ResourceIndexedSearchParamString() {
	}

	public ResourceIndexedSearchParamString(String theName, String theValue) {
		setName(theName);
		setValue(theValue);
	}

	public BaseResourceTable<?> getResource() {
		return myResource;
	}

	public String getValue() {
		return myValue;
	}

	protected void setResource(BaseResourceTable<?> theResource) {
		myResource = theResource;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}

}
