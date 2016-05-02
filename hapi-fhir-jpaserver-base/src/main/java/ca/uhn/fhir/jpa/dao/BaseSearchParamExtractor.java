package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.annotations.VisibleForTesting;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.util.FhirTerser;

public abstract class BaseSearchParamExtractor implements ISearchParamExtractor {
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSearchParamExtractor.class);
	protected static final Pattern SPLIT = Pattern.compile("\\||( or )");

	@Autowired
	private FhirContext myContext;
	
	public BaseSearchParamExtractor() {
		super();
	}
	
	public BaseSearchParamExtractor(FhirContext theCtx) {
		myContext = theCtx;
	}

	protected List<Object> extractValues(String thePaths, IBaseResource theResource) {
		List<Object> values = new ArrayList<Object>();
		String[] nextPathsSplit = SPLIT.split(thePaths);
		FhirTerser t = myContext.newTerser();
		for (String nextPath : nextPathsSplit) {
			String nextPathTrimmed = nextPath.trim();
			try {
				values.addAll(t.getValues(theResource, nextPathTrimmed));
			} catch (Exception e) {
				RuntimeResourceDefinition def = myContext.getResourceDefinition(theResource);
				ourLog.warn("Failed to index values from path[{}] in resource type[{}]: {}", new Object[] { nextPathTrimmed, def.getName(), e.toString(), e } );
			}
		}
		return values;
	}
	
	protected FhirContext getContext() {
		return myContext;
	}

	@VisibleForTesting
	void setContextForUnitTest(FhirContext theContext) {
		myContext = theContext;
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


}
