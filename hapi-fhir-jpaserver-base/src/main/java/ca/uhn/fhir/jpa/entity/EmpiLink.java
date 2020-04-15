package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * hapi-fhir-empi-jpalink
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

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.*;

@Entity
@Table(name = "HFJ_EMPI_LINK", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_EMPI_PERSON_TGT", columnNames = {"PERSON_PID", "TARGET_PID"}),
})
public class EmpiLink {
	private static final int MATCH_RESULT_LENGTH = 16;
	private static final int LINK_SOURCE_LENGTH = 16;

	@SequenceGenerator(name = "SEQ_EMPI_LINK_ID", sequenceName = "SEQ_EMPI_LINK_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EMPI_LINK_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "PERSON_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_EMPI_LINK_PERSON"), insertable=false, updatable=false, nullable=false)
	private ResourceTable myPerson;

	@Column(name = "PERSON_PID", updatable=false, nullable=false)
	private Long myPersonPid;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "TARGET_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_EMPI_LINK_TARGET"), insertable=false, updatable=false, nullable=false)
	private ResourceTable myTarget;

	@Column(name = "TARGET_PID", updatable=false, nullable=false)
	private Long myTargetPid;

	@Column(name = "MATCH_RESULT", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@OptimisticLock(excluded = true)
	private EmpiMatchResultEnum myMatchResult;

	@Column(name = "LINK_SOURCE", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@OptimisticLock(excluded = true)
	private EmpiLinkSourceEnum myLinkSource;

	public Long getId() {
		return myId;
	}

	public EmpiLink setId(Long theId) {
		myId = theId;
		return this;
	}

	public ResourceTable getPerson() {
		return myPerson;
	}

	public EmpiLink setPerson(ResourceTable thePerson) {
		myPerson = thePerson;
		myPersonPid = thePerson.getId();
		return this;
	}

	public Long getPersonPid() {
		return myPersonPid;
	}

	public EmpiLink setPersonPid(Long thePersonPid) {
		myPersonPid = thePersonPid;
		return this;
	}

	public ResourceTable getTarget() {
		return myTarget;
	}

	public EmpiLink setTarget(ResourceTable theTarget) {
		myTarget = theTarget;
		myTargetPid = theTarget.getId();
		return this;
	}

	public Long getTargetPid() {
		return myTargetPid;
	}

	public EmpiLink setTargetPid(Long theTargetPid) {
		myTargetPid = theTargetPid;
		return this;
	}

	public EmpiMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public EmpiLink setMatchResult(EmpiMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
		return this;
	}

	public EmpiLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public EmpiLink setLinkSource(EmpiLinkSourceEnum theLinkSource) {
		myLinkSource = theLinkSource;
		return this;
	}
}
