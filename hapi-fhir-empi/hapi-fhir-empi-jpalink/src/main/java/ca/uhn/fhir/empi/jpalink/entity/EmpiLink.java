package ca.uhn.fhir.empi.jpalink.entity;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.rules.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.*;

@Entity
@Table(name = "HFJ_EMPI_LINK", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_EMPI_PERSONID_RESID", columnNames = {"PERSON_PID", "RESOURCE_PID"}),
	// FIXME KHS what happens when we find new patients we think should be in the same person--i.e. we want to request a merge, or if we match people in more than one eid
	// Fundamentally, matching might not be transitive.  If unsure send to manual sorting
})
public class EmpiLink {
	private static final int MATCH_RESULT_LENGTH = 16;
	private static final int LINK_SOURCE_LENGTH = 16;

	@SequenceGenerator(name = "SEQ_EMPI_LINK_ID", sequenceName = "SEQ_EMPI_LINK_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EMPI_LINK_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@JoinColumn(name = "PERSON_PID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_EMPI_LINK_PERSON"))
	@OneToOne(fetch = FetchType.LAZY)
	private ResourceTable myPerson;

	@Column(name = "PERSON_PID", nullable = false, updatable = false, insertable = false)
	private Long myPersonPid;

	@JoinColumn(name = "RESOURCE_PID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_EMPI_LINK_RESOURCE"))
	@OneToOne(fetch = FetchType.LAZY)
	private ResourceTable myResource;

	@Column(name = "RESOURCE_PID", nullable = false, updatable = false, insertable = false)
	private Long myResourcePid;

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

	public ResourceTable getResource() {
		return myResource;
	}

	public EmpiLink setResource(ResourceTable theResource) {
		myResource = theResource;
		myResourcePid = theResource.getId();
		return this;
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public EmpiLink setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
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
