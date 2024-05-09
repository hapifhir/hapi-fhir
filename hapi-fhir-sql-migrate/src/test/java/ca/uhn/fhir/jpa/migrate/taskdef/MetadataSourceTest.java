package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import com.google.common.base.Strings;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MockitoSettings(strictness = Strictness.LENIENT)
class MetadataSourceTest {
	MetadataSource myMetadataSource = new MetadataSource();
	@Mock
	DriverTypeEnum.ConnectionProperties myConnectionProperties;
	@Mock
	JdbcTemplate myJdbcTemplate;

	@ParameterizedTest
	@CsvSource({
		"H2_EMBEDDED,,false",
		"DERBY_EMBEDDED,,false",
		"MARIADB_10_1,,false",
		"MYSQL_5_7,,false",
		"POSTGRES_9_4,,true",
		"ORACLE_12C,Oracle Database 19c Enterprise Edition Release 19.0.0.0.0 - Production,true",
		"ORACLE_12C,Oracle Database 19c Express Edition Release 11.2.0.2.0 - 64bit Production,false",
		"COCKROACHDB_21_1,,true",
		// sql server only supports it in Enterprise
		// https://docs.microsoft.com/en-us/sql/sql-server/editions-and-components-of-sql-server-2019?view=sql-server-ver16#RDBMSHA
		"MSSQL_2012,Developer Edition (64-bit),false",
		"MSSQL_2012,Developer Edition (64-bit),false",
		"MSSQL_2012,Standard Edition (64-bit),false",
		"MSSQL_2012,Enterprise Edition (64-bit),true"
	})
	void isOnlineIndexSupported(DriverTypeEnum theType, String theEdition, boolean theSupportedFlag) {
		// stub out our Sql Server edition lookup
		Mockito.when(myConnectionProperties.getDriverType()).thenReturn(theType);
		Mockito.when(myConnectionProperties.newJdbcTemplate()).thenReturn(myJdbcTemplate);
		Mockito.when(myJdbcTemplate.queryForObject(Mockito.any(), Mockito.eq(String.class))).thenReturn(Strings.nullToEmpty(theEdition));

		assertEquals(theSupportedFlag, myMetadataSource.isOnlineIndexSupported(myConnectionProperties));
	}

}
