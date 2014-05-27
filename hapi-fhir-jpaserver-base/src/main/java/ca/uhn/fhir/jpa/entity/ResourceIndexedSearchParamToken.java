package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_SPIDX_TOKEN" /*, indexes = { @Index(name = "IDX_SP_TOKEN", columnList = "SP_SYSTEM,SP_VALUE") }*/)
@org.hibernate.annotations.Table(appliesTo="HFJ_SPIDX_TOKEN", indexes= {
		@org.hibernate.annotations.Index(name="IDX_SP_TOKEN", columnNames= {"SP_SYSTEM","SP_VALUE"})})
public class ResourceIndexedSearchParamToken extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE", nullable = true, length = 100)
	public String myValue;

	@Column(name = "SP_SYSTEM", nullable = true, length = 100)
	public String mySystem;

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public ResourceIndexedSearchParamToken() {
	}

	public ResourceIndexedSearchParamToken(String theName, String theSystem, String theValue) {
		setName(theName);
		setSystem(theSystem);
		setValue(theValue);
	}

	public String getValue() {
		return myValue;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}

}
