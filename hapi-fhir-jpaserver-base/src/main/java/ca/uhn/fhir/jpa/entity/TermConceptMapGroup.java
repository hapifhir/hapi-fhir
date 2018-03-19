package ca.uhn.fhir.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRM_CONCEPT_MAP_GROUP")
public class TermConceptMapGroup implements Serializable {
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_GROUP_PID", sequenceName = "SEQ_CONCEPT_MAP_GROUP_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_GROUP_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(name = "CONCEPT_MAP_PID", nullable = false, referencedColumnName = "PID")
	private TermConceptMap myConceptMap;

	@Column(name = "SOURCE_URL", nullable = false, length = 200)
	private String mySourceUrl;

	@Column(name = "TARGET_URL", nullable = false, length = 200)
	private String myTargetUrl;

	@OneToMany()
	private List<TermConceptMapGroupElement> myConceptMapGroupElements;

	public List<TermConceptMapGroupElement> getConceptMapGroupElements() {
		if (myConceptMapGroupElements == null) {
			myConceptMapGroupElements = new ArrayList<>();
		}

		return myConceptMapGroupElements;
	}

	public TermConceptMap getConceptMap() {
		return myConceptMap;
	}

	public void setConceptMap(TermConceptMap conceptMap) {
		myConceptMap = conceptMap;
	}

	public String getSourceUrl() {
		return mySourceUrl;
	}

	public void setSourceUrl(String mySourceUrl) {
		this.mySourceUrl = mySourceUrl;
	}

	public String getTargetUrl() {
		return myTargetUrl;
	}

	public void setTargetUrl(String myTargetUrl) {
		this.myTargetUrl = myTargetUrl;
	}
}
