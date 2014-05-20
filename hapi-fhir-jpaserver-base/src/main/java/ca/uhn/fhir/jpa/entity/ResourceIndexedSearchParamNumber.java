package ca.uhn.fhir.jpa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SPIDX_NUMBER", indexes= {@Index(name="IDX_SP_NUMBER", columnList="myValue")})
public class ResourceIndexedSearchParamNumber extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_SYSTEM", nullable = true, length = 100)
	public String mySystem;

	@Column(name = "SP_UNITS", nullable = true, length = 100)
	public String myUnits;

	@Column(name = "SP_VALUE", nullable = true)
	public BigDecimal myValue;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RESOURCE_PID", nullable = false, foreignKey=@ForeignKey(name="FK_ISN_RESOURCE"))
	private BaseResourceTable<?> myResource;

	@Column(name = "RESOURCE_PID", insertable = false, updatable = false)
	private Long myResourcePid;

	public ResourceIndexedSearchParamNumber(String theParamName, BigDecimal theValue, String theSystem, String theUnits) {
		setName(theParamName);
		setSystem(theSystem);
		setValue(theValue);
		setUnits(theUnits);
	}

	public BaseResourceTable<?> getResource() {
		return myResource;
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

	protected void setResource(BaseResourceTable<?> theResource) {
		myResource = theResource;
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
