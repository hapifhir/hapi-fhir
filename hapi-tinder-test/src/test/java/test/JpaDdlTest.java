package test;

import ca.uhn.fhir.tinder.ddl.GenerateDdlMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JpaDdlTest {

	@Test
	public void testGenerate() throws MojoExecutionException, MojoFailureException {
		/*
		 * Note, to execute this for real entities, add the following snippet to this module's POM. The whole project won't work with
		 * that added, but you can add it temporarily in order to debug this in IJ:
		 * 		<dependency>
		 * 			<groupId>ca.uhn.hapi.fhir</groupId>
		 * 			<artifactId>hapi-fhir-jpaserver-model</artifactId>
		 * 			<version>${project.version}</version>
		 * 		</dependency>
		 *
		 * Alternately, there is a unit test with fake entities that also runs this class.
		 */
		GenerateDdlMojo m = new GenerateDdlMojo();
		m.packageNames = List.of(
			"ca.uhn.fhir.jpa.entity",
			"ca.uhn.fhir.jpa.model.entity"
		);
		m.outputDirectory = "hapi-tinder-plugin/target";
		m.dialects = List.of(
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect", "h2.sql"),
			new GenerateDdlMojo.Dialect("ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect", "postgres.sql")
		);
		m.trimConditionalIdsFromPrimaryKeys = true;
		m.execute();
	}


}
