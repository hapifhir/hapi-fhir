package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.DERBY_EMBEDDED;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.H2_EMBEDDED;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.MARIADB_10_1;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.MSSQL_2012;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.MYSQL_5_7;
import static ca.uhn.fhir.jpa.migrate.DriverTypeEnum.ORACLE_12C;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RenameTableTaskDbSpecificTest {

	private String createRenameTableSql(DriverTypeEnum theDriverTypeEnum) {
		RenameTableTask task = new RenameTableTask("1", "1", "oldname","newname");
		task.setDriverType(theDriverTypeEnum);

		return task.buildRenameTableSqlStatement();
	}

	static Stream<Arguments> paramArguments(){
		return Stream.of(
			Arguments.of("rename table oldname to newname", MYSQL_5_7),
			Arguments.of("rename table oldname to newname", DERBY_EMBEDDED),
			Arguments.of("alter table oldname rename to newname", ORACLE_12C),
			Arguments.of("alter table oldname rename to newname", MARIADB_10_1),
			Arguments.of("alter table oldname rename to newname", DriverTypeEnum.POSTGRES_9_4),
			Arguments.of("alter table oldname rename to newname", H2_EMBEDDED),
			Arguments.of("sp_rename 'oldname', 'newname'", MSSQL_2012)

		);
	}
	@ParameterizedTest
	@MethodSource("paramArguments")
	public void testBuildSqlStatementForMySql(String theExpectedSqlString, DriverTypeEnum theDriverTypeEnum) {
		assertEquals(theExpectedSqlString, createRenameTableSql(theDriverTypeEnum));
	}

}
