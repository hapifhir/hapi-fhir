package ca.uhn.fhir.jpa.entity;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;

//@formatter:off
@Entity
@Indexed()	
@Table(name="TRM_CONCEPT", uniqueConstraints= {
	@UniqueConstraint(name="IDX_CONCEPT_CS_CODE", columnNames= {"CODESYSTEM_PID", "CODE"})
})
//@formatter:on
public class TermConcept implements Serializable {
	private static final int MAX_DESC_LENGTH = 400;

	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="myParent")
	private Collection<TermConceptParentChildLink> myChildren;

	@Column(name="CODE", length=100, nullable=false)
	@Fields({
		@Field(name = "myCode", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "exactAnalyzer")),
	})
	private String myCode;
	
	@ManyToOne()
	@JoinColumn(name="CODESYSTEM_PID", referencedColumnName="PID", foreignKey=@ForeignKey(name="FK_CONCEPT_PID_CS_PID"))
	private TermCodeSystemVersion myCodeSystem;
	
	//@formatter:off
	@Column(name="DISPLAY", length=MAX_DESC_LENGTH, nullable=true)
	@Fields({
		@Field(name = "myDisplay", index = org.hibernate.search.annotations.Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
		@Field(name = "myDisplayEdgeNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
		@Field(name = "myDisplayNGram", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer")),
		@Field(name = "myDisplayPhonetic", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompletePhoneticAnalyzer"))
	})
	private String myDisplay;
	//@formatter:on

	@Id()
	@SequenceGenerator(name="SEQ_CONCEPT_PID", sequenceName="SEQ_CONCEPT_PID")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_CONCEPT_PID")
	@Column(name="PID")
	private Long myId;

	@Transient
	@Fields({
		@Field(name = "myParentPids", index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
	})
	private String myParentPids;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="myChild")
	private Collection<TermConceptParentChildLink> myParents;

	@Column(name="CODESYSTEM_PID", insertable=false, updatable=false)
	@Fields({
		@Field(name="myCodeSystemVersionPid")
	})
	private long myCodeSystemVersionPid;

	public TermConcept() {
		super();
	}

	public TermConcept(TermCodeSystemVersion theCs, String theCode) {
		setCodeSystem(theCs);
		setCode(theCode);
	}

	public TermConcept addChild(TermConcept theChild, RelationshipTypeEnum theRelationshipType) {
		Validate.notNull(theRelationshipType, "theRelationshipType must not be null");
		TermConceptParentChildLink link = new TermConceptParentChildLink();
		link.setParent(this);
		link.setChild(theChild);
		link.setRelationshipType(theRelationshipType);
		getChildren().add(link);
		return this;
	}

	public void addChildren(List<TermConcept> theChildren, RelationshipTypeEnum theRelationshipType) {
		for (TermConcept next : theChildren) {
			addChild(next, theRelationshipType);
		}
	}

	@Override
	public boolean equals(Object theObj) {
		if (!(theObj instanceof TermConcept)) {
			return false;
		}
		if (theObj == this) {
			return true;
		}
		
		TermConcept obj = (TermConcept)theObj;
		if (obj.myId == null) {
			return false;
		}
		
		EqualsBuilder b = new EqualsBuilder();
		b.append(myId, obj.myId);
		return b.isEquals();
	}

	public Collection<TermConceptParentChildLink> getChildren() {
		if (myChildren == null) {
			myChildren = new ArrayList<TermConceptParentChildLink>();
		}
		return myChildren;
	}

	public String getCode() {
		return myCode;
	}

	public TermCodeSystemVersion getCodeSystem() {
		return myCodeSystem;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public Long getId() {
		return myId;
	}

	public Collection<TermConceptParentChildLink> getParents() {
		if (myParents == null) {
			myParents = new ArrayList<TermConceptParentChildLink>();
		}
		return myParents;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(myId);
		return b.toHashCode();
	}
	
	public void setCode(String theCode) {
		myCode = theCode;
	}

	public void setCodeSystem(TermCodeSystemVersion theCodeSystem) {
		myCodeSystem = theCodeSystem;
		if (theCodeSystem.getPid() != null) {
			myCodeSystemVersionPid = theCodeSystem.getPid();
		}
	}

	public TermConcept setDisplay(String theDisplay) {
		myDisplay = theDisplay;
		if (isNotBlank(theDisplay) && theDisplay.length() > MAX_DESC_LENGTH) {
			myDisplay = myDisplay.substring(0, MAX_DESC_LENGTH);
		}
		return this;
	}

	public void setParentPids(Set<Long> theParentPids) {
		StringBuilder b = new StringBuilder();
		for (Long next : theParentPids) {
			if (b.length() > 0) {
				b.append(' ');
			}
			b.append(next);
		}
			
		myParentPids = b.toString();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("code", myCode).append("display", myDisplay).build();
	}

}
