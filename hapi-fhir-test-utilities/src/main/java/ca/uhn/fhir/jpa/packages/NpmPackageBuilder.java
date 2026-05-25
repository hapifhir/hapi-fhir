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
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.utilities.json.model.JsonObject;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for creating small NPM packages for use in tests.
 */
public class NpmPackageBuilder {

	private record Dependency(String theName, String theVersion) {}

	private final String myName;
	private final String myVersion;
	private String myDescription;
	private FhirVersionEnum myFhirVersion;

	private final List<Dependency> myDependencies = new ArrayList<>();

	public NpmPackageBuilder(String theName, String theVersion) {
		myName = theName;
		myVersion = theVersion;
	}

	public NpmPackageBuilder withDescription(String theDescription) {
		myDescription = theDescription;
		return this;
	}

	public NpmPackageBuilder withFhirVersion(FhirVersionEnum theFhirVersion) {
		myFhirVersion = theFhirVersion;
		return this;
	}

	public NpmPackageBuilder withDependency(String theUrl, String theVersion) {
		Dependency dependency = new Dependency(theUrl, theVersion);
		myDependencies.add(dependency);
		return this;
	}

	public NpmPackage build() {
		JsonObject npmObject = new JsonObject();
		npmObject.set("name", myName);
		npmObject.set("version", myVersion);
		npmObject.set("description", myDescription);

		if (myFhirVersion != null) {
			npmObject.add("fhirVersions", List.of(myFhirVersion.getFhirVersionString()));
		}

		if (!myDependencies.isEmpty()) {
			JsonObject dependenciesObject = new JsonObject();
			for (Dependency dependency : myDependencies) {
				dependenciesObject.add(dependency.theName, dependency.theVersion);
			}
			npmObject.add("dependencies", dependenciesObject);
		}

		NpmPackage cachePackage = NpmPackage.empty();
		cachePackage.setNpm(npmObject);

		return cachePackage;
	}

	public byte[] buildAsByteArray() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		build().save(byteStream);
		return byteStream.toByteArray();
	}
}
