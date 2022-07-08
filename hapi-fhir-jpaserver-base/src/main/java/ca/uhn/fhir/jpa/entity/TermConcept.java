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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.search.DeferConceptIndexingRoutingBinder;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.RoutingBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hl7.fhir.r4.model.Coding;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.length;

@Entity
@Indexed(routingBinder=@RoutingBinderRef(type =  DeferConceptIndexingRoutingBinder.class))
@Table(name = "TRM_CONCEPT", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_CONCEPT_CS_CODE", columnNames = {"CODESYSTEM_PID", "CODEVAL"})
}, indexes = {
	@Index(name = "IDX_CONCEPT_INDEXSTATUS", columnList = "INDEX_STATUS"),
	@Index(name = "IDX_CONCEPT_UPDATED", columnList = "CONCEPT_UPDATED")
})
public class TermConcept implements Serializable {
	public static final int MAX_CODE_LENGTH = 500;
	public static final int MAX_DESC_LENGTH = 400;
	public static final int MAX_DISP_LENGTH = 500;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TermConcept.class);
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myParent", cascade = {})
	private List<TermConceptParentChildLink> myChildren;

	@Column(name = "CODEVAL", nullable = false, length = MAX_CODE_LENGTH)
	@FullTextField(name = "myCode", searchable = Searchable.YES, projectable = Projectable.YES, analyzer = "exactAnalyzer")
	private String myCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONCEPT_UPDATED", nullable = true)
	private Date myUpdated;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODESYSTEM_PID", referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_CONCEPT_PID_CS_PID"))
	private TermCodeSystemVersion myCodeSystem;

	@Column(name = "CODESYSTEM_PID", insertable = false, updatable = false)
	@GenericField(name = "myCodeSystemVersionPid")
	private long myCodeSystemVersionPid;

	@Column(name = "DISPLAY", nullable = true, length = MAX_DESC_LENGTH)
	@FullTextField(name = "myDisplay", searchable = Searchable.YES, projectable = Projectable.YES, analyzer = "standardAnalyzer")
	@FullTextField(name = "myDisplayEdgeNGram", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompleteEdgeAnalyzer")
	@FullTextField(name = "myDisplayWordEdgeNGram", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompleteWordEdgeAnalyzer")
	@FullTextField(name = "myDisplayNGram", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompleteNGramAnalyzer")
	@FullTextField(name = "myDisplayPhonetic", searchable= Searchable.YES, projectable= Projectable.NO, analyzer =  "autocompletePhoneticAnalyzer")
	private String myDisplay;

	/**
	 * IndexedEmbedded uses ObjectStructure.NESTED to be able to use hibernate search nested predicate queries.
	 * @see "https://docs.jboss.org/hibernate/search/6.0/reference/en-US/html_single/#search-dsl-predicate-nested"
	 */
	@OneToMany(mappedBy = "myConcept", orphanRemoval = false, fetch = FetchType.LAZY)
	@IndexedEmbedded(structure = ObjectStructure.NESTED)
	private Collection<TermConceptProperty> myProperties;

	@OneToMany(mappedBy = "myConcept", orphanRemoval = false, fetch = FetchType.LAZY)
	private Collection<TermConceptDesignation> myDesignations;

	@Id
	@SequenceGenerator(name = "SEQ_CONCEPT_PID", sequenceName = "SEQ_CONCEPT_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_PID")
	@Column(name = "PID")
	@GenericField
	private Long myId;

	@Column(name = "INDEX_STATUS", nullable = true)
	private Long myIndexStatus;

	@Lob
	@Column(name = "PARENT_PIDS", nullable = true)
	@FullTextField(name = "myParentPids", searchable = Searchable.YES, projectable = Projectable.YES, analyzer = "conceptParentPidsAnalyzer")
	private String myParentPids;

	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "myChild")
	private List<TermConceptParentChildLink> myParents;

	@Column(name = "CODE_SEQUENCE", nullable = true)
	private Integer mySequence;

	public TermConcept() {
		super();
	}

	public TermConcept(TermCodeSystemVersion theCs, String theCode) {
		setCodeSystemVersion(theCs);
		setCode(theCode);
	}

	public TermConcept addChild(RelationshipTypeEnum theRelationshipType) {
		TermConcept child = new TermConcept();
		child.setCodeSystemVersion(myCodeSystem);
		addChild(child, theRelationshipType);
		return child;
	}

	public TermConceptParentChildLink addChild(TermConcept theChild, RelationshipTypeEnum theRelationshipType) {
		Validate.notNull(theRelationshipType, "theRelationshipType must not be null");
		TermConceptParentChildLink link = new TermConceptParentChildLink();
		link.setParent(this);
		link.setChild(theChild);
		link.setRelationshipType(theRelationshipType);
		getChildren().add(link);

		theChild.getParents().add(link);
		return link;
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
		if (!getProperties().contains(property)) {
			getProperties().add(property);
		}

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

	public List<TermConceptParentChildLink> getChildren() {
		if (myChildren == null) {
			myChildren = new ArrayList<>();
		}
		return myChildren;
	}

	public String getCode() {
		return myCode;
	}

	public TermConcept setCode(@Nonnull String theCode) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theCode, "theCode must not be null or empty");
		ValidateUtil.isNotTooLongOrThrowIllegalArgument(theCode, MAX_CODE_LENGTH,
			"Code exceeds maximum length (" + MAX_CODE_LENGTH + "): " + length(theCode));
		myCode = theCode;
		return this;
	}

	public TermCodeSystemVersion getCodeSystemVersion() {
		return myCodeSystem;
	}

	public TermConcept setCodeSystemVersion(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystem = theCodeSystemVersion;
		if (theCodeSystemVersion != null && theCodeSystemVersion.getPid() != null) {
			myCodeSystemVersionPid = theCodeSystemVersion.getPid();
		}
		return this;
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
		myDisplay = left(theDisplay, MAX_DESC_LENGTH);
		return this;
	}

	public Long getId() {
		return myId;
	}

	public TermConcept setId(Long theId) {
		myId = theId;
		return this;
	}

	public Long getIndexStatus() {
		return myIndexStatus;
	}

	public TermConcept setIndexStatus(Long theIndexStatus) {
		myIndexStatus = theIndexStatus;
		return this;
	}

	public String getParentPidsAsString() {
		return myParentPids;
	}

	public List<TermConceptParentChildLink> getParents() {
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

	public TermConcept setSequence(Integer theSequence) {
		mySequence = theSequence;
		return this;
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

	public TermConcept setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
		return this;
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

		setParentPids(b.toString());
	}

	public TermConcept setParentPids(String theParentPids) {
		myParentPids = theParentPids;
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("pid", myId);
		b.append("csvPid", myCodeSystemVersionPid);
		b.append("code", myCode);
		b.append("display", myDisplay);
		if (mySequence != null) {
			b.append("sequence", mySequence);
		}
		return b.build();
	}

	public List<IValidationSupport.BaseConceptProperty> toValidationProperties() {
		List<IValidationSupport.BaseConceptProperty> retVal = new ArrayList<>();
		for (TermConceptProperty next : getProperties()) {
			switch (next.getType()) {
				case STRING:
					retVal.add(new IValidationSupport.StringConceptProperty(next.getKey(), next.getValue()));
					break;
				case CODING:
					retVal.add(new IValidationSupport.CodingConceptProperty(next.getKey(), next.getCodeSystem(), next.getValue(), next.getDisplay()));
					break;
				default:
					throw new IllegalStateException(Msg.code(830) + "Don't know how to handle " + next.getType());
			}
		}
		return retVal;
	}

	/**
	 * Returns a view of {@link #getChildren()} but containing the actual child codes
	 */
	public List<TermConcept> getChildCodes() {
		return getChildren().stream().map(t -> t.getChild()).collect(Collectors.toList());
	}

}
