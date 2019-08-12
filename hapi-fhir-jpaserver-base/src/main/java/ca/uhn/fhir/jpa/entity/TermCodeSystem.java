package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

//@formatter:off
@Table(name = "TRM_CODESYSTEM", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_CS_CODESYSTEM", columnNames = {"CODE_SYSTEM_URI"})
})
@Entity()
//@formatter:on
public class TermCodeSystem implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int MAX_NAME_LENGTH = 200;
	public static final int MAX_URL_LENGTH = 200;

	@Column(name = "CODE_SYSTEM_URI", nullable = false, length = MAX_URL_LENGTH)
	private String myCodeSystemUri;

	@OneToOne()
	@JoinColumn(name = "CURRENT_VERSION_PID", referencedColumnName = "PID", nullable = true, foreignKey = @ForeignKey(name = "FK_TRMCODESYSTEM_CURVER"))
	private TermCodeSystemVersion myCurrentVersion;
	@Id()
	@SequenceGenerator(name = "SEQ_CODESYSTEM_PID", sequenceName = "SEQ_CODESYSTEM_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CODESYSTEM_PID")
	@Column(name = "PID")
	private Long myPid;
	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TRMCODESYSTEM_RES"))
	private ResourceTable myResource;
	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;
	@Column(name = "CS_NAME", nullable = true, length = MAX_NAME_LENGTH)
	private String myName;

	/**
	 * Constructor
	 */
	public TermCodeSystem() {
		super();
	}

	public String getCodeSystemUri() {
		return myCodeSystemUri;
	}

	public String getName() {
		return myName;
	}

	public TermCodeSystem setCodeSystemUri(@Nonnull String theCodeSystemUri) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theCodeSystemUri, "theCodeSystemUri must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theCodeSystemUri, MAX_URL_LENGTH,
			"URI exceeds maximum length (" + MAX_URL_LENGTH + "): " + length(theCodeSystemUri));
		myCodeSystemUri = theCodeSystemUri;
		return this;
	}

	public TermCodeSystemVersion getCurrentVersion() {
		return myCurrentVersion;
	}

	public TermCodeSystem setCurrentVersion(TermCodeSystemVersion theCurrentVersion) {
		myCurrentVersion = theCurrentVersion;
		return this;
	}

	public Long getPid() {
		return myPid;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public TermCodeSystem setName(String theName) {
		myName = left(theName, MAX_NAME_LENGTH);
		return this;
	}

	public TermCodeSystem setResource(ResourceTable theResource) {
		myResource = theResource;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("codeSystemUri", myCodeSystemUri)
			.append("currentVersion", myCurrentVersion)
			.append("pid", myPid)
			.append("resourcePid", myResourcePid)
			.append("name", myName)
			.toString();
	}
}
