package ca.uhn.fhir.tinder.ddl;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateDdlMojoTest {

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

	private static void verifySequence(String fileName) throws IOException {
		String contents = FileUtils.readFileToString(new File("target/generate-ddl-plugin-test/" + fileName), StandardCharsets.UTF_8).toUpperCase(Locale.ROOT);
		assertThat(contents).as(fileName).contains("CREATE SEQUENCE");
	}


}
