package ca.uhn.fhir.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IDX_SP_DATE")
public class ResourceIndexedSearchParamDate extends BaseResourceIndexedSearchParam<Date> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RESOURCE_PID", nullable = false)
	private BaseResourceTable<?> myResource;

	@Column(name = "SP_VALUE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date myValue;

	public BaseResourceTable<?> getResource() {
		return myResource;
	}

	public Date getValue() {
		return myValue;
	}

	public void setResource(BaseResourceTable<?> theResource) {
		myResource = theResource;
	}

	public void setValue(Date theValue) {
		myValue = theValue;
	}

}
