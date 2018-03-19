package ca.uhn.fhir.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TRM_CONCEPT_MAP_GRP_ELM_TGT")
public class TermConceptMapGroupElementTarget implements Serializable {
	@Id()
	@SequenceGenerator(name = "SEQ_CONCEPT_MAP_GRP_ELM_TGT_PID", sequenceName = "SEQ_CONCEPT_MAP_GRP_ELM_TGT_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CONCEPT_MAP_GRP_ELM_TGT_PID")
	@Column(name = "PID")
	private Long myId;

	@ManyToOne()
	@JoinColumn(name = "CONCEPT_MAP_GRP_ELM_PID", nullable = false, referencedColumnName = "PID")
	private TermConceptMapGroupElement myConceptMapGroupElement;

	@Column(name = "TARGET_CODE", nullable = false, length = 50)
	private String myTargetCode;

	@Column(name = "TARGET_DISPLAY", length = 100)
	private String myTargetDisplay;

	public TermConceptMapGroupElement getConceptMapGroupElement() {
		return myConceptMapGroupElement;
	}

	public void setConceptMapGroupElement(TermConceptMapGroupElement conceptMapGroupElement) {
		myConceptMapGroupElement = conceptMapGroupElement;
	}

	public String getTargetCode() {
		return myTargetCode;
	}

	public void setTargetCode(String targetCode) {
		myTargetCode = targetCode;
	}

	public String getTargetDisplay() {
		return myTargetDisplay;
	}

	public void setTargetDisplay(String targetDisplay) {
		myTargetDisplay = targetDisplay;
	}
}
