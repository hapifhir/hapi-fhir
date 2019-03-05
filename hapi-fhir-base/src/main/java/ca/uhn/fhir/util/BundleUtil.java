package ca.uhn.fhir.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.rest.api.RequestTypeEnum;

/**
 * Fetch resources from a bundle
 */
public class BundleUtil {

	/**
	 * @return Returns <code>null</code> if the link isn't found or has no value
	 */
	public static String getLinkUrlOfType(FhirContext theContext, IBaseBundle theBundle, String theLinkRelation) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("link");
		List<IBase> links = entryChild.getAccessor().getValues(theBundle);
		for (IBase nextLink : links) {

			boolean isRightRel = false;
			BaseRuntimeElementCompositeDefinition relDef = (BaseRuntimeElementCompositeDefinition) theContext.getElementDefinition(nextLink.getClass());
			BaseRuntimeChildDefinition relChild = relDef.getChildByName("relation");
			List<IBase> relValues = relChild.getAccessor().getValues(nextLink);
			for (IBase next : relValues) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>)next;
				if (theLinkRelation.equals(nextValue.getValueAsString())) {
					isRightRel = true;
				}
			}

			if (!isRightRel) {
				continue;
			}

			BaseRuntimeElementCompositeDefinition linkDef = (BaseRuntimeElementCompositeDefinition) theContext.getElementDefinition(nextLink.getClass());
			BaseRuntimeChildDefinition urlChild = linkDef.getChildByName("url");
			List<IBase> values = urlChild.getAccessor().getValues(nextLink);
			for (IBase nextUrl : values) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>)nextUrl;
				if (isNotBlank(nextValue.getValueAsString())) {
					return nextValue.getValueAsString();
				}
			}

		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Pair<String, IBaseResource>> getBundleEntryUrlsAndResources(FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildElem = (BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName("resource");
		
		BaseRuntimeChildDefinition requestChild = entryChildElem.getChildByName("request");
		BaseRuntimeElementCompositeDefinition<?> requestDef = (BaseRuntimeElementCompositeDefinition<?>) requestChild.getChildByName("request");
		
		BaseRuntimeChildDefinition urlChild = requestDef.getChildByName("url");

		List<Pair<String, IBaseResource>> retVal = new ArrayList<>(entries.size());
		for (IBase nextEntry : entries) {
			
			String url = null;
			IBaseResource resource = null;
			
			for (IBase nextEntryValue : requestChild.getAccessor().getValues(nextEntry)) {
				for (IBase nextUrlValue : urlChild.getAccessor().getValues(nextEntryValue)) {
					url = ((IPrimitiveType<String>)nextUrlValue).getValue();
				}
			}
			
			// Should return 0..1 only
			for (IBase nextValue : resourceChild.getAccessor().getValues(nextEntry)) {
				resource = (IBaseResource) nextValue;
			}
			
			retVal.add(Pair.of(url, resource));
		}
		
		return retVal;		
	}
	
	public static String getBundleType(FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("type");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);
		if (entries.size() > 0) {
			IPrimitiveType<?> typeElement = (IPrimitiveType<?>) entries.get(0);
			return typeElement.getValueAsString();
		}
		return null;
	}

	public static Integer getTotal(FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("total");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);
		if (entries.size() > 0) {
			IPrimitiveType<Number> typeElement = (IPrimitiveType<Number>) entries.get(0);
			if (typeElement != null && typeElement.getValue() != null) {
				return typeElement.getValue().intValue();
			}
		}
		return null;
	}

	/**
	 * Extract all of the resources from a given bundle
	 */
	public static List<BundleEntryParts> toListOfEntries(FhirContext theContext, IBaseBundle theBundle) {
		List<BundleEntryParts> retVal = new ArrayList<>();

		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildElem = (BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
		
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName("resource");
		BaseRuntimeChildDefinition requestChild = entryChildElem.getChildByName("request");
		BaseRuntimeElementCompositeDefinition<?>  requestElem = (BaseRuntimeElementCompositeDefinition<?>) requestChild.getChildByName("request");
		BaseRuntimeChildDefinition urlChild = requestElem.getChildByName("url");
		BaseRuntimeChildDefinition methodChild = requestElem.getChildByName("method");
		
		for (IBase nextEntry : entries) {
			IBaseResource resource = null;
			String url = null;
			RequestTypeEnum requestType = null;

			for (IBase next : resourceChild.getAccessor().getValues(nextEntry)) {
				resource = (IBaseResource) next;
			}
			for (IBase nextRequest : requestChild.getAccessor().getValues(nextEntry)) {
				for (IBase nextUrl : urlChild.getAccessor().getValues(nextRequest)) {
					url = ((IPrimitiveType<?>)nextUrl).getValueAsString();
				}
				for (IBase nextUrl : methodChild.getAccessor().getValues(nextRequest)) {
					String methodString = ((IPrimitiveType<?>)nextUrl).getValueAsString();
					if (isNotBlank(methodString)) {
						requestType = RequestTypeEnum.valueOf(methodString);
					}
				}
			}

			/* 
			 * All 3 might be null - That's ok because we still want to know the
			 * order in the original bundle.
			 */
			retVal.add(new BundleEntryParts(requestType, url, resource));
		}

		
		return retVal;
	}
	
	/**
	 * Extract all of the resources from a given bundle
	 */
	public static List<IBaseResource> toListOfResources(FhirContext theContext, IBaseBundle theBundle) {
		return toListOfResourcesOfType(theContext, theBundle, null);
	}

	/**
	 * Extract all of the resources of a given type from a given bundle 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IBaseResource> List<T> toListOfResourcesOfType(FhirContext theContext, IBaseBundle theBundle, Class<T> theTypeToInclude) {
		List<T> retVal = new ArrayList<>();

		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildElem = (BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName("resource");
		for (IBase nextEntry : entries) {
			for (IBase next : resourceChild.getAccessor().getValues(nextEntry)) {
				if (theTypeToInclude != null && !theTypeToInclude.isAssignableFrom(next.getClass())) {
					continue;
				}
				retVal.add((T) next);
			}
		}

		return retVal;
	}

	public static class BundleEntryParts
	{
		private final RequestTypeEnum myRequestType;
		private final IBaseResource myResource;
		private final String myUrl;
		BundleEntryParts(RequestTypeEnum theRequestType, String theUrl, IBaseResource theResource) {
			super();
			myRequestType = theRequestType;
			myUrl = theUrl;
			myResource = theResource;
		}
		public RequestTypeEnum getRequestType() {
			return myRequestType;
		}
		public IBaseResource getResource() {
			return myResource;
		}
		public String getUrl() {
			return myUrl;
		}
	}

}
