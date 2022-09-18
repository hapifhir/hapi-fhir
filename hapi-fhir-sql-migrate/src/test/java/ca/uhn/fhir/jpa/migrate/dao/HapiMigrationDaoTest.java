package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.BaseMigrationTest;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HapiMigrationDaoTest extends BaseMigrationTest {

	@Test
	public void findAll_empty_returnsNothing() {
		Set<MigrationVersion> result = ourHapiMigrationDao.fetchSuccessfulMigrationVersions();
		assertThat(result, hasSize(0));
	}

	@Test
	public void findAll_2records_returnsBoth() {
		HapiMigrationEntity record1 = buildEntity("DESC1", "1.1");

		HapiMigrationEntity result1 = ourHapiMigrationDao.save(record1);
		assertEquals(1, result1.getPid());
		{
			Set<MigrationVersion> all = ourHapiMigrationDao.fetchSuccessfulMigrationVersions();
			assertThat(all, hasSize(1));
		}
		HapiMigrationEntity record2 = buildEntity("DESC2", "1.2");

		HapiMigrationEntity result2 = ourHapiMigrationDao.save(record2);
		assertEquals(2, result2.getPid());
		{
			Set<MigrationVersion> all = ourHapiMigrationDao.fetchSuccessfulMigrationVersions();
			assertThat(all, hasSize(2));
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
