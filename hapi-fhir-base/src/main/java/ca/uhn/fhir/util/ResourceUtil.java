/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.EncodingEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Strings;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ResourceUtil {

	private static final String ENCODING = "ENCODING_TYPE";
	private static final String RAW_ = "RAW_";
	private static final String EQUALS_DEEP = "equalsDeep";
	public static final String DATA_ABSENT_REASON_EXTENSION_URI =
			"http://hl7.org/fhir/StructureDefinition/data-absent-reason";

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceUtil.class);

	/**
	 * A strategy object that specifies which rules to apply when merging <code>Coding</code>
	 * and <code>CodeableConcept</code> fields
	 */
	public static class MergeControlParameters {
		private boolean myIgnoreCodeableConceptCodingOrder;
		private boolean myMergeCodings;
		private boolean myMergeCodingDetails;

		public boolean isIgnoreCodeableConceptCodingOrder() {
			return myIgnoreCodeableConceptCodingOrder;
		}

		/**
		 * In most cases, the order of elements in a FHIR list should not be considered meaningful. This parameter
		 * specifies whether the order of <code>Coding</code> entities within a <code>CodeableConcept</code>
		 * matters when performing a merge.
		 * @param theIgnoreCodeableConceptCodingOrder if true, two <code>CodeableConcept</code> entities will
		 *                                            be considered to match each other if they contain
		 *                                            matching <code>Coding</code>s in any order. If false,
		 *                                            the <code>Coding</code>s must be in the same order.
		 */
		public void setIgnoreCodeableConceptCodingOrder(boolean theIgnoreCodeableConceptCodingOrder) {
			myIgnoreCodeableConceptCodingOrder = theIgnoreCodeableConceptCodingOrder;
		}

		public boolean isMergeCodings() {
			return myMergeCodings;
		}

		/**
		 * If the <code>Coding</code>s of one <code>CodeableConcept</code> are a strict subset of another, the two
		 * may be considered to match, and can be merged into a single element that contains the larger list of
		 * <code>Coding</code>s. The case where the two lists of <code>Coding</code>s overlap, but each contains
		 * elements that are absent from the other, the two <code>CodeableConcept</code>s will be considered distinct,
		 * and will not be merged.
		 * @param theMergeCodings if true, two <code>CodeableConcept</code> entities will be considered to match each
		 *                        other if all the <code>Coding</code>s from the shorter list occur in the longer list.
		 *                        If false, the lists must be the same length, and contain exactly the same elements.
		 */
		public void setMergeCodings(boolean theMergeCodings) {
			myMergeCodings = theMergeCodings;
		}

		public boolean isMergeCodingDetails() {
			return myMergeCodingDetails;
		}

		/**
		 * Two <code>Coding</code>s may be considered to match if they share the same business key (system, code).
		 * If this is the case, the remaining fields of the element can be merged into a survivor <code>Coding</code>.
		 * @param theMergeCodingDetails if true, will match on the <code>Coding</code> business key only.
		 *                              if false, matching requires exact equality of every field.
		 */
		public void setMergeCodingDetails(boolean theMergeCodingDetails) {
			myMergeCodingDetails = theMergeCodingDetails;
		}
	}

	private ResourceUtil() {}

	/**
	 * Exclusion predicate for keeping all fields.
	 */
	public static final Predicate<String> INCLUDE_ALL = s -> true;

	/**
	 * This method removes the narrative from the resource, or if the resource is a bundle, removes the narrative from
	 * all of the resources in the bundle
	 *
	 * @param theContext The fhir context
	 * @param theInput   The resource to remove the narrative from
	 */
	public static void removeNarrative(FhirContext theContext, IBaseResource theInput) {
		if (theInput instanceof IBaseBundle) {
			for (IBaseResource next : BundleUtil.toListOfResources(theContext, (IBaseBundle) theInput)) {
				removeNarrative(theContext, next);
			}
		}

		BaseRuntimeElementCompositeDefinition<?> element = theContext.getResourceDefinition(theInput.getClass());
		BaseRuntimeChildDefinition textElement = element.getChildByName("text");
		if (textElement != null) {
			textElement.getMutator().setValue(theInput, null);
		}
	}

	public static void addRawDataToResource(
			@Nonnull IBaseResource theResource, @Nonnull EncodingEnum theEncodingType, String theSerializedData) {
		theResource.setUserData(getRawUserDataKey(theEncodingType), theSerializedData);
		theResource.setUserData(ENCODING, theEncodingType);
	}

	public static EncodingEnum getEncodingTypeFromUserData(@Nonnull IBaseResource theResource) {
		return (EncodingEnum) theResource.getUserData(ENCODING);
	}

	public static void clearRawStringFromResource(@Nonnull IBaseResource theResource) {
		EncodingEnum type = (EncodingEnum) theResource.getUserData(ENCODING);
		if (type != null) {
			theResource.setUserData(getRawUserDataKey(type), null);
		}
	}

	public static String getRawStringFromResourceOrNull(@Nonnull IBaseResource theResource) {
		EncodingEnum type = (EncodingEnum) theResource.getUserData(ENCODING);
		if (type != null) {
			return (String) theResource.getUserData(getRawUserDataKey(type));
		}
		return null;
	}

	private static String getRawUserDataKey(EncodingEnum theEncodingEnum) {
		return RAW_ + theEncodingEnum.name();
	}

	/**
	 * Merges all fields on the provided instance. <code>theTarget</code> will contain a union of all values from
	 * <code>theSource</code> instance and <code>theTarget</code> instance.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theSource      The FHIR element to merge the fields from
	 * @param theTarget      The FHIR element to merge the fields into
	 */
	public static void mergeAllFields(FhirContext theFhirContext, IBase theSource, IBase theTarget) {
		mergeAllFields(theFhirContext, theSource, theTarget, new MergeControlParameters());
	}

	/**
	 * Merges all fields on the provided instance. <code>theTarget</code> will contain a union of all values from
	 * <code>theSource</code> instance and <code>theTarget</code> instance.
	 *
	 * @param theFhirContext            Context holding resource definition
	 * @param theSource                 The FHIR element to merge the fields from
	 * @param theTarget                 The FHIR element to merge the fields into
	 * @param theMergeControlParameters Parameters to provide fine-grained control over the behaviour of the merge
	 */
	public static void mergeAllFields(
			FhirContext theFhirContext,
			IBase theSource,
			IBase theTarget,
			MergeControlParameters theMergeControlParameters) {
		mergeFields(theFhirContext, theSource, theTarget, INCLUDE_ALL, theMergeControlParameters);
	}

	/**
	 * Merges values of all field from <code>theSource</code> resource to <code>theTarget</code> resource. Fields
	 * values are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext       Context holding resource definition
	 * @param theSource            Resource to merge the specified field from
	 * @param theTarget            Resource to merge the specified field into
	 * @param theInclusionStrategy Predicate to test which fields should be merged
	 */
	public static void mergeFields(
			FhirContext theFhirContext, IBase theSource, IBase theTarget, Predicate<String> theInclusionStrategy) {
		mergeFields(theFhirContext, theSource, theTarget, theInclusionStrategy, new MergeControlParameters());
	}

	/**
	 * Merges values of all field from <code>theSource</code> resource to <code>theTarget</code> resource. Fields
	 * with type <code>Coding</code> or <code>CodeableConcept</code> will be recursively merged according to the
	 * strategy specified by <code>theMergeControlParameters</code>. Fields of other types
	 * are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext            Context holding resource definition
	 * @param theSource                 Resource to merge the specified field from
	 * @param theTarget                 Resource to merge the specified field into
	 * @param theInclusionStrategy      Predicate to test which fields should be merged
	 * @param theMergeControlParameters Parameters to provide fine-grained control over the behaviour of the merge
	 */
	public static void mergeFields(
			FhirContext theFhirContext,
			IBase theSource,
			IBase theTarget,
			Predicate<String> theInclusionStrategy,
			MergeControlParameters theMergeControlParameters) {
		BaseRuntimeElementDefinition<?> definition = theFhirContext.getElementDefinition(theSource.getClass());
		if (definition instanceof BaseRuntimeElementCompositeDefinition<?> compositeDefinition) {
			for (BaseRuntimeChildDefinition childDefinition : compositeDefinition.getChildrenAndExtension()) {
				if (!theInclusionStrategy.test(childDefinition.getElementName())) {
					continue;
				}

				List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theSource);
				List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTarget);

				mergeFields(
						theFhirContext,
						theTarget,
						childDefinition,
						theFromFieldValues,
						theToFieldValues,
						theMergeControlParameters);
			}
		}
	}

	/**
	 * Merges value of the specified field from <code>theSource</code> resource to <code>theTarget</code> resource. Fields
	 * values are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFieldName   Name of the child filed to merge
	 * @param theSource      Resource to merge the specified field from
	 * @param theTarget      Resource to merge the specified field into
	 */
	public static void mergeField(
			FhirContext theFhirContext, String theFieldName, IBaseResource theSource, IBaseResource theTarget) {
		mergeField(theFhirContext, theFieldName, theSource, theTarget, new MergeControlParameters());
	}

	/**
	 * Merges value of the specified field from <code>theSource</code> resource to <code>theTarget</code> resource. Fields
	 * with type <code>Coding</code> or <code>CodeableConcept</code> will be recursively merged according to the
	 * strategy specified by <code>theMergeControlParameters</code>. Fields of other types
	 * are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext            Context holding resource definition
	 * @param theFieldName              Name of the child filed to merge
	 * @param theSource                 Resource to merge the specified field from
	 * @param theTarget                 Resource to merge the specified field into
	 * @param theMergeControlParameters Parameters to provide fine-grained control over the behaviour of the merge
	 */
	public static void mergeField(
			FhirContext theFhirContext,
			String theFieldName,
			IBaseResource theSource,
			IBaseResource theTarget,
			MergeControlParameters theMergeControlParameters) {
		BaseRuntimeChildDefinition childDefinition =
				getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theSource);

		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theSource);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTarget);

		mergeFields(
				theFhirContext,
				theTarget,
				childDefinition,
				theFromFieldValues,
				theToFieldValues,
				theMergeControlParameters);
	}

	private static void mergeFields(
			FhirContext theFhirContext,
			IBase theTarget,
			BaseRuntimeChildDefinition theChildDefinition,
			List<IBase> theSourceFieldValues,
			List<IBase> theTargetFieldValues,
			MergeControlParameters theMergeControlParameters) {
		FhirTerser terser = theFhirContext.newTerser();

		if (!theSourceFieldValues.isEmpty()
				&& theTargetFieldValues.stream().anyMatch(ResourceUtil::hasDataAbsentReason)) {
			// If the target resource has a data absent reason, and there is potentially real data incoming
			// in the source resource, we should clear the data absent reason because it won't be absent anymore.
			theTargetFieldValues = removeDataAbsentReason(theTarget, theChildDefinition, theTargetFieldValues);
		}

		List<IBase> filteredFromFieldValues = filterValuesThatAlreadyExistInTarget(
				terser, theSourceFieldValues, theTargetFieldValues, theMergeControlParameters);

		for (IBase fromFieldValue : filteredFromFieldValues) {
			IBase newFieldValue;
			if (Strings.CI.equals(fromFieldValue.fhirType(), "CodeableConcept")) {
				newFieldValue = mergeOrClone(
						theFhirContext,
						theChildDefinition,
						fromFieldValue,
						theTargetFieldValues,
						theMergeControlParameters,
						targetValue -> isCodeableConceptMergeCandidate(
								fromFieldValue, targetValue, terser, theMergeControlParameters));
			} else if (Strings.CI.equals(fromFieldValue.fhirType(), "Coding")) {
				newFieldValue = mergeOrClone(
						theFhirContext,
						theChildDefinition,
						fromFieldValue,
						theTargetFieldValues,
						theMergeControlParameters,
						targetValue ->
								isCodingMergeCandidate(terser, fromFieldValue, targetValue, theMergeControlParameters));
			} else {
				newFieldValue = createNewElement(terser, theChildDefinition, fromFieldValue);
			}

			if (newFieldValue != null) {
				try {
					theTargetFieldValues.add(newFieldValue);
				} catch (UnsupportedOperationException e) {
					theChildDefinition.getMutator().setValue(theTarget, newFieldValue);
					theTargetFieldValues = theChildDefinition.getAccessor().getValues(theTarget);
				}
			}
		}
	}

	/**
	 * If <code>theMergePredicate</code> identifies one of the elements in <code>theTargetFieldValues</code>
	 * as a match for <code>theSourceFieldValue</code>, those two elements will be merged.
	 * Otherwise, <code>theSourceFieldValue</code> will be cloned and returned.
	 * @param theFhirContext            Context holding resource definition
	 * @param theChildDefinition        The definition of the field being merged
	 * @param theSourceFieldValue       A value from the source resource
	 * @param theTargetFieldValues      The values already present in the target resource
	 * @param theMergeControlParameters Parameters to provide fine-grained control over the behaviour of the merge
	 * @param theMergePredicate         An algorithm to identify matching resources
	 * @return a clone of <code>theSourceFieldValue</code> if none of the values in <code>theTargetFieldValues</code>
	 *         is a match. Otherwise, null, indicating that a merge took place
	 */
	private static IBase mergeOrClone(
			FhirContext theFhirContext,
			BaseRuntimeChildDefinition theChildDefinition,
			IBase theSourceFieldValue,
			List<IBase> theTargetFieldValues,
			MergeControlParameters theMergeControlParameters,
			Predicate<IBase> theMergePredicate) {
		IBase newFieldValue = null;
		FhirTerser terser = theFhirContext.newTerser();
		Optional<IBase> matchedTargetValue =
				theTargetFieldValues.stream().filter(theMergePredicate).findFirst();
		if (matchedTargetValue.isPresent()) {
			mergeAllFields(theFhirContext, theSourceFieldValue, matchedTargetValue.get(), theMergeControlParameters);
		} else {
			newFieldValue = createNewElement(terser, theChildDefinition, theSourceFieldValue);
		}
		return newFieldValue;
	}

	private static IBase createNewElement(
			FhirTerser theTerser, BaseRuntimeChildDefinition theChildDefinition, IBase theFromFieldValue) {
		IBase newFieldValue = newElement(theTerser, theChildDefinition, theFromFieldValue);
		if (theFromFieldValue instanceof IPrimitiveType) {
			try {
				Method copyMethod = getMethod(theFromFieldValue, "copy");
				if (copyMethod != null) {
					newFieldValue = (IBase) copyMethod.invoke(theFromFieldValue);
				}
			} catch (Exception t) {
				((IPrimitiveType<?>) newFieldValue)
						.setValueAsString(((IPrimitiveType<?>) theFromFieldValue).getValueAsString());
			}
		} else {
			theTerser.cloneInto(theFromFieldValue, newFieldValue, true);
		}
		return newFieldValue;
	}

	private static List<IBase> filterValuesThatAlreadyExistInTarget(
			FhirTerser theTerser,
			List<IBase> theFromFieldValues,
			List<IBase> theToFieldValues,
			MergeControlParameters theMergeControlParameters) {
		List<IBase> filteredFromFieldValues = new ArrayList<>();
		for (IBase fromFieldValue : theFromFieldValues) {
			if (theToFieldValues.isEmpty()) {
				// if the target field is unpopulated, accept any value from the source field
				filteredFromFieldValues.add(fromFieldValue);
			} else if (!hasDataAbsentReason(fromFieldValue)) {
				// if the value from the source field does not have a data absent reason extension,
				// evaluate its suitability for inclusion
				if (Strings.CI.equals(fromFieldValue.fhirType(), "codeableConcept")) {
					if (!containsCodeableConcept(
							fromFieldValue, theToFieldValues, theTerser, theMergeControlParameters)) {
						filteredFromFieldValues.add(fromFieldValue);
					}
				} else if (!contains(fromFieldValue, theToFieldValues)) {
					// include it if the target list doesn't already contain an exact match
					filteredFromFieldValues.add(fromFieldValue);
				}
			}
		}
		return filteredFromFieldValues;
	}

	private static BaseRuntimeChildDefinition getBaseRuntimeChildDefinition(
			FhirContext theFhirContext, String theFieldName, IBaseResource theFrom) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(theFieldName);
		Objects.requireNonNull(childDefinition);
		return childDefinition;
	}

	private static Method getMethod(IBase theBase, String theMethodName) {
		Method method = null;
		for (Method m : theBase.getClass().getDeclaredMethods()) {
			if (m.getName().equals(theMethodName)) {
				method = m;
				break;
			}
		}
		return method;
	}

	private static boolean evaluateEquality(IBase theItem1, IBase theItem2, Method theMethod) {
		if (theMethod != null) {
			try {
				return (Boolean) theMethod.invoke(theItem1, theItem2);
			} catch (Exception e) {
				ourLog.debug("{} Unable to compare equality via {}", Msg.code(2821), theMethod.getName(), e);
			}
		}
		return theItem1.equals(theItem2);
	}

	private static boolean contains(IBase theItem, List<IBase> theItems) {
		final Method method = getMethod(theItem, EQUALS_DEEP);
		return theItems.stream().anyMatch(i -> evaluateEquality(i, theItem, method));
	}

	/**
	 * Evaluates whether a given source CodeableConcept can be merged into a target
	 * CodeableConcept
	 *
	 * @param theSourceItem             the source item
	 * @param theTargetItem             the target item
	 * @param theTerser                 a terser for introspecting the items
	 * @param theMergeControlParameters parameters providing fine-grained control over the merge operation
	 * @return true if the source item can be merged into the target item
	 */
	private static boolean isCodeableConceptMergeCandidate(
			IBase theSourceItem,
			IBase theTargetItem,
			FhirTerser theTerser,
			MergeControlParameters theMergeControlParameters) {
		// First, compare the shallow fields of the CodeableConcepts.
		Method shallowEquals = getMethod(theSourceItem, "equalsShallow");
		boolean isMergeCandidate = evaluateEquality(theSourceItem, theTargetItem, shallowEquals);

		// if the shallow fields match, we proceed to compare the lists of Codings
		if (theMergeControlParameters.isIgnoreCodeableConceptCodingOrder()) {
			isMergeCandidate &=
					isCodingListsMatchUnordered(theSourceItem, theTargetItem, theTerser, theMergeControlParameters);
		} else {
			isMergeCandidate &= isCodingListsMatchOrdered(
					theSourceItem, theTargetItem, theTerser, theMergeControlParameters, isMergeCandidate);
		}

		return isMergeCandidate;
	}

	private static boolean isCodingListsMatchOrdered(
			IBase theSourceItem,
			IBase theTargetItem,
			FhirTerser theTerser,
			MergeControlParameters theMergeControlParameters,
			boolean isMergeCandidate) {
		List<IBase> sourceCodings = theTerser.getValues(theSourceItem, "coding");
		List<IBase> targetCodings = theTerser.getValues(theTargetItem, "coding");
		if (theMergeControlParameters.isMergeCodings()) {
			int prefixLength = Math.min(sourceCodings.size(), targetCodings.size());
			for (int i = 0; i < prefixLength; i++) {
				isMergeCandidate &= isCodingMergeCandidate(
						theTerser, sourceCodings.get(i), targetCodings.get(i), theMergeControlParameters);
			}
		} else {
			if (sourceCodings.size() == targetCodings.size()) {
				for (int i = 0; i < sourceCodings.size(); i++) {
					isMergeCandidate &= isCodingMergeCandidate(
							theTerser, sourceCodings.get(i), targetCodings.get(i), theMergeControlParameters);
				}
			} else {
				isMergeCandidate = false;
			}
		}
		return isMergeCandidate;
	}

	private static boolean isCodingListsMatchUnordered(
			IBase theSourceItem,
			IBase theTargetItem,
			FhirTerser theTerser,
			MergeControlParameters theMergeControlParameters) {
		boolean isMergeCandidate;
		List<IBase> sourceCodings = theTerser.getValues(theSourceItem, "coding");
		List<IBase> targetCodings = theTerser.getValues(theTargetItem, "coding");
		if (theMergeControlParameters.isMergeCodings()) {
			if (sourceCodings.size() < targetCodings.size()) {
				isMergeCandidate = sourceCodings.stream().allMatch(sourceCoding -> targetCodings.stream()
						.anyMatch(targetCoding -> isCodingMergeCandidate(
								theTerser, sourceCoding, targetCoding, theMergeControlParameters)));
			} else {
				isMergeCandidate = targetCodings.stream().allMatch(targetCoding -> sourceCodings.stream()
						.anyMatch(sourceCoding -> isCodingMergeCandidate(
								theTerser, sourceCoding, targetCoding, theMergeControlParameters)));
			}
		} else {
			isMergeCandidate = sourceCodings.size() == targetCodings.size()
					&& sourceCodings.stream().allMatch(sourceCoding -> targetCodings.stream()
							.anyMatch(targetCoding -> isCodingMergeCandidate(
									theTerser, sourceCoding, targetCoding, theMergeControlParameters)));
		}
		return isMergeCandidate;
	}

	@SuppressWarnings("rawtypes")
	private static boolean isCodingMergeCandidate(
			FhirTerser theTerser,
			IBase theSourceCoding,
			IBase theTargetCoding,
			MergeControlParameters theMergeControlParameters) {
		boolean codingMatches;
		if (theMergeControlParameters.isMergeCodingDetails()) {
			// Use the tuple (system,code) as a business key on Coding
			Optional<IPrimitiveType> sourceSystem =
					theTerser.getSingleValue(theSourceCoding, "system", IPrimitiveType.class);
			Optional<IPrimitiveType> sourceCode =
					theTerser.getSingleValue(theSourceCoding, "code", IPrimitiveType.class);
			Optional<IPrimitiveType> targetSystem =
					theTerser.getSingleValue(theTargetCoding, "system", IPrimitiveType.class);
			Optional<IPrimitiveType> targetCode =
					theTerser.getSingleValue(theTargetCoding, "code", IPrimitiveType.class);
			boolean systemMatches = sourceSystem.isPresent()
					&& targetSystem.isPresent()
					&& Strings.CS.equals(
							sourceSystem.get().getValueAsString(),
							targetSystem.get().getValueAsString());
			boolean codeMatches = sourceCode.isPresent()
					&& targetCode.isPresent()
					&& Strings.CS.equals(
							sourceCode.get().getValueAsString(),
							targetCode.get().getValueAsString());
			codingMatches = systemMatches && codeMatches;
		} else {
			// require an exact match on every field
			Method deepEquals = getMethod(theSourceCoding, EQUALS_DEEP);
			codingMatches = evaluateEquality(theSourceCoding, theTargetCoding, deepEquals);
		}
		return codingMatches;
	}

	/**
	 * Evaluates whether a list of target CodeableConcepts already contains a given source
	 * CodeableConcept. The order of Codings may or may not matter, depending on the
	 * configuration parameters, but otherwise we evaluate equivalence in the strictest
	 * available sense, since values filtered out by this method will not be candidates
	 * for subsequent merge operations.
	 *
	 * @param theSourceItem             The source value
	 * @param theTargetItems            The list of target values
	 * @param theTerser                 A terser to use to inspect the values
	 * @param theMergeControlParameters A set of parameters to control the operation
	 * @return true if the source item already exists in the list of target items
	 */
	private static boolean containsCodeableConcept(
			IBase theSourceItem,
			List<IBase> theTargetItems,
			FhirTerser theTerser,
			MergeControlParameters theMergeControlParameters) {
		Method shallowEquals = getMethod(theSourceItem, "equalsShallow");
		List<IBase> shallowMatches = theTargetItems.stream()
				.filter(targetItem -> evaluateEquality(targetItem, theSourceItem, shallowEquals))
				.toList();

		if (theMergeControlParameters.isIgnoreCodeableConceptCodingOrder()) {
			return shallowMatches.stream().anyMatch(targetItem -> {
				List<IBase> sourceCodings = theTerser.getValues(theSourceItem, "coding");
				List<IBase> targetCodings = theTerser.getValues(targetItem, "coding");
				return sourceCodings.stream().allMatch(sourceCoding -> {
					Method deepEquals = getMethod(sourceCoding, EQUALS_DEEP);
					return targetCodings.stream()
							.anyMatch(targetCoding -> evaluateEquality(sourceCoding, targetCoding, deepEquals));
				});
			});
		} else {
			return shallowMatches.stream().anyMatch(targetItem -> {
				boolean match = true;
				List<IBase> sourceCodings = theTerser.getValues(theSourceItem, "coding");
				List<IBase> targetCodings = theTerser.getValues(targetItem, "coding");
				if (sourceCodings.size() == targetCodings.size()) {
					for (int i = 0; i < sourceCodings.size(); i++) {
						Method deepEquals = getMethod(sourceCodings.get(i), EQUALS_DEEP);
						match &= evaluateEquality(sourceCodings.get(i), targetCodings.get(i), deepEquals);
					}
				} else {
					match = false;
				}
				return match;
			});
		}
	}

	private static boolean hasDataAbsentReason(IBase theItem) {
		if (theItem instanceof IBaseHasExtensions hasExtensions) {
			return hasExtensions.getExtension().stream()
					.anyMatch(t -> Strings.CS.equals(t.getUrl(), DATA_ABSENT_REASON_EXTENSION_URI));
		}
		return false;
	}

	private static List<IBase> removeDataAbsentReason(
			IBase theFhirElement, BaseRuntimeChildDefinition theFieldDefinition, List<IBase> theFieldValues) {
		for (int i = 0; i < theFieldValues.size(); i++) {
			if (hasDataAbsentReason(theFieldValues.get(i))) {
				try {
					theFieldDefinition.getMutator().remove(theFhirElement, i);
				} catch (UnsupportedOperationException e) {
					// the field must be single-valued, just clear it
					theFieldDefinition.getMutator().setValue(theFhirElement, null);
				}
			}
		}
		return theFieldDefinition.getAccessor().getValues(theFhirElement);
	}

	/**
	 * Creates a new element taking into consideration elements with choice that are not directly retrievable by element
	 * name
	 *
	 * @param theFhirTerser      A terser instance for the FHIR release
	 * @param theChildDefinition Child to create a new instance for
	 * @param theFromFieldValue  The base parent field
	 * @return Returns the new element with the given value if configured
	 */
	private static IBase newElement(
			FhirTerser theFhirTerser, BaseRuntimeChildDefinition theChildDefinition, IBase theFromFieldValue) {
		BaseRuntimeElementDefinition<?> runtimeElementDefinition;
		if (theChildDefinition instanceof RuntimeChildChoiceDefinition) {
			runtimeElementDefinition =
					theChildDefinition.getChildElementDefinitionByDatatype(theFromFieldValue.getClass());
		} else {
			runtimeElementDefinition = theChildDefinition.getChildByName(theChildDefinition.getElementName());
		}
		if ("contained".equals(runtimeElementDefinition.getName())) {
			IBaseResource sourceResource = (IBaseResource) theFromFieldValue;
			return theFhirTerser.clone(sourceResource);
		} else {
			return runtimeElementDefinition.newInstance();
		}
	}
}
