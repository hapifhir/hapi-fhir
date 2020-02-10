package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Embeddable
public class TenantId implements Cloneable {

	@Column(name = "TENANT_ID", nullable = true, insertable = true, updatable = false)
	private Integer myTenantId;
	@Column(name = "TENANT_DATE", nullable = true, insertable = true, updatable = false)
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
		return defaultIfNull(myTenantId, "null").toString();
	}
}
