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

import java.util.List;

import static ca.uhn.fhir.mdm.util.GoldenResourceHelper.FIELD_NAME_IDENTIFIER;

final class TerserUtil {

	private TerserUtil() {
	}

	/**
	 * Clones the specified canonical EID into the identifier field on the resource
	 *
	 * @param theFhirContext Context to pull resource definitions from
	 * @param theResourceToCloneInto Resource to set the EID on
	 * @param theEid EID to be set
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
			if (contains(theFromFieldValue, theToFieldValues)) {
				continue;
			}

			BaseRuntimeElementCompositeDefinition<?> compositeDefinition = (BaseRuntimeElementCompositeDefinition<?>) childDefinition.getChildByName(field);
			IBase newFieldValue = compositeDefinition.newInstance();
			terser.cloneInto(theFromFieldValue, newFieldValue, true);

			theToFieldValues.add(newFieldValue);
		}
	}

	private static boolean contains(IBase theItem, List<IBase> theItems) {
		PrimitiveTypeEqualsPredicate predicate = new PrimitiveTypeEqualsPredicate();
		return theItems.stream().anyMatch(i -> {
			return predicate.test(i, theItem);
		});
	}

}
