package ca.uhn.fhir.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HFJ_SPIDX_DATE" /*, indexes= {@Index(name="IDX_SP_DATE", columnList= "SP_VALUE_LOW,SP_VALUE_HIGH")}*/)
@org.hibernate.annotations.Table(appliesTo = "HFJ_SPIDX_DATE", indexes= {@org.hibernate.annotations.Index(name="IDX_SP_DATE", columnNames= {"RES_TYPE", "SP_NAME", "SP_VALUE_LOW","SP_VALUE_HIGH"})})
public class ResourceIndexedSearchParamDate extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE_HIGH", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date myValueHigh;

	@Column(name = "SP_VALUE_LOW", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date myValueLow;

	
	
	public ResourceIndexedSearchParamDate() {
	}
	
	public ResourceIndexedSearchParamDate(String theName, Date theLow, Date theHigh) {
		setParamName(theName);
		setValueLow(theLow);
		setValueHigh(theHigh);
	}

	

	public Date getValueHigh() {
		return myValueHigh;
	}

	public Date getValueLow() {
		return myValueLow;
	}

	public void setValueHigh(Date theValueHigh) {
		myValueHigh = theValueHigh;
	}

	public void setValueLow(Date theValueLow) {
		myValueLow = theValueLow;
	}

	

}
