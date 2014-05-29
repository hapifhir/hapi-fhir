package ca.uhn.fhir.jpa.entity;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade= {})
	@JoinColumn(name="TAG_ID", nullable=false)
	private TagDefinition myTag;

	public TagDefinition getTag() {
		return myTag;
	}

	public void setTag(TagDefinition theTag) {
		myTag = theTag;
	}
	
	
}
