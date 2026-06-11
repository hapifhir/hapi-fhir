/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.implementationguide;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NpmPackageFactory {

	private static final Logger ourLog = LoggerFactory.getLogger(NpmPackageFactory.class);

	private final FhirContext myFhirContext;
	private final IParser myParser;
	private String myFhirVersion;
	private String myName = "test.fhir.ca.com";
	private String myVersion = "1.2.3";
	private final Map<String, Map<String, IBaseResource>> myResources = new HashMap<>();
	private final Map<String, String> myDependencies = new HashMap<>();

	public static NpmPackageFactory create(@Nonnull FhirContext theFhirContext) {
		return new NpmPackageFactory(theFhirContext);
	}

	public NpmPackageFactory(@Nonnull FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
		myParser = myFhirContext.newJsonParser();
		myFhirVersion = theFhirContext.getVersion().getVersion().getFhirVersionString();
	}

	public NpmPackageFactory name(String theName) {
		myName = theName;
		return this;
	}

	public NpmPackageFactory version(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	public NpmPackageFactory fhirVersion(String theFhirVersion) {
		myFhirVersion = theFhirVersion;
		return this;
	}

	public NpmPackageFactory addResource(String theFileName, IBaseResource theResource) {
		return addResourceToFolder("package", theFileName, theResource);
	}

	public NpmPackageFactory addResourceToFolder(
			String theFolder, String theFileName, IBaseResource theResource) {
		myResources.computeIfAbsent(theFolder, k -> new HashMap<>()).put(theFileName, theResource);
		return this;
	}

	public NpmPackageFactory addDependency(String thePackageName, String thePackageVersion) {
		myDependencies.put(thePackageName, thePackageVersion);
		return this;
	}

	public String getPackageName() {
		return myName;
	}

	public String getPackageVersion() {
		return myVersion;
	}

	/**
	 * Creates an in-memory NpmPackage from all added resources.
	 */
	public NpmPackage createPackage() {
		PackageGenerator manifest = new PackageGenerator();
		manifest.name(myName);
		manifest.version(myVersion);
		manifest.description("Test Implementation Guide");
		manifest.fhirVersions(List.of(myFhirVersion));

		myDependencies.forEach(manifest::dependency);

		NpmPackage pkg = NpmPackage.empty(manifest);

		myResources.forEach((folder, resources) -> resources.forEach((fileName, resource) -> {
			String json = myParser.encodeResourceToString(resource);
			String type = myFhirContext.getResourceType(resource);
			pkg.addFile(folder, fileName + ".json", json.getBytes(StandardCharsets.UTF_8), type);
		}));

		int resourceCount = myResources.values().stream().mapToInt(Map::size).sum();
		ourLog.debug("Added {} resources to package.", resourceCount);
		return pkg;
	}

	/**
	 * Creates the package as a byte array in tar.gz format.
	 */
	public byte[] createPackageBytes() throws IOException {
		return getAsBytes(createPackage());
	}

	/**
	 * Serializes the given NpmPackage as a byte array in tar.gz format.
	 */
	public static byte[] getAsBytes(NpmPackage theNpmPackage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		theNpmPackage.save(baos);
		return baos.toByteArray();
	}

	/**
	 * Writes the package as a tar.gz file into the given directory.
	 * @return the path to the written file
	 */
	public Path writeToDirectory(Path theDirectory) throws IOException {
		NpmPackage pkg = createPackage();
		Path igPath = theDirectory.resolve(pkg.name() + ".tgz");
		Files.write(igPath, getAsBytes(pkg));
		return igPath;
	}
}
