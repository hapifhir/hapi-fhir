package ca.uhn.fhir.jpa.model.entity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

@Entity
@Indexed(index = "code_index")
@Embeddable
@Table(name = "HFJ_SPIDX_LASTN_CODEABLE_CONCEPT")
public class ObservationIndexedCodeCodeableConceptEntity {

    @Id
    @DocumentId(name = "codeable_concept_id")
    @Column(name="CODEABLE_CONCEPT_ID")
    private String myCodeableConceptId;

    @Field(name = "text")
    @Column(name = "CODEABLE_CONCEPT_TEXT", nullable = true)
    private String myCodeableConceptText;

    // TODO: Make coding a Collection. Need to first figure out how to maintain this over time.
    @IndexedEmbedded(depth=2, prefix = "coding")
//    @OneToMany(mappedBy = "myCodeableConceptId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	 @JoinColumn(name = "CODEABLE_CONCEPT_ID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_CONCEPT_CODE"))
	 @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private Set<ObservationIndexedCodeCodingEntity> myObservationIndexedCodeCodingEntitySet;
	 private ObservationIndexedCodeCodingEntity myObservationIndexedCodeCodingEntity;

    public ObservationIndexedCodeCodeableConceptEntity() {

    }

    public ObservationIndexedCodeCodeableConceptEntity(String theCodeableConceptText, String theCodeableConceptId) {
        setCodeableConceptText(theCodeableConceptText);
        setCodeableConceptId(theCodeableConceptId);
    }

    public void addCoding(ObservationIndexedCodeCodingEntity theObservationIndexedCodeCodingEntity) {
//        if (myObservationIndexedCodeCodingEntitySet == null) {
//            myObservationIndexedCodeCodingEntitySet = new HashSet<>();
//        }
//        myObservationIndexedCodeCodingEntitySet.add(theObservationIndexedCodeCodingEntity);
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
