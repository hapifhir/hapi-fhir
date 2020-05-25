package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity()
@Table(name = "NPM_PACKAGE_VER", uniqueConstraints = {
}, indexes = {
})
public class NpmPackageVersionEntity {

	public static final int VERSION_ID_LENGTH = 200;

	@EmbeddedId
	private NpmPackageVersionEntityPk myId;
	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_NPM_PKV_PKG"))
	private NpmPackageEntity myPackage;
	@OneToOne
	@JoinColumn(name = "BINARY_RES_ID", referencedColumnName = "RES_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_NPM_PKV_RESID"))
	private ResourceTable myPackageBinary;
	@Column(name = "BYTES_COUNT", nullable = false)
	private long myBytes;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAVED_TIME", nullable = false)
	private Date mySavedTime;

	public NpmPackageVersionEntityPk getId() {
		return myId;
	}

	public void setId(NpmPackageVersionEntityPk theId) {
		myId = theId;
	}

	public NpmPackageEntity getPackage() {
		return myPackage;
	}

	public void setPackage(NpmPackageEntity thePackage) {
		myPackage = thePackage;
	}

	public ResourceTable getPackageBinary() {
		return myPackageBinary;
	}

	public void setPackageBinary(ResourceTable thePackageBinary) {
		myPackageBinary = thePackageBinary;
	}

}
