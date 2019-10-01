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

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
	private Set<Class<?>> myIgnoredForSearchDatatypes;

	public BaseSearchParamExtractor() {
		super();
	}

	// Used for testing
	protected BaseSearchParamExtractor(FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		myContext = theCtx;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	protected Set<Class<?>> getIgnoredForSearchDatatypes() {
		return myIgnoredForSearchDatatypes;
	}

	@Override
	public List<PathAndRef> extractResourceLinks(IBaseResource theResource, RuntimeSearchParam theNextSpDef) {
		List<PathAndRef> refs = new ArrayList<PathAndRef>();
		String[] nextPathsSplit = theNextSpDef.getPath().split("\\|");
		for (String nextPath : nextPathsSplit) {
			nextPath = nextPath.trim();
			for (Object nextObject : extractValues(nextPath, theResource)) {
				if (nextObject != null) {
					refs.add(new PathAndRef(nextPath, nextObject));
				}
			}
		}
		return refs;
	}

	/**
	 * Override parent because we're using FHIRPath here
	 */
	protected List<IBase> extractValues(String thePaths, IBaseResource theResource) {
		List<IBase> values = new ArrayList<>();
		if (isNotBlank(thePaths)) {
			String[] nextPathsSplit = SPLIT_R4.split(thePaths);
			for (String nextPath : nextPathsSplit) {
				List<? extends IBase> allValues;

				Supplier<List<? extends IBase>> allValuesFunc = getPathValueExtractor(theResource, nextPath);
				allValues = allValuesFunc.get();

				values.addAll(allValues);
			}

			for (int i = 0; i < values.size(); i++) {
				IBase nextObject = values.get(i);
				if (nextObject instanceof IBaseExtension) {
					IBaseExtension nextExtension = (IBaseExtension) nextObject;
					nextObject = nextExtension.getValue();
					values.set(i, nextObject);
				}
			}
		}
		return values;
	}

	protected abstract Supplier<List<? extends IBase>> getPathValueExtractor(IBaseResource theResource, String theNextPath);

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

	@PostConstruct
	public void start() {
		myIgnoredForSearchDatatypes = new HashSet<>();
		addIgnoredType(getContext(), "Age", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Annotation", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Attachment", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Count", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Distance", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Ratio", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "SampledData", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "Signature", myIgnoredForSearchDatatypes);
		addIgnoredType(getContext(), "LocationPositionComponent", myIgnoredForSearchDatatypes);
	}

	private static void addIgnoredType(FhirContext theCtx, String theType, Set<Class<?>> theIgnoredTypes) {
		BaseRuntimeElementDefinition<?> elementDefinition = theCtx.getElementDefinition(theType);
		if (elementDefinition != null) {
			theIgnoredTypes.add(elementDefinition.getImplementingClass());
		}
	}


}
