package ca.uhn.fhir.tinder.ddl;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateDdlMojoTest {

	private static final Logger ourLog = LoggerFactory.getLogger(GenerateDdlMojoTest.class);

	@Test
	public void testGenerateSequences() throws MojoExecutionException, MojoFailureException, IOException {

		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.tinder.ddl.test");
		m.outputDirectory = "target/generate-ddl-plugin-test/";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect", "h2.sql"),
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql"),
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect", "oracle.sql"),
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect", "sqlserver.sql")
		);
		m.execute();

		verifySequence("sqlserver.sql");
		verifySequence("oracle.sql");
		verifySequence("postgres.sql");
		verifySequence("h2.sql");

	}

	@Test
	public void testPruneComplexId_Enabled() throws MojoExecutionException, MojoFailureException, IOException {

		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.tinder.ddl.test");
		m.outputDirectory = "target/generate-ddl-plugin-test/";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.trimConditionalIdsFromPrimaryKeys = true;
		m.execute();

		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/postgres.sql"), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		ourLog.info("SQL: {}", contents);

		String[] sqlStatements = contents.replaceAll("\\s+", " ").split(";");
		assertThat(sqlStatements).anyMatch(s -> Pattern.compile("CREATE TABLE ENTITY_WITH_COMPLEX_ID_CHILD .* PRIMARY KEY \\(PID\\)").matcher(s).find());
		assertThat(sqlStatements).anyMatch(s -> Pattern.compile("CREATE TABLE ENTITY_WITH_COMPLEX_ID_PARENT .* PRIMARY KEY \\(PID\\)").matcher(s).find());
		assertThat(sqlStatements).anyMatch(s -> Pattern.compile("ALTER TABLE IF EXISTS ENTITY_WITH_COMPLEX_ID_CHILD .* FOREIGN KEY \\(PARENT_PID\\)").matcher(s).find());
	}

	@Test
	public void testPruneComplexId_Disabled() throws MojoExecutionException, MojoFailureException, IOException {

		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.tinder.ddl.test");
		m.outputDirectory = "target/generate-ddl-plugin-test/";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.trimConditionalIdsFromPrimaryKeys = false;
		m.execute();

		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/postgres.sql"), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		ourLog.info("SQL: {}", contents);

		String[] sqlStatements = contents.replaceAll("\\s+", " ").split(";");
		assertThat(sqlStatements).anyMatch(s -> Pattern.compile("CREATE TABLE ENTITY_WITH_COMPLEX_ID_CHILD .* PRIMARY KEY \\(PID, PARTITION_ID\\)").matcher(s).find());
		assertThat(sqlStatements).anyMatch(s -> Pattern.compile("CREATE TABLE ENTITY_WITH_COMPLEX_ID_PARENT .* PRIMARY KEY \\(PID, PARTITION_ID\\)").matcher(s).find());
		assertThat(sqlStatements).anyMatch(s -> Pattern.compile("ALTER TABLE IF EXISTS ENTITY_WITH_COMPLEX_ID_CHILD .* FOREIGN KEY \\(PARENT_PID, PARENT_PARTITION_ID\\)").matcher(s).find());
	}

	private static void verifySequence(String fileName) throws IOException {
		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/" + fileName), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		assertThat(contents).as(fileName).contains("CREATE SEQUENCE");
	}
}
