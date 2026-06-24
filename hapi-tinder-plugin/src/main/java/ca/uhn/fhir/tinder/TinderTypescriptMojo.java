package ca.uhn.fhir.tinder;

// Created by Claude Opus 4.8

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.tinder.ts.TsModel;
import ca.uhn.fhir.tinder.ts.TypescriptModelExtractor;
import ca.uhn.fhir.tinder.ts.TypescriptWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Generates TypeScript model interfaces for a single FHIR version by introspecting HAPI's runtime
 * model. The output mirrors how the Java structures are generated, but emits {@code .ts} files instead
 * of {@code .java}. This drives publication of the {@code @smile-cdr/fhirts} npm package.
 */
@Mojo(name = "generate-typescript", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class TinderTypescriptMojo extends AbstractMojo {

	private static final Logger ourLog = LoggerFactory.getLogger(TinderTypescriptMojo.class);

	/**
	 * The FHIR version to generate. One of: dstu2, dstu3, r4, r4b, r5.
	 */
	@Parameter(required = true)
	private String version;

	/**
	 * The directory into which the generated {@code .ts} files will be written.
	 */
	@Parameter(required = true)
	private File targetDirectory;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		FhirContext context = createContext(version);

		ourLog.info("Generating TypeScript models for FHIR {} into {}", version, targetDirectory.getAbsolutePath());

		TsModel model = new TypescriptModelExtractor(context).extract();
		try {
			new TypescriptWriter().writeModel(model, targetDirectory);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to write TypeScript models", e);
		}
	}

	private static FhirContext createContext(String theVersion) throws MojoFailureException {
		switch (theVersion.toLowerCase()) {
			case "dstu2":
				return FhirContext.forDstu2();
			case "dstu3":
				return FhirContext.forDstu3();
			case "r4":
				return FhirContext.forR4();
			case "r4b":
				return FhirContext.forR4B();
			case "r5":
				return FhirContext.forR5();
			default:
				throw new MojoFailureException(Msg.code(2985) + "Unknown FHIR version configured: " + theVersion);
		}
	}
}
