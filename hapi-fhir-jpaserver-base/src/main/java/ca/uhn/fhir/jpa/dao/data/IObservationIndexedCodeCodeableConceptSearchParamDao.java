package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedCodeCodeableConceptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IObservationIndexedCodeCodeableConceptSearchParamDao extends JpaRepository<ObservationIndexedCodeCodeableConceptEntity, Long> {
}
