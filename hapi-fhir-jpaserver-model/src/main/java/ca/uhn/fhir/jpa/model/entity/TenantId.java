package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class TenantId implements Cloneable {

	@Column(name = "TENANT_ID", nullable = true)
	private Integer myTenantId;
	@Column(name = "TENANT_DATE", nullable = true)
	private LocalDate myTenantDate;

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
}
