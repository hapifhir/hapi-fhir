package ca.uhn.fhir.jpa.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;

@MappedSuperclass
public abstract class BaseHasResource {

	public static final int MAX_TITLE_LENGTH = 100;

	@Column(name = "RES_DELETED_AT", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myDeleted;

	@Column(name = "RES_ENCODING", nullable = false, length = 5)
	@Enumerated(EnumType.STRING)
	private ResourceEncodingEnum myEncoding;
	
	@OneToOne(optional = true, fetch = FetchType.EAGER, cascade = {}, orphanRemoval = false)
	@JoinColumn(name = "FORCED_ID_PID")
	private ForcedId myForcedId;

	@Column(name = "HAS_TAGS", nullable = false)
	private boolean myHasTags;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_PUBLISHED", nullable = false)
	private Date myPublished;

	@Column(name = "RES_TEXT", length = Integer.MAX_VALUE - 1, nullable = false)
	@Lob()
	private byte[] myResource;

	@Column(name = "RES_TITLE", nullable = true, length = MAX_TITLE_LENGTH)
	private String myTitle;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_UPDATED", nullable = false)
	private Date myUpdated;

	public abstract BaseTag addTag(TagDefinition theDef);

	public Date getDeleted() {
		return myDeleted;
	}

	public ResourceEncodingEnum getEncoding() {
		return myEncoding;
	}

	public ForcedId getForcedId() {
		return myForcedId;
	}

	public abstract IdDt getIdDt();

	public InstantDt getPublished() {
		return new InstantDt(myPublished);
	}

	public byte[] getResource() {
		return myResource;
	}

	public abstract String getResourceType();

	public abstract Collection<? extends BaseTag> getTags();

	public String getTitle() {
		return myTitle;
	}

	public InstantDt getUpdated() {
		return new InstantDt(myUpdated);
	}

	public abstract long getVersion();

	public boolean isHasTags() {
		return myHasTags;
	}

	public void setDeleted(Date theDate) {
		myDeleted = theDate;
	}

	public void setEncoding(ResourceEncodingEnum theEncoding) {
		myEncoding = theEncoding;
	}

	public void setForcedId(ForcedId theForcedId) {
		myForcedId = theForcedId;
	}

	public void setHasTags(boolean theHasTags) {
		myHasTags = theHasTags;
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	public void setPublished(InstantDt thePublished) {
		myPublished = thePublished.getValue();
	}

	public void setResource(byte[] theResource) {
		myResource = theResource;
	}

	public void setTitle(String theTitle) {
		myTitle = theTitle;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	public void setUpdated(InstantDt theUpdated) {
		myUpdated = theUpdated.getValue();
	}

}
