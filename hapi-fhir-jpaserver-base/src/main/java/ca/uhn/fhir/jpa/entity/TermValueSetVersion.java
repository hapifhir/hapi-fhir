package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.length;

@Table(name = "TRM_VALUESET_VER", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_VALUESET_URL_AND_VER", columnNames = {"VALUESET_PID", "VS_VERSION_ID"})
})
@Entity()
public class TermValueSetVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAX_EXPANSION_STATUS_LENGTH = 50;
	public static final int MAX_NAME_LENGTH = 200;
	public static final int MAX_VERSION_LENGTH = 200;

	@Id()
	@SequenceGenerator(name = "SEQ_VALUESETVER_PID", sequenceName = "SEQ_VALUESETVER_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VALUESETVER_PID")
	@Column(name = "PID")
	private Long myId;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TRMVALUESETVER_RES"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	@OneToMany(mappedBy = "myValueSet", fetch = FetchType.LAZY)
	private List<TermValueSetConcept> myConcepts;

	@Column(name = "TOTAL_CONCEPTS", nullable = false)
	@ColumnDefault("0")
	private Long myTotalConcepts;

	@Column(name = "TOTAL_CONCEPT_DESIGNATIONS", nullable = false)
	@ColumnDefault("0")
	private Long myTotalConceptDesignations;

	@Enumerated(EnumType.STRING)
	@Column(name = "EXPANSION_STATUS", nullable = false, length = MAX_EXPANSION_STATUS_LENGTH)
	private TermValueSetPreExpansionStatusEnum myExpansionStatus;

	@Column(name = "VS_VERSION_ID", nullable = true, updatable = false, length = MAX_VERSION_LENGTH)
	private String myValueSetVersionId;

	/**
	 * This was added in HAPI FHIR 3.3.0 and is nullable just to avoid migration
	 * issued. It should be made non-nullable at some point.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VALUESET_PID", referencedColumnName = "PID", nullable = true, foreignKey = @ForeignKey(name = "FK_VALSETVER_VS_ID"))
	private TermValueSet myValueSet;

	@Column(name = "VALUESET_PID", insertable = false, updatable = false)
	private Long myValueSetPid;

	@SuppressWarnings("unused")
	@OneToOne(mappedBy = "myCurrentVersion", optional = true, fetch = FetchType.LAZY)
	private TermValueSet myValueSetHavingThisVersionAsCurrentVersionIfAny;

	public TermValueSetVersion() {
		super();
		myExpansionStatus = TermValueSetPreExpansionStatusEnum.NOT_EXPANDED;
		myTotalConcepts = 0L;
		myTotalConceptDesignations = 0L;
	}

	public Long getId() {
		return myId;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public TermValueSetVersion setResource(ResourceTable theResource) {
		myResource = theResource;
		return this;
	}

	public List<TermValueSetConcept> getConcepts() {
		if (myConcepts == null) {
			myConcepts = new ArrayList<>();
		}

		return myConcepts;
	}

	public Long getTotalConcepts() {
		return myTotalConcepts;
	}

	public TermValueSetVersion setTotalConcepts(Long theTotalConcepts) {
		myTotalConcepts = theTotalConcepts;
		return this;
	}

	public TermValueSetVersion decrementTotalConcepts() {
		if (myTotalConcepts > 0) {
			myTotalConcepts--;
		}
		return this;
	}

	public TermValueSetVersion incrementTotalConcepts() {
		myTotalConcepts++;
		return this;
	}

	public Long getTotalConceptDesignations() {
		return myTotalConceptDesignations;
	}

	public TermValueSetVersion setTotalConceptDesignations(Long theTotalConceptDesignations) {
		myTotalConceptDesignations = theTotalConceptDesignations;
		return this;
	}

	public TermValueSetVersion decrementTotalConceptDesignations() {
		if (myTotalConceptDesignations > 0) {
			myTotalConceptDesignations--;
		}
		return this;
	}

	public TermValueSetVersion incrementTotalConceptDesignations() {
		myTotalConceptDesignations++;
		return this;
	}

	public TermValueSetPreExpansionStatusEnum getExpansionStatus() {
		return myExpansionStatus;
	}

	public void setExpansionStatus(TermValueSetPreExpansionStatusEnum theExpansionStatus) {
		myExpansionStatus = theExpansionStatus;
	}

	public String getValueSetVersionId() {
		return myValueSetVersionId;
	}

	public TermValueSetVersion setValueSetVersionId(String theValueSetVersionId) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(
			theValueSetVersionId, MAX_VERSION_LENGTH,
			"Version ID exceeds maximum length (" + MAX_VERSION_LENGTH + "): " + length(theValueSetVersionId));
		myValueSetVersionId = theValueSetVersionId;
		return this;
	}

	public TermValueSet getValueSet() {
		return myValueSet;
	}

	public TermValueSetVersion setValueSet(TermValueSet theValueSet) {
		myValueSet = theValueSet;
		return this;
	}

	public Long getValueSetPid() {
		return myValueSetPid;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof TermValueSetVersion)) return false;

		TermValueSetVersion that = (TermValueSetVersion) theO;

		return new EqualsBuilder()
			.append(myValueSetVersionId, that.myValueSetVersionId)
			.append(myValueSetPid, that.myValueSetPid)
			.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		b.append(myValueSetVersionId);
		b.append(myValueSetPid);
		return b.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("myId", myId)
			.append(myResource != null ? ("myResource=" + myResource.toString()) : ("myResource=(null)"))
			.append("myResourcePid", myResourcePid)
			.append("valueSetPid", myValueSetPid)
			.append("valueSetVersionId", myValueSetVersionId)
			.append(myConcepts != null ? ("myConcepts - size=" + myConcepts.size()) : ("myConcepts=(null)"))
			.append("myTotalConcepts", myTotalConcepts)
			.append("myTotalConceptDesignations", myTotalConceptDesignations)
			.append("myExpansionStatus", myExpansionStatus)
			.toString();
	}
}
