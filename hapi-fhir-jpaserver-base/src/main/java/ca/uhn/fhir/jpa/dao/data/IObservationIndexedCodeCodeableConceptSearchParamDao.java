package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedCodeCodeableConceptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IObservationIndexedCodeCodeableConceptSearchParamDao extends JpaRepository<ObservationIndexedCodeCodeableConceptEntity, Long> {
	@Query("" +
		"SELECT t FROM ObservationIndexedCodeCodeableConceptEntity t " +
		"WHERE t.myCodeableConceptId = :codeableConceptId" +
		"")
	ObservationIndexedCodeCodeableConceptEntity findByCodeableConceptId(@Param("codeableConceptId") String theCodeableConceptId);

}
