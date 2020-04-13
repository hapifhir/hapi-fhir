package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IObservationIndexedSearchParamLastNDao extends JpaRepository<ObservationIndexedSearchParamLastNEntity, Long>{
}
