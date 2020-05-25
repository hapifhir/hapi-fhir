package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IObservationIndexedSearchParamLastNDao extends JpaRepository<ObservationIndexedSearchParamLastNEntity, Long>{
	@Query("" +
		"SELECT t FROM ObservationIndexedSearchParamLastNEntity t " +
		"WHERE t.myIdentifier = :identifier" +
		"")
	ObservationIndexedSearchParamLastNEntity findForIdentifier(@Param("identifier") String theIdentifier);

}
