package ca.uhn.fhir.cql.dstu3.listener;

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

	private IFhirResourceDao<org.hl7.fhir.dstu3.model.Library> libraryDao;
	private Map<VersionedIdentifier, Library> globalLibraryCache;

	public ElmCacheResourceChangeListener(IFhirResourceDao<org.hl7.fhir.dstu3.model.Library> libraryDao, Map<VersionedIdentifier, Library> globalLibraryCache) {
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
			org.hl7.fhir.dstu3.model.Library library = this.libraryDao.read(theId);

			this.globalLibraryCache.remove(new VersionedIdentifier().withId(library.getName()).withVersion(library.getVersion()));
		}
		catch (Exception e) {
			// TODO: This needs to be smarter...
			// Couldn't locate the library since it was deleted. Clear the cache entirely.
			this.globalLibraryCache.clear();
		}
	}
}
