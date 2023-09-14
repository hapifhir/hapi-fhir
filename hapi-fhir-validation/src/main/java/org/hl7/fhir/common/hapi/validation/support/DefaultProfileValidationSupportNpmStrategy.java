package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import javax.annotation.Nonnull;

public class DefaultProfileValidationSupportNpmStrategy extends NpmPackageValidationSupport {
	private static final Logger ourLog = LoggerFactory.getLogger(DefaultProfileValidationSupportNpmStrategy.class);

	/**
	 * Constructor
	 */
	public DefaultProfileValidationSupportNpmStrategy(@Nonnull FhirContext theFhirContext) {
		super(theFhirContext);

		Validate.isTrue(theFhirContext.getVersion().getVersion() == FhirVersionEnum.R5);

		ourLog.info("Loading R5 Core+Extension packages into memory");
		StopWatch sw = new StopWatch();

		try {
			loadPackageFromClasspath("org/hl7/fhir/r5/packages/hl7.fhir.r5.core-5.0.0.tgz");
			loadPackageFromClasspath("org/hl7/fhir/r5/packages/hl7.fhir.uv.extensions.r5-1.0.0.tgz");
			loadPackageFromClasspath("org/hl7/fhir/r5/packages/hl7.terminology-5.1.0.tgz");
		} catch (IOException e) {
			throw new ConfigurationException(
					Msg.code(2333)
							+ "Failed to load required validation resources. Make sure that the appropriate hapi-fhir-validation-resources-VER JAR is on the classpath",
					e);
		}

		ourLog.info("Loaded {} Core+Extension resources in {}", countAll(), sw);
	}
}
