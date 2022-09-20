package ca.uhn.fhir.jpa.migrate.dao;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.entity.HapiMigrationEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;

class MigrationQueryBuilderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(MigrationQueryBuilderTest.class);

	private static final String TABLE = "TEST_TABLE_NAME";

	@Test
	void derby_insert() {
		MigrationQueryBuilder mqb = new MigrationQueryBuilder(DriverTypeEnum.DERBY_EMBEDDED, TABLE);
		HapiMigrationEntity entity = buildEntity();
		String insertSql = mqb.insertStatement(entity);
		ourLog.info(insertSql);
		assertThat(insertSql, endsWith(",NULL,true)"));
	}

	@Test
	void oracle_insert() {
		MigrationQueryBuilder mqb = new MigrationQueryBuilder(DriverTypeEnum.ORACLE_12C, TABLE);
		HapiMigrationEntity entity = buildEntity();
		String insertSql = mqb.insertStatement(entity);
		assertThat(insertSql, endsWith(",NULL,1)"));
	}

	private HapiMigrationEntity buildEntity() {
		HapiMigrationEntity retval = new HapiMigrationEntity();
		retval.setInstalledOn(new Date());
		retval.setSuccess(true);
		return retval;
	}

}
