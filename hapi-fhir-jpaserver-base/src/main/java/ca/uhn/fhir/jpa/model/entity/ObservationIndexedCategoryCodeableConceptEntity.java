package ca.uhn.fhir.jpa.model.entity;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Set;

@Embeddable
public class ObservationIndexedCategoryCodeableConceptEntity {

    @Id
    @DocumentId(name = "category_concept_id")
    @SequenceGenerator(name = "SEQ_CATEGORY_CONCEPT", sequenceName = "SEQ_CATEGORY_CONCEPT")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CATEGORY_CONCEPT")
    @Column(name = "CATEGORY_CONCEPT_ID")
    private Long myId;

    @Field(name = "text")
    @Column(name = "CODEABLE_CONCEPT_TEXT", nullable = true)
    private String myCodeableConceptText;

    @IndexedEmbedded(depth=2, prefix = "coding")
    @OneToMany(mappedBy = "myCodeableConceptId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ObservationIndexedCategoryCodingEntity> myObservationIndexedCategoryCodingEntitySet;

    public ObservationIndexedCategoryCodeableConceptEntity(String theCodeableConceptText) {
        setCodeableConceptText(theCodeableConceptText);
    }

    public void setObservationIndexedCategoryCodingEntitySet(Set<ObservationIndexedCategoryCodingEntity> theObservationIndexedCategoryCodingEntitySet) {
        myObservationIndexedCategoryCodingEntitySet = theObservationIndexedCategoryCodingEntitySet;
    }

    public void setCodeableConceptText(String theCodeableConceptText) {
        myCodeableConceptText = theCodeableConceptText;
    }

}
