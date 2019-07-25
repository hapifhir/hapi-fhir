package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.search.DeferConceptIndexingInterceptor;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.*;
import org.hl7.fhir.r4.model.Coding;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.persistence.Index;
import java.io.Serializable;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

@Entity
@Indexed(interceptor = DeferConceptIndexingInterceptor.class)
@Table(name = "TRM_CONCEPT", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_CONCEPT_CS_CODE", columnNames = {"CODESYSTEM_PID", "CODE"})
}, indexes = {
	@Index(name = "IDX_CONCEPT_INDEXSTATUS", columnList = "INDEX_STATUS"),
	@Index(name = "IDX_CONCEPT_UPDATED", columnList = "CONCEPT_UPDATED")
})
public class TermConcept implements Serializable {
	public static final int CODE_LENGTH = 500;
	protected static final int MAX_DESC_LENGTH = 400;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TermConcept.class);

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myParent", cascade = {})
	private Collection<TermConceptParentChildLink> myChildren;

	@Column(name = "CODE", length = CODE_LENGTH, nullable = false)
	@Fields({@Field(name = "myCode", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "exactAnalyzer")),})
	private String myCode;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONCEPT_UPDATED", nullable = true)
	private Date myUpdated;
	@ManyToOne()
	@JoinColumn(name = "CODESYSTEM_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPT_PID_CS_PID"))
	private TermCodeSystemVersion myCodeSystem;
	@Column(name = "CODESYSTEM_PID", insertable = false, updatable = false)
	@Fields({@Field(name = "myCodeSystemVersionPid")})
	private long myCodeSystemVersionPid;
	@Column(name = "DISPLAY", length = MAX_DESC_LENGTH, nullable = true)
	@Fields({
		@Field(name = "myDisplay", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
		@Field(name = "myDisplayEdgeNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
		@Field(name = "myDisplayNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer")),
		@Field(name = "myDisplayPhonetic", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompletePhoneticAnalyzer"))
	})
	private String myDisplay;
	@OneToMany(mappedBy = "myConcept", orphanRemoval = false)
	@Field(name = "PROPmyProperties", analyzer = @Analyzer(definition = "termConceptPropertyAnalyzer"))
	@FieldBridge(impl = TermConceptPropertyFieldBridge.class)
	private Collection<TermConceptProperty> myProperties;
	@OneToMany(mappedBy = "myConcept", orphanRemoval = false)
	private Collection<TermConceptDesignation> myDesignations;
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_PID", sequenceName = "SEQ_CONCEPT_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_PID")
	@Column(name = "PID")
	private Long myId;
	@Column(name = "INDEX_STATUS", nullable = true)
	private Long myIndexStatus;
	@Field(name = "myParentPids", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "conceptParentPidsAnalyzer"))
	@Lob
	@Column(name = "PARENT_PIDS", nullable = true)
	private String myParentPids;
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "myChild")
	private Collection<TermConceptParentChildLink> myParents;
	@Column(name = "CODE_SEQUENCE", nullable = true)
	private Integer mySequence;

	public TermConcept() {
		super();
	}

	public TermConcept(TermCodeSystemVersion theCs, String theCode) {
		setCodeSystemVersion(theCs);
		setCode(theCode);
	}

	public TermConcept addChild(TermConcept theChild, RelationshipTypeEnum theRelationshipType) {
		Validate.notNull(theRelationshipType, "theRelationshipType must not be null");
		TermConceptParentChildLink link = new TermConceptParentChildLink();
		link.setParent(this);
		link.setChild(theChild);
		link.setRelationshipType(theRelationshipType);
		getChildren().add(link);

		theChild.getParents().add(link);
		return this;
	}

	public void addChildren(List<TermConcept> theChildren, RelationshipTypeEnum theRelationshipType) {
		for (TermConcept next : theChildren) {
			addChild(next, theRelationshipType);
		}
	}

	public TermConceptDesignation addDesignation() {
		TermConceptDesignation designation = new TermConceptDesignation();
		designation.setConcept(this);
		designation.setCodeSystemVersion(myCodeSystem);
		getDesignations().add(designation);
		return designation;
	}

	private TermConceptProperty addProperty(@Nonnull TermConceptPropertyTypeEnum thePropertyType, @Nonnull String thePropertyName, @Nonnull String thePropertyValue) {
		Validate.notBlank(thePropertyName);

		TermConceptProperty property = new TermConceptProperty();
		property.setConcept(this);
		property.setCodeSystemVersion(myCodeSystem);
		property.setType(thePropertyType);
		property.setKey(thePropertyName);
		property.setValue(thePropertyValue);
		getProperties().add(property);

		return property;
	}

	public TermConceptProperty addPropertyCoding(@Nonnull String thePropertyName, @Nonnull String thePropertyCodeSystem, @Nonnull String thePropertyCode, String theDisplayName) {
		return addProperty(TermConceptPropertyTypeEnum.CODING, thePropertyName, thePropertyCode)
			.setCodeSystem(thePropertyCodeSystem)
			.setDisplay(theDisplayName);
	}

	public TermConceptProperty addPropertyString(@Nonnull String thePropertyName, @Nonnull String thePropertyValue) {
		return addProperty(TermConceptPropertyTypeEnum.STRING, thePropertyName, thePropertyValue);
	}

	@Override
	public boolean equals(Object theObj) {
		if (!(theObj instanceof TermConcept)) {
			return false;
		}
		if (theObj == this) {
			return true;
		}

		TermConcept obj = (TermConcept) theObj;

		EqualsBuilder b = new EqualsBuilder();
		b.append(myCodeSystem, obj.myCodeSystem);
		b.append(myCode, obj.myCode);
		return b.isEquals();
	}

	public Collection<TermConceptParentChildLink> getChildren() {
		if (myChildren == null) {
			myChildren = new ArrayList<>();
		}
		return myChildren;
	}

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theCode, "Code must not be null or empty");
		myCode = theCode;
	}

	public TermCodeSystemVersion getCodeSystemVersion() {
		return myCodeSystem;
	}

	public void setCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystem = theCodeSystemVersion;
		if (theCodeSystemVersion.getPid() != null) {
			myCodeSystemVersionPid = theCodeSystemVersion.getPid();
		}
	}

	public List<Coding> getCodingProperties(String thePropertyName) {
		List<Coding> retVal = new ArrayList<>();
		for (TermConceptProperty next : getProperties()) {
			if (thePropertyName.equals(next.getKey())) {
				if (next.getType() == TermConceptPropertyTypeEnum.CODING) {
					Coding coding = new Coding();
					coding.setSystem(next.getCodeSystem());
					coding.setCode(next.getValue());
					coding.setDisplay(next.getDisplay());
					retVal.add(coding);
				}
			}
		}
		return retVal;
	}

	public Collection<TermConceptDesignation> getDesignations() {
		if (myDesignations == null) {
			myDesignations = new ArrayList<>();
		}
		return myDesignations;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public TermConcept setDisplay(String theDisplay) {
		myDisplay = theDisplay;
		if (isNotBlank(theDisplay) && theDisplay.length() > MAX_DESC_LENGTH) {
			myDisplay = myDisplay.substring(0, MAX_DESC_LENGTH);
		}
		return this;
	}

	public Long getId() {
		return myId;
	}

	public Long getIndexStatus() {
		return myIndexStatus;
	}

	public void setIndexStatus(Long theIndexStatus) {
		myIndexStatus = theIndexStatus;
	}

	public String getParentPidsAsString() {
		return myParentPids;
	}

	public Collection<TermConceptParentChildLink> getParents() {
		if (myParents == null) {
			myParents = new ArrayList<>();
		}
		return myParents;
	}

	public Collection<TermConceptProperty> getProperties() {
		if (myProperties == null) {
			myProperties = new ArrayList<>();
		}
		return myProperties;
	}

	public Integer getSequence() {
		return mySequence;
	}

	public void setSequence(Integer theSequence) {
		mySequence = theSequence;
	}

	public List<String> getStringProperties(String thePropertyName) {
		List<String> retVal = new ArrayList<>();
		for (TermConceptProperty next : getProperties()) {
			if (thePropertyName.equals(next.getKey())) {
				if (next.getType() == TermConceptPropertyTypeEnum.STRING) {
					retVal.add(next.getValue());
				}
			}
		}
		return retVal;
	}

	public String getStringProperty(String thePropertyName) {
		List<String> properties = getStringProperties(thePropertyName);
		if (properties.size() > 0) {
			return properties.get(0);
		}
		return null;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(myCodeSystem);
		b.append(myCode);
		return b.toHashCode();
	}

	private void parentPids(TermConcept theNextConcept, Set<Long> theParentPids) {
		for (TermConceptParentChildLink nextParentLink : theNextConcept.getParents()) {
			TermConcept parent = nextParentLink.getParent();
			if (parent != null) {
				Long parentConceptId = parent.getId();
				Validate.notNull(parentConceptId);
				if (theParentPids.add(parentConceptId)) {
					parentPids(parent, theParentPids);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	@PreUpdate
	@PrePersist
	public void prePersist() {
		if (myParentPids == null) {
			Set<Long> parentPids = new HashSet<>();
			TermConcept entity = this;
			parentPids(entity, parentPids);
			entity.setParentPids(parentPids);

			ourLog.trace("Code {}/{} has parents {}", entity.getId(), entity.getCode(), entity.getParentPidsAsString());
		}
	}

	private void setParentPids(Set<Long> theParentPids) {
		StringBuilder b = new StringBuilder();
		for (Long next : theParentPids) {
			if (b.length() > 0) {
				b.append(' ');
			}
			b.append(next);
		}

		if (b.length() == 0) {
			b.append("NONE");
		}

		myParentPids = b.toString();
	}

	public void setParentPids(String theParentPids) {
		myParentPids = theParentPids;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("code", myCode).append("display", myDisplay).build();
	}

	public List<IContextValidationSupport.BaseConceptProperty> toValidationProperties() {
		List<IContextValidationSupport.BaseConceptProperty> retVal = new ArrayList<>();
		for (TermConceptProperty next : getProperties()) {
			switch (next.getType()) {
				case STRING:
					retVal.add(new IContextValidationSupport.StringConceptProperty(next.getKey(), next.getValue()));
					break;
				case CODING:
					retVal.add(new IContextValidationSupport.CodingConceptProperty(next.getKey(), next.getCodeSystem(), next.getValue(), next.getDisplay()));
					break;
				default:
					throw new IllegalStateException("Don't know how to handle " + next.getType());
			}
		}
		return retVal;
	}
}
