package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.bundle.BundleEntryMutator;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.EntryListAccumulator;
import ca.uhn.fhir.util.bundle.ModifiableBundleEntry;
import ca.uhn.fhir.util.bundle.SearchBundleEntryParts;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
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

/**
 * Fetch resources from a bundle
 */
public class BundleUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BundleUtil.class);


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

	public static void setBundleType(FhirContext theContext, IBaseBundle theBundle, String theType) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("type");
		BaseRuntimeElementDefinition<?> element = entryChild.getChildByName("type");
		IPrimitiveType<?> typeInstance = (IPrimitiveType<?>) element.newInstance(entryChild.getInstanceConstructorArguments());
		typeInstance.setValueAsString(theType);

		entryChild.getMutator().setValue(theBundle, typeInstance);
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

	static int WHITE = 1;
	static int GRAY = 2;
	static int BLACK = 3;

	/**
	 * Function which will do an in-place sort of a bundles' entries, to the correct processing order, which is:
	 * 1. Deletes
	 * 2. Creates
	 * 3. Updates
	 *
	 * Furthermore, within these operation types, the entries will be sorted based on the order in which they should be processed
	 * e.g. if you have 2 CREATEs, one for a Patient, and one for an Observation which has this Patient as its Subject,
	 * the patient will come first, then the observation.
	 *
	 * In cases of there being a cyclic dependency (e.g. Organization/1 is partOf Organization/2 and Organization/2 is partOf Organization/1)
	 * this function will throw an IllegalStateException.
	 *
	 * @param theContext The FhirContext.
	 * @param theBundle The {@link IBaseBundle} which contains the entries you would like sorted into processing order.
	 */
	public static void sortEntriesIntoProcessingOrder(FhirContext theContext, IBaseBundle theBundle) throws IllegalStateException {
		Map<BundleEntryParts, IBase> partsToIBaseMap = getPartsToIBaseMap(theContext, theBundle);
		LinkedHashSet<IBase> retVal = new LinkedHashSet<>();

		//Get all deletions.
		LinkedHashSet<IBase> deleteParts = sortEntriesOfTypeIntoProcessingOrder(theContext, RequestTypeEnum.DELETE, partsToIBaseMap);
		validatePartsNotNull(deleteParts);
		retVal.addAll(deleteParts);

		//Get all Creations
		LinkedHashSet<IBase> createParts= sortEntriesOfTypeIntoProcessingOrder(theContext, RequestTypeEnum.POST, partsToIBaseMap);
		validatePartsNotNull(createParts);
		retVal.addAll(createParts);

		// Get all Updates
		LinkedHashSet<IBase> updateParts= sortEntriesOfTypeIntoProcessingOrder(theContext, RequestTypeEnum.PUT, partsToIBaseMap);
		validatePartsNotNull(updateParts);
		retVal.addAll(updateParts);

		//Once we are done adding all DELETE, POST, PUT operations, add everything else.
		//Since this is a set, it will just fail to add already-added operations.
		retVal.addAll(partsToIBaseMap.values());

		//Blow away the entries and reset them in the right order.
		TerserUtil.clearField(theContext, theBundle, "entry");
		TerserUtil.setField(theContext, "entry", theBundle, retVal.toArray(new IBase[0]));
	}

	private static void validatePartsNotNull(LinkedHashSet<IBase> theDeleteParts) {
		if (theDeleteParts == null) {
			throw new IllegalStateException(Msg.code(1745) + "This transaction contains a cycle, so it cannot be sorted.");
		}
	}

	private static LinkedHashSet<IBase> sortEntriesOfTypeIntoProcessingOrder(FhirContext theContext, RequestTypeEnum theRequestTypeEnum, Map<BundleEntryParts, IBase> thePartsToIBaseMap) {
		SortLegality legality = new SortLegality();
		HashMap<String, Integer> color = new HashMap<>();
		HashMap<String, List<String>> adjList = new HashMap<>();
		List<String> topologicalOrder = new ArrayList<>();
		Set<BundleEntryParts> bundleEntryParts = thePartsToIBaseMap.keySet().stream().filter(part -> part.getRequestType().equals(theRequestTypeEnum)).collect(Collectors.toSet());
		HashMap<String, BundleEntryParts> resourceIdToBundleEntryMap = new HashMap<>();

		for (BundleEntryParts bundleEntryPart : bundleEntryParts) {
			IBaseResource resource = bundleEntryPart.getResource();
			if (resource != null) {
				String resourceId = resource.getIdElement().toVersionless().toString();
				resourceIdToBundleEntryMap.put(resourceId, bundleEntryPart);
				if (resourceId == null) {
					if (bundleEntryPart.getFullUrl() != null) {
						resourceId = bundleEntryPart.getFullUrl();
					}
				}

				color.put(resourceId, WHITE);
			}
		}

		for (BundleEntryParts bundleEntryPart : bundleEntryParts) {
			IBaseResource resource = bundleEntryPart.getResource();
			if (resource != null) {
				String resourceId = resource.getIdElement().toVersionless().toString();
				resourceIdToBundleEntryMap.put(resourceId, bundleEntryPart);
				if (resourceId == null) {
					if (bundleEntryPart.getFullUrl() != null) {
						resourceId = bundleEntryPart.getFullUrl();
					}
				}
				List<ResourceReferenceInfo> allResourceReferences = theContext.newTerser().getAllResourceReferences(resource);
				String finalResourceId = resourceId;
				allResourceReferences
					.forEach(refInfo -> {
						String referencedResourceId = refInfo.getResourceReference().getReferenceElement().toVersionless().getValue();
						if (color.containsKey(referencedResourceId)) {
							if (!adjList.containsKey(finalResourceId)) {
								adjList.put(finalResourceId, new ArrayList<>());
							}
							adjList.get(finalResourceId).add(referencedResourceId);
						}
					});
			}
		}

		for (Map.Entry<String, Integer> entry:color.entrySet()) {
			if (entry.getValue() == WHITE) {
				depthFirstSearch(entry.getKey(), color, adjList, topologicalOrder, legality);
			}
		}

		if (legality.isLegal()) {
			if (ourLog.isDebugEnabled()) {
				ourLog.debug("Topological order is: {}", String.join(",", topologicalOrder));
			}

			LinkedHashSet<IBase> orderedEntries = new LinkedHashSet<>();
			for (int i = 0; i < topologicalOrder.size(); i++) {
				BundleEntryParts bep;
				if (theRequestTypeEnum.equals(RequestTypeEnum.DELETE)) {
					int index = topologicalOrder.size() - i - 1;
					bep = resourceIdToBundleEntryMap.get(topologicalOrder.get(index));
				} else {
					bep = resourceIdToBundleEntryMap.get(topologicalOrder.get(i));
				}
				IBase base = thePartsToIBaseMap.get(bep);
				orderedEntries.add(base);
			}

			return orderedEntries;

		} else {
			return null;
		}
	}

	private static void depthFirstSearch(String theResourceId, HashMap<String, Integer> theResourceIdToColor, HashMap<String, List<String>> theAdjList, List<String> theTopologicalOrder, SortLegality theLegality) {

		if (!theLegality.isLegal()) {
			ourLog.debug("Found a cycle while trying to sort bundle entries. This bundle is not sortable.");
			return;
		}

		//We are currently recursing over this node (gray)
		theResourceIdToColor.put(theResourceId, GRAY);

		for (String neighbourResourceId: theAdjList.getOrDefault(theResourceId, new ArrayList<>())) {
			if (theResourceIdToColor.get(neighbourResourceId) == WHITE) {
				depthFirstSearch(neighbourResourceId, theResourceIdToColor, theAdjList, theTopologicalOrder, theLegality);
			} else if (theResourceIdToColor.get(neighbourResourceId) == GRAY) {
				theLegality.setLegal(false);
				return;
			}
		}
		//Mark the node as black
		theResourceIdToColor.put(theResourceId, BLACK);
		theTopologicalOrder.add(theResourceId);
	}

	private static Map<BundleEntryParts, IBase> getPartsToIBaseMap(FhirContext theContext, IBaseBundle theBundle) {
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
		Map<BundleEntryParts, IBase> map = new HashMap<>();
		for (IBase nextEntry : entries) {
			BundleEntryParts parts = getBundleEntryParts(fullUrlChildDef, resourceChildDef, requestChildDef, requestUrlChildDef, requestIfNoneExistChildDef, methodChildDef, nextEntry);
			/*
			 * All 3 might be null - That's ok because we still want to know the
			 * order in the original bundle.
			 */
			map.put(parts, nextEntry);
		}
		return map;
	}


	public static List<SearchBundleEntryParts> getSearchBundleEntryParts(FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition bundleDef = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChildDef = bundleDef.getChildByName("entry");
		List<IBase> entries = entryChildDef.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildContentsDef = (BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");
		BaseRuntimeChildDefinition fullUrlChildDef = entryChildContentsDef.getChildByName("fullUrl");
		BaseRuntimeChildDefinition resourceChildDef = entryChildContentsDef.getChildByName("resource");
		BaseRuntimeChildDefinition searchChildDef = entryChildContentsDef.getChildByName("search");
		BaseRuntimeElementCompositeDefinition<?> searchChildContentsDef = (BaseRuntimeElementCompositeDefinition<?>) searchChildDef.getChildByName("search");
		BaseRuntimeChildDefinition searchModeChildDef = searchChildContentsDef.getChildByName("mode");

		List<SearchBundleEntryParts> retVal = new ArrayList<>();
		for (IBase nextEntry : entries) {
			SearchBundleEntryParts parts = getSearchBundleEntryParts(fullUrlChildDef, resourceChildDef, searchChildDef, searchModeChildDef, nextEntry);
			retVal.add(parts);
		}
		return retVal;

	}

	private static SearchBundleEntryParts getSearchBundleEntryParts( BaseRuntimeChildDefinition fullUrlChildDef, BaseRuntimeChildDefinition resourceChildDef, BaseRuntimeChildDefinition searchChildDef, BaseRuntimeChildDefinition searchModeChildDef, IBase entry) {
		IBaseResource resource = null;
		String matchMode = null;

		String fullUrl = fullUrlChildDef
			.getAccessor()
			.getFirstValueOrNull(entry)
			.map(t->((IPrimitiveType<?>)t).getValueAsString())
			.orElse(null);

		for (IBase nextResource : resourceChildDef.getAccessor().getValues(entry)) {
			resource = (IBaseResource) nextResource;
		}

		for (IBase nextSearch : searchChildDef.getAccessor().getValues(entry)) {
			for (IBase nextUrl : searchModeChildDef.getAccessor().getValues(nextSearch)) {
				matchMode = ((IPrimitiveType<?>) nextUrl).getValueAsString();
			}
		}

		SearchBundleEntryParts parts = new SearchBundleEntryParts(fullUrl, resource, matchMode);
		return parts;
	}

	/**
	 * Given a bundle, and a consumer, apply the consumer to each entry in the bundle.
	 * @param theContext The FHIR Context
	 * @param theBundle The bundle to have its entries processed.
	 * @param theProcessor a {@link Consumer} which will operate on all the entries of a bundle.
	 */
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
			BundleEntryParts parts = getBundleEntryParts(fullUrlChildDef, resourceChildDef, requestChildDef, requestUrlChildDef, requestIfNoneExistChildDef, methodChildDef, nextEntry);
			/*
			 * All 3 might be null - That's ok because we still want to know the
			 * order in the original bundle.
			 */
			BundleEntryMutator mutator = new BundleEntryMutator(theContext, nextEntry, requestChildDef, requestChildContentsDef, entryChildContentsDef);
			ModifiableBundleEntry entry = new ModifiableBundleEntry(parts, mutator);
			theProcessor.accept(entry);
		}
	}

	private static BundleEntryParts getBundleEntryParts(BaseRuntimeChildDefinition fullUrlChildDef, BaseRuntimeChildDefinition resourceChildDef, BaseRuntimeChildDefinition requestChildDef, BaseRuntimeChildDefinition requestUrlChildDef, BaseRuntimeChildDefinition requestIfNoneExistChildDef, BaseRuntimeChildDefinition methodChildDef, IBase nextEntry) {
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
		BundleEntryParts parts = new BundleEntryParts(fullUrl, requestType, url, resource, conditionalUrl);
		return parts;
	}

	/**
	 * Extract all of the resources from a given bundle
	 */
	public static List<IBaseResource> toListOfResources(FhirContext theContext, IBaseBundle theBundle) {
		return toListOfResourcesOfType(theContext, theBundle, IBaseResource.class);
	}

	/**
	 * Extract all of ids of all the resources from a given bundle
	 */
	public static List<String> toListOfResourceIds(FhirContext theContext, IBaseBundle theBundle) {
		return toListOfResourcesOfType(theContext, theBundle, IBaseResource.class).stream()
			.map(resource -> resource.getIdElement().getIdPart())
			.collect(Collectors.toList());
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
	public static boolean isDstu3TransactionPatch(FhirContext theContext, IBaseResource thePayloadResource) {
		boolean isPatch = false;
		if (thePayloadResource instanceof IBaseBinary) {
			String contentType = ((IBaseBinary) thePayloadResource).getContentType();
			 try {
				 PatchTypeEnum.forContentTypeOrThrowInvalidRequestException(theContext, contentType);
				 isPatch = true;
			 } catch (InvalidRequestException e) {
				 // ignore
			 }
		}
		return isPatch;
	}


	/**
	 * create a new bundle entry and set a value for a single field
	 * @param theContext     Context holding resource definition
	 * @param theFieldName   Child field name of the bundle entry to set
	 * @param theValues      The values to set on the bundle entry child field name
	 * @return the new bundle entry
	 */
	public static IBase createNewBundleEntryWithSingleField(FhirContext theContext, String theFieldName, IBase... theValues) {
		IBaseBundle newBundle = TerserUtil.newResource(theContext, "Bundle");
		BaseRuntimeChildDefinition entryChildDef = theContext.getResourceDefinition(newBundle).getChildByName("entry");

		BaseRuntimeElementCompositeDefinition<?> entryChildElem = (BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName(theFieldName);
		IBase bundleEntry = entryChildElem.newInstance();
		for (IBase value : theValues) {
			try {
				resourceChild.getMutator().addValue(bundleEntry, value);
			} catch (UnsupportedOperationException e) {
				ourLog.warn("Resource {} does not support multiple values, but an attempt to set {} was made. Setting the first item only", bundleEntry, theValues);
				resourceChild.getMutator().setValue(bundleEntry, value);
				break;
			}
		}
		return bundleEntry;
	}

	private static class SortLegality {
		private boolean myIsLegal;

		SortLegality() {
			this.myIsLegal = true;
		}
		private void setLegal(boolean theLegal) {
			myIsLegal = theLegal;
		}

		public boolean isLegal() {
			return myIsLegal;
		}
	}

}
