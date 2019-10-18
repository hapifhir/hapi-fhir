package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.StringNormalizer;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseSearchParamExtractor implements ISearchParamExtractor {

	public static final Pattern SPLIT = Pattern.compile("\\||( or )");
	public static final Pattern SPLIT_R4 = Pattern.compile("\\|");
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSearchParamExtractor.class);
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private ModelConfig myModelConfig;

	public BaseSearchParamExtractor() {
		super();
	}

	// Used for testing
	protected BaseSearchParamExtractor(FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		myContext = theCtx;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	protected void addSearchTerm(ResourceTable theEntity, Set<ResourceIndexedSearchParamString> retVal, String resourceName, String searchTerm) {
		if (isBlank(searchTerm)) {
			return;
		}
		retVal.add(createResourceIndexedSearchParamString(theEntity, resourceName, searchTerm));
	}

	protected void addStringParam(ResourceTable theEntity, Set<BaseResourceIndexedSearchParam> retVal, RuntimeSearchParam nextSpDef, String value) {
		retVal.add(createResourceIndexedSearchParamString(theEntity, nextSpDef.getName(), value));
	}

	@Override
	public List<PathAndRef> extractResourceLinks(IBaseResource theResource, RuntimeSearchParam theNextSpDef) {
		List<PathAndRef> refs = new ArrayList<PathAndRef>();
		String[] nextPathsSplit = theNextSpDef.getPath().split("\\|");
		for (String nextPath : nextPathsSplit) {
			nextPath = nextPath.trim();
			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject == null) {
					continue;
				}
				refs.add(new PathAndRef(nextPath, nextObject));
			}
		}
		return refs;
	}

	protected abstract List<Object> extractValues(String thePaths, IBaseResource theResource);

	protected FhirContext getContext() {
		return myContext;
	}

	protected ModelConfig getModelConfig() {
		return myModelConfig;
	}

	public Collection<RuntimeSearchParam> getSearchParams(IBaseResource theResource) {
		RuntimeResourceDefinition def = getContext().getResourceDefinition(theResource);
		Collection<RuntimeSearchParam> retVal = mySearchParamRegistry.getActiveSearchParams(def.getName()).values();
		List<RuntimeSearchParam> defaultList = Collections.emptyList();
		retVal = ObjectUtils.defaultIfNull(retVal, defaultList);
		return retVal;
	}

	@VisibleForTesting
	void setContextForUnitTest(FhirContext theContext) {
		myContext = theContext;
	}

	private ResourceIndexedSearchParamString createResourceIndexedSearchParamString(ResourceTable theEntity, String name, String value) {
		if (value.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			value = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		}
		String normalizedValue = StringNormalizer.normalizeString(value);
		if (normalizedValue.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			normalizedValue = normalizedValue.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		}
		ResourceIndexedSearchParamString nextEntity = new ResourceIndexedSearchParamString(getModelConfig(), name, normalizedValue, value);
		nextEntity.setResource(theEntity);
		return nextEntity;
	}

}
