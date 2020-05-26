package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity()
@Table(name = "NPM_PACKAGE_VER_RES", uniqueConstraints = {
}, indexes = {
})
public class NpmPackageVersionValidationResourceEntity {

	public static final int VERSION_ID_LENGTH = 200;

	@Id
	@SequenceGenerator(name = "SEQ_NPM_PACKVERRES", sequenceName = "SEQ_NPM_PACKVERRES")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_NPM_PACKVERRES")
	@Column(name = "SP_ID")
	private Long myId;

	@ManyToOne
	@JoinColumns(value = {
		@JoinColumn(name = "PACKAGE_ID", referencedColumnName = "PACKAGE_ID"),
		@JoinColumn(name = "VERSION_ID", referencedColumnName = "VERSION_ID")
	},
		foreignKey = @ForeignKey(name = "FK_NPM_PACKVERRES_PACKVER")
	)
	private NpmPackageVersionEntity myPackageVersion;

	@Column(name = "FILE_DIR", length = 200)
	private String myDirectory;
	@Column(name = "FILE_NAME", length = 200)
	private String myFilename;
	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN)
	private String myResourceType;
	@Column(name = "FILE_URL", length = 200)
	private String myUrl;

	public NpmPackageVersionEntity getPackageVersion() {
		return myPackageVersion;
	}

	public void setPackageVersion(NpmPackageVersionEntity thePackageVersion) {
		myPackageVersion = thePackageVersion;
	}

	public String getDirectory() {
		return myDirectory;
	}

	public void setDirectory(String theDirectory) {
		myDirectory = theDirectory;
	}

	public String getFilename() {
		return myFilename;
	}

	public void setFilename(String theFilename) {
		myFilename = theFilename;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

}
