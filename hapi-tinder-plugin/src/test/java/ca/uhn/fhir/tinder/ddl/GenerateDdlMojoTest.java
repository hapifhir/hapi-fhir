package ca.uhn.fhir.tinder.ddl;

import jakarta.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
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
	public void testDatabasePartitionMode_Disabled() throws MojoExecutionException, MojoFailureException, IOException {

		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.tinder.ddl.test.pks");
		m.outputDirectory = "target/generate-ddl-plugin-test/";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.databasePartitionMode = false;
		m.execute();

		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/postgres.sql"), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		ourLog.info("SQL: {}", contents);

		String[] sqlStatements = contents.replaceAll("\\s+", " ").split(";");
		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_COMPLEX_ID_PARENT .* PID .* PARTITION_ID .* NAME .* PRIMARY KEY \\(PID\\)"));
		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_COMPLEX_ID_CHILD .* PID .* PARTITION_ID .* NAME .* PARENT_PARTITION_ID .* PARENT_PID .* PRIMARY KEY \\(PID\\)"));
		assertThat(sqlStatements).anyMatch(regex("ALTER TABLE IF EXISTS ENTITY_WITH_COMPLEX_ID_CHILD ADD CONSTRAINT FK_COMPLEX_ID_PARENT_CHILD FOREIGN KEY \\(PARENT_PID\\)"));

		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_EMBEDDED_ID_PARENT .* PID .* PARTITION_ID .* NAME .* PRIMARY KEY \\(PID\\)"));
		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_EMBEDDED_ID_CHILD .* PID .* PARTITION_ID .* NAME .* PARENT_PARTITION_ID .* PARENT_PID .* PRIMARY KEY \\(PID\\)"));
		assertThat(sqlStatements).anyMatch(regex("ALTER TABLE IF EXISTS ENTITY_WITH_EMBEDDED_ID_CHILD ADD CONSTRAINT FK_EMBEDDED_ID_PARENT_CHILD FOREIGN KEY \\(PARENT_PID\\)"));

		// Should not be NOT NULL
		assertThat(sqlStatements).anyMatch(substring("PARTITION_ID INTEGER,"));
	}

	@Test
	public void testDatabasePartitionMode_Disabled_PruneNonPks() throws MojoExecutionException, MojoFailureException, IOException {

		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.tinder.ddl.test.nonpks");
		m.outputDirectory = "target/generate-ddl-plugin-test/";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.databasePartitionMode = false;
		m.execute();

		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/postgres.sql"), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		ourLog.info("SQL: {}", contents);

		String[] sqlStatements = contents.replaceAll("\\s+", " ").split(";");
		assertThat(sqlStatements).anyMatch(regex("LB_RES_ID BIGINT,"));
		assertThat(sqlStatements).anyMatch(regex("LB_RES_PARTITION_ID INTEGER,"));

	}

	@Test
	public void testDatabasePartitionMode_Enabled() throws MojoExecutionException, MojoFailureException, IOException {

		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of("ca.uhn.fhir.tinder.ddl.test.pks");
		m.outputDirectory = "target/generate-ddl-plugin-test/";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.databasePartitionMode = true;
		m.execute();

		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/postgres.sql"), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		ourLog.info("SQL: {}", contents);

		String[] sqlStatements = contents.replaceAll("\\s+", " ").split(";");
		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_COMPLEX_ID_PARENT .* PID .* PARTITION_ID .* NAME .* PRIMARY KEY \\(PID, PARTITION_ID\\)"));
		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_COMPLEX_ID_CHILD .* PID .* PARTITION_ID .* NAME .* PARENT_PARTITION_ID .* PARENT_PID .* PRIMARY KEY \\(PID, PARTITION_ID\\)"));
		assertThat(sqlStatements).anyMatch(regex("ALTER TABLE IF EXISTS ENTITY_WITH_COMPLEX_ID_CHILD ADD CONSTRAINT FK_COMPLEX_ID_PARENT_CHILD FOREIGN KEY \\(PARENT_PID, PARENT_PARTITION_ID\\)"));

		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_EMBEDDED_ID_PARENT .* PID .* PARTITION_ID .* NAME .* PRIMARY KEY \\(PID, PARTITION_ID\\)"));
		assertThat(sqlStatements).anyMatch(regex("CREATE TABLE ENTITY_WITH_EMBEDDED_ID_CHILD .* PID .* PARTITION_ID .* NAME .* PARENT_PARTITION_ID .* PARENT_PID .* PRIMARY KEY \\(PID, PARTITION_ID\\)"));
		assertThat(sqlStatements).anyMatch(regex("ALTER TABLE IF EXISTS ENTITY_WITH_EMBEDDED_ID_CHILD ADD CONSTRAINT FK_EMBEDDED_ID_PARENT_CHILD FOREIGN KEY \\(PARENT_PID, PARENT_PARTITION_ID\\)"));

		// Part of the PK, should be NOT NULL
		assertThat(sqlStatements).anyMatch(substring("PARTITION_ID INTEGER NOT NULL,"));
	}

	@Nonnull
	private static Predicate<String> regex(@Language("Regexp") String theRegex) {
		return s -> Pattern.compile(theRegex).matcher(s).find();
	}

	@Nonnull
	private static Predicate<String> substring(@Language("Regexp") String theSubstring) {
		return s -> s.contains(theSubstring);
	}

	private static void verifySequence(String fileName) throws IOException {
		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/" + fileName), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		assertThat(contents).as(fileName).contains("CREATE SEQUENCE");
	}
}
