/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.cqframework.cql.cql2elm.model.CompiledLibrary;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.fhir.utility.Reflections;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class listens for changes to Library resources and invalidates the CodeCache. The CodeCache is used in CQL evaluatuon to speed up the measure operations. If underlying values change in the library then cache requires updating.
 **/
public class ElmCacheResourceChangeListener implements IResourceChangeListener {

	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(ElmCacheResourceChangeListener.class);

	private final IFhirResourceDao<?> myLibraryDao;
	private final Map<VersionedIdentifier, CompiledLibrary> myGlobalLibraryCache;
	private final Function<IBaseResource, String> myNameFunction;
	private final Function<IBaseResource, String> myVersionFunction;

	public ElmCacheResourceChangeListener(
			DaoRegistry theDaoRegistry, Map<VersionedIdentifier, CompiledLibrary> theGlobalLibraryCache) {
		this.myLibraryDao = theDaoRegistry.getResourceDao("Library");
		this.myGlobalLibraryCache = theGlobalLibraryCache;
		this.myNameFunction = Reflections.getNameFunction(myLibraryDao.getResourceType());
		this.myVersionFunction = Reflections.getVersionFunction(myLibraryDao.getResourceType());
	}

	@Override
	public void handleInit(Collection<IIdType> theResourceIds) {
		// Intentionally empty. Only cache ELM on eval request
	}

	@Override
	public void handleChange(IResourceChangeEvent theResourceChangeEvent) {
		if (theResourceChangeEvent == null) {
			return;
		}

		this.invalidateCacheByIds(theResourceChangeEvent.getDeletedResourceIds());
		this.invalidateCacheByIds(theResourceChangeEvent.getUpdatedResourceIds());
	}

	private void invalidateCacheByIds(List<IIdType> theIds) {
		if (theIds == null) {
			return;
		}

		for (IIdType id : theIds) {
			this.invalidateCacheById(id);
		}
	}

	private void invalidateCacheById(IIdType theId) {
		if (!theId.getResourceType().equals("Library")) {
			return;
		}

		IBaseResource library;
		try {
			library = this.myLibraryDao.read(theId);
		} catch (ResourceGoneException | ResourceNotFoundException e) {
			// TODO: This needs to be smarter... the issue is that ELM is cached with
			// library name and version as the key since
			// that's the access path the CQL engine uses, but change notifications occur
			// with the resource Id, which is not
			// necessarily tied to the resource name. In any event, if a unknown resource is
			// deleted, clear all libraries as a workaround.
			// One option is to maintain a cache with multiple indices.
			ourLog.debug(
					"Failed to locate resource {} to look up name and version. Clearing all libraries from cache.",
					theId.getValueAsString());
			this.myGlobalLibraryCache.clear();
			return;
		}

		String name = this.myNameFunction.apply(library);
		String version = this.myVersionFunction.apply(library);

		this.myGlobalLibraryCache.remove(new VersionedIdentifier().withId(name).withVersion(version));
	}
}
