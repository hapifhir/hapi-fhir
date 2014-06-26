package ca.uhn.fhir.jpa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "HFJ_SPIDX_NUMBER" /*, indexes= {@Index(name="IDX_SP_NUMBER", columnList="SP_VALUE")}*/ )
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_NUMBER", indexes= {@org.hibernate.annotations.Index(name="IDX_SP_NUMBER", columnNames= {"RES_TYPE", "SP_NAME", "SP_VALUE"})})
public class ResourceIndexedSearchParamNumber extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_SYSTEM", nullable = true, length = 100)
	public String mySystem;

	@Column(name = "SP_UNITS", nullable = true, length = 100)
	public String myUnits;

	@Column(name = "SP_VALUE", nullable = true)
	public BigDecimal myValue;

	public ResourceIndexedSearchParamNumber(String theParamName, BigDecimal theValue, String theSystem, String theUnits) {
		setParamName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
		setUnits(theUnits);
	}

	public String getSystem() {
		return mySystem;
	}

	public String getUnits() {
		return myUnits;
	}

	public BigDecimal getValue() {
		return myValue;
	}


	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public void setUnits(String theUnits) {
		myUnits = theUnits;
	}

	public void setValue(BigDecimal theValue) {
		myValue = theValue;
	}

}
