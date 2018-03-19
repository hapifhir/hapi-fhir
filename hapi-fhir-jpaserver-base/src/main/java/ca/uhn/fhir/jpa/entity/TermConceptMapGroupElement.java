package ca.uhn.fhir.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRM_CONCEPT_MAP_GRP_ELEMENT")
public class TermConceptMapGroupElement implements Serializable {
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_GRP_ELM_PID", sequenceName = "SEQ_CONCEPT_MAP_GRP_ELM_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_GRP_ELM_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(name = "CONCEPT_MAP_GROUP_PID", nullable = false, referencedColumnName = "PID")
	private TermConceptMapGroup myConceptMapGroup;

	@Column(name = "SOURCE_CODE", nullable = false, length = 50)
	private String mySourceCode;

	@Column(name = "SOURCE_DISPLAY", length = 100)
	private String mySourceDisplay;

	@OneToMany()
	private List<TermConceptMapGroupElementTarget> myConceptMapGroupElementTargets;

	public List<TermConceptMapGroupElementTarget> getConceptMapGroupElementTargets() {
		if (myConceptMapGroupElementTargets == null) {
			myConceptMapGroupElementTargets = new ArrayList<>();
		}

		return myConceptMapGroupElementTargets;
	}

	public TermConceptMapGroup getConceptMapGroup() {
		return myConceptMapGroup;
	}

	public void setConceptMapGroup(TermConceptMapGroup conceptMapGroup) {
		myConceptMapGroup = conceptMapGroup;
	}

	public String getSourceCode() {
		return mySourceCode;
	}

	public void setSourceCode(String sourceCode) {
		mySourceCode = sourceCode;
	}

	public String getSourceDisplay() {
		return mySourceDisplay;
	}

	public void setSourceDisplay(String sourceDisplay) {
		mySourceDisplay = sourceDisplay;
	}
}
