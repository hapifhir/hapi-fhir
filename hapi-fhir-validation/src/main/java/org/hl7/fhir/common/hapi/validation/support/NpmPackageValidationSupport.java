package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;

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
				loadResourcesFromPackage(pkg);
				loadBinariesFromPackage(pkg);
			}
		}
	}

	private void loadResourcesFromPackage(NpmPackage thePackage) {
		NpmPackage.NpmPackageFolder packageFolder = thePackage.getFolders().get("package");

		for (String nextFile : packageFolder.listFiles()) {
			if (nextFile.toLowerCase(Locale.US).endsWith(".json")) {
				String input = new String(packageFolder.getContent().get(nextFile), StandardCharsets.UTF_8);
				IParser parser = getFhirContext().newJsonParser();
				parser.setParserErrorHandler(new LenientErrorHandler(false));
				IBaseResource resource = parser.parseResource(input);
				super.addResource(resource);
			}
		}
	}

	private void loadBinariesFromPackage(NpmPackage thePackage) throws IOException {
		List<String> binaries = thePackage.list("other");
		for (String binaryName : binaries) {
			addBinary(TextFile.streamToBytes(thePackage.load("other", binaryName)), binaryName);
		}
	}
}
