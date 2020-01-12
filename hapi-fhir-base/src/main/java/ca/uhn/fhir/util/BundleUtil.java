package ca.uhn.fhir.util;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.bundle.BundleEntryMutator;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.EntryListAccumulator;
import ca.uhn.fhir.util.bundle.ModifiableBundleEntry;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) next;
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
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) nextUrl;
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
					url = ((IPrimitiveType<String>) nextUrlValue).getValue();
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

	public static void setTotal(FhirContext theContext, IBaseBundle theBundle, Integer theTotal) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("total");
		IPrimitiveType<Integer> value = (IPrimitiveType<Integer>) entryChild.getChildByName("total").newInstance();
		value.setValue(theTotal);
		entryChild.getMutator().setValue(theBundle, value);
	}

	/**
	 * Extract all of the resources from a given bundle
	 */
	public static List<BundleEntryParts> toListOfEntries(FhirContext theContext, IBaseBundle theBundle) {
		EntryListAccumulator entryListAccumulator = new EntryListAccumulator();
		processEntries(theContext, theBundle, entryListAccumulator);
		return entryListAccumulator.getList();
	}


	public static void processEntries(FhirContext theContext, IBaseBundle theBundle, Consumer<ModifiableBundleEntry> theProcessor) {
		RuntimeResourceDefinition bundleDef = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChildDef = bundleDef.getChildByName("entry");
		List<IBase> entries = entryChildDef.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildContentsDef = (BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");

		BaseRuntimeChildDefinition fullUrlChildDef = entryChildContentsDef.getChildByName("fullUrl");

		BaseRuntimeChildDefinition resourceChildDef = entryChildContentsDef.getChildByName("resource");
		BaseRuntimeChildDefinition requestChildDef = entryChildContentsDef.getChildByName("request");
		BaseRuntimeElementCompositeDefinition<?> requestChildContentsDef = (BaseRuntimeElementCompositeDefinition<?>) requestChildDef.getChildByName("request");
		BaseRuntimeChildDefinition requestUrlChildDef = requestChildContentsDef.getChildByName("url");
		BaseRuntimeChildDefinition requestIfNoneExistChildDef = requestChildContentsDef.getChildByName("ifNoneExist");
		BaseRuntimeChildDefinition methodChildDef = requestChildContentsDef.getChildByName("method");

		for (IBase nextEntry : entries) {
			IBaseResource resource = null;
			String url = null;
			RequestTypeEnum requestType = null;
			String conditionalUrl = null;
			String fullUrl = fullUrlChildDef
				.getAccessor()
				.getFirstValueOrNull(nextEntry)
				.map(t->((IPrimitiveType<?>)t).getValueAsString())
				.orElse(null);

			for (IBase nextResource : resourceChildDef.getAccessor().getValues(nextEntry)) {
				resource = (IBaseResource) nextResource;
			}
			for (IBase nextRequest : requestChildDef.getAccessor().getValues(nextEntry)) {
				for (IBase nextUrl : requestUrlChildDef.getAccessor().getValues(nextRequest)) {
					url = ((IPrimitiveType<?>) nextUrl).getValueAsString();
				}
				for (IBase nextMethod : methodChildDef.getAccessor().getValues(nextRequest)) {
					String methodString = ((IPrimitiveType<?>) nextMethod).getValueAsString();
					if (isNotBlank(methodString)) {
						requestType = RequestTypeEnum.valueOf(methodString);
					}
				}

				if (requestType != null) {
					//noinspection EnumSwitchStatementWhichMissesCases
					switch (requestType) {
						case PUT:
							conditionalUrl = url != null && url.contains("?") ? url : null;
							break;
						case POST:
							List<IBase> ifNoneExistReps = requestIfNoneExistChildDef.getAccessor().getValues(nextRequest);
							if (ifNoneExistReps.size() > 0) {
								IPrimitiveType<?> ifNoneExist = (IPrimitiveType<?>) ifNoneExistReps.get(0);
								conditionalUrl = ifNoneExist.getValueAsString();
							}
							break;
					}
				}
			}

			/*
			 * All 3 might be null - That's ok because we still want to know the
			 * order in the original bundle.
			 */
			BundleEntryMutator mutator = new BundleEntryMutator(nextEntry, requestChildDef, requestChildContentsDef);
			ModifiableBundleEntry entry = new ModifiableBundleEntry(new BundleEntryParts(fullUrl, requestType, url, resource, conditionalUrl), mutator);
			theProcessor.accept(entry);
		}
	}

	/**
	 * Extract all of the resources from a given bundle
	 */
	public static List<IBaseResource> toListOfResources(FhirContext theContext, IBaseBundle theBundle) {
		return toListOfResourcesOfType(theContext, theBundle, IBaseResource.class);
	}

	/**
	 * Extract all of the resources of a given type from a given bundle
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IBaseResource> List<T> toListOfResourcesOfType(FhirContext theContext, IBaseBundle theBundle, Class<T> theTypeToInclude) {
		Objects.requireNonNull(theTypeToInclude, "ResourceType must not be null");
		List<T> retVal = new ArrayList<>();

		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildElem = (BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName("resource");
		for (IBase nextEntry : entries) {
			for (IBase next : resourceChild.getAccessor().getValues(nextEntry)) {
				if (theTypeToInclude.isAssignableFrom(next.getClass())) {
					retVal.add((T) next);
				}
			}
		}
		return retVal;
	}

	/**
	 * DSTU3 did not allow the PATCH verb for transaction bundles- so instead we infer that a bundle
	 * is a patch if the payload is a binary resource containing a patch. This method
	 * tests whether a resource (which should have come from
	 * <code>Bundle.entry.resource</code> is a Binary resource with a patch
	 * payload type.
	 */
	public static boolean isDstu3TransactionPatch(IBaseResource thePayloadResource) {
		boolean isPatch = false;
		if (thePayloadResource instanceof IBaseBinary) {
			String contentType = ((IBaseBinary) thePayloadResource).getContentType();
			 try {
				 PatchTypeEnum.forContentTypeOrThrowInvalidRequestException(contentType);
				 isPatch = true;
			 } catch (InvalidRequestException e) {
				 // ignore
			 }
		}
		return isPatch;
	}
}
