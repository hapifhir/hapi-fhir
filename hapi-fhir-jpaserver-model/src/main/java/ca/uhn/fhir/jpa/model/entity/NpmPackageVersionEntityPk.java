package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class NpmPackageVersionEntityPk implements Serializable {

	@Column(name = "PACKAGE_ID", length = NpmPackageEntity.PACKAGE_ID_LENGTH, nullable = false)
	private String myPackageId;
	@Column(name = "VERSION_ID", length = NpmPackageVersionEntity.VERSION_ID_LENGTH, nullable = false)
	private String myVersionId;

	/**
	 * Constructor
	 */
	public NpmPackageVersionEntityPk() {
		super();
	}

	/**
	 * Constructor
	 */
	public NpmPackageVersionEntityPk(String thePackageId, String theVersionId) {
		setPackageId(thePackageId);
		setVersionId(theVersionId);
	}

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
			.append(getPackageId(), that.getPackageId())
			.append(getVersionId(), that.getVersionId())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getPackageId())
			.append(getVersionId())
			.toHashCode();
	}

	public String getPackageId() {
		return myPackageId;
	}

	public void setPackageId(String thePackageId) {
		myPackageId = thePackageId;
	}

	public String getVersionId() {
		return myVersionId;
	}

	public void setVersionId(String theVersionId) {
		myVersionId = theVersionId;
	}


}
