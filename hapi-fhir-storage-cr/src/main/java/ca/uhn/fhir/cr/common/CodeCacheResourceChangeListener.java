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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.fhir.utility.Reflections;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class listens for changes to ValueSet resources and invalidates the CodeCache. The CodeCache is used in CQL evaluation to speed up terminology operations. If ValueSet changes, it's possible that the constituent codes change and therefore the cache needs to be updated.
 **/
public class CodeCacheResourceChangeListener implements IResourceChangeListener {

	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(CodeCacheResourceChangeListener.class);

	private final IFhirResourceDao<?> myValueSetDao;
	private final Map<String, List<Code>> myGlobalValueSetCache;
	private final Function<IBaseResource, String> myUrlFunction;
	private final Function<IBaseResource, String> myVersionFunction;

	public CodeCacheResourceChangeListener(DaoRegistry theDaoRegistry, Map<String, List<Code>> theGlobalValueSetCache) {
		this.myValueSetDao = theDaoRegistry.getResourceDao("ValueSet");
		this.myGlobalValueSetCache = theGlobalValueSetCache;
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

		IBaseResource valueSet;
		try {
			valueSet = this.myValueSetDao.read(theId.toUnqualifiedVersionless());
		}
		// This happens when a Library is deleted entirely, so it's impossible to look up
		// name and version.
		catch (ResourceGoneException | ResourceNotFoundException e) {
			ourLog.debug(
					"Failed to locate resource {} to look up url and version. Clearing all codes from cache.",
					theId.getValueAsString());
			myGlobalValueSetCache.clear();
			return;
		}

		String url = this.myUrlFunction.apply(valueSet);

		var valuesets = myGlobalValueSetCache.keySet();

		for (String key : valuesets) {
			var urlKey = key;
			if (urlKey.contains(url)) {
				myGlobalValueSetCache.remove(key);
				ourLog.warn("Successfully removed valueSet from ValueSetCache: " + url + " due to updated resource");
			}
		}
	}
}
