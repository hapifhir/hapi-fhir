package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.cr.common.behavior.DaoRegistryUser;
import ca.uhn.fhir.cr.common.utility.Libraries;
import ca.uhn.fhir.cr.common.utility.Searches;
import ca.uhn.fhir.cr.common.utility.Versions;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.cqframework.cql.cql2elm.LibraryContentType;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.hl7.elm.r1.VersionedIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class JpaLibrarySourceProvider
		implements LibrarySourceProvider, DaoRegistryUser {
	protected final DaoRegistry daoRegistry;
	protected final RequestDetails requestDetails;

	public JpaLibrarySourceProvider(DaoRegistry daoRegistry) {
		this(daoRegistry, null);
	}

	public JpaLibrarySourceProvider(DaoRegistry daoRegistry, RequestDetails requestDetails) {
		this.daoRegistry = daoRegistry;
		this.requestDetails = requestDetails;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return this.daoRegistry;
	}

	@Override
	public InputStream getLibraryContent(VersionedIdentifier libraryIdentifier,
                                         LibraryContentType libraryContentType) {
		String name = libraryIdentifier.getId();
		String version = libraryIdentifier.getVersion();
		List<IBaseResource> libraries = search(getClass("Library"), Searches.byName(name), requestDetails)
				.getAllResources();
		IBaseResource library = Versions.selectByVersion(libraries, version,
				Libraries::getVersion);

		if (library == null) {
			return null;
		}
		byte[] content = Libraries.getContent(library, libraryContentType.mimeType());
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
