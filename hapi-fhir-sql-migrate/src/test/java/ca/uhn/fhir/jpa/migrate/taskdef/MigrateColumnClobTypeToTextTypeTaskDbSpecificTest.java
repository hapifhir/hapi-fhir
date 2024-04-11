package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.COCKROACHDB_21_1;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.DERBY_EMBEDDED;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.H2_EMBEDDED;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.MARIADB_10_1;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.MSSQL_2012;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.MYSQL_5_7;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.ORACLE_12C;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MigrateColumnClobTypeToTextTypeTaskDbSpecificTest {

	private String createMigrationSqlForDriverType(DriverTypeEnum theDriverTypeEnum) {
		MigrateColumnClobTypeToTextTypeTask task = new MigrateColumnClobTypeToTextTypeTask("1", "1", "SOMETABLE", "CLOB_COLUM_NAME", "TEXT_COLUM_NAME");
		task.setDriverType(theDriverTypeEnum);

		return task.buildSqlStatement();
	}

	static Stream<Arguments> paramArguments(){
		return Stream.of(
			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = CLOB_COLUM_NAME  where CLOB_COLUM_NAME is not null", MYSQL_5_7),
			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = CLOB_COLUM_NAME  where CLOB_COLUM_NAME is not null", DERBY_EMBEDDED),
			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = CLOB_COLUM_NAME  where CLOB_COLUM_NAME is not null", ORACLE_12C),
			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = CLOB_COLUM_NAME  where CLOB_COLUM_NAME is not null", MARIADB_10_1),
			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = CLOB_COLUM_NAME  where CLOB_COLUM_NAME is not null", COCKROACHDB_21_1),
			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = CLOB_COLUM_NAME  where CLOB_COLUM_NAME is not null", H2_EMBEDDED),
			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = CLOB_COLUM_NAME  where CLOB_COLUM_NAME is not null", MSSQL_2012),

			Arguments.of("update SOMETABLE set TEXT_COLUM_NAME = convert_from(lo_get(CLOB_COLUM_NAME), 'UTF8') where CLOB_COLUM_NAME is not null", DriverTypeEnum.POSTGRES_9_4)
		);
	}
	@ParameterizedTest
	@MethodSource("paramArguments")
	public void testBuildSqlStatementForMySql(String theExpectedSqlString, DriverTypeEnum theDriverTypeEnum) {
		assertEquals(theExpectedSqlString, createMigrationSqlForDriverType(theDriverTypeEnum));
	}

}
