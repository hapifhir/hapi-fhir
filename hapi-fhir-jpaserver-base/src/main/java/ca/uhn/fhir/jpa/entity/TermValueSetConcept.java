package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

/*
 * DM 2019-08-01 - Do not use IDX_VALUESET_CONCEPT_CS_CD or IDX_VALUESET_CONCEPT_CS_CODE; this was previously used as an index so reusing the name will
 * bork up migration tasks.
 */
@Table(name = "TRM_VALUESET_CONCEPT", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_VS_CONCEPT_CSCD", columnNames = {"VALUESET_PID", "SYSTEM_URL", "CODEVAL"}),
	@UniqueConstraint(name = "IDX_VS_CONCEPT_ORDER", columnNames = {"VALUESET_PID", "VALUESET_ORDER"})
})
@Entity()
public class TermValueSetConcept implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id()
	@SequenceGenerator(name = "SEQ_VALUESET_CONCEPT_PID", sequenceName = "SEQ_VALUESET_CONCEPT_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VALUESET_CONCEPT_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VALUESET_PID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name = "FK_TRM_VALUESET_PID"))
	private TermValueSet myValueSet;

	@Column(name = "VALUESET_PID", insertable = false, updatable = false, nullable = false)
	private Long myValueSetPid;

	@Column(name = "INDEX_STATUS", nullable = true)
	private Long myIndexStatus;

	@Column(name = "VALUESET_ORDER", nullable = false)
	private int myOrder;

	@Transient
	private String myValueSetUrl;

	@Transient
	private String myValueSetName;

	@Column(name = "SOURCE_PID", nullable = true)
	private Long mySourceConceptPid;

	@Lob
	@Column(name = "SOURCE_DIRECT_PARENT_PIDS", nullable = true)
	private String mySourceConceptDirectParentPids;

	@Column(name = "SYSTEM_URL", nullable = false, length = TermCodeSystem.MAX_URL_LENGTH)
	private String mySystem;

	@Column(name = "SYSTEM_VER", nullable = true, length = TermCodeSystemVersion.MAX_VERSION_LENGTH)
	private String mySystemVer;

	@Column(name = "CODEVAL", nullable = false, length = TermConcept.MAX_CODE_LENGTH)
	private String myCode;

	@Column(name = "DISPLAY", nullable = true, length = TermConcept.MAX_DESC_LENGTH)
	private String myDisplay;

	@OneToMany(mappedBy = "myConcept", fetch = FetchType.LAZY)
	private List<TermValueSetConceptDesignation> myDesignations;

	@Transient
	private transient Integer myHashCode;

	/**
	 * Constructor
	 */
	public TermValueSetConcept() {
		super();
	}

	public Long getId() {
		return myId;
	}

	public TermValueSet getValueSet() {
		return myValueSet;
	}

	public TermValueSetConcept setValueSet(TermValueSet theValueSet) {
		myValueSet = theValueSet;
		return this;
	}

	public int getOrder() {
		return myOrder;
	}

	public TermValueSetConcept setOrder(int theOrder) {
		myOrder = theOrder;
		return this;
	}

	public String getValueSetUrl() {
		if (myValueSetUrl == null) {
			myValueSetUrl = getValueSet().getUrl();
		}

		return myValueSetUrl;
	}

	public String getValueSetName() {
		if (myValueSetName == null) {
			myValueSetName = getValueSet().getName();
		}

		return myValueSetName;
	}

	public String getSystem() {
		return mySystem;
	}

	public TermValueSetConcept setSystem(@Nonnull String theSystem) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theSystem, "theSystem must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theSystem, TermCodeSystem.MAX_URL_LENGTH,
			"System exceeds maximum length (" + TermCodeSystem.MAX_URL_LENGTH + "): " + length(theSystem));
		mySystem = theSystem;
		return this;
	}

	public String getSystemVersion() {
		return mySystemVer;
	}

	public TermValueSetConcept setSystemVersion(String theSystemVersion) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theSystemVersion, TermCodeSystemVersion.MAX_VERSION_LENGTH,
			"System version exceeds maximum length (" + TermCodeSystemVersion.MAX_VERSION_LENGTH + "): " + length(theSystemVersion));
		mySystemVer = theSystemVersion;
		return this;
	}

	public String getCode() {
		return myCode;
	}

	public TermValueSetConcept setCode(@Nonnull String theCode) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theCode, "theCode must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theCode, TermConcept.MAX_CODE_LENGTH,
			"Code exceeds maximum length (" + TermConcept.MAX_CODE_LENGTH + "): " + length(theCode));
		myCode = theCode;
		return this;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public TermValueSetConcept setDisplay(String theDisplay) {
		myDisplay = left(theDisplay, TermConcept.MAX_DESC_LENGTH);
		return this;
	}

	public List<TermValueSetConceptDesignation> getDesignations() {
		if (myDesignations == null) {
			myDesignations = new ArrayList<>();
		}

		return myDesignations;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof TermValueSetConcept)) return false;

		TermValueSetConcept that = (TermValueSetConcept) theO;

		return new EqualsBuilder()
			.append(myValueSetPid, that.myValueSetPid)
			.append(getSystem(), that.getSystem())
			.append(getCode(), that.getCode())
			.isEquals();
	}

	@Override
	public int hashCode() {
		if (myHashCode == null) {
			myHashCode = new HashCodeBuilder(17, 37)
				.append(myValueSetPid)
				.append(getSystem())
				.append(getCode())
				.toHashCode();
		}
		return myHashCode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", myId)
			.append("order", myOrder)
			.append("system", mySystem)
			.append("code", myCode)
			.append("valueSet", myValueSet != null ? myValueSet.getId() : "(null)")
			.append("valueSetPid", myValueSetPid)
			.append("valueSetUrl", this.getValueSetUrl())
			.append("valueSetName", this.getValueSetName())
			.append("display", myDisplay)
			.append("designationCount", myDesignations != null ? myDesignations.size() : "(null)")
			.append("parentPids", mySourceConceptDirectParentPids)
			.toString();
	}

	public Long getIndexStatus() {
		return myIndexStatus;
	}

	public void setIndexStatus(Long theIndexStatus) {
		myIndexStatus = theIndexStatus;
	}

	public void setSourceConceptPid(Long theSourceConceptPid) {
		mySourceConceptPid = theSourceConceptPid;
	}

	public void setSourceConceptDirectParentPids(String theSourceConceptDirectParentPids) {
		mySourceConceptDirectParentPids = theSourceConceptDirectParentPids;
	}
}
