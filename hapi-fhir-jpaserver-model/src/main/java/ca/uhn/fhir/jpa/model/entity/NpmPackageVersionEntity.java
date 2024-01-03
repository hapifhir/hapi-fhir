/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.util.StringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

@Entity()
@Table(
		name = "NPM_PACKAGE_VER",
		uniqueConstraints = {},
		indexes = {
			@Index(name = "IDX_PACKVER", columnList = "PACKAGE_ID,VERSION_ID", unique = true),
			@Index(name = "FK_NPM_PKV_PKG", columnList = "PACKAGE_PID"),
			@Index(name = "FK_NPM_PKV_RESID", columnList = "BINARY_RES_ID")
		})
public class NpmPackageVersionEntity {

	public static final int VERSION_ID_LENGTH = 200;
	public static final int PACKAGE_DESC_LENGTH = 200;
	public static final int FHIR_VERSION_LENGTH = 10;
	public static final int FHIR_VERSION_ID_LENGTH = 20;

	@SequenceGenerator(name = "SEQ_NPM_PACKVER", sequenceName = "SEQ_NPM_PACKVER")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_NPM_PACKVER")
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "PACKAGE_ID", length = NpmPackageEntity.PACKAGE_ID_LENGTH, nullable = false)
	private String myPackageId;

	@Column(name = "VERSION_ID", length = NpmPackageVersionEntity.VERSION_ID_LENGTH, nullable = false)
	private String myVersionId;

	@ManyToOne
	@JoinColumn(name = "PACKAGE_PID", nullable = false, foreignKey = @ForeignKey(name = "FK_NPM_PKV_PKG"))
	private NpmPackageEntity myPackage;

	@ManyToOne
	@JoinColumn(
			name = "BINARY_RES_ID",
			referencedColumnName = "RES_ID",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_NPM_PKV_RESID"))
	private ResourceTable myPackageBinary;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAVED_TIME", nullable = false)
	private Date mySavedTime;

	@Column(name = "PKG_DESC", nullable = true, length = PACKAGE_DESC_LENGTH)
	private String myDescription;

	@Column(name = "DESC_UPPER", nullable = true, length = PACKAGE_DESC_LENGTH)
	private String myDescriptionUpper;

	@Column(name = "CURRENT_VERSION", nullable = false)
	private boolean myCurrentVersion;

	@Column(name = "FHIR_VERSION_ID", length = NpmPackageVersionEntity.FHIR_VERSION_ID_LENGTH, nullable = false)
	private String myFhirVersionId;

	@Enumerated(EnumType.STRING)
	@Column(name = "FHIR_VERSION", length = NpmPackageVersionEntity.FHIR_VERSION_LENGTH, nullable = false)
	private FhirVersionEnum myFhirVersion;

	@Column(name = "PACKAGE_SIZE_BYTES", nullable = false)
	private long myPackageSizeBytes;

	@Temporal(TemporalType.TIMESTAMP)
	@Version
	@Column(name = "UPDATED_TIME", nullable = false)
	private Date myUpdatedTime;

	@OneToMany(mappedBy = "myPackageVersion")
	private List<NpmPackageVersionResourceEntity> myResources;

	public Date getUpdatedTime() {
		return myUpdatedTime;
	}

	public long getPackageSizeBytes() {
		return myPackageSizeBytes;
	}

	public void setPackageSizeBytes(long thePackageSizeBytes) {
		myPackageSizeBytes = thePackageSizeBytes;
	}

	public boolean isCurrentVersion() {
		return myCurrentVersion;
	}

	public void setCurrentVersion(boolean theCurrentVersion) {
		myCurrentVersion = theCurrentVersion;
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

	public void setSavedTime(Date theSavedTime) {
		mySavedTime = theSavedTime;
	}

	public String getDescription() {
		return myDescription;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
		myDescriptionUpper = StringUtil.normalizeStringForSearchIndexing(theDescription);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("myId", myId)
				.append("myPackageId", myPackageId)
				.append("myVersionId", myVersionId)
				.append("myDescriptionUpper", myDescriptionUpper)
				.append("myFhirVersionId", myFhirVersionId)
				.toString();
	}

	public List<NpmPackageVersionResourceEntity> getResources() {
		return myResources;
	}
}
