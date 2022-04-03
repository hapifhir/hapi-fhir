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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.ColumnDefault;

import javax.annotation.Nonnull;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

@Table(name = "TRM_VALUESET", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_VALUESET_URL", columnNames = {"URL", "VER"})
})
@Entity()
public class TermValueSet implements Serializable {
	public static final int MAX_EXPANSION_STATUS_LENGTH = 50;
	public static final int MAX_NAME_LENGTH = 200;
	public static final int MAX_URL_LENGTH = 200;
	public static final int MAX_VER_LENGTH = 200;
	private static final long serialVersionUID = 1L;
	@Id()
	@SequenceGenerator(name = "SEQ_VALUESET_PID", sequenceName = "SEQ_VALUESET_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_VALUESET_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "URL", nullable = false, length = MAX_URL_LENGTH)
	private String myUrl;

	@Column(name = "VER", nullable = true, length = MAX_VER_LENGTH)
	private String myVersion;

	@OneToOne()
	@JoinColumn(name = "RES_ID", referencedColumnName = "RES_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TRMVALUESET_RES"))
	private ResourceTable myResource;

	@Column(name = "RES_ID", insertable = false, updatable = false)
	private Long myResourcePid;

	@Column(name = "VSNAME", nullable = true, length = MAX_NAME_LENGTH)
	private String myName;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPANDED_AT", nullable = true)
	private Date myExpansionTimestamp;

	@Transient
	private transient Integer myHashCode;

	public TermValueSet() {
		super();
		myExpansionStatus = TermValueSetPreExpansionStatusEnum.NOT_EXPANDED;
		myTotalConcepts = 0L;
		myTotalConceptDesignations = 0L;
	}

	public Date getExpansionTimestamp() {
		return myExpansionTimestamp;
	}

	public void setExpansionTimestamp(Date theExpansionTimestamp) {
		myExpansionTimestamp = theExpansionTimestamp;
	}

	public Long getId() {
		return myId;
	}

	public String getUrl() {
		return myUrl;
	}

	public TermValueSet setUrl(@Nonnull String theUrl) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theUrl, "theUrl must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theUrl, MAX_URL_LENGTH,
			"URL exceeds maximum length (" + MAX_URL_LENGTH + "): " + length(theUrl));
		myUrl = theUrl;
		return this;
	}

	public ResourceTable getResource() {
		return myResource;
	}

	public TermValueSet setResource(ResourceTable theResource) {
		myResource = theResource;
		return this;
	}

	public String getName() {
		return myName;
	}

	public TermValueSet setName(String theName) {
		myName = left(theName, MAX_NAME_LENGTH);
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

	public TermValueSet setTotalConcepts(Long theTotalConcepts) {
		myTotalConcepts = theTotalConcepts;
		return this;
	}

	public TermValueSet decrementTotalConcepts() {
		if (myTotalConcepts > 0) {
			myTotalConcepts--;
		}
		return this;
	}

	public TermValueSet incrementTotalConcepts() {
		myTotalConcepts++;
		return this;
	}

	public Long getTotalConceptDesignations() {
		return myTotalConceptDesignations;
	}

	public TermValueSet setTotalConceptDesignations(Long theTotalConceptDesignations) {
		myTotalConceptDesignations = theTotalConceptDesignations;
		return this;
	}

	public TermValueSet decrementTotalConceptDesignations() {
		if (myTotalConceptDesignations > 0) {
			myTotalConceptDesignations--;
		}
		return this;
	}

	public TermValueSet incrementTotalConceptDesignations() {
		myTotalConceptDesignations++;
		return this;
	}

	public TermValueSetPreExpansionStatusEnum getExpansionStatus() {
		return myExpansionStatus;
	}

	public void setExpansionStatus(TermValueSetPreExpansionStatusEnum theExpansionStatus) {
		myExpansionStatus = theExpansionStatus;
	}

	public String getVersion() {
		return myVersion;
	}

	public TermValueSet setVersion(String theVersion) {
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theVersion, MAX_VER_LENGTH,
			"Version exceeds maximum length (" + MAX_VER_LENGTH + "): " + length(theVersion));
		myVersion = theVersion;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof TermValueSet)) return false;

		TermValueSet that = (TermValueSet) theO;

		return new EqualsBuilder()
			.append(getUrl(), that.getUrl())
			.isEquals();
	}

	@Override
	public int hashCode() {
		if (myHashCode == null) {
			myHashCode = getUrl().hashCode();
		}
		return myHashCode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", myId)
			.append("url", myUrl)
			.append(myResource != null ? ("resource=" + myResource.toString()) : ("resource=(null)"))
			.append("resourcePid", myResourcePid)
			.append("name", myName)
			.append(myConcepts != null ? ("concepts - size=" + myConcepts.size()) : ("concepts=(null)"))
			.append("totalConcepts", myTotalConcepts)
			.append("totalConceptDesignations", myTotalConceptDesignations)
			.append("expansionStatus", myExpansionStatus)
			.toString();
	}
}
