package ca.uhn.fhir.cql.r4.listener;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Quality Language
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;

public class ElmCacheResourceChangeListener implements IResourceChangeListener {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ElmCacheResourceChangeListener.class);

	private IFhirResourceDao<org.hl7.fhir.r4.model.Library> libraryDao;
	private Map<VersionedIdentifier, Library> globalLibraryCache;

	public ElmCacheResourceChangeListener(IFhirResourceDao<org.hl7.fhir.r4.model.Library> libraryDao, Map<VersionedIdentifier, Library> globalLibraryCache) {
		this.libraryDao = libraryDao;
		this.globalLibraryCache = globalLibraryCache;
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
		if (!theId.getResourceType().equals("Library"))  {
			return;
		}

		try {
			org.hl7.fhir.r4.model.Library library = this.libraryDao.read(theId);

			this.globalLibraryCache.remove(new VersionedIdentifier().withId(library.getName()).withVersion(library.getVersion()));
		}
		// This happens when a Library is deleted entirely so it's impossible to look up name and version.
		catch (Exception e) {
			// TODO: This needs to be smarter... the issue is that ELM is cached with library name and version as the key since
			// that's the access path the CQL engine uses, but change notifications occur with the resource Id, which is not
			// necessarily tied to the resource name. In any event, if a unknown resource is deleted, clear all libraries as a workaround.
			// One option is to maintain a cache with multiple indices.
			ourLog.debug("Failed to locate resource {} to look up name and version. Clearing all libraries from cache.", theId.getValueAsString());
			this.globalLibraryCache.clear();
		}
	}
}
