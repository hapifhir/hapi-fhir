package ca.uhn.fhir.jpa.dao.lastn;

import ca.uhn.fhir.jpa.dao.data.IObservationIndexedCodeCodingSearchParamDao;
import ca.uhn.fhir.jpa.dao.lastn.entity.*;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedCodeCodeableConceptSearchParamDao;
import ca.uhn.fhir.jpa.dao.data.IObservationIndexedSearchParamLastNDao;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

//@Component
public class ObservationLastNIndexPersistSvc {

    @Autowired
    IObservationIndexedSearchParamLastNDao myResourceIndexedObservationLastNDao;

    @Autowired
    IObservationIndexedCodeCodeableConceptSearchParamDao myObservationIndexedCodeableConceptSearchParamDao;

    @Autowired
    IObservationIndexedCodeCodingSearchParamDao myObservationIndexedCodeCodingSearchParamDao;

    public void indexObservation(Observation theObservation) {
        ObservationIndexedSearchParamLastNEntity indexedObservation = new ObservationIndexedSearchParamLastNEntity();
        String resourcePID = theObservation.getId();
        indexedObservation.setIdentifier(resourcePID);
        // TODO: Need to improve this as there are multiple use cases involving subject.
        String subjectId = "Patient/" + theObservation.getSubject().getReference();
        indexedObservation.setSubject(subjectId);
        Date effectiveDtm = theObservation.getEffectiveDateTimeType().getValue();
        indexedObservation.setEffectiveDtm(effectiveDtm);

        // Build CodeableConcept entities for Observation.Category
        Set<ObservationIndexedCategoryCodeableConceptEntity> categoryConcepts = new HashSet<>();
        for(CodeableConcept categoryCodeableConcept : theObservation.getCategory()) {
            // Build Coding entities for each category CodeableConcept
            Set<ObservationIndexedCategoryCodingEntity> categoryCodingEntities = new HashSet<>();
            ObservationIndexedCategoryCodeableConceptEntity categoryCodeableConceptEntity = new ObservationIndexedCategoryCodeableConceptEntity(categoryCodeableConcept.getText());
            for(Coding categoryCoding : categoryCodeableConcept.getCoding()){
                categoryCodingEntities.add(new ObservationIndexedCategoryCodingEntity(categoryCoding.getSystem(), categoryCoding.getCode(), categoryCoding.getDisplay()));
            }
            categoryCodeableConceptEntity.setObservationIndexedCategoryCodingEntitySet(categoryCodingEntities);
            categoryConcepts.add(categoryCodeableConceptEntity);
        }
        indexedObservation.setCategoryCodeableConcepts(categoryConcepts);

        // Build CodeableConcept entity for Observation.Code.
        CodeableConcept codeCodeableConcept = theObservation.getCode();
        String observationCodeNormalizedId = null;

        // Determine if a Normalized ID was created previously for Observation Code
        for (Coding codeCoding : codeCodeableConcept.getCoding()) {
            if (codeCoding.hasCode() && codeCoding.hasSystem()) {
                observationCodeNormalizedId = myObservationIndexedCodeCodingSearchParamDao.findForCodeAndSystem(codeCoding.getCode(), codeCoding.getSystem());
            } else {
                observationCodeNormalizedId = myObservationIndexedCodeCodingSearchParamDao.findForDisplay(codeCoding.getDisplay());
            }
        }
        // Generate a new a normalized ID if necessary
        if (observationCodeNormalizedId == null) {
            observationCodeNormalizedId = UUID.randomUUID().toString();
        }

        // Create/update normalized Observation Code index record
        ObservationIndexedCodeCodeableConceptEntity codeableConceptField = new ObservationIndexedCodeCodeableConceptEntity(codeCodeableConcept.getText(), observationCodeNormalizedId);
        for (Coding codeCoding : codeCodeableConcept.getCoding()) {
            codeableConceptField.addCoding(new ObservationIndexedCodeCodingEntity(codeCoding.getSystem(), codeCoding.getCode(), codeCoding.getDisplay(), observationCodeNormalizedId));
        }
        myObservationIndexedCodeableConceptSearchParamDao.save(codeableConceptField);

        indexedObservation.setObservationCode(codeableConceptField);
        indexedObservation.setCodeNormalizedId(observationCodeNormalizedId);
        myResourceIndexedObservationLastNDao.save(indexedObservation);

    }
}
