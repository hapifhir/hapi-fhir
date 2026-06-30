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
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
		name = "TRM_VALUESET_CONCEPT_PC_LINK",
		indexes = {
			@Index(name = "FK_VS_CONCEPTPC_CHILD", columnList = "CHILD_PID,PARTITION_ID"),
			@Index(name = "FK_VS_CONCEPTPC_PARENT", columnList = "PARENT_PID,PARTITION_ID"),
			@Index(name = "FK_VS_CONCEPTPC_VS", columnList = "VALUESET_PID,PARTITION_ID")
		})
public class TermValueSetConceptParentChildLink implements Serializable {
	@Serial
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
			foreignKey = @ForeignKey(name = "FK_VS_CONCEPTPC_CHILD"))
	private TermValueSetConcept myChild;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "VALUESET_PID",
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
			foreignKey = @ForeignKey(name = "FK_VS_CONCEPTPC_CS"))
	private TermValueSet myValueSet;

	@Column(name = "VALUESET_PID", insertable = true, updatable = true, nullable = false)
	private Long myValueSetPid;

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
			foreignKey = @ForeignKey(name = "FK_VS_CONCEPTPC_PARENT"))
	private TermValueSetConcept myParent;

	@EmbeddedId
	private TermValueSetConceptParentChildLinkPk myId;

	@Column(name = PartitionablePartitionId.PARTITION_ID, nullable = true, insertable = false, updatable = false)
	private Integer myPartitionIdValue;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TermValueSetConceptParentChildLink other = (TermValueSetConceptParentChildLink) obj;
		if (myChild == null) {
			if (other.myChild != null) return false;
		} else if (!myChild.equals(other.myChild)) return false;
		if (myValueSet == null) {
			if (other.myValueSet != null) return false;
		} else if (!myValueSet.equals(other.myValueSet)) return false;
		if (myParent == null) {
			return other.myParent == null;
		} else return myParent.equals(other.myParent);
	}

	public TermValueSetConcept getChild() {
		return myChild;
	}

	public TermValueSetConcept getParent() {
		return myParent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myChild == null) ? 0 : myChild.hashCode());
		result = prime * result + ((myValueSet == null) ? 0 : myValueSet.hashCode());
		result = prime * result + ((myParent == null) ? 0 : myParent.hashCode());
		return result;
	}

	public void setValueSet(TermValueSet theValueSet) {
		myValueSet = theValueSet;
		myValueSetPid = theValueSet.getId();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("vsPid", myValueSetPid)
				.append("childPid", myId.myChildPid)
				.append("parentPid", myId.myParentPid)
				.toString();
	}

	public void setId(TermValueSetConceptParentChildLinkPk thePk) {
		myId = thePk;
		myPartitionIdValue = thePk.myPartitionIdValue;
	}

	public TermValueSetConceptParentChildLinkPk getId() {
		return myId;
	}

	public PartitionablePartitionId getPartitionId() {
		Integer partitionId = myId.getPartitionId();
		if (partitionId == null) {
			partitionId = myChild.getPartitionId().getPartitionId();
		}
		return PartitionablePartitionId.with(partitionId, null);
	}

	@Embeddable
	public static class TermValueSetConceptParentChildLinkPk {

		@Column(name = "CHILD_PID")
		private Long myChildPid;

		@Column(name = "PARENT_PID")
		private Long myParentPid;

		@PartitionedIdProperty
		@Column(name = PartitionablePartitionId.PARTITION_ID, nullable = false)
		private Integer myPartitionIdValue;

		@Override
		public int hashCode() {
			return Objects.hash(myChildPid, myParentPid, myPartitionIdValue);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}
			if (!(theO instanceof TermValueSetConceptParentChildLinkPk that)) {
				return false;
			}
			return Objects.equals(myChildPid, that.myChildPid)
					&& Objects.equals(myParentPid, that.myParentPid)
					&& Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
		}

		@Override
		public String toString() {
			return myPartitionIdValue + "/" + myChildPid + "/" + myParentPid;
		}

		public void setPartitionId(Integer thePartitionIdValue) {
			myPartitionIdValue = thePartitionIdValue;
		}

		public void setParentPid(Long theParentPid) {
			Validate.notNull(theParentPid, "theParentPid must not be null");
			myParentPid = theParentPid;
		}

		public void setChildPid(Long theChildPid) {
			Validate.notNull(theChildPid, "theChildPid must not be null");
			myChildPid = theChildPid;
		}

		public Integer getPartitionId() {
			return myPartitionIdValue;
		}
	}
}
