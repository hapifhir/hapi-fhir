package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.thymeleaf.util.Validate;

import java.util.List;
import java.util.Objects;

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
 * Utilities for working with the subscription resource
 */
public class SubscriptionUtil {

	private static void populatePrimitiveValue(FhirContext theContext, IBaseResource theSubscription, String theChildName, String theValue) {
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theSubscription);
		Validate.isTrue(def.getName().equals("Subscription"), "theResource is not a subscription");
		BaseRuntimeChildDefinition statusChild = def.getChildByName(theChildName);
		List<IBase> entries = statusChild.getAccessor().getValues(theSubscription);
		IPrimitiveType<?> instance;
		if (entries.size() == 0) {
			BaseRuntimeElementDefinition<?> statusElement = statusChild.getChildByName(theChildName);
			instance = (IPrimitiveType<?>) statusElement.newInstance(statusChild.getInstanceConstructorArguments());
			statusChild.getMutator().addValue(theSubscription, instance);
		} else {
			instance = (IPrimitiveType<?>) entries.get(0);
		}

		instance.setValueAsString(theValue);
	}

	public static void setReason(FhirContext theContext, IBaseResource theSubscription, String theMessage) {
		populatePrimitiveValue(theContext, theSubscription, "reason", theMessage);
	}

	public static void setStatus(FhirContext theContext, IBaseResource theSubscription, String theStatus) {
		populatePrimitiveValue(theContext, theSubscription, "status", theStatus);
	}

	public static boolean isCrossPartition(IBaseResource theSubscription) {
		if (theSubscription instanceof IBaseHasExtensions) {
			IBaseExtension extension = ExtensionUtil.getExtensionByUrl(theSubscription, HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION);
			if (Objects.nonNull(extension)) {
				try {
					IBaseBooleanDatatype booleanDatatype = (IBaseBooleanDatatype) (extension.getValue());
					return booleanDatatype.getValue();
				} catch (ClassCastException theClassCastException) {
					return false;
				}
			}
		}
		return false;
	}
}
