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
import javax.persistence.*;
import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

@Table(name = "TRM_VALUESET_C_DESIGNATION", indexes = {
	// must have same name that indexed FK or SchemaMigrationTest complains because H2 sets this index automatically
	@Index(name = "FK_TRM_VALUESET_CONCEPT_PID",  columnList = "VALUESET_CONCEPT_PID", unique = false)
})
@Entity()
public class TermValueSetConceptDesignation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id()
	@SequenceGenerator(name = "SEQ_VALUESET_C_DSGNTN_PID", sequenceName = "SEQ_VALUESET_C_DSGNTN_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VALUESET_C_DSGNTN_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VALUESET_CONCEPT_PID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name = "FK_TRM_VALUESET_CONCEPT_PID"))
	private TermValueSetConcept myConcept;

	@Column(name = "VALUESET_CONCEPT_PID", insertable = false, updatable = false, nullable = false)
	private Long myConceptPid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VALUESET_PID", referencedColumnName = "PID", nullable = false, foreignKey = @ForeignKey(name = "FK_TRM_VSCD_VS_PID"))
	private TermValueSet myValueSet;

	@Column(name = "VALUESET_PID", insertable = false, updatable = false, nullable = false)
	private Long myValueSetPid;

	@Transient
	private String myValueSetUrl;

	@Transient
	private String myValueSetName;

	@Column(name = "LANG", nullable = true, length = TermConceptDesignation.MAX_LENGTH)
	private String myLanguage;

	@Column(name = "USE_SYSTEM", nullable = true, length = TermConceptDesignation.MAX_LENGTH)
	private String myUseSystem;

	@Column(name = "USE_CODE", nullable = true, length = TermConceptDesignation.MAX_LENGTH)
	private String myUseCode;

	@Column(name = "USE_DISPLAY", nullable = true, length = TermConceptDesignation.MAX_LENGTH)
	private String myUseDisplay;

	@Column(name = "VAL", nullable = false, length = TermConceptDesignation.MAX_VAL_LENGTH)
	private String myValue;

	@Transient
	private transient Integer myHashCode;

	public Long getId() {
		return myId;
	}

	public TermValueSetConcept getConcept() {
		return myConcept;
	}

	public TermValueSetConceptDesignation setConcept(TermValueSetConcept theConcept) {
		myConcept = theConcept;
		return this;
	}

	public TermValueSet getValueSet() {
		return myValueSet;
	}

	public TermValueSetConceptDesignation setValueSet(TermValueSet theValueSet) {
		myValueSet = theValueSet;
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

	public String getLanguage() {
		return myLanguage;
	}

	public TermValueSetConceptDesignation setLanguage(String theLanguage) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theLanguage, TermConceptDesignation.MAX_LENGTH,
			"Language exceeds maximum length (" + TermConceptDesignation.MAX_LENGTH + "): " + length(theLanguage));
		myLanguage = theLanguage;
		return this;
	}

	public String getUseSystem() {
		return myUseSystem;
	}

	public TermValueSetConceptDesignation setUseSystem(String theUseSystem) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theUseSystem, TermConceptDesignation.MAX_LENGTH,
			"Use system exceeds maximum length (" + TermConceptDesignation.MAX_LENGTH + "): " + length(theUseSystem));
		myUseSystem = theUseSystem;
		return this;
	}

	public String getUseCode() {
		return myUseCode;
	}

	public TermValueSetConceptDesignation setUseCode(String theUseCode) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theUseCode, TermConceptDesignation.MAX_LENGTH,
			"Use code exceeds maximum length (" + TermConceptDesignation.MAX_LENGTH + "): " + length(theUseCode));
		myUseCode = theUseCode;
		return this;
	}

	public String getUseDisplay() {
		return myUseDisplay;
	}

	public TermValueSetConceptDesignation setUseDisplay(String theUseDisplay) {
		myUseDisplay = left(theUseDisplay, TermConceptDesignation.MAX_LENGTH);
		return this;
	}

	public String getValue() {
		return myValue;
	}

	public TermValueSetConceptDesignation setValue(@Nonnull String theValue) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theValue, "theValue must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theValue, TermConceptDesignation.MAX_VAL_LENGTH,
			"Value exceeds maximum length (" + TermConceptDesignation.MAX_VAL_LENGTH + "): " + length(theValue));
		myValue = theValue;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof TermValueSetConceptDesignation)) return false;

		TermValueSetConceptDesignation that = (TermValueSetConceptDesignation) theO;

		return new EqualsBuilder()
			.append(getLanguage(), that.getLanguage())
			.append(getUseSystem(), that.getUseSystem())
			.append(getUseCode(), that.getUseCode())
			.append(getUseDisplay(), that.getUseDisplay())
			.append(getValue(), that.getValue())
			.isEquals();
	}

	@Override
	public int hashCode() {
		if (myHashCode == null) {
			myHashCode = new HashCodeBuilder(17, 37)
				.append(getLanguage())
				.append(getUseSystem())
				.append(getUseCode())
				.append(getUseDisplay())
				.append(getValue())
				.toHashCode();
		}
		return myHashCode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myId", myId)
			.append(myConcept != null ? ("myConcept - id=" + myConcept.getId()) : ("myConcept=(null)"))
			.append("myConceptPid", myConceptPid)
			.append(myValueSet != null ? ("myValueSet - id=" + myValueSet.getId()) : ("myValueSet=(null)"))
			.append("myValueSetPid", myValueSetPid)
			.append("myValueSetUrl", this.getValueSetUrl())
			.append("myValueSetName", this.getValueSetName())
			.append("myLanguage", myLanguage)
			.append("myUseSystem", myUseSystem)
			.append("myUseCode", myUseCode)
			.append("myUseDisplay", myUseDisplay)
			.append("myValue", myValue)
			.toString();
	}
}
