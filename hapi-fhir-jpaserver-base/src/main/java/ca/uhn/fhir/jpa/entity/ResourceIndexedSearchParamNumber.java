package ca.uhn.fhir.jpa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@formatter:off
@Entity
@Table(name = "HFJ_SPIDX_NUMBER" /*, indexes= {@Index(name="IDX_SP_NUMBER", columnList="SP_VALUE")}*/ )
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_NUMBER", indexes= {
		@org.hibernate.annotations.Index(name="IDX_SP_NUMBER", columnNames= {"RES_TYPE", "SP_NAME", "SP_VALUE"}
	)})
//@formatter:on
public class ResourceIndexedSearchParamNumber extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE", nullable = true)
	public BigDecimal myValue;
	
	public ResourceIndexedSearchParamNumber() {
	}
	
	public ResourceIndexedSearchParamNumber(String theParamName, BigDecimal theValue) {
		setParamName(theParamName);
		setValue(theValue);
	}

	public BigDecimal getValue() {
		return myValue;
	}

	public void setValue(BigDecimal theValue) {
		myValue = theValue;
	}

}
