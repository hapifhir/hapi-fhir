package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class TenantId implements Cloneable {

	@Column(name = "TENANT_ID", nullable = true)
	private Integer myTenantId;
	@Column(name = "TENANT_DATE", nullable = true)
	private LocalDate myTenantDate;

	/**
	 * Constructor
	 */
	public TenantId() {
		super();
	}

	/**
	 * Constructor
	 */
	public TenantId(int theTenantId, LocalDate theTenantDate) {
		setTenantId(theTenantId);
		setTenantDate(theTenantDate);
	}

    public Integer getTenantId() {
		return myTenantId;
	}

	public TenantId setTenantId(Integer theTenantId) {
		myTenantId = theTenantId;
		return this;
	}

	public LocalDate getTenantDate() {
		return myTenantDate;
	}

	public TenantId setTenantDate(LocalDate theTenantDate) {
		myTenantDate = theTenantDate;
		return this;
	}

	@Override
	protected TenantId clone() {
		return new TenantId()
			.setTenantId(getTenantId())
			.setTenantDate(getTenantDate());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", myTenantId)
			.append("date", myTenantDate)
			.toString();
	}
}
