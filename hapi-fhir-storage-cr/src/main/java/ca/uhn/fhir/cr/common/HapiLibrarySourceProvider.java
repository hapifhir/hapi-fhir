package ca.uhn.fhir.cr.common;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.cr.behavior.IDaoRegistryUser;
import ca.uhn.fhir.cr.utility.Libraries;
import ca.uhn.fhir.cr.utility.Searches;
import ca.uhn.fhir.cr.utility.Versions;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.cqframework.cql.cql2elm.LibraryContentType;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * This class provides an implementation of the cql-translator's LibrarySourceProvider
 * interface which is used for loading
 * library resources during CQL evaluation.
 */
public class HapiLibrarySourceProvider
	implements LibrarySourceProvider, IDaoRegistryUser {
	protected final DaoRegistry myDaoRegistry;
	protected final RequestDetails myRequestDetails;

	public HapiLibrarySourceProvider(DaoRegistry theDaoRegistry) {
		this(theDaoRegistry, null);
	}

	public HapiLibrarySourceProvider(DaoRegistry theDaoRegistry, RequestDetails theRequestDetails) {
		this.myDaoRegistry = theDaoRegistry;
		this.myRequestDetails = theRequestDetails;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.myDaoRegistry;
	}

	@Override
	public InputStream getLibraryContent(VersionedIdentifier theLibraryIdentifier,
													 LibraryContentType theLibraryContentType) {
		String name = theLibraryIdentifier.getId();
		String version = theLibraryIdentifier.getVersion();
		List<IBaseResource> libraries = search(getClass("Library"), Searches.byName(name), myRequestDetails)
			.getAllResources();
		IBaseResource library = Versions.selectByVersion(libraries, version,
			Libraries::getVersion);

		if (library == null) {
			return null;
		}
		byte[] content = Libraries.getContent(library, theLibraryContentType.mimeType());
		if (content == null) {
			return null;
		}

		return new ByteArrayInputStream(content);
	}

	@Override
	public InputStream getLibrarySource(VersionedIdentifier libraryIdentifier) {
		return this.getLibraryContent(libraryIdentifier, LibraryContentType.CQL);
	}
}
