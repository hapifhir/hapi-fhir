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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TRM_CONCEPT_PC_LINK", indexes = {
	// must have same name that indexed FK or SchemaMigrationTest complains because H2 sets this index automatically
	@Index(name = "FK_TERM_CONCEPTPC_CHILD",  columnList = "CHILD_PID", unique = false),
	@Index(name = "FK_TERM_CONCEPTPC_PARENT",  columnList = "PARENT_PID", unique = false)
})
public class TermConceptParentChildLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHILD_PID", nullable = false, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_TERM_CONCEPTPC_CHILD"))
	private TermConcept myChild;

	@Column(name = "CHILD_PID", insertable = false, updatable = false)
	private Long myChildPid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODESYSTEM_PID", nullable = false, foreignKey = @ForeignKey(name = "FK_TERM_CONCEPTPC_CS"))
	private TermCodeSystemVersion myCodeSystem;

	@Column(name = "CODESYSTEM_PID", insertable = false, updatable = false, nullable = false)
	@FullTextField(name = "myCodeSystemVersionPid")
	private long myCodeSystemVersionPid;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "PARENT_PID", nullable = false, referencedColumnName = "PID", foreignKey = @ForeignKey(name = "FK_TERM_CONCEPTPC_PARENT"))
	private TermConcept myParent;

	@Column(name = "PARENT_PID", insertable = false, updatable = false)
	private Long myParentPid;

	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_PC_PID", sequenceName = "SEQ_CONCEPT_PC_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_PC_PID")
	@Column(name = "PID")
	private Long myPid;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "REL_TYPE", length = 5, nullable = true)
	private RelationshipTypeEnum myRelationshipType;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermConceptParentChildLink other = (TermConceptParentChildLink) obj;
		if (myChild == null) {
			if (other.myChild != null)
				return false;
		} else if (!myChild.equals(other.myChild))
			return false;
		if (myCodeSystem == null) {
			if (other.myCodeSystem != null)
				return false;
		} else if (!myCodeSystem.equals(other.myCodeSystem))
			return false;
		if (myParent == null) {
			if (other.myParent != null)
				return false;
		} else if (!myParent.equals(other.myParent))
			return false;
		if (myRelationshipType != other.myRelationshipType)
			return false;
		return true;
	}

	public TermConcept getChild() {
		return myChild;
	}

	public Long getChildPid() {
		return myChildPid;
	}

	public TermCodeSystemVersion getCodeSystem() {
		return myCodeSystem;
	}

	public Long getId() {
		return myPid;
	}

	public TermConcept getParent() {
		return myParent;
	}

	public Long getParentPid() {
		return myParentPid;
	}

	public RelationshipTypeEnum getRelationshipType() {
		return myRelationshipType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myChild == null) ? 0 : myChild.hashCode());
		result = prime * result + ((myCodeSystem == null) ? 0 : myCodeSystem.hashCode());
		result = prime * result + ((myParent == null) ? 0 : myParent.hashCode());
		result = prime * result + ((myRelationshipType == null) ? 0 : myRelationshipType.hashCode());
		return result;
	}

	public TermConceptParentChildLink setChild(TermConcept theChild) {
		myChild = theChild;
		return this;
	}

	public TermConceptParentChildLink setCodeSystem(TermCodeSystemVersion theCodeSystem) {
		myCodeSystem = theCodeSystem;
		return this;
	}

	public TermConceptParentChildLink setParent(TermConcept theParent) {
		myParent = theParent;
		return this;
	}

	public TermConceptParentChildLink setRelationshipType(RelationshipTypeEnum theRelationshipType) {
		myRelationshipType = theRelationshipType;
		return this;
	}

	public enum RelationshipTypeEnum {
		// ********************************************
		// IF YOU ADD HERE MAKE SURE ORDER IS PRESERVED
		ISA
	}
}
