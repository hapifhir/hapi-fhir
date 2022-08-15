package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirVersionEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;

@Entity()
@Table(name = "NPM_PACKAGE_VER_RES", uniqueConstraints = {
}, indexes = {
	@Index(name = "IDX_PACKVERRES_URL", columnList = "CANONICAL_URL")
})
public class NpmPackageVersionResourceEntity {

	@Id
	@SequenceGenerator(name = "SEQ_NPM_PACKVERRES", sequenceName = "SEQ_NPM_PACKVERRES")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_NPM_PACKVERRES")
	@Column(name = "PID")
	private Long myId;
	@ManyToOne
	@JoinColumn(name = "PACKVER_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_NPM_PACKVERRES_PACKVER"), nullable = false)
	private NpmPackageVersionEntity myPackageVersion;
	@OneToOne
	@JoinColumn(name = "BINARY_RES_ID", referencedColumnName = "RES_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_NPM_PKVR_RESID"))
	private ResourceTable myResourceBinary;
	@Column(name = "FILE_DIR", length = 200)
	private String myDirectory;
	@Column(name = "FILE_NAME", length = 200)
	private String myFilename;
	@Column(name = "RES_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;
	@Column(name = "CANONICAL_URL", length = 200)
	private String myCanonicalUrl;
	@Column(name = "CANONICAL_VERSION", length = 200)
	private String myCanonicalVersion;
	@Enumerated(EnumType.STRING)
	@Column(name = "FHIR_VERSION", length = NpmPackageVersionEntity.FHIR_VERSION_LENGTH, nullable = false)
	private FhirVersionEnum myFhirVersion;
	@Column(name = "FHIR_VERSION_ID", length = NpmPackageVersionEntity.FHIR_VERSION_ID_LENGTH, nullable = false)
	private String myFhirVersionId;
	@Column(name = "RES_SIZE_BYTES", nullable = false)
	private long myResSizeBytes;
	@Temporal(TemporalType.TIMESTAMP)
	@Version
	@Column(name = "UPDATED_TIME", nullable = false)
	private Date myVersion;

	public long getResSizeBytes() {
		return myResSizeBytes;
	}

	public void setResSizeBytes(long theResSizeBytes) {
		myResSizeBytes = theResSizeBytes;
	}

	public String getCanonicalVersion() {
		return myCanonicalVersion;
	}

	public void setCanonicalVersion(String theCanonicalVersion) {
		myCanonicalVersion = theCanonicalVersion;
	}

	public ResourceTable getResourceBinary() {
		return myResourceBinary;
	}

	public void setResourceBinary(ResourceTable theResourceBinary) {
		myResourceBinary = theResourceBinary;
	}

	public String getFhirVersionId() {
		return myFhirVersionId;
	}

	public void setFhirVersionId(String theFhirVersionId) {
		myFhirVersionId = theFhirVersionId;
	}

	public FhirVersionEnum getFhirVersion() {
		return myFhirVersion;
	}

	public void setFhirVersion(FhirVersionEnum theFhirVersion) {
		myFhirVersion = theFhirVersion;
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

	public String getCanonicalUrl() {
		return myCanonicalUrl;
	}

	public void setCanonicalUrl(String theCanonicalUrl) {
		myCanonicalUrl = theCanonicalUrl;
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myId", myId)
			.append("myCanonicalUrl", myCanonicalUrl)
			.append("myCanonicalVersion", myCanonicalVersion)
			.append("myResourceType", myResourceType)
			.append("myDirectory", myDirectory)
			.append("myFilename", myFilename)
			.append("myPackageVersion", myPackageVersion)
			.append("myResSizeBytes", myResSizeBytes)
			.append("myVersion", myVersion)
			.toString();
	}

}
