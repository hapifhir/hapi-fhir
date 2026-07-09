/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.packages.loader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parses FHIR resources from NPM packages during IG installation. Resources are read as UTF-8 JSON.
 */
public class PackageResourceParsingSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(PackageResourceParsingSvc.class);

	public static final String DEFAULT_PACKAGE_FOLDER = "package";
	private final FhirContext myFhirContext;

	public PackageResourceParsingSvc(FhirContext theContext) {
		myFhirContext = theContext;
	}

	/**
	 * Parses all resources from a single named folder, regardless of type. Logs a warning if the
	 * folder is not present in the package.
	 *
	 * @param theFolderName folder name within the NPM package (e.g. {@code "examples"})
	 * @param thePkg the NPM package to read from
	 * @return all parsed resources in the folder; empty list if the folder is not present
	 */
	public List<IBaseResource> parseResourcesFromFolder(String theFolderName, NpmPackage thePkg) {
		if (!thePkg.getFolders().containsKey(theFolderName)) {
			ourLog.warn("Resource folder '{}' not found in package", theFolderName);
			return Collections.emptyList();
		}
		try {
			Set<String> typesInFolder =
					thePkg.getFolders().get(theFolderName).getTypes().keySet();
			return parseResourcesOfTypesFromFolder(typesInFolder, theFolderName, thePkg);
		} catch (IOException e) {
			throw new InternalErrorException(
					Msg.code(2994) + "Cannot read type index for folder '" + theFolderName + "'", e);
		}
	}

	/**
	 * Parses all resources from each of the given folders, regardless of type.
	 * Delegates to {@link #parseResourcesFromFolder} for each folder.
	 *
	 * @param theFolderNames folder names within the NPM package
	 * @param thePkg the NPM package to read from
	 * @return all parsed resources across the named folders
	 */
	public List<IBaseResource> parseResourcesFromFolders(Set<String> theFolderNames, NpmPackage thePkg) {
		return theFolderNames.stream()
				.flatMap(folderName -> parseResourcesFromFolder(folderName, thePkg).stream())
				.toList();
	}

	/**
	 * Parses all resources of the given type from the standard {@code package/} folder.
	 *
	 * @param theType FHIR resource type name (e.g. {@code "StructureDefinition"})
	 * @param thePkg the NPM package to read from
	 * @return parsed resources; empty list if the type is not present in the package
	 */
	public List<IBaseResource> parseResourcesOfType(String theType, NpmPackage thePkg) {
		return parseResourcesOfType(theType, DEFAULT_PACKAGE_FOLDER, thePkg);
	}

	/**
	 * Parses all resources matching any of the given types from a named folder within the package.
	 *
	 * @param theTypes FHIR resource type names to include (e.g. {@code ["Patient", "Organization"]})
	 * @param thePackageFolder folder name within the NPM package (e.g. {@code "examples"})
	 * @param thePkg the NPM package to read from
	 * @return parsed resources across all requested types; empty list if none are present
	 */
	public List<IBaseResource> parseResourcesOfTypesFromFolder(
			Collection<String> theTypes, String thePackageFolder, NpmPackage thePkg) {
		List<IBaseResource> resources = new ArrayList<>();
		for (String type : theTypes) {
			resources.addAll(parseResourcesOfType(type, thePackageFolder, thePkg));
		}
		return resources;
	}

	/**
	 * Parses all resources of the given type from a named folder within the package.
	 *
	 * @param theType FHIR resource type name (e.g. {@code "Patient"})
	 * @param thePackageFolder folder name within the NPM package (e.g. {@code "examples"})
	 * @param thePkg the NPM package to read from
	 * @return parsed resources; empty list if the folder or type is not present
	 * @throws InternalErrorException if the package type index or a resource file cannot be read
	 */
	public List<IBaseResource> parseResourcesOfType(String theType, String thePackageFolder, NpmPackage thePkg) {
		if (!thePkg.getFolders().containsKey(thePackageFolder)) {
			return Collections.emptyList();
		}

		NpmPackage.NpmPackageFolder folder = thePkg.getFolders().get(thePackageFolder);
		Map<String, List<String>> packageFolderTypes;
		try {
			packageFolderTypes = folder.getTypes();
		} catch (IOException e) {
			throw new InternalErrorException(
					Msg.code(2370) + "Cannot install resource of type " + theType + ": Could not get types", e);
		}

		List<String> filesForType = packageFolderTypes.getOrDefault(theType, Collections.emptyList());
		if (filesForType.isEmpty()) {
			return Collections.emptyList();
		}

		IParser parser = myFhirContext.newJsonParser().setSuppressNarratives(true);
		List<IBaseResource> resources = new ArrayList<>();
		for (String file : filesForType) {
			try {
				byte[] content = folder.fetchFile(file);
				resources.add(parser.parseResource(new String(content, StandardCharsets.UTF_8)));
			} catch (IOException e) {
				throw new InternalErrorException(
						Msg.code(1289) + "Cannot install resource of type " + theType + ": Could not fetch file "
								+ file,
						e);
			}
		}
		return resources;
	}
}
