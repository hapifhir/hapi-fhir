package ca.uhn.fhir.jpa.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRM_CONCEPT_MAP")
public class TermConceptMap implements Serializable {
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_PID", sequenceName = "SEQ_CONCEPT_MAP_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "SOURCE_URL", nullable = false, length = 200)
	private String mySource;

	@Column(name = "TARGET_URL", nullable = false, length = 200)
	private String myTarget;

	@Column(name = "URL", length = 200)
	private String myUrl;

	@OneToMany(mappedBy = "myConceptMap")
	private List<TermConceptMapGroup> myConceptMapGroups;

	public List<TermConceptMapGroup> getConceptMapGroups() {
		if (myConceptMapGroups == null) {
			myConceptMapGroups = new ArrayList<>();
		}

		return myConceptMapGroups;
	}

	public Long getId() {
		return myId;
	}

	public String getSource() {
		return mySource;
	}

	public void setSource(String source) {
		mySource = source;
	}

	public String getTarget() {
		return myTarget;
	}

	public void setTarget(String target) {
		myTarget = target;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("myId", myId)
			.append("mySource", mySource)
			.append("myTarget", myTarget)
			.append("myUrl", myUrl)
			.append("myConceptMapGroups - size", myConceptMapGroups.size())
			.toString();
	}
}
