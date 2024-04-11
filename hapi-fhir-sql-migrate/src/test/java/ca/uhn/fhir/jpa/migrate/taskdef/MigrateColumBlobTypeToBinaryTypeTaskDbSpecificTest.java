package ca.uhn.fhir.jpa.migrate.taskdef;

import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.*;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MigrateColumBlobTypeToBinaryTypeTaskDbSpecificTest {

	private String createMigrationSqlForDriverType(DriverTypeEnum theDriverTypeEnum) {
		MigrateColumBlobTypeToBinaryTypeTask task = new MigrateColumBlobTypeToBinaryTypeTask("1", "1", "SOMETABLE", "BLOB_COLUM_NAME", "BIN_COLUM_NAME");
		task.setDriverType(theDriverTypeEnum);

		return task.buildSqlStatement();
	}

	static Stream<Arguments> paramArguments(){
		return Stream.of(
			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = BLOB_COLUM_NAME  where BLOB_COLUM_NAME is not null", MYSQL_5_7),
			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = BLOB_COLUM_NAME  where BLOB_COLUM_NAME is not null", DERBY_EMBEDDED),
			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = BLOB_COLUM_NAME  where BLOB_COLUM_NAME is not null", ORACLE_12C),
			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = BLOB_COLUM_NAME  where BLOB_COLUM_NAME is not null", MARIADB_10_1),
			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = BLOB_COLUM_NAME  where BLOB_COLUM_NAME is not null", COCKROACHDB_21_1),
			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = BLOB_COLUM_NAME  where BLOB_COLUM_NAME is not null", H2_EMBEDDED),
			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = BLOB_COLUM_NAME  where BLOB_COLUM_NAME is not null", MSSQL_2012),

			Arguments.of("update SOMETABLE set BIN_COLUM_NAME = lo_get(BLOB_COLUM_NAME)  where BLOB_COLUM_NAME is not null", DriverTypeEnum.POSTGRES_9_4)
		);
	}
	@ParameterizedTest
	@MethodSource("paramArguments")
	public void testBuildSqlStatementForMySql(String theExpectedSqlString, DriverTypeEnum theDriverTypeEnum) {
		assertEquals(theExpectedSqlString, createMigrationSqlForDriverType(theDriverTypeEnum));
	}

}
