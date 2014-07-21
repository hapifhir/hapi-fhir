package ca.uhn.fhir.jpa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@formatter:off
@Entity
@Table(name = "HFJ_SPIDX_QUANTITY" /*, indexes= {@Index(name="IDX_SP_NUMBER", columnList="SP_VALUE")}*/ )
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_QUANTITY", indexes= {
		@org.hibernate.annotations.Index(name="IDX_SP_QUANTITY", columnNames= {"RES_TYPE", "SP_NAME", "SP_SYSTEM", "SP_UNITS", "SP_VALUE"}
	)})
//@formatter:on
public class ResourceIndexedSearchParamQuantity extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_SYSTEM", nullable = true, length = 100)
	public String mySystem;

	@Column(name = "SP_UNITS", nullable = true, length = 100)
	public String myUnits;

	@Column(name = "SP_VALUE", nullable = true)
	public BigDecimal myValue;

	public ResourceIndexedSearchParamQuantity() {
		//nothing
	}
	
	public ResourceIndexedSearchParamQuantity(String theParamName, BigDecimal theValue, String theSystem, String theUnits) {
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
