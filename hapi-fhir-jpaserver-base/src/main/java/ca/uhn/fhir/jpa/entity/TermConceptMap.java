package ca.uhn.fhir.jpa.entity;

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
}
