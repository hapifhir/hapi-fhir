package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

	public static final int MAX_LENGTH = 200;

	@Id
	@SequenceGenerator(name = "SEQ_LASTN", sequenceName = "SEQ_LASTN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_LASTN")
	@Column(name = "LASTN_ID")
	private Long myId;

	@Field(name = "subject", analyze = Analyze.NO)
	@Column(name = "LASTN_SUBJECT_ID", nullable = true, length = MAX_LENGTH)
	private String mySubject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODEABLE_CONCEPT_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_OBSERVATION_CODE_FK"))
	@IndexedEmbedded(depth = 2, prefix = "codeconcept")
	private ObservationIndexedCodeCodeableConceptEntity myObservationCode;

	@Field(name = "codeconceptid", analyze = Analyze.NO)
	@Column(name = "CODEABLE_CONCEPT_ID", nullable = false, updatable = false, insertable = false, length = MAX_LENGTH)
	private String myCodeNormalizedId;

	@IndexedEmbedded(depth = 2, prefix = "categoryconcept")
	@Transient
	private Set<ObservationIndexedCategoryCodeableConceptEntity> myCategoryCodeableConcepts;

	@Field(name = "effectivedtm", analyze = Analyze.NO)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTN_EFFECTIVE_DATETIME", nullable = true)
	private Date myEffectiveDtm;

	@DocumentId(name = "identifier")
	@Column(name = "RESOURCE_IDENTIFIER", nullable = false, length = MAX_LENGTH)
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
