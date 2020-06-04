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

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

@Entity
@Indexed(index = "code_index")
@Embeddable
@Table(name = "HFJ_SPIDX_LASTN_CODE_CONCEPT")
public class ObservationIndexedCodeCodeableConceptEntity {

	public static final int MAX_LENGTH = 200;

	@Id
    @DocumentId(name = "codeable_concept_id")
    @Column(name="CODEABLE_CONCEPT_ID", length = MAX_LENGTH)
    private String myCodeableConceptId;

    @Field(name = "text")
    @Column(name = "CODEABLE_CONCEPT_TEXT", nullable = true, length = MAX_LENGTH)
    private String myCodeableConceptText;

    @IndexedEmbedded(depth=2, prefix = "coding")
	 @JoinColumn(name = "CODEABLE_CONCEPT_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_CONCEPT_CODE"))
	 @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	 private ObservationIndexedCodeCodingEntity myObservationIndexedCodeCodingEntity;

    public ObservationIndexedCodeCodeableConceptEntity() {

    }

    public ObservationIndexedCodeCodeableConceptEntity(String theCodeableConceptText, String theCodeableConceptId) {
        setCodeableConceptText(theCodeableConceptText);
        setCodeableConceptId(theCodeableConceptId);
    }

    public void addCoding(ObservationIndexedCodeCodingEntity theObservationIndexedCodeCodingEntity) {
		 myObservationIndexedCodeCodingEntity = theObservationIndexedCodeCodingEntity;
    }

    public String getCodeableConceptId() {
        return myCodeableConceptId;
    }

    public void setCodeableConceptId(String theCodeableConceptId) {
        myCodeableConceptId = theCodeableConceptId;
    }

    public String getCodeableConceptText() {
        return myCodeableConceptText;
    }

    public void setCodeableConceptText(String theCodeableConceptText) {
        myCodeableConceptText = theCodeableConceptText;
    }

}
