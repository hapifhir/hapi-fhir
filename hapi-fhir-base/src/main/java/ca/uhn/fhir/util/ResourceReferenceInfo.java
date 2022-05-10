package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.model.api.Include;

/**
 * Created by Bill de Beaubien on 2/26/2015.
 */
public class ResourceReferenceInfo {
	private String myOwningResource;
	private String myName;
	private IBaseReference myResource;
	private FhirContext myContext;

	public ResourceReferenceInfo(FhirContext theContext, IBaseResource theOwningResource, List<String> thePathToElement, IBaseReference theElement) {
		myContext = theContext;
		myOwningResource = theContext.getResourceType(theOwningResource);

		myResource = theElement;
		if (thePathToElement != null && !thePathToElement.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (Iterator<String> iterator = thePathToElement.iterator(); iterator.hasNext();) {
				sb.append(iterator.next());
				if (iterator.hasNext())
					sb.append(".");
			}
			myName = sb.toString();
		} else {
			myName = null;
		}
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("name", myName);
		b.append("resource", myResource.getReferenceElement());
		return b.build();
	}

	public String getName() {
		return myName;
	}

	public IBaseReference getResourceReference() {
		return myResource;
	}

	public boolean matchesIncludeSet(Set<Include> theIncludes) {
		if (theIncludes == null)
			return false;
		for (Include include : theIncludes) {
			if (matchesInclude(include))
				return true;
		}
		return false;
	}

	public boolean matchesInclude(Include theInclude) {
		if (theInclude.getValue().equals("*")) {
			return true;
		}
		int colonIndex = theInclude.getValue().indexOf(':');
		if (colonIndex != -1) {
			// DSTU2+ style
			String resourceName = theInclude.getValue().substring(0, colonIndex);
			String paramName = theInclude.getValue().substring(colonIndex + 1);
			RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(resourceName);
			if (resourceDef != null) {
				RuntimeSearchParam searchParamDef = resourceDef.getSearchParam(paramName);
				if (searchParamDef!=null) {
					final String completeName = myOwningResource + "." + myName;
					boolean matched = false;
					for (String s : searchParamDef.getPathsSplit()) {
						if (s.equals(completeName) ||
								       s.startsWith(completeName + ".")) {
							matched = true; break;
						}
					}
					return matched;
				}
			}
			return false;
		}
		// DSTU1 style
		return (theInclude.getValue().equals(myOwningResource + '.' + myName));
	}
}
