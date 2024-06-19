/*
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.bundle.BundleEntryMutator;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.EntryListAccumulator;
import ca.uhn.fhir.util.bundle.ModifiableBundleEntry;
import ca.uhn.fhir.util.bundle.SearchBundleEntryParts;
import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.instance.model.api.IBaseBundle.LINK_PREV;

/**
 * Fetch resources from a bundle
 */
public class BundleUtil {

	/** Non instantiable */
	private BundleUtil() {
		// nothing
	}

	private static final Logger ourLog = LoggerFactory.getLogger(BundleUtil.class);

	private static final String PREVIOUS = LINK_PREV;
	private static final String PREV = "prev";
	private static final Set<String> previousOrPrev = Sets.newHashSet(PREVIOUS, PREV);

	public static final String DIFFERENT_LINK_ERROR_MSG =
			"Mismatching 'previous' and 'prev' links exist. 'previous' " + "is: '$PREVIOUS' and 'prev' is: '$PREV'.";

	/**
	 * @return Returns <code>null</code> if the link isn't found or has no value
	 */
	public static String getLinkUrlOfType(FhirContext theContext, IBaseBundle theBundle, String theLinkRelation) {
		return getLinkUrlOfType(theContext, theBundle, theLinkRelation, true);
	}

	private static String getLinkUrlOfType(
			FhirContext theContext, IBaseBundle theBundle, String theLinkRelation, boolean isPreviousCheck) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("link");
		List<IBase> links = entryChild.getAccessor().getValues(theBundle);
		for (IBase nextLink : links) {

			boolean isRightRel = false;
			BaseRuntimeElementCompositeDefinition<?> relDef =
					(BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(nextLink.getClass());
			BaseRuntimeChildDefinition relChild = relDef.getChildByName("relation");
			List<IBase> relValues = relChild.getAccessor().getValues(nextLink);
			for (IBase next : relValues) {
				IPrimitiveType<?> nextValue = (IPrimitiveType<?>) next;
				if (isRelationMatch(
						theContext, theBundle, theLinkRelation, nextValue.getValueAsString(), isPreviousCheck)) {
					isRightRel = true;
				}
			}

			if (!isRightRel) {
				continue;
			}

			BaseRuntimeElementCompositeDefinition<?> linkDef =
					(BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(nextLink.getClass());
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

	private static boolean isRelationMatch(
			FhirContext theContext, IBaseBundle theBundle, String value, String matching, boolean theIsPreviousCheck) {
		if (!theIsPreviousCheck) {
			return value.equals(matching);
		}

		if (previousOrPrev.contains(value)) {
			validateUniqueOrMatchingPreviousValues(theContext, theBundle);
			if (previousOrPrev.contains(matching)) {
				return true;
			}
		}
		return (value.equals(matching));
	}

	private static void validateUniqueOrMatchingPreviousValues(FhirContext theContext, IBaseBundle theBundle) {
		String previousLink = getLinkNoCheck(theContext, theBundle, PREVIOUS);
		String prevLink = getLinkNoCheck(theContext, theBundle, PREV);
		if (prevLink != null && previousLink != null) {
			if (!previousLink.equals(prevLink)) {
				String msg = DIFFERENT_LINK_ERROR_MSG
						.replace("$PREVIOUS", previousLink)
						.replace("$PREV", prevLink);
				throw new InternalErrorException(Msg.code(2368) + msg);
			}
		}
	}

	private static String getLinkNoCheck(FhirContext theContext, IBaseBundle theBundle, String theLinkRelation) {
		return getLinkUrlOfType(theContext, theBundle, theLinkRelation, false);
	}

	/**
	 * Returns a collection of Pairs, one for each entry in the bundle. Each pair will contain
	 * the values of Bundle.entry.fullUrl, and Bundle.entry.resource respectively. Nulls
	 * are possible in either or both values in the Pair.
	 *
	 * @since 7.0.0
	 */
	@SuppressWarnings("unchecked")
	public static List<Pair<String, IBaseResource>> getBundleEntryFullUrlsAndResources(
			FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildElem =
				(BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName("resource");

		BaseRuntimeChildDefinition urlChild = entryChildElem.getChildByName("fullUrl");

		List<Pair<String, IBaseResource>> retVal = new ArrayList<>(entries.size());
		for (IBase nextEntry : entries) {

			String fullUrl = urlChild.getAccessor()
					.getFirstValueOrNull(nextEntry)
					.map(t -> (((IPrimitiveType<?>) t).getValueAsString()))
					.orElse(null);
			IBaseResource resource = (IBaseResource)
					resourceChild.getAccessor().getFirstValueOrNull(nextEntry).orElse(null);

			retVal.add(Pair.of(fullUrl, resource));
		}

		return retVal;
	}

	public static List<Pair<String, IBaseResource>> getBundleEntryUrlsAndResources(
			FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildElem =
				(BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName("resource");

		BaseRuntimeChildDefinition requestChild = entryChildElem.getChildByName("request");
		BaseRuntimeElementCompositeDefinition<?> requestDef =
				(BaseRuntimeElementCompositeDefinition<?>) requestChild.getChildByName("request");

		BaseRuntimeChildDefinition urlChild = requestDef.getChildByName("url");

		List<Pair<String, IBaseResource>> retVal = new ArrayList<>(entries.size());
		for (IBase nextEntry : entries) {

			String url = requestChild
					.getAccessor()
					.getFirstValueOrNull(nextEntry)
					.flatMap(e -> urlChild.getAccessor().getFirstValueOrNull(e))
					.map(t -> ((IPrimitiveType<?>) t).getValueAsString())
					.orElse(null);

			IBaseResource resource = (IBaseResource)
					resourceChild.getAccessor().getFirstValueOrNull(nextEntry).orElse(null);

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

	public static BundleTypeEnum getBundleTypeEnum(FhirContext theContext, IBaseBundle theBundle) {
		String bundleTypeCode = BundleUtil.getBundleType(theContext, theBundle);
		if (isBlank(bundleTypeCode)) {
			return null;
		}
		return BundleTypeEnum.forCode(bundleTypeCode);
	}

	public static void setBundleType(FhirContext theContext, IBaseBundle theBundle, String theType) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("type");
		BaseRuntimeElementDefinition<?> element = entryChild.getChildByName("type");
		IPrimitiveType<?> typeInstance =
				(IPrimitiveType<?>) element.newInstance(entryChild.getInstanceConstructorArguments());
		typeInstance.setValueAsString(theType);

		entryChild.getMutator().setValue(theBundle, typeInstance);
	}

	public static Integer getTotal(FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("total");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);
		if (entries.size() > 0) {
			@SuppressWarnings("unchecked")
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
		@SuppressWarnings("unchecked")
		IPrimitiveType<Integer> value =
				(IPrimitiveType<Integer>) entryChild.getChildByName("total").newInstance();
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
	 * <p>
	 * Furthermore, within these operation types, the entries will be sorted based on the order in which they should be processed
	 * e.g. if you have 2 CREATEs, one for a Patient, and one for an Observation which has this Patient as its Subject,
	 * the patient will come first, then the observation.
	 * <p>
	 * In cases of there being a cyclic dependency (e.g. Organization/1 is partOf Organization/2 and Organization/2 is partOf Organization/1)
	 * this function will throw an IllegalStateException.
	 *
	 * @param theContext The FhirContext.
	 * @param theBundle The {@link IBaseBundle} which contains the entries you would like sorted into processing order.
	 */
	public static void sortEntriesIntoProcessingOrder(FhirContext theContext, IBaseBundle theBundle)
			throws IllegalStateException {
		Map<BundleEntryParts, IBase> partsToIBaseMap = getPartsToIBaseMap(theContext, theBundle);

		// Get all deletions.
		LinkedHashSet<IBase> deleteParts =
				sortEntriesOfTypeIntoProcessingOrder(theContext, RequestTypeEnum.DELETE, partsToIBaseMap);
		validatePartsNotNull(deleteParts);
		LinkedHashSet<IBase> retVal = new LinkedHashSet<>(deleteParts);

		// Get all Creations
		LinkedHashSet<IBase> createParts =
				sortEntriesOfTypeIntoProcessingOrder(theContext, RequestTypeEnum.POST, partsToIBaseMap);
		validatePartsNotNull(createParts);
		retVal.addAll(createParts);

		// Get all Updates
		LinkedHashSet<IBase> updateParts =
				sortEntriesOfTypeIntoProcessingOrder(theContext, RequestTypeEnum.PUT, partsToIBaseMap);
		validatePartsNotNull(updateParts);
		retVal.addAll(updateParts);

		// Once we are done adding all DELETE, POST, PUT operations, add everything else.
		// Since this is a set, it will just fail to add already-added operations.
		retVal.addAll(partsToIBaseMap.values());

		// Blow away the entries and reset them in the right order.
		TerserUtil.clearField(theContext, theBundle, "entry");
		TerserUtil.setField(theContext, "entry", theBundle, retVal.toArray(new IBase[0]));
	}

	/**
	 * Converts a Bundle containing resources into a FHIR transaction which
	 * creates/updates the resources. This method does not modify the original
	 * bundle, but returns a new copy.
	 * <p>
	 * This method is mostly intended for test scenarios where you have a Bundle
	 * containing search results or other sourced resources, and want to upload
	 * these resources to a server using a single FHIR transaction.
	 * </p>
	 * <p>
	 * The Bundle is converted using the following logic:
	 * <ul>
	 *     <li>Bundle.type is changed to <code>transaction</code></li>
	 *     <li>Bundle.request.method is changed to <code>PUT</code></li>
	 *     <li>Bundle.request.url is changed to <code>[resourceType]/[id]</code></li>
	 *     <li>Bundle.fullUrl is changed to <code>[resourceType]/[id]</code></li>
	 * </ul>
	 * </p>
	 *
	 * @param theContext The FhirContext to use with the bundle
	 * @param theBundle The Bundle to modify. All resources in the Bundle should have an ID.
	 * @param thePrefixIdsOrNull If not <code>null</code>, all resource IDs and all references in the Bundle will be
	 *                           modified to such that their IDs contain the given prefix. For example, for a value
	 *                           of "A", the resource "Patient/123" will be changed to be "Patient/A123". If set to
	 *                           <code>null</code>, resource IDs are unchanged.
	 * @since 7.4.0
	 */
	public static <T extends IBaseBundle> T convertBundleIntoTransaction(
			@Nonnull FhirContext theContext, @Nonnull T theBundle, @Nullable String thePrefixIdsOrNull) {
		String prefix = defaultString(thePrefixIdsOrNull);

		BundleBuilder bb = new BundleBuilder(theContext);

		FhirTerser terser = theContext.newTerser();
		List<IBase> entries = terser.getValues(theBundle, "Bundle.entry");
		for (var entry : entries) {
			IBaseResource resource = terser.getSingleValueOrNull(entry, "resource", IBaseResource.class);
			if (resource != null) {
				Validate.isTrue(resource.getIdElement().hasIdPart(), "Resource in bundle has no ID");
				String newId = theContext.getResourceType(resource) + "/" + prefix
						+ resource.getIdElement().getIdPart();

				IBaseResource resourceClone = terser.clone(resource);
				resourceClone.setId(newId);

				if (isNotBlank(prefix)) {
					for (var ref : terser.getAllResourceReferences(resourceClone)) {
						var refElement = ref.getResourceReference().getReferenceElement();
						ref.getResourceReference()
								.setReference(refElement.getResourceType() + "/" + prefix + refElement.getIdPart());
					}
				}

				bb.addTransactionUpdateEntry(resourceClone);
			}
		}

		return bb.getBundleTyped();
	}

	private static void validatePartsNotNull(LinkedHashSet<IBase> theDeleteParts) {
		if (theDeleteParts == null) {
			throw new IllegalStateException(
					Msg.code(1745) + "This transaction contains a cycle, so it cannot be sorted.");
		}
	}

	private static LinkedHashSet<IBase> sortEntriesOfTypeIntoProcessingOrder(
			FhirContext theContext,
			RequestTypeEnum theRequestTypeEnum,
			Map<BundleEntryParts, IBase> thePartsToIBaseMap) {
		SortLegality legality = new SortLegality();
		HashMap<String, Integer> color = new HashMap<>();
		HashMap<String, List<String>> adjList = new HashMap<>();
		List<String> topologicalOrder = new ArrayList<>();
		Set<BundleEntryParts> bundleEntryParts = thePartsToIBaseMap.keySet().stream()
				.filter(part -> part.getRequestType().equals(theRequestTypeEnum))
				.collect(Collectors.toSet());
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
				List<ResourceReferenceInfo> allResourceReferences =
						theContext.newTerser().getAllResourceReferences(resource);
				String finalResourceId = resourceId;
				allResourceReferences.forEach(refInfo -> {
					String referencedResourceId = refInfo.getResourceReference()
							.getReferenceElement()
							.toVersionless()
							.getValue();
					if (color.containsKey(referencedResourceId)) {
						if (!adjList.containsKey(finalResourceId)) {
							adjList.put(finalResourceId, new ArrayList<>());
						}
						adjList.get(finalResourceId).add(referencedResourceId);
					}
				});
			}
		}

		for (Map.Entry<String, Integer> entry : color.entrySet()) {
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

	private static void depthFirstSearch(
			String theResourceId,
			HashMap<String, Integer> theResourceIdToColor,
			HashMap<String, List<String>> theAdjList,
			List<String> theTopologicalOrder,
			SortLegality theLegality) {

		if (!theLegality.isLegal()) {
			ourLog.debug("Found a cycle while trying to sort bundle entries. This bundle is not sortable.");
			return;
		}

		// We are currently recursing over this node (gray)
		theResourceIdToColor.put(theResourceId, GRAY);

		for (String neighbourResourceId : theAdjList.getOrDefault(theResourceId, new ArrayList<>())) {
			if (theResourceIdToColor.get(neighbourResourceId) == WHITE) {
				depthFirstSearch(
						neighbourResourceId, theResourceIdToColor, theAdjList, theTopologicalOrder, theLegality);
			} else if (theResourceIdToColor.get(neighbourResourceId) == GRAY) {
				theLegality.setLegal(false);
				return;
			}
		}
		// Mark the node as black
		theResourceIdToColor.put(theResourceId, BLACK);
		theTopologicalOrder.add(theResourceId);
	}

	private static Map<BundleEntryParts, IBase> getPartsToIBaseMap(FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition bundleDef = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChildDef = bundleDef.getChildByName("entry");
		List<IBase> entries = entryChildDef.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildContentsDef =
				(BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");
		BaseRuntimeChildDefinition fullUrlChildDef = entryChildContentsDef.getChildByName("fullUrl");
		BaseRuntimeChildDefinition resourceChildDef = entryChildContentsDef.getChildByName("resource");
		BaseRuntimeChildDefinition requestChildDef = entryChildContentsDef.getChildByName("request");
		BaseRuntimeElementCompositeDefinition<?> requestChildContentsDef =
				(BaseRuntimeElementCompositeDefinition<?>) requestChildDef.getChildByName("request");
		BaseRuntimeChildDefinition requestUrlChildDef = requestChildContentsDef.getChildByName("url");
		BaseRuntimeChildDefinition requestIfNoneExistChildDef = requestChildContentsDef.getChildByName("ifNoneExist");
		BaseRuntimeChildDefinition methodChildDef = requestChildContentsDef.getChildByName("method");
		Map<BundleEntryParts, IBase> map = new HashMap<>();
		for (IBase nextEntry : entries) {
			BundleEntryParts parts = getBundleEntryParts(
					fullUrlChildDef,
					resourceChildDef,
					requestChildDef,
					requestUrlChildDef,
					requestIfNoneExistChildDef,
					methodChildDef,
					nextEntry);
			/*
			 * All 3 might be null - That's ok because we still want to know the
			 * order in the original bundle.
			 */
			map.put(parts, nextEntry);
		}
		return map;
	}

	public static List<SearchBundleEntryParts> getSearchBundleEntryParts(
			FhirContext theContext, IBaseBundle theBundle) {
		RuntimeResourceDefinition bundleDef = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChildDef = bundleDef.getChildByName("entry");
		List<IBase> entries = entryChildDef.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildContentsDef =
				(BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");
		BaseRuntimeChildDefinition fullUrlChildDef = entryChildContentsDef.getChildByName("fullUrl");
		BaseRuntimeChildDefinition resourceChildDef = entryChildContentsDef.getChildByName("resource");
		BaseRuntimeChildDefinition searchChildDef = entryChildContentsDef.getChildByName("search");
		BaseRuntimeElementCompositeDefinition<?> searchChildContentsDef =
				(BaseRuntimeElementCompositeDefinition<?>) searchChildDef.getChildByName("search");
		BaseRuntimeChildDefinition searchModeChildDef = searchChildContentsDef.getChildByName("mode");
		BaseRuntimeChildDefinition searchScoreChildDef = searchChildContentsDef.getChildByName("score");

		List<SearchBundleEntryParts> retVal = new ArrayList<>();
		for (IBase nextEntry : entries) {
			SearchBundleEntryParts parts = getSearchBundleEntryParts(
					fullUrlChildDef,
					resourceChildDef,
					searchChildDef,
					searchModeChildDef,
					searchScoreChildDef,
					nextEntry);
			retVal.add(parts);
		}
		return retVal;
	}

	private static SearchBundleEntryParts getSearchBundleEntryParts(
			BaseRuntimeChildDefinition theFullUrlChildDef,
			BaseRuntimeChildDefinition theResourceChildDef,
			BaseRuntimeChildDefinition theSearchChildDef,
			BaseRuntimeChildDefinition theSearchModeChildDef,
			BaseRuntimeChildDefinition theSearchScoreChildDef,
			IBase entry) {
		IBaseResource resource = null;
		String matchMode = null;
		BigDecimal searchScore = null;

		String fullUrl = theFullUrlChildDef
				.getAccessor()
				.getFirstValueOrNull(entry)
				.map(t -> ((IPrimitiveType<?>) t).getValueAsString())
				.orElse(null);

		for (IBase nextResource : theResourceChildDef.getAccessor().getValues(entry)) {
			resource = (IBaseResource) nextResource;
		}

		for (IBase nextSearch : theSearchChildDef.getAccessor().getValues(entry)) {
			for (IBase nextUrl : theSearchModeChildDef.getAccessor().getValues(nextSearch)) {
				matchMode = ((IPrimitiveType<?>) nextUrl).getValueAsString();
			}
			for (IBase nextUrl : theSearchScoreChildDef.getAccessor().getValues(nextSearch)) {
				searchScore = (BigDecimal) ((IPrimitiveType<?>) nextUrl).getValue();
			}
		}

		return new SearchBundleEntryParts(fullUrl, resource, matchMode, searchScore);
	}

	/**
	 * Given a bundle, and a consumer, apply the consumer to each entry in the bundle.
	 * @param theContext The FHIR Context
	 * @param theBundle The bundle to have its entries processed.
	 * @param theProcessor a {@link Consumer} which will operate on all the entries of a bundle.
	 */
	public static void processEntries(
			FhirContext theContext, IBaseBundle theBundle, Consumer<ModifiableBundleEntry> theProcessor) {
		RuntimeResourceDefinition bundleDef = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChildDef = bundleDef.getChildByName("entry");
		List<IBase> entries = entryChildDef.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildContentsDef =
				(BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");
		BaseRuntimeChildDefinition fullUrlChildDef = entryChildContentsDef.getChildByName("fullUrl");
		BaseRuntimeChildDefinition resourceChildDef = entryChildContentsDef.getChildByName("resource");
		BaseRuntimeChildDefinition requestChildDef = entryChildContentsDef.getChildByName("request");
		BaseRuntimeElementCompositeDefinition<?> requestChildContentsDef =
				(BaseRuntimeElementCompositeDefinition<?>) requestChildDef.getChildByName("request");
		BaseRuntimeChildDefinition requestUrlChildDef = requestChildContentsDef.getChildByName("url");
		BaseRuntimeChildDefinition requestIfNoneExistChildDef = requestChildContentsDef.getChildByName("ifNoneExist");
		BaseRuntimeChildDefinition methodChildDef = requestChildContentsDef.getChildByName("method");

		for (IBase nextEntry : entries) {
			BundleEntryParts parts = getBundleEntryParts(
					fullUrlChildDef,
					resourceChildDef,
					requestChildDef,
					requestUrlChildDef,
					requestIfNoneExistChildDef,
					methodChildDef,
					nextEntry);
			/*
			 * All 3 might be null - That's ok because we still want to know the
			 * order in the original bundle.
			 */
			BundleEntryMutator mutator = new BundleEntryMutator(
					theContext, nextEntry, requestChildDef, requestChildContentsDef, entryChildContentsDef);
			ModifiableBundleEntry entry = new ModifiableBundleEntry(parts, mutator);
			theProcessor.accept(entry);
		}
	}

	private static BundleEntryParts getBundleEntryParts(
			BaseRuntimeChildDefinition fullUrlChildDef,
			BaseRuntimeChildDefinition resourceChildDef,
			BaseRuntimeChildDefinition requestChildDef,
			BaseRuntimeChildDefinition requestUrlChildDef,
			BaseRuntimeChildDefinition requestIfNoneExistChildDef,
			BaseRuntimeChildDefinition methodChildDef,
			IBase nextEntry) {
		IBaseResource resource = null;
		String url = null;
		RequestTypeEnum requestType = null;
		String conditionalUrl = null;
		String fullUrl = fullUrlChildDef
				.getAccessor()
				.getFirstValueOrNull(nextEntry)
				.map(t -> ((IPrimitiveType<?>) t).getValueAsString())
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
					case DELETE:
					case PATCH:
						conditionalUrl = url != null && url.contains("?") ? url : null;
						break;
					case POST:
						List<IBase> ifNoneExistReps =
								requestIfNoneExistChildDef.getAccessor().getValues(nextRequest);
						if (ifNoneExistReps.size() > 0) {
							IPrimitiveType<?> ifNoneExist = (IPrimitiveType<?>) ifNoneExistReps.get(0);
							conditionalUrl = ifNoneExist.getValueAsString();
						}
						break;
				}
			}
		}
		return new BundleEntryParts(fullUrl, requestType, url, resource, conditionalUrl);
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
	public static <T extends IBaseResource> List<T> toListOfResourcesOfType(
			FhirContext theContext, IBaseBundle theBundle, Class<T> theTypeToInclude) {
		Objects.requireNonNull(theTypeToInclude, "ResourceType must not be null");
		List<T> retVal = new ArrayList<>();

		RuntimeResourceDefinition def = theContext.getResourceDefinition(theBundle);
		BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
		List<IBase> entries = entryChild.getAccessor().getValues(theBundle);

		BaseRuntimeElementCompositeDefinition<?> entryChildElem =
				(BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
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

	public static IBase getReferenceInBundle(
			@Nonnull FhirContext theFhirContext, @Nonnull String theUrl, @Nullable Object theAppContext) {
		if (!(theAppContext instanceof IBaseBundle) || isBlank(theUrl) || theUrl.startsWith("#")) {
			return null;
		}

		/*
		 * If this is a reference that is a UUID, we must be looking for local references within a Bundle
		 */
		IBaseBundle bundle = (IBaseBundle) theAppContext;

		final boolean isPlaceholderReference = theUrl.startsWith("urn:");
		final String unqualifiedVersionlessReference =
				new IdDt(theUrl).toUnqualifiedVersionless().getValue();

		for (BundleEntryParts next : BundleUtil.toListOfEntries(theFhirContext, bundle)) {
			IBaseResource nextResource = next.getResource();
			if (nextResource == null) {
				continue;
			}
			if (isPlaceholderReference) {
				if (theUrl.equals(next.getFullUrl())
						|| theUrl.equals(nextResource.getIdElement().getValue())) {
					return nextResource;
				}
			} else {
				if (unqualifiedVersionlessReference.equals(
						nextResource.getIdElement().toUnqualifiedVersionless().getValue())) {
					return nextResource;
				}
			}
		}
		return null;
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
	public static IBase createNewBundleEntryWithSingleField(
			FhirContext theContext, String theFieldName, IBase... theValues) {
		IBaseBundle newBundle = TerserUtil.newResource(theContext, "Bundle");
		BaseRuntimeChildDefinition entryChildDef =
				theContext.getResourceDefinition(newBundle).getChildByName("entry");

		BaseRuntimeElementCompositeDefinition<?> entryChildElem =
				(BaseRuntimeElementCompositeDefinition<?>) entryChildDef.getChildByName("entry");
		BaseRuntimeChildDefinition resourceChild = entryChildElem.getChildByName(theFieldName);
		IBase bundleEntry = entryChildElem.newInstance();
		for (IBase value : theValues) {
			try {
				resourceChild.getMutator().addValue(bundleEntry, value);
			} catch (UnsupportedOperationException e) {
				ourLog.warn(
						"Resource {} does not support multiple values, but an attempt to set {} was made. Setting the first item only",
						bundleEntry,
						theValues);
				resourceChild.getMutator().setValue(bundleEntry, value);
				break;
			}
		}
		return bundleEntry;
	}

	/**
	 * Get resource from bundle by resource type and reference
	 * @param theContext   FhirContext
	 * @param theBundle    IBaseBundle
	 * @param theReference IBaseReference
	 * @return IBaseResource if found and null if not found.
	 */
	@Nonnull
	public static IBaseResource getResourceByReferenceAndResourceType(
			@Nonnull FhirContext theContext, @Nonnull IBaseBundle theBundle, @Nonnull IBaseReference theReference) {
		return toListOfResources(theContext, theBundle).stream()
				.filter(theResource -> theReference
						.getReferenceElement()
						.getIdPart()
						.equals(theResource.getIdElement().getIdPart()))
				.findFirst()
				.orElse(null);
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
