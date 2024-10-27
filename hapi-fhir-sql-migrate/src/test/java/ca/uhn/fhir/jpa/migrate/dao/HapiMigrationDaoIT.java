package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.BaseMigrationTest;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HapiMigrationDaoIT extends BaseMigrationTest {

	@Test
	public void findAll_empty_returnsNothing() {
		Set<MigrationVersion> result = ourHapiMigrationDao.fetchSuccessfulMigrationVersions();
		assertThat(result).hasSize(0);
	}

	@Test
	public void findAll_2records_returnsBoth() {
		HapiMigrationEntity record1 = buildEntity("DESC1", "1.1");

		boolean result1 = ourHapiMigrationDao.save(record1);
		assertTrue(result1);
		{
			Set<MigrationVersion> all = ourHapiMigrationDao.fetchSuccessfulMigrationVersions();
			assertThat(all).hasSize(1);
		}
		HapiMigrationEntity record2 = buildEntity("DESC2", "1.2");

		boolean result2 = ourHapiMigrationDao.save(record2);
		assertTrue(result2);
		{
			Set<MigrationVersion> all = ourHapiMigrationDao.fetchSuccessfulMigrationVersions();
			assertThat(all).hasSize(2);
		}
	}

	private HapiMigrationEntity buildEntity(String theDesc, String theVersion) {
		HapiMigrationEntity retval = new HapiMigrationEntity();
		retval.setVersion(theVersion);
		retval.setDescription(theDesc);
		retval.setExecutionTime(1);
		retval.setSuccess(true);
		return retval;
	}
}
