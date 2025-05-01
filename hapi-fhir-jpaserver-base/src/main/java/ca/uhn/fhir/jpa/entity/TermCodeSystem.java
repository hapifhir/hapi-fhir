/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.model.entity.BasePartitionable;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

@Table(
		name = "TRM_CODESYSTEM",
		uniqueConstraints = {
			@UniqueConstraint(
					name = "IDX_CS_CODESYSTEM",
					columnNames = {"PARTITION_ID", "CODE_SYSTEM_URI"})
		},
		indexes = {
			@Index(name = "FK_TRMCODESYSTEM_RES", columnList = "RES_ID"),
			@Index(name = "FK_TRMCODESYSTEM_CURVER", columnList = "CURRENT_VERSION_PID")
		})
@Entity()
@IdClass(IdAndPartitionId.class)
public class TermCodeSystem extends BasePartitionable implements Serializable {
	public static final int MAX_URL_LENGTH = 200;
	private static final long serialVersionUID = 1L;
	private static final int MAX_NAME_LENGTH = 200;
	public static final String FK_TRMCODESYSTEM_CURVER = "FK_TRMCODESYSTEM_CURVER";

	@Column(name = "CODE_SYSTEM_URI", nullable = false, length = MAX_URL_LENGTH)
	private String myCodeSystemUri;

	/**
	 * Note that this uses a separate partition_id column because it needs
	 * to be nullable, unlike the PK one which has to be non-nullable
	 * when we're including partition IDs in PKs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "CURRENT_VERSION_PID",
						referencedColumnName = "PID",
						insertable = false,
						updatable = false,
						nullable = true),
				@JoinColumn(
						name = "CURRENT_VERSION_PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = true)
			},
			foreignKey = @ForeignKey(name = FK_TRMCODESYSTEM_CURVER))
	private TermCodeSystemVersion myCurrentVersion;

	@Column(name = "CURRENT_VERSION_PID", nullable = true, insertable = true, updatable = true)
	private Long myCurrentVersionPid;

	@Column(name = "CURRENT_VERSION_PARTITION_ID", nullable = true, insertable = true, updatable = true)
	private Integer myCurrentVersionPartitionId;

	@Id()
	@SequenceGenerator(name = "SEQ_CODESYSTEM_PID", sequenceName = "SEQ_CODESYSTEM_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CODESYSTEM_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_ID",
						referencedColumnName = "RES_ID",
						nullable = false,
						updatable = false,
						insertable = false),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						nullable = false,
						updatable = false,
						insertable = false)
			},
			foreignKey = @ForeignKey(name = "FK_TRMCODESYSTEM_RES"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", nullable = false)
	private Long myResourcePid;

	@Column(name = "CS_NAME", nullable = true, length = MAX_NAME_LENGTH)
	private String myName;

	/**
	 * Constructor
	 */
	public TermCodeSystem() {
		super();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		TermCodeSystem that = (TermCodeSystem) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(myCodeSystemUri, that.myCodeSystemUri);
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		b.append(myCodeSystemUri);
		return b.toHashCode();
	}

	public String getCodeSystemUri() {
		return myCodeSystemUri;
	}

	public TermCodeSystem setCodeSystemUri(@Nonnull String theCodeSystemUri) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theCodeSystemUri, "theCodeSystemUri must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(
				theCodeSystemUri,
				MAX_URL_LENGTH,
				"URI exceeds maximum length (" + MAX_URL_LENGTH + "): " + length(theCodeSystemUri));
		myCodeSystemUri = theCodeSystemUri;
		return this;
	}

	public String getName() {
		return myName;
	}

	public TermCodeSystem setName(String theName) {
		myName = left(theName, MAX_NAME_LENGTH);
		return this;
	}

	public TermCodeSystemVersion getCurrentVersion() {
		return myCurrentVersion;
	}

	public TermCodeSystem setCurrentVersion(TermCodeSystemVersion theCurrentVersion) {
		if (theCurrentVersion == null) {
			myCurrentVersion = null;
			myCurrentVersionPid = null;
			myCurrentVersionPartitionId = null;
		} else {
			myCurrentVersion = theCurrentVersion;
			myCurrentVersionPid = theCurrentVersion.getPid();
			assert myCurrentVersionPid != null;
			myCurrentVersionPartitionId = theCurrentVersion.getPartitionId().getPartitionId();
		}
		return this;
	}

	public Long getPid() {
		return myId;
	}

	public IdAndPartitionId getPartitionedId() {
		return IdAndPartitionId.forId(myId, this);
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public TermCodeSystem setResource(ResourceTable theResource) {
		myResource = theResource;
		myResourcePid = theResource.getId().getId();
		setPartitionId(theResource.getPartitionId());
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("pid", myId);
		b.append("codeSystemUri", myCodeSystemUri);
		b.append("currentVersionPid", myCurrentVersionPid);
		b.append("resourcePid", myResourcePid);
		b.append("name", myName);
		return b.toString();
	}
}
