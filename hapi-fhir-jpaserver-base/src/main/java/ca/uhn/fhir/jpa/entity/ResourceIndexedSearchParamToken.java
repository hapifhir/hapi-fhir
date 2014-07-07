package ca.uhn.fhir.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "HFJ_SPIDX_TOKEN" /* , indexes = { @Index(name = "IDX_SP_TOKEN", columnList = "SP_SYSTEM,SP_VALUE") } */)
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_TOKEN", indexes = { 
		@org.hibernate.annotations.Index(name = "IDX_SP_TOKEN", columnNames = { "RES_TYPE", "SP_NAME", "SP_SYSTEM", "SP_VALUE" }), 
		@org.hibernate.annotations.Index(name = "IDX_SP_TOKEN_UNQUAL", columnNames = { "RES_TYPE", "SP_NAME", "SP_VALUE" }) 
})
public class ResourceIndexedSearchParamToken extends BaseResourceIndexedSearchParam {

	public static final int MAX_LENGTH = 100;

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_SYSTEM", nullable = true, length = MAX_LENGTH)
	public String mySystem;

	@Column(name = "SP_VALUE", nullable = true, length = 100)
	public String myValue;

	public ResourceIndexedSearchParamToken() {
	}

	public ResourceIndexedSearchParamToken(String theName, String theSystem, String theValue) {
		setParamName(theName);
		setSystem(theSystem);
		setValue(theValue);
	}

	public String getSystem() {
		return mySystem;
	}

	public String getValue() {
		return myValue;
	}

	public void setSystem(String theSystem) {
		mySystem = StringUtils.defaultIfBlank(theSystem, null);
	}

	public void setValue(String theValue) {
		myValue = StringUtils.defaultIfBlank(theValue, null);
	}

}
