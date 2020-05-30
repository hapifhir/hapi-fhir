package ca.uhn.fhir.jpa.model.entity;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Set;

@Embeddable
public class ObservationIndexedCategoryCodeableConceptEntity {

    @Field(name = "text")
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
