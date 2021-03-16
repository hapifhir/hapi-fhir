package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.TermValueSetConceptViewOracle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface ITermValueSetConceptViewOracleDao extends JpaRepository<TermValueSetConceptViewOracle, Long> {
		@Query("SELECT v FROM TermValueSetConceptView v WHERE v.myConceptValueSetPid = :pid AND v.myConceptOrder >= :from AND v.myConceptOrder < :to ORDER BY v.myConceptOrder")
		List<TermValueSetConceptViewOracle> findByTermValueSetId(@Param("from") int theFrom, @Param("to") int theTo, @Param("pid") Long theValueSetId);

		@Query("SELECT v FROM TermValueSetConceptView v WHERE v.myConceptValueSetPid = :pid AND LOWER(v.myConceptDisplay) LIKE :display ORDER BY v.myConceptOrder")
		List<TermValueSetConceptViewOracle> findByTermValueSetId(@Param("pid") Long theValueSetId, @Param("display") String theDisplay);
}
