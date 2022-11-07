package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.cr.common.utility.Reflections;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import org.cqframework.cql.elm.execution.VersionedIdentifier;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.cql.engine.runtime.Code;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CodeCacheResourceChangeListener implements IResourceChangeListener {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory
			.getLogger(CodeCacheResourceChangeListener.class);

	private final IFhirResourceDao<?> myValueSetDao;
	private final Map<VersionedIdentifier, List<Code>> myGlobalCodeCache;
	private final Function<IBaseResource, String> myUrlFunction;
	private final Function<IBaseResource, String> myVersionFunction;

	public CodeCacheResourceChangeListener(DaoRegistry theDaoRegistry,
			Map<VersionedIdentifier, List<Code>> theGlobalCodeCache) {
		this.myValueSetDao = theDaoRegistry.getResourceDao("ValueSet");
		this.myGlobalCodeCache = theGlobalCodeCache;
		this.myUrlFunction = Reflections.getUrlFunction(myValueSetDao.getResourceType());
		this.myVersionFunction = Reflections.getVersionFunction(myValueSetDao.getResourceType());
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
		if (!theId.getResourceType().equals("ValueSet")) {
			return;
		}

		try {
			IBaseResource valueSet = this.myValueSetDao.read(theId);

			String url = this.myUrlFunction.apply(valueSet);
			String version = this.myVersionFunction.apply(valueSet);

			this.myGlobalCodeCache.remove(new VersionedIdentifier().withId(url)
					.withVersion(version));
		}
		// This happens when a Library is deleted entirely so it's impossible to look up
		// name and version.
		catch (Exception e) {
			ourLog.debug("Failed to locate resource {} to look up url and version. Clearing all codes from cache.",
					theId.getValueAsString());
			this.myGlobalCodeCache.clear();
		}
	}
}
