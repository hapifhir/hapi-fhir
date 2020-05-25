package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "NPM_PACKAGE", uniqueConstraints = {
}, indexes = {
})
public class NpmPackageEntity {

	protected static final int PACKAGE_ID_LENGTH = 200;
	@Id
	@Column(name = "PACKAGE_ID", length = PACKAGE_ID_LENGTH, nullable = false)
	private String myPackageId;
	@Column(name = "CUR_VERSION_ID", length = NpmPackageVersionEntity.VERSION_ID_LENGTH, nullable = true)
	private String myCurrentVersionId;

	public void setPackageId(String thePackageId) {
		myPackageId = thePackageId;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		NpmPackageEntity that = (NpmPackageEntity) theO;

		return new EqualsBuilder()
			.append(myPackageId, that.myPackageId)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myPackageId)
			.toHashCode();
	}

	public String getCurrentVersionId() {
		return myCurrentVersionId;
	}

	public void setCurrentVersionId(String theCurrentVersionId) {
		myCurrentVersionId = theCurrentVersionId;
	}
}
