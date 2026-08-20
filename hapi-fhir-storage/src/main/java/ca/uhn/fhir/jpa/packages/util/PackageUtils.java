/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.packages.util;

import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import com.google.common.collect.Lists;
import org.hl7.fhir.utilities.json.model.JsonElement;
import org.hl7.fhir.utilities.json.model.JsonObject;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.util.Collections;
import java.util.List;

public class PackageUtils {

	public static final String LOADER_WITH_CACHE = "loaderWithCache";

	public record DependentPackage(String name, String version) {}

	/**
	 * Default install types
	 */
	public static final List<String> DEFAULT_INSTALL_TYPES = Collections.unmodifiableList(Lists.newArrayList(
			"NamingSystem",
			"CodeSystem",
			"ValueSet",
			"StructureDefinition",
			"ConceptMap",
			"SearchParameter",
			"Subscription"));

	public static List<DependentPackage> extractDependentPackages(
			NpmPackage theNpmPackage,
			PackageInstallationSpec theInstallationSpec,
			PackageInstallOutcomeJson theOutcome) {
		List<DependentPackage> retVal = Lists.newArrayList();
		JsonElement dependenciesElement = theNpmPackage.getNpm().get("dependencies");

		if (dependenciesElement == null) {
			return retVal;
		}

		JsonObject dependenciesObject = dependenciesElement.asJsonObject();
		for (String id : dependenciesObject.getNames()) {
			String ver = dependenciesObject.getJsonString(id).asString();
			theOutcome
					.getMessage()
					.add("Package " + theNpmPackage.id() + "#" + theNpmPackage.version() + " depends on package " + id
							+ "#" + ver);

			boolean include = true;
			for (String next : theInstallationSpec.getDependencyExcludes()) {
				if (id.matches(next)) {
					theOutcome
							.getMessage()
							.add("Not installing dependency " + id + " because it matches exclude criteria: " + next);
					include = false;
					break;
				}
			}

			if (include) {
				retVal.add(new DependentPackage(id, ver));
			}
		}

		return retVal;
	}
}
