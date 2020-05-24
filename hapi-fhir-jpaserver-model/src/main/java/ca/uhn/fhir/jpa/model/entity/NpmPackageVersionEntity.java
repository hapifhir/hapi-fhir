package ca.uhn.fhir.jpa.model.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "NPM_PACKAGE_VER", uniqueConstraints = {
}, indexes = {
})
public class NpmPackageVersionEntity {

	@EmbeddedId
	private NpmPackageVersionEntityPk myId;

	@OneToOne
	@JoinColumn(name = "BINARY_RES_ID", referencedColumnName = "RES_ID")
	private ResourceTable myPackageBinary;

}
