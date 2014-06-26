package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ca.uhn.fhir.model.api.Tag;

@Entity
@Table(name = "HFJ_TAG_DEF", uniqueConstraints= {@UniqueConstraint(columnNames= {"TAG_SCHEME","TAG_TERM"})})
//@org.hibernate.annotations.Table(appliesTo="HFJ_TAG", indexes= {@Index(name)})
public class TagDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToMany(cascade= {}, fetch= FetchType.LAZY, mappedBy= "myTag")
	private Collection<ResourceTag> myResources;

	@OneToMany(cascade= {}, fetch= FetchType.LAZY, mappedBy= "myTag")
	private Collection<ResourceHistoryTag> myResourceVersions;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TAG_ID")
	private Long myId;

	@Column(name = "TAG_LABEL", length = 200)
	private String myLabel;

	@Column(name = "TAG_SCHEME", length = 200)
	private String myScheme;

	@Column(name = "TAG_TERM", length = 200)
	private String myTerm;

	public TagDefinition() {
	}

	public TagDefinition(String theTerm, String theLabel, String theScheme) {
		setTerm(theTerm);
		setScheme(theScheme);
		setLabel(theLabel);
	}

	public void setLabel(String theLabel) {
		myLabel = theLabel;
	}

	public void setScheme(String theScheme) {
		myScheme = theScheme;
	}

	public String getLabel() {
		return myLabel;
	}

	public String getScheme() {
		return myScheme;
	}

	public String getTerm() {
		return myTerm;
	}

	public void setTerm(String theTerm) {
		myTerm = theTerm;
	}

	public Tag toTag() {
		return new Tag( getScheme(), getTerm(),getLabel());
	}

}
