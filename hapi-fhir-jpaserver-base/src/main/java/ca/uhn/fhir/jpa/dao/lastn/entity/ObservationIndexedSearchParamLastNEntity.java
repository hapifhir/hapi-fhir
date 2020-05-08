package ca.uhn.fhir.jpa.dao.lastn.entity;

import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.persistence.Index;
import java.util.*;

@Entity
@Table(name = "HFJ_LASTN_OBSERVATION", indexes = {
	@Index(name = "IDX_LASTN_OBSERVATION_RESID", columnList = "RESOURCE_IDENTIFIER", unique = true)
})
@Indexed(index = "observation_index")
public class ObservationIndexedSearchParamLastNEntity {

	@Id
	@SequenceGenerator(name = "SEQ_LASTN", sequenceName = "SEQ_LASTN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_LASTN")
	@Column(name = "LASTN_ID")
	private Long myId;

	@Field(name = "subject", analyze = Analyze.NO)
	@Column(name = "LASTN_SUBJECT_ID", nullable = true)
	private String mySubject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODEABLE_CONCEPT_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_OBSERVATION_CODE_FK"))
	@IndexedEmbedded(depth = 2, prefix = "codeconcept")
	private ObservationIndexedCodeCodeableConceptEntity myObservationCode;

	@Field(name = "codeconceptid", analyze = Analyze.NO)
	@Column(name = "CODEABLE_CONCEPT_ID", nullable = false, updatable = false, insertable = false)
	private String myCodeNormalizedId;

	@IndexedEmbedded(depth = 2, prefix = "categoryconcept")
	@Transient
	private Set<ObservationIndexedCategoryCodeableConceptEntity> myCategoryCodeableConcepts;

	@Field(name = "effectivedtm", analyze = Analyze.NO)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTN_EFFECTIVE_DATETIME", nullable = true)
	private Date myEffectiveDtm;

	@DocumentId(name = "identifier")
	@Column(name = "RESOURCE_IDENTIFIER", nullable = false)
	private String myIdentifier;

	public ObservationIndexedSearchParamLastNEntity() {
	}

	public String getSubject() {
		return mySubject;
	}

	public void setSubject(String theSubject) {
		mySubject = theSubject;
	}

	public String getIdentifier() {
		return myIdentifier;
	}

	public void setIdentifier(String theIdentifier) {
		myIdentifier = theIdentifier;
	}

	public void setEffectiveDtm(Date theEffectiveDtm) {
		myEffectiveDtm = theEffectiveDtm;
	}

	public Date getEffectiveDtm() {
		return myEffectiveDtm;
	}

	public void setCodeNormalizedId(String theCodeNormalizedId) {
		myCodeNormalizedId = theCodeNormalizedId;
	}

	public String getCodeNormalizedId() {
		return myCodeNormalizedId;
	}


	public void setObservationCode(ObservationIndexedCodeCodeableConceptEntity theObservationCode) {
		myObservationCode = theObservationCode;
	}

	public void setCategoryCodeableConcepts(Set<ObservationIndexedCategoryCodeableConceptEntity> theCategoryCodeableConcepts) {
		myCategoryCodeableConcepts = theCategoryCodeableConcepts;
	}

}
