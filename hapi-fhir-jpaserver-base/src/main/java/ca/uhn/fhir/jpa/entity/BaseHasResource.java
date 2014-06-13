package ca.uhn.fhir.jpa.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;

@MappedSuperclass
public abstract class BaseHasResource {

	@Column(name = "RES_ENCODING", nullable = false, length=5)
	@Enumerated(EnumType.STRING)
	private ResourceEncodingEnum myEncoding;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_PUBLISHED", nullable = false)
	private Date myPublished;

	@Column(name = "RES_TEXT", length = Integer.MAX_VALUE - 1, nullable = false)
	@Lob()
	private byte[] myResource;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RES_UPDATED", nullable = false)
	private Date myUpdated;

	public ResourceEncodingEnum getEncoding() {
		return myEncoding;
	}

	public abstract String getResourceType();
	
	public abstract Collection<? extends BaseTag> getTags();

	public abstract IdDt getIdDt();

	public InstantDt getPublished() {
		return new InstantDt(myPublished);
	}

	public byte[] getResource() {
		return myResource;
	}

	public InstantDt getUpdated() {
		return new InstantDt(myUpdated);
	}

	public abstract long getVersion();

	public void setEncoding(ResourceEncodingEnum theEncoding) {
		myEncoding = theEncoding;
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

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	public void setUpdated(InstantDt theUpdated) {
		myUpdated = theUpdated.getValue();
	}

	public abstract  BaseTag addTag(TagDefinition theDef);

}
