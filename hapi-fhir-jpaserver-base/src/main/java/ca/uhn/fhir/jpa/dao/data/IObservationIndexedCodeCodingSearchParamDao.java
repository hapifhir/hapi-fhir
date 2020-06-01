package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IObservationIndexedCodeCodingSearchParamDao extends JpaRepository<ObservationIndexedCodeCodingEntity, Long> {

    @Query("" +
            "SELECT t.myCodeableConceptId FROM ObservationIndexedCodeCodingEntity t " +
            "WHERE t.myCode = :code " +
            "AND t.mySystem = :system " +
            "")
    String findByCodeAndSystem(@Param("code") String theCode, @Param("system") String theSystem);


    @Query("" +
            "SELECT t.myCodeableConceptId FROM ObservationIndexedCodeCodingEntity t " +
            "WHERE t.myDisplay = :display" +
            "")
    String findByDisplay(@Param("display") String theDisplay);

}
