package ca.uhn.fhir.jpa.entity;

import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TRM_CONCEPT_MAP_GRP_ELM_TGT")
public class TermConceptMapGroupElementTarget implements Serializable {
	@Id()
	@SequenceGenerator(name = "SEQ_CNCPT_MAP_GRP_ELM_TGT_PID", sequenceName = "SEQ_CNCPT_MAP_GRP_ELM_TGT_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CNCPT_MAP_GRP_ELM_TGT_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(name = "CONCEPT_MAP_GRP_ELM_PID", nullable = false, referencedColumnName = "PID", foreignKey=@ForeignKey(name="FK_TCMGETARGET_ELEMENT"))
	private TermConceptMapGroupElement myConceptMapGroupElement;

	@Column(name = "TARGET_CODE", nullable = false, length = 50)
	private String myCode;

	@Column(name = "TARGET_DISPLAY", length = 100)
	private String myDisplay;

	@Enumerated(EnumType.STRING)
	@Column(name = "TARGET_EQUIVALENCE", length = 50)
	private ConceptMapEquivalence myEquivalence;

	// FIXME: Implement ConceptMap.group.element.target.comment

	private String myConceptMapUrl;
	private String mySystem;
	private String mySystemVersion;
	private String myValueSet;

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		myCode = theCode;
	}

	public TermConceptMapGroupElement getConceptMapGroupElement() {
		return myConceptMapGroupElement;
	}

	public void setConceptMapGroupElement(TermConceptMapGroupElement theTermConceptMapGroupElement) {
		myConceptMapGroupElement = theTermConceptMapGroupElement;
	}

	public String getConceptMapUrl() {
		if (myConceptMapUrl == null) {
			myConceptMapUrl = getConceptMapGroupElement().getConceptMapGroup().getConceptMap().getUrl();
		}
		return myConceptMapUrl;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

	public ConceptMapEquivalence getEquivalence() {
		return myEquivalence;
	}

	public void setEquivalence(ConceptMapEquivalence theEquivalence) {
		myEquivalence = theEquivalence;
	}

	public String getSystem() {
		if (mySystem == null) {
			mySystem = getConceptMapGroupElement().getConceptMapGroup().getTarget();
		}
		return mySystem;
	}

	public String getSystemVersion() {
		if (mySystemVersion == null) {
			mySystemVersion = getConceptMapGroupElement().getConceptMapGroup().getTargetVersion();
		}
		return mySystemVersion;
	}

	public String getValueSet() {
		if (myValueSet == null) {
			myValueSet = getConceptMapGroupElement().getConceptMapGroup().getTargetValueSet();
		}
		return myValueSet;
	}
}
