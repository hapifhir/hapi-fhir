package ca.uhn.fhir.cql.dstu3.provider;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.cql.common.provider.LibraryResolutionProvider;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Library;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class LibraryResolutionProviderImpl implements LibraryResolutionProvider<Library> {
	@Autowired
	private IFhirResourceDao<Library> myLibraryDao;

	// TODO: Figure out if we should throw an exception or something here.
	@Override
	public void update(Library library) {
		myLibraryDao.update(library);
	}

	@Override
	public Library resolveLibraryById(String libraryId, RequestDetails theRequestDetails) {
		try {
			return myLibraryDao.read(new IdType(libraryId), theRequestDetails);
		} catch (Exception e) {
			throw new IllegalArgumentException(Msg.code(1641) + String.format("Could not resolve library id %s", libraryId));
		}
	}

	@Override
	public Library resolveLibraryByCanonicalUrl(String url, RequestDetails theRequestDetails) {
		Objects.requireNonNull(url, "url must not be null");

		String[] parts = url.split("\\|");
		String resourceUrl = parts[0];
		String version = null;
		if (parts.length > 1) {
			version = parts[1];
		}

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("url", new UriParam(resourceUrl));
		if (version != null) {
			map.add("version", new TokenParam(version));
		}

		ca.uhn.fhir.rest.api.server.IBundleProvider bundleProvider = myLibraryDao.search(map, theRequestDetails);

		if (bundleProvider.size() == null || bundleProvider.size() == 0) {
			return null;
		}
		List<IBaseResource> resourceList = bundleProvider.getAllResources();
		return LibraryResolutionProvider.selectFromList(resolveLibraries(resourceList), version, x -> x.getVersion());
	}

	@Override
	public Library resolveLibraryByName(String libraryName, String libraryVersion) {
		Iterable<org.hl7.fhir.dstu3.model.Library> libraries = getLibrariesByName(libraryName);
		org.hl7.fhir.dstu3.model.Library library = LibraryResolutionProvider.selectFromList(libraries, libraryVersion,
			x -> x.getVersion());

		if (library == null) {
			throw new IllegalArgumentException(Msg.code(1642) + String.format("Could not resolve library name %s", libraryName));
		}

		return library;
	}

	private Iterable<org.hl7.fhir.dstu3.model.Library> getLibrariesByName(String name) {
		// Search for libraries by name
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("name", new StringParam(name, true));
		ca.uhn.fhir.rest.api.server.IBundleProvider bundleProvider = myLibraryDao.search(map, new SystemRequestDetails());

		if (bundleProvider.size() == null || bundleProvider.size() == 0) {
			return new ArrayList<>();
		}
		List<IBaseResource> resourceList = bundleProvider.getAllResources();
		return resolveLibraries(resourceList);
	}

	private Iterable<org.hl7.fhir.dstu3.model.Library> resolveLibraries(List<IBaseResource> resourceList) {
		List<org.hl7.fhir.dstu3.model.Library> ret = new ArrayList<>();
		for (IBaseResource res : resourceList) {
			Class<?> clazz = res.getClass();
			ret.add((org.hl7.fhir.dstu3.model.Library) clazz.cast(res));
		}
		return ret;
	}
}
