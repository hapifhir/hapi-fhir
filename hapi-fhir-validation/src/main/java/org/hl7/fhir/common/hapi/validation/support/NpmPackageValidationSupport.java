package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.npm.NpmPackage;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * This interceptor loads and parses FHIR NPM Conformance Packages, and makes the
 * artifacts foudn within them available to the FHIR validator.
 *
 * @since 5.5.0
 */
public class NpmPackageValidationSupport extends PrePopulatedValidationSupport {

	/**
	 * Constructor
	 */
	public NpmPackageValidationSupport(@Nonnull FhirContext theFhirContext) {
		super(theFhirContext);
	}

	/**
	 * Load an NPM package using a classpath specification, e.g. <code>/path/to/resource/my_package.tgz</code>. The
	 * classpath spec can optionally be prefixed with the string <code>classpath:</code>
	 *
	 * @throws InternalErrorException If the classpath file can't be found
	 */
	public void loadPackageFromClasspath(String theClasspath) throws IOException {
		try (InputStream is = ClasspathUtil.loadResourceAsStream(theClasspath)) {
			NpmPackage pkg = NpmPackage.fromPackage(is);
			if (pkg.getFolders().containsKey("package")) {
				NpmPackage.NpmPackageFolder packageFolder = pkg.getFolders().get("package");

				for (String nextFile : packageFolder.listFiles()) {
					if (nextFile.toLowerCase(Locale.US).endsWith(".json")) {
						String input = new String(packageFolder.getContent().get(nextFile), StandardCharsets.UTF_8);
						IBaseResource resource = getFhirContext().newJsonParser().parseResource(input);
						super.addResource(resource);
					}
				}

			}
		}
	}

}
