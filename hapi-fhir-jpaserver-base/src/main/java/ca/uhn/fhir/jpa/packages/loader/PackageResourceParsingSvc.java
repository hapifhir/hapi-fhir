/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageResourceParsingSvc {

	private final FhirContext myFhirContext;

	public PackageResourceParsingSvc(FhirContext theContext) {
		myFhirContext = theContext;
	}

	/**
	 * Parses out resource of theType from provided package
	 * @param theType - the resource type
	 * @param thePkg - the npm package
	 * @return - a list of all resources that match type theType in package thePkg
	 */
	public List<IBaseResource> parseResourcesOfType(String theType, NpmPackage thePkg) {
		if (!thePkg.getFolders().containsKey("package")) {
			return Collections.emptyList();
		}
		ArrayList<IBaseResource> resources = new ArrayList<>();
		List<String> filesForType = null;
		try {
			filesForType = thePkg.getFolders().get("package").getTypes().get(theType);
		} catch (IOException e) {
			throw new InternalErrorException(
					Msg.code(2370) + "Cannot install resource of type " + theType + ": Could not get types", e);
		}
		if (filesForType != null) {
			for (String file : filesForType) {
				try {
					byte[] content = thePkg.getFolders().get("package").fetchFile(file);
					resources.add(myFhirContext.newJsonParser().parseResource(new String(content)));
				} catch (IOException e) {
					throw new InternalErrorException(
							Msg.code(1289) + "Cannot install resource of type " + theType + ": Could not fetch file "
									+ file,
							e);
				}
			}
		}
		return resources;
	}
}
