package ca.uhn.fhir.jpa.entity;

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
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="TRM_CONCEPT", uniqueConstraints= {
	@UniqueConstraint(name="IDX_CONCEPT_CS_CODE", columnNames= {"CODESYSTEM_PID", "CODE"})
})
public class TermConcept implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="myParent")
	private Collection<TermConceptParentChildLink> myChildren;

	@Column(name="CODE", length=100, nullable=false)
	private String myCode;
	
	@ManyToOne()
	@JoinColumn(name="CODESYSTEM_PID", referencedColumnName="PID", foreignKey=@ForeignKey(name="FK_CONCEPT_PID_CS_PID"))
	private TermCodeSystemVersion myCodeSystem;
	
	@Column(name="DISPLAY", length=200, nullable=true)
	private String myDisplay;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="myChild")
	private Collection<TermConceptParentChildLink> myParents;

	@Id()
	@SequenceGenerator(name="SEQ_CONCEPT_PID", sequenceName="SEQ_CONCEPT_PID")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="SEQ_CONCEPT_PID")
	@Column(name="PID")
	private Long myPid;

	public TermConcept() {
		super();
	}

	public TermConcept(TermCodeSystemVersion theCs, String theCode) {
		setCodeSystem(theCs);
		setCode(theCode);
	}

	public TermConcept addChild(TermConcept theChild) {
		Validate.notNull(theChild.getCodeSystem(), "theChild.getCodeSystem() must not return null");
		TermConceptParentChildLink link = new TermConceptParentChildLink();
		link.setParent(this);
		link.setCodeSystem(theChild.getCodeSystem());
		link.setChild(theChild);
		getChildren().add(link);
		return this;
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
		if (obj.myPid == null) {
			return false;
		}
		
		EqualsBuilder b = new EqualsBuilder();
		b.append(myPid, obj.myPid);
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

	public Collection<TermConceptParentChildLink> getParents() {
		if (myParents == null) {
			myParents = new ArrayList<TermConceptParentChildLink>();
		}
		return myParents;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(myPid);
		return b.toHashCode();
	}
	
	public void setCode(String theCode) {
		myCode = theCode;
	}

	public void setCodeSystem(TermCodeSystemVersion theCodeSystem) {
		myCodeSystem = theCodeSystem;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

}
