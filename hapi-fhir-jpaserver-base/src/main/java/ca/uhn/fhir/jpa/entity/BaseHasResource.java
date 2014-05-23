package ca.uhn.fhir.jpa.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.server.EncodingEnum;

@MappedSuperclass
public abstract class BaseHasResource {

	@Column(name = "ENCODING")
	private EncodingEnum myEncoding;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISHED")
	private Date myPublished;

	@Column(name = "RESOURCE_TEXT", length = Integer.MAX_VALUE - 1)
	@Lob()
	private String myResource;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date myUpdated;

	public EncodingEnum getEncoding() {
		return myEncoding;
	}

	public abstract Collection<? extends BaseTag> getTags();

	public abstract IdDt getIdDt();

	public InstantDt getPublished() {
		return new InstantDt(myPublished);
	}

	public String getResource() {
		return myResource;
	}

	public InstantDt getUpdated() {
		return new InstantDt(myUpdated);
	}

	public abstract long getVersion();

	public void setEncoding(EncodingEnum theEncoding) {
		myEncoding = theEncoding;
	}

	public void setPublished(Date thePublished) {
		myPublished = thePublished;
	}

	public void setPublished(InstantDt thePublished) {
		myPublished = thePublished.getValue();
	}

	public void setResource(String theResource) {
		myResource = theResource;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

	public void setUpdated(InstantDt theUpdated) {
		myUpdated = theUpdated.getValue();
	}

}
