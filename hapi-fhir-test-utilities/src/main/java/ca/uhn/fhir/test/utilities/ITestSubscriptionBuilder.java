/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Objects;

public interface ITestSubscriptionBuilder extends ITestDataBuilder {
	/**
	 * This is an internal API to this interface.
	 */
	static void setElementValue(FhirContext theFhirContext, IBase theTarget, String theElementName, String theValue) {
		BaseRuntimeElementCompositeDefinition<? extends IBase> def = (BaseRuntimeElementCompositeDefinition<? extends IBase>) theFhirContext.getElementDefinition(theTarget.getClass());
		BaseRuntimeChildDefinition activeChild = def.getChildByName(theElementName);

		IPrimitiveType<?> booleanType = (IPrimitiveType<?>) activeChild.getChildByName(theElementName).newInstance(activeChild.getInstanceConstructorArguments());
		booleanType.setValueAsString(theValue);
		activeChild.getMutator().addValue(theTarget, booleanType);
	}

	/**
	 * Build Subscription resource
	 */
	default IBaseResource buildSubscription(ICreationArgument... theModifiers) {
		return buildResource("Subscription", theModifiers);
	}

	/**
	 * Set Subscription.criteria
	 */
	default ICreationArgument withCriteria(String theCriteria) {
		return t -> setElementValue(getFhirContext(), t, "criteria", theCriteria);
	}

	/**
	 * Set Subscription.reason
	 */
	default ICreationArgument withReason(String theReason) {
		return t -> setElementValue(getFhirContext(), t, "reason", theReason);
	}

	/**
	 * Set Subscription.channel
	 */
	default ICreationArgument withChannel(ICreationArgument... theModifiers) {
		return withElementAt("channel", theModifiers);
	}

	/**
	 * Set Subscription.channel.endpoint
	 */
	default ICreationArgument withEndpoint(String theEndpoint) {
		return t -> setElementValue(getFhirContext(), t, "endpoint", theEndpoint);
	}

	/**
	 * Set Subscription.channel.payload
	 */
	default ICreationArgument withPayload(String thePayload) {
		return t -> setElementValue(getFhirContext(), t, "payload", thePayload);
	}

	/**
	 * Set Subscription.channel.type
	 */
	default ICreationArgument withType(String theType) {
		return t -> setElementValue(getFhirContext(), t, "type", theType);
	}


	default ICreationArgument withChannelExtension(String theUrl, String theValue) {
		return t -> {
			if (! (t instanceof IBaseHasExtensions)) {
				throw new IllegalArgumentException("Resource does not support extensions");
			}
			IBaseExtension<?, ?> ext = ((IBaseHasExtensions) t).addExtension();
			ext.setUrl(theUrl);
			//noinspection unchecked
			IPrimitiveType<String> val = (IPrimitiveType<String>)
					Objects.requireNonNull(getFhirContext().getElementDefinition("string")).newInstance();
			val.setValueAsString(theValue);
			ext.setValue(val);
		};
	}
}
