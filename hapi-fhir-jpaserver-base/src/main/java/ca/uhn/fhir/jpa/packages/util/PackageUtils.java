/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.packages.NpmPackageMetadataLiteJson;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class PackageUtils {

	public static final String LOADER_WITH_CACHE = "loaderWithCache";

	/**
	 * Default install types
	 */
	public static List<String> DEFAULT_INSTALL_TYPES = Collections.unmodifiableList(Lists.newArrayList(
			"NamingSystem",
			"CodeSystem",
			"ValueSet",
			"StructureDefinition",
			"ConceptMap",
			"SearchParameter",
			"Subscription"));

	public static final String PKG_METADATA_KEY = "PKG_METADATA";

	/**
	 * Adds package metadata to the resource for identification purposes downstream.
	 * @param theResource the resource
	 * @param thePkg the npm package (IG) it is from
	 */
	public static void addPackageMetadata(IBaseResource theResource, NpmPackage thePkg) {
		NpmPackageMetadataLiteJson metadata = new NpmPackageMetadataLiteJson();
		metadata.setName(thePkg.name());
		metadata.setVersion(thePkg.version());

		theResource.setUserData(PKG_METADATA_KEY, JsonUtil.serialize(metadata));
	}

	/**
	 * Retrieves whatever npm pkg metadata is on this resource, or null if none is found.
	 * @param theResource resource that originated from an npm package
	 * @return the metadata about the pkg (or null if not available)
	 */
	public static NpmPackageMetadataLiteJson getPackageMetadata(IBaseResource theResource) {
		String metadataJson = (String) theResource.getUserData(PKG_METADATA_KEY);
		if (isBlank(metadataJson)) {
			// metadata was not on this resource
			return null;
		}
		return JsonUtil.deserialize(metadataJson, NpmPackageMetadataLiteJson.class);
	}
}
