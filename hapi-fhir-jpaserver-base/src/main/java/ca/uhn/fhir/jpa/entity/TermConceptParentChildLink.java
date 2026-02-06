/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
		name = "TRM_CONCEPT_PC_LINK",
		indexes = {
			// must have same name that indexed FK or SchemaMigrationTest complains because H2 sets this index
			// automatically
			@Index(name = "FK_TERM_CONCEPTPC_CHILD", columnList = "CHILD_PID", unique = false),
			@Index(name = "FK_TERM_CONCEPTPC_PARENT", columnList = "PARENT_PID", unique = false),
			@Index(name = "FK_TERM_CONCEPTPC_CS", columnList = "CODESYSTEM_PID")
		})
public class TermConceptParentChildLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "CHILD_PID",
						insertable = false,
						updatable = false,
						nullable = false,
						referencedColumnName = "PID"),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = false)
			},
			foreignKey = @ForeignKey(name = "FK_TERM_CONCEPTPC_CHILD"))
	private TermConcept myChild;

	@Column(name = "CHILD_PID", insertable = true, updatable = true, nullable = false)
	private Long myChildPid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "CODESYSTEM_PID",
						referencedColumnName = "PID",
						insertable = false,
						updatable = false,
						nullable = false),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = false)
			},
			foreignKey = @ForeignKey(name = "FK_TERM_CONCEPTPC_CS"))
	private TermCodeSystemVersion myCodeSystem;

	@Column(name = "CODESYSTEM_PID", insertable = true, updatable = true, nullable = false)
	@FullTextField(name = "myCodeSystemVersionPid")
	private Long myCodeSystemVersionPid;

	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = {})
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "PARENT_PID",
						insertable = false,
						updatable = false,
						nullable = false,
						referencedColumnName = "PID"),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = false)
			},
			foreignKey = @ForeignKey(name = "FK_TERM_CONCEPTPC_PARENT"))
	private TermConcept myParent;

	@Column(name = "PARENT_PID", insertable = true, updatable = true, nullable = false)
	private Long myParentPid;

	@EmbeddedId
	private TermConceptParentChildLinkPk myId;

	@Column(name = PartitionablePartitionId.PARTITION_ID, nullable = true, insertable = false, updatable = false)
	private Integer myPartitionIdValue;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "REL_TYPE", length = 5, nullable = true)
	@JdbcTypeCode(SqlTypes.INTEGER)
	private RelationshipTypeEnum myRelationshipType;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TermConceptParentChildLink other = (TermConceptParentChildLink) obj;
		if (myChild == null) {
			if (other.myChild != null) return false;
		} else if (!myChild.equals(other.myChild)) return false;
		if (myCodeSystem == null) {
			if (other.myCodeSystem != null) return false;
		} else if (!myCodeSystem.equals(other.myCodeSystem)) return false;
		if (myParent == null) {
			if (other.myParent != null) return false;
		} else if (!myParent.equals(other.myParent)) return false;
		if (myRelationshipType != other.myRelationshipType) return false;
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

	public TermConceptParentChildLinkPk getPid() {
		if (myId == null) {
			myId = new TermConceptParentChildLinkPk();
		}
		return myId;
	}

	public Long getId() {
		return getPid().myId;
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

	@PrePersist
	public void prePersist() {
		if (myChildPid == null) {
			myChildPid = myChild.getId();
			assert myChildPid != null;
		}
		if (myParentPid == null) {
			myParentPid = myParent.getId();
			assert myParentPid != null;
		}
		if (myCodeSystemVersionPid == null) {
			myCodeSystemVersionPid = myCodeSystem.getPid();
			assert myCodeSystemVersionPid != null;
		}

		// TODO GGG/JA. Eventually, this class should extend base partitionable.
		if (myPartitionIdValue == null && myParent != null) {
			myPartitionIdValue = myParent.getPartitionId().getPartitionId();
			getPid().myPartitionIdValue = myPartitionIdValue;
		}
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
		myChildPid = theChild.getId();
		return this;
	}

	public TermConceptParentChildLink setCodeSystem(TermCodeSystemVersion theCodeSystemVersion) {
		myCodeSystem = theCodeSystemVersion;
		myCodeSystemVersionPid = theCodeSystemVersion.getPid();
		return this;
	}

	public TermConceptParentChildLink setParent(TermConcept theParent) {
		myParent = theParent;
		myParentPid = theParent.getId();
		myPartitionIdValue = theParent.getPartitionId().getPartitionId();
		getPid().myPartitionIdValue = myPartitionIdValue;
		return this;
	}

	public TermConceptParentChildLink setRelationshipType(RelationshipTypeEnum theRelationshipType) {
		myRelationshipType = theRelationshipType;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("pid", myId)
				.append("csvPid", myCodeSystemVersionPid)
				.append("parentPid", myParentPid)
				.append("childPid", myChildPid)
				.append("rel", myRelationshipType)
				.toString();
	}

	public enum RelationshipTypeEnum {
		// ********************************************
		// IF YOU ADD HERE MAKE SURE ORDER IS PRESERVED
		ISA
	}

	@Embeddable
	public static class TermConceptParentChildLinkPk {

		@SequenceGenerator(name = "SEQ_CONCEPT_PC_PID", sequenceName = "SEQ_CONCEPT_PC_PID")
		@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_PC_PID")
		@Column(name = "PID")
		private Long myId;

		@PartitionedIdProperty
		@Column(name = PartitionablePartitionId.PARTITION_ID, nullable = false)
		private Integer myPartitionIdValue;

		@Override
		public int hashCode() {
			return Objects.hash(myId, myPartitionIdValue);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}
			if (!(theO instanceof TermConceptParentChildLinkPk)) {
				return false;
			}
			TermConceptParentChildLinkPk that = (TermConceptParentChildLinkPk) theO;
			return Objects.equals(myId, that.myId) && Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
		}

		@Override
		public String toString() {
			return myPartitionIdValue + "/" + myId;
		}
	}
}
