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
	@JoinColumn(name = "CONCEPT_MAP_GROUP_PID", nullable = false, referencedColumnName = "PID", foreignKey=@ForeignKey(name="FK_TCMGELEMENT_GROUP"))
	private TermConceptMapGroup myConceptMapGroup;

	@Column(name = "SOURCE_CODE", nullable = false, length = 50)
	private String myCode;

	@Column(name = "SOURCE_DISPLAY", length = 100)
	private String myDisplay;

	@OneToMany(mappedBy = "myConceptMapGroupElement")
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

	public void setConceptMapGroup(TermConceptMapGroup theTermConceptMapGroup) {
		myConceptMapGroup = theTermConceptMapGroup;
	}

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		myCode = theCode;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}
}
