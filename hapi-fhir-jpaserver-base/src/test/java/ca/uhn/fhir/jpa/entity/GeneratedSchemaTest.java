package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneratedSchemaTest {

	/**
	 * Make sure that the RES_TEXT_VC column, which is supposed to be an unlimited-length
	 * string datatype, actually uses an appropriate datatype on the various databases
	 * we care about.
	 */
	@Test
	public void testVerifyLongVarcharColumnDefinition() {
		validateLongVarcharDatatype("cockroachdb.sql", "varchar(2147483647)");
		validateLongVarcharDatatype("derby.sql", "clob");
		validateLongVarcharDatatype("mysql.sql", "longtext");
		validateLongVarcharDatatype("mariadb.sql", "longtext");

		validateLongVarcharDatatype("h2.sql", "clob");
		validateLongVarcharDatatype("postgres.sql", "text");
		validateLongVarcharDatatype("oracle.sql", "clob");
		validateLongVarcharDatatype("sqlserver.sql", "varchar(max)");

	}

	private static void validateLongVarcharDatatype(String schemaName, String expectedDatatype) {
		String schema = ClasspathUtil.loadResource("ca/uhn/hapi/fhir/jpa/docs/database/" + schemaName);
		String[] lines = StringUtils.split(schema, '\n');
		String resTextVc = Arrays.stream(lines).filter(t -> t.contains("RES_TEXT_VC ")).findFirst().orElseThrow();
		assertThat(resTextVc).as("Wrong type in " + schemaName).contains("RES_TEXT_VC " + expectedDatatype);
	}

}
