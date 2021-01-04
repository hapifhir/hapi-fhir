package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * This is an experiment to see if we can make test data creation for storage unit tests a bit more readable.
 */
@SuppressWarnings({"unchecked", "ConstantConditions"})
public interface ITestDataBuilder {

	/**
	 * Set Patient.active = true
	 */
	default Consumer<IBaseResource> withActiveTrue() {
		return t -> __setPrimitiveChild(getFhirContext(), t, "active", "boolean", "true");
	}

	/**
	 * Set Patient.active = false
	 */
	default Consumer<IBaseResource> withActiveFalse() {
		return t -> __setPrimitiveChild(getFhirContext(), t, "active", "boolean", "false");
	}

	default Consumer<IBaseResource> withFamily(String theFamily) {
		return t -> {
			IPrimitiveType<?> family = (IPrimitiveType<?>) getFhirContext().getElementDefinition("string").newInstance();
			family.setValueAsString(theFamily);

			BaseRuntimeElementCompositeDefinition<?> humanNameDef = (BaseRuntimeElementCompositeDefinition<?>) getFhirContext().getElementDefinition("HumanName");
			ICompositeType humanName = (ICompositeType) humanNameDef.newInstance();
			humanNameDef.getChildByName("family").getMutator().addValue(humanName, family);

			RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(t.getClass());
			resourceDef.getChildByName("name").getMutator().addValue(t, humanName);
		};
	}

	/**
	 * Set Patient.birthdate
	 */
	default Consumer<IBaseResource> withBirthdate(String theBirthdate) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "birthDate", "dateTime", theBirthdate);
	}

	/**
	 * Set Observation.status
	 */
	default Consumer<IBaseResource> withStatus(String theStatus) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "status", "code", theStatus);
	}

	/**
	 * Set [Resource].identifier.system and [Resource].identifier.value
	 */
	default Consumer<IBaseResource> withIdentifier(String theSystem, String theValue) {
		return t -> {
			IPrimitiveType<?> system = (IPrimitiveType<?>) getFhirContext().getElementDefinition("uri").newInstance();
			system.setValueAsString(theSystem);

			IPrimitiveType<?> value = (IPrimitiveType<?>) getFhirContext().getElementDefinition("string").newInstance();
			value.setValueAsString(theValue);

			BaseRuntimeElementCompositeDefinition<?> identifierDef = (BaseRuntimeElementCompositeDefinition<?>) getFhirContext().getElementDefinition("Identifier");
			ICompositeType identifier = (ICompositeType) identifierDef.newInstance();
			identifierDef.getChildByName("system").getMutator().addValue(identifier, system);
			identifierDef.getChildByName("value").getMutator().addValue(identifier, value);

			RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(t.getClass());
			resourceDef.getChildByName("identifier").getMutator().addValue(t, identifier);
		};
	}

	/**
	 * Set Organization.name
	 */
	default Consumer<IBaseResource> withName(String theStatus) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "name", "string", theStatus);
	}

	default Consumer<IBaseResource> withId(String theId) {
		return t -> {
			assertThat(theId, matchesPattern("[a-zA-Z0-9-]+"));
			t.setId(theId);
		};
	}

	default Consumer<IBaseResource> withId(IIdType theId) {
		return t -> t.setId(theId.toUnqualifiedVersionless());
	}

	default Consumer<IBaseResource> withTag(String theSystem, String theCode) {
		return t -> t.getMeta().addTag().setSystem(theSystem).setCode(theCode).setDisplay(theCode);
	}

	default IIdType createObservation(Consumer<IBaseResource>... theModifiers) {
		return createResource("Observation", theModifiers);
	}

	default IIdType createPatient(Consumer<IBaseResource>... theModifiers) {
		return createResource("Patient", theModifiers);
	}

	default IIdType createOrganization(Consumer<IBaseResource>... theModifiers) {
		return createResource("Organization", theModifiers);
	}

	default IIdType createResource(String theResourceType, Consumer<IBaseResource>[] theModifiers) {
		IBaseResource resource = getFhirContext().getResourceDefinition(theResourceType).newInstance();
		for (Consumer<IBaseResource> next : theModifiers) {
			next.accept(resource);
		}

		if (isNotBlank(resource.getIdElement().getValue())) {
			return doUpdateResource(resource);
		} else {
			return doCreateResource(resource);
		}
	}


	default Consumer<IBaseResource> withSubject(@Nullable IIdType theSubject) {
		return t -> {
			if (theSubject != null) {
				IBaseReference reference = (IBaseReference) getFhirContext().getElementDefinition("Reference").newInstance();
				reference.setReference(theSubject.getValue());

				RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(t.getClass());
				resourceDef.getChildByName("subject").getMutator().addValue(t, reference);
			}
		};
	}

	default  Consumer<IBaseResource> withObservationHasMember(@Nullable IIdType theHasMember) {
		return t -> {
			if (theHasMember != null) {
				IBaseReference reference = (IBaseReference) getFhirContext().getElementDefinition("Reference").newInstance();
				reference.setReference(theHasMember.getValue());

				RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(t.getClass());
				resourceDef.getChildByName("hasMember").getMutator().addValue(t, reference);
			}
		};
	}

	default  Consumer<IBaseResource> withOrganization(@Nullable IIdType theHasMember) {
		return t -> {
			if (theHasMember != null) {
				IBaseReference reference = (IBaseReference) getFhirContext().getElementDefinition("Reference").newInstance();
				reference.setReference(theHasMember.getValue());

				RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(t.getClass());
				resourceDef.getChildByName("managingOrganization").getMutator().addValue(t, reference);
			}
		};
	}

	/**
	 * Users of this API must implement this method
	 */
	IIdType doCreateResource(IBaseResource theResource);

	/**
	 * Users of this API must implement this method
	 */
	IIdType doUpdateResource(IBaseResource theResource);

	/**
	 * Users of this API must implement this method
	 */
	FhirContext getFhirContext();

	/**
	 * Name chosen to avoid potential for conflict. This is an internal API to this interface.
	 */
	static void __setPrimitiveChild(FhirContext theFhirContext, IBaseResource theTarget, String theElementName, String theElementType, String theValue) {
		RuntimeResourceDefinition def = theFhirContext.getResourceDefinition(theTarget.getClass());
		BaseRuntimeChildDefinition activeChild = def.getChildByName(theElementName);

		IPrimitiveType<?> booleanType = (IPrimitiveType<?>) activeChild.getChildByName(theElementName).newInstance();
		booleanType.setValueAsString(theValue);
		activeChild.getMutator().addValue(theTarget, booleanType);
	}


}
