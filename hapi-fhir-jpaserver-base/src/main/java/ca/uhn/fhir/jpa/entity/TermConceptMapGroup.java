package ca.uhn.fhir.jpa.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
	@JoinColumn(name = "CONCEPT_MAP_PID", nullable = false, referencedColumnName = "PID", foreignKey=@ForeignKey(name="FK_TCMGROUP_CONCEPTMAP"))
	private TermConceptMap myConceptMap;

	@Column(name = "SOURCE_URL", nullable = false, length = 200)
	private String mySource;

	@Column(name = "SOURCE_VERSION", length = 50)
	private String mySourceVersion;

	@Column(name = "TARGET_URL", nullable = false, length = 200)
	private String myTarget;

	@Column(name = "TARGET_VERSION", length = 50)
	private String myTargetVersion;

	@OneToMany(mappedBy = "myConceptMapGroup")
	private List<TermConceptMapGroupElement> myConceptMapGroupElements;

	private String myConceptMapUrl;
	private String mySourceValueSet;
	private String myTargetValueSet;

	public TermConceptMap getConceptMap() {
		return myConceptMap;
	}

	public void setConceptMap(TermConceptMap theTermConceptMap) {
		myConceptMap = theTermConceptMap;
	}

	public List<TermConceptMapGroupElement> getConceptMapGroupElements() {
		if (myConceptMapGroupElements == null) {
			myConceptMapGroupElements = new ArrayList<>();
		}

		return myConceptMapGroupElements;
	}

	public String getConceptMapUrl() {
		if (myConceptMapUrl == null) {
			myConceptMapUrl = getConceptMap().getUrl();
		}
		return myConceptMapUrl;
	}

	public Long getId() {
		return myId;
	}

	public String getSource() {
		return mySource;
	}

	public void setSource(String theSource) {
		this.mySource = theSource;
	}

	public String getSourceValueSet() {
		if (mySourceValueSet == null) {
			mySourceValueSet = getConceptMap().getSource();
		}
		return mySourceValueSet;
	}

	public String getSourceVersion() {
		return mySourceVersion;
	}

	public void setSourceVersion(String theSourceVersion) {
		mySourceVersion = theSourceVersion;
	}

	public String getTarget() {
		return myTarget;
	}

	public void setTarget(String theTarget) {
		this.myTarget = theTarget;
	}

	public String getTargetValueSet() {
		if (myTargetValueSet == null) {
			myTargetValueSet = getConceptMap().getTarget();
		}
		return myTargetValueSet;
	}

	public String getTargetVersion() {
		return myTargetVersion;
	}

	public void setTargetVersion(String theTargetVersion) {
		myTargetVersion = theTargetVersion;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("myId", myId)
			.append("myConceptMap - id", myConceptMap.getId())
			.append("mySource", mySource)
			.append("mySourceVersion", mySourceVersion)
			.append("myTarget", myTarget)
			.append("myTargetVersion", myTargetVersion)
			.append("myConceptMapGroupElements - size", myConceptMapGroupElements.size())
			.append("myConceptMapUrl", this.getConceptMapUrl())
			.append("mySourceValueSet", this.getSourceValueSet())
			.append("myTargetValueSet", this.getTargetValueSet())
			.toString();
	}
}
