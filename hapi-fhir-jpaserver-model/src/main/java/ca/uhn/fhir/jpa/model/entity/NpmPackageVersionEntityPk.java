package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class NpmPackageVersionEntityPk {

	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID", nullable = false)
	private NpmPackageEntity myPackage;
	@Column(name = "VERSION_ID", length = 200, nullable = false)
	private String myVersionId;

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		NpmPackageVersionEntityPk that = (NpmPackageVersionEntityPk) theO;

		return new EqualsBuilder()
			.append(myPackage, that.myPackage)
			.append(myVersionId, that.myVersionId)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myPackage)
			.append(myVersionId)
			.toHashCode();
	}

	public NpmPackageEntity getPackage() {
		return myPackage;
	}

	public void setPackage(NpmPackageEntity thePackage) {
		myPackage = thePackage;
	}

	public String getVersionId() {
		return myVersionId;
	}

	public void setVersionId(String theVersionId) {
		myVersionId = theVersionId;
	}


}
