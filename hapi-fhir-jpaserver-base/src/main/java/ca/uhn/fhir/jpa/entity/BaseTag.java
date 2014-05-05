package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="TAG_LABEL", length=200)
	private String myLabel;

	@Column(name="TAG_SCHEME", length=200)
	private String myScheme;

	@Column(name="TAG_TERM", length=200)
	private String myTerm;

	public String getLabel() {
		return myLabel;
	}

	public String getScheme() {
		return myScheme;
	}

	public String getTerm() {
		return myTerm;
	}

	public void setLabel(String theLabel) {
		myLabel = theLabel;
	}

	public void setScheme(String theScheme) {
		myScheme = theScheme;
	}

	public void setTerm(String theTerm) {
		myTerm = theTerm;
	}

}
