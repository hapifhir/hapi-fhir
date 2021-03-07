package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.mdm.util.GoldenResourceHelper.FIELD_NAME_IDENTIFIER;
import static org.slf4j.LoggerFactory.getLogger;

public final class TerserUtil {
	private static final Logger ourLog = getLogger(TerserUtil.class);

	public static final Collection<String> IDS_AND_META_EXCLUDES =
		Collections.unmodifiableSet(Stream.of("id", "identifier", "meta").collect(Collectors.toSet()));

	public static final Predicate<String> EXCLUDE_IDS_AND_META = new Predicate<String>() {
		@Override
		public boolean test(String s) {
			return !IDS_AND_META_EXCLUDES.contains(s);
		}
	};

	public static final Predicate<String> INCLUDE_ALL = new Predicate<String>() {
		@Override
		public boolean test(String s) {
			return true;
		}
	};

	private TerserUtil() {
	}

	/**
	 * Clones the specified canonical EID into the identifier field on the resource
	 *
	 * @param theFhirContext         Context to pull resource definitions from
	 * @param theResourceToCloneInto Resource to set the EID on
	 * @param theEid                 EID to be set
	 */
	public static void cloneEidIntoResource(FhirContext theFhirContext, IBaseResource theResourceToCloneInto, CanonicalEID theEid) {
		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResourceToCloneInto);
		// hapi has 2 metamodels: for children and types
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		cloneEidIntoResource(theFhirContext, resourceIdentifier, IdentifierUtil.toId(theFhirContext, theEid), theResourceToCloneInto);
	}

	/**
	 * Given an Child Definition of `identifier`, a R4/DSTU3 EID Identifier, and a new resource, clone the EID into that resources' identifier list.
	 */
	public static void cloneEidIntoResource(FhirContext theFhirContext, BaseRuntimeChildDefinition theIdentifierDefinition, IBase theEid, IBase theResourceToCloneEidInto) {
		// FHIR choice types - fields within fhir where we have a choice of ids
		BaseRuntimeElementCompositeDefinition<?> childIdentifier = (BaseRuntimeElementCompositeDefinition<?>) theIdentifierDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		IBase resourceNewIdentifier = childIdentifier.newInstance();

		FhirTerser terser = theFhirContext.newTerser();
		terser.cloneInto(theEid, resourceNewIdentifier, true);
		theIdentifierDefinition.getMutator().addValue(theResourceToCloneEidInto, resourceNewIdentifier);
	}

	/**
	 * Checks if the specified fields has any values
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theResource    Resource to check if the specified field is set
	 * @param theFieldName   name of the field to check
	 * @return Returns true if field exists and has any values set, and false otherwise
	 */
	public static boolean hasValues(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResource);
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(theFieldName);
		if (resourceIdentifier == null) {
			return false;
		}
		return !(resourceIdentifier.getAccessor().getValues(theResource).isEmpty());
	}
	/**
	 * get the Values of a specified field.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theResource    Resource to check if the specified field is set
	 * @param theFieldName   name of the field to check
	 * @return Returns true if field exists and has any values set, and false otherwise
	 */
	public static List<IBase> getValues(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResource);
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(theFieldName);
		if (resourceIdentifier == null) {
			ourLog.info("There is no field named {} in Resource {}", theFieldName, resourceDefinition.getName());
			return null;
		}
		return resourceIdentifier.getAccessor().getValues(theResource);
	}

	/**
	 * Clones specified composite field (collection). Composite field values must confirm to the collections
	 * contract.
	 *
	 * @param theFrom Resource to clone the specified filed from
	 * @param theTo   Resource to clone the specified filed to
	 * @param field   Field name to be copied
	 */
	public static void cloneCompositeField(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, String field) {
		FhirTerser terser = theFhirContext.newTerser();

		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(field);

		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

		for (IBase theFromFieldValue : theFromFieldValues) {
			if (containsPrimitiveValue(theFromFieldValue, theToFieldValues)) {
				continue;
			}

			IBase newFieldValue = childDefinition.getChildByName(field).newInstance();
			terser.cloneInto(theFromFieldValue, newFieldValue, true);

			try {
				theToFieldValues.add(newFieldValue);
			} catch (Exception e) {
				childDefinition.getMutator().setValue(theTo, newFieldValue);
			}
		}
	}

	private static boolean containsPrimitiveValue(IBase theItem, List<IBase> theItems) {
		PrimitiveTypeEqualsPredicate predicate = new PrimitiveTypeEqualsPredicate();
		return theItems.stream().anyMatch(i -> {
			return predicate.test(i, theItem);
		});
	}

	private static boolean contains(IBase theItem, List<IBase> theItems) {
		Method method = null;
		for (Method m : theItem.getClass().getDeclaredMethods()) {
			if (m.getName().equals("equalsDeep")) {
				method = m;
				break;
			}
		}

		final Method m = method;
		return theItems.stream().anyMatch(i -> {
			if (m != null) {
				try {
					return (Boolean) m.invoke(theItem, i);
				} catch (Exception e) {
					throw new RuntimeException("Unable to compare equality via equalsDeep", e);
				}
			}
			return theItem.equals(i);
		});
	}

	public static void mergeAllFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo) {
		mergeFields(theFhirContext, theFrom, theTo, INCLUDE_ALL);
	}

	public static void replaceFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, Predicate<String> inclusionStrategy) {
		FhirTerser terser = theFhirContext.newTerser();

		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		for (BaseRuntimeChildDefinition childDefinition : definition.getChildrenAndExtension()) {
			if (!inclusionStrategy.test(childDefinition.getElementName())) {
				continue;
			}

			replaceField(theFrom, theTo, childDefinition);
		}
	}

	public static boolean fieldExists(FhirContext theFhirContext, String theFieldName, IBaseResource theInstance) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theInstance);
		return definition.getChildByName(theFieldName) != null;
	}

	public static void replaceField(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		replaceField(theFhirContext, theFhirContext.newTerser(), theFieldName, theFrom, theTo);
	}

	public static void replaceField(FhirContext theFhirContext, FhirTerser theTerser, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		replaceField(theFrom, theTo, getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theFrom));
	}

	private static void replaceField(IBaseResource theFrom, IBaseResource theTo, BaseRuntimeChildDefinition childDefinition) {
		childDefinition.getAccessor().getFirstValueOrNull(theFrom).ifPresent(v -> {
				childDefinition.getMutator().setValue(theTo, v);
			}
		);
	}

	public static void mergeFieldsExceptIdAndMeta(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo) {
		mergeFields(theFhirContext, theFrom, theTo, EXCLUDE_IDS_AND_META);
	}

	public static void mergeFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, Predicate<String> inclusionStrategy) {
		FhirTerser terser = theFhirContext.newTerser();

		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		for (BaseRuntimeChildDefinition childDefinition : definition.getChildrenAndExtension()) {
			if (!inclusionStrategy.test(childDefinition.getElementName())) {
				continue;
			}

			List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
			List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

			mergeFields(terser, theTo, childDefinition, theFromFieldValues, theToFieldValues);
		}
	}

	/**
	 * Merges value of the specified field from theFrom resource to theTo resource. Fields values are compared via
	 * the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext
	 * @param theFieldName
	 * @param theFrom
	 * @param theTo
	 */
	public static void mergeField(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		mergeField(theFhirContext, theFhirContext.newTerser(), theFieldName, theFrom, theTo);
	}

	/**
	 * Merges value of the specified field from theFrom resource to theTo resource. Fields values are compared via
	 * the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext
	 * @param theTerser
	 * @param theFieldName
	 * @param theFrom
	 * @param theTo
	 */
	public static void mergeField(FhirContext theFhirContext, FhirTerser theTerser, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		BaseRuntimeChildDefinition childDefinition = getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theFrom);

		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

		mergeFields(theTerser, theTo, childDefinition, theFromFieldValues, theToFieldValues);
	}

	private static BaseRuntimeChildDefinition getBaseRuntimeChildDefinition(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(theFieldName);
		if (childDefinition == null) {
			throw new IllegalStateException(String.format("Field %s does not exist", theFieldName));
		}
		return childDefinition;
	}

	private static void mergeFields(FhirTerser theTerser, IBaseResource theTo, BaseRuntimeChildDefinition childDefinition, List<IBase> theFromFieldValues, List<IBase> theToFieldValues) {
		for (IBase theFromFieldValue : theFromFieldValues) {
			if (contains(theFromFieldValue, theToFieldValues)) {
				continue;
			}

			IBase newFieldValue = childDefinition.getChildByName(childDefinition.getElementName()).newInstance();
			theTerser.cloneInto(theFromFieldValue, newFieldValue, true);

			try {
				theToFieldValues.add(newFieldValue);
			} catch (UnsupportedOperationException e) {
				childDefinition.getMutator().setValue(theTo, newFieldValue);
				break;
			}
		}
	}

	public static <T extends IBaseResource> T clone(FhirContext theFhirContext, T theInstance) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theInstance.getClass());
		T retVal = (T) definition.newInstance();

		FhirTerser terser = theFhirContext.newTerser();
		terser.cloneInto(theInstance, retVal, true);
		return retVal;
	}

}
