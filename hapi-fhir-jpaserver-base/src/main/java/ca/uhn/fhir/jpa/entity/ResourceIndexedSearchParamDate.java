package ca.uhn.fhir.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SPIDX_DATE", indexes= {@Index(name="IDX_SP_DATE", columnList="myValueLow,myValueHigh")})
public class ResourceIndexedSearchParamDate extends BaseResourceIndexedSearchParam {

	private static final long serialVersionUID = 1L;

	@Column(name = "SP_VALUE_HIGH", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date myValueHigh;

	@Column(name = "SP_VALUE_LOW", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date myValueLow;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RESOURCE_PID", nullable = false, foreignKey=@ForeignKey(name="FK_ISD_RESOURCE"))
	private BaseResourceTable<?> myResource;

	@Column(name = "RESOURCE_PID", insertable = false, updatable = false)
	private Long myResourcePid;

	public ResourceIndexedSearchParamDate(String theName, Date theLow, Date theHigh) {
		setName(theName);
		setValueLow(theLow);
		setValueHigh(theHigh);
	}

	public BaseResourceTable<?> getResource() {
		return myResource;
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

	protected void setResource(BaseResourceTable<?> theResource) {
		myResource = theResource;
	}

}
