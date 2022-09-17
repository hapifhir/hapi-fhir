package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HapiMigrationDao extends JpaRepository<HapiMigrationEntity, Integer> {
	@Query("SELECT s.myVersion FROM HapiMigrationEntity s")
	List<String> fetchMigrationVersions();
}
