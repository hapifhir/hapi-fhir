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
		MigrateColumnClobTypeToTextTypeTask task = new MigrateColumnClobTypeToTextTypeTask("1", "1", "sometable", "clob_colum_name", "text_colum_name");
		task.setDriverType(theDriverTypeEnum);

		return task.buildSqlStatement();
	}

	static Stream<Arguments> paramArguments(){
		return Stream.of(
			Arguments.of("update sometable set text_colum_name = clob_colum_name where clob_colum_name is not null", MYSQL_5_7),
			Arguments.of("update sometable set text_colum_name = clob_colum_name where clob_colum_name is not null", DERBY_EMBEDDED),
			Arguments.of("update sometable set text_colum_name = clob_colum_name where clob_colum_name is not null", ORACLE_12C),
			Arguments.of("update sometable set text_colum_name = clob_colum_name where clob_colum_name is not null", MARIADB_10_1),
			Arguments.of("update sometable set text_colum_name = clob_colum_name where clob_colum_name is not null", COCKROACHDB_21_1),
			Arguments.of("update sometable set text_colum_name = clob_colum_name where clob_colum_name is not null", H2_EMBEDDED),
			Arguments.of("update sometable set text_colum_name = clob_colum_name where clob_colum_name is not null", MSSQL_2012),

			Arguments.of("update sometable set text_colum_name = convert_from(lo_get(clob_colum_name), 'UTF8') where clob_colum_name is not null", DriverTypeEnum.POSTGRES_9_4)
		);
	}
	@ParameterizedTest
	@MethodSource("paramArguments")
	public void testBuildSqlStatementForMySql(String theExpectedSqlString, DriverTypeEnum theDriverTypeEnum) {
		assertEquals(theExpectedSqlString, createMigrationSqlForDriverType(theDriverTypeEnum));
	}

}
