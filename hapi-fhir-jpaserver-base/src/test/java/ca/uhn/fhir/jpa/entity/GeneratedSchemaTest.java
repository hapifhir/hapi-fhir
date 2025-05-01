package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

	@Test
	void testVerifyPksNonPartitioned() {
		String file = "ca/uhn/hapi/fhir/jpa/docs/database/nonpartitioned/postgres.sql";
		List<String> lines = loadSchemaLines(file);

		List<String> fks = lines.stream().filter(t -> t.startsWith("alter table if exists HFJ_RES_VER add constraint FK_RESOURCE_HISTORY_RESOURCE ")).toList();
		assertEquals(1, fks.size(), () -> String.join("\n", lines));
		assertThat(fks.get(0)).contains("foreign key (RES_ID)");
	}

	@Test
	void testVerifyPksPartitioned() {
		String file = "ca/uhn/hapi/fhir/jpa/docs/database/partitioned/postgres.sql";
		List<String> lines = loadSchemaLines(file);

		List<String> fks = lines.stream().filter(t -> t.startsWith("alter table if exists HFJ_RES_VER add constraint FK_RESOURCE_HISTORY_RESOURCE ")).toList();
		assertEquals(1, fks.size(), () -> String.join("\n", lines));
		assertThat(fks.get(0)).contains("foreign key (RES_ID, PARTITION_ID)");
	}

	@ParameterizedTest
	@CsvSource({
		"ca/uhn/hapi/fhir/jpa/docs/database/partitioned/postgres.sql  , false",
		"ca/uhn/hapi/fhir/jpa/docs/database/partitioned/sqlserver.sql , false",
		"ca/uhn/hapi/fhir/jpa/docs/database/partitioned/oracle.sql    , false",
		"ca/uhn/hapi/fhir/jpa/docs/database/partitioned/h2.sql        , true"
	})
	void verifyNoResourceLinkTargetFkConstraint(String theFileName, boolean theShouldContainConstraint) {
		String file = ClasspathUtil.loadResource(theFileName);
		if (theShouldContainConstraint) {
			assertThat(file).contains("FK_RESLINK_TARGET");
		} else {
			assertThat(file).doesNotContain("FK_RESLINK_TARGET");
		}
	}

	private static void validateLongVarcharDatatype(String schemaName, String expectedDatatype) {
		String schema = ClasspathUtil.loadResource("ca/uhn/hapi/fhir/jpa/docs/database/nonpartitioned/" +
			"" + schemaName);
		String[] lines = StringUtils.split(schema, '\n');
		String resTextVc = Arrays.stream(lines).filter(t -> t.contains("RES_TEXT_VC ")).findFirst().orElseThrow();
		assertThat(resTextVc).as("Wrong type in " + schemaName).contains("RES_TEXT_VC " + expectedDatatype);
	}

	@Nonnull
	private static List<String> loadSchemaLines(String file) {
		String schema = ClasspathUtil.loadResource(file);
		schema = schema.replace("\n", " ").replaceAll(" +", " ");
		List<String> lines = Arrays.stream(StringUtils.split(schema, ';'))
			.map(StringUtils::trim)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toList());
		return lines;
	}

}
