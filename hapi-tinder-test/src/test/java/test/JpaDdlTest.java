package test;

import ca.uhn.fhir.tinder.ddl.GenerateDdlMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

public class JpaDdlTest {

	/**
	 * This test uses the generate-ddl plugin to generate the schema. This can be
	 * used to debug issues in the conditional ID PK mapping work.
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testGenerate(boolean theTrimConditionalIdsFromPrimaryKeys) throws MojoExecutionException, MojoFailureException {
		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of(
			"ca.uhn.fhir.jpa.entity",
			"ca.uhn.fhir.jpa.model.entity"
		);
		m.outputDirectory = "hapi-tinder-test/target/schema";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect", "h2.sql"),
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.trimConditionalIdsFromPrimaryKeys = theTrimConditionalIdsFromPrimaryKeys;
		m.execute();

		// FIXME: add check
	}


}
