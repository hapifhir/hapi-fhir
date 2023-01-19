package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.MetaUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

/**
 * This is an experiment to see if we can make test data creation for storage unit tests a bit more readable.
 */
@SuppressWarnings({"unchecked", "ConstantConditions"})
public interface ITestDataBuilder {
	Logger ourLog = LoggerFactory.getLogger(ITestDataBuilder.class);

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


	/** Patient.name.given */
	default <T extends IBaseResource>  Consumer<T> withGiven(String theName) {
		return withPrimitiveAttribute("name.given", theName);
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
	 * Set Observation.effectiveDate
	 */
	default Consumer<IBaseResource> withEffectiveDate(String theDate) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "effectiveDateTime", "dateTime", theDate);
	}

	/**
	 * Set Observation.effectiveDate
	 */
	default Consumer<IBaseResource> withDateTimeAt(String thePath, String theDate) {
		return t -> __setPrimitiveChild(getFhirContext(), t, thePath, "dateTime", theDate);
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
		return t -> t.getMeta().addTag().setSystem(theSystem).setCode(theCode);
	}

	default Consumer<IBaseResource> withSecurity(String theSystem, String theCode) {
		return t -> t.getMeta().addSecurity().setSystem(theSystem).setCode(theCode);
	}

	default Consumer<IBaseResource> withProfile(String theProfile) {
		return t -> t.getMeta().addProfile(theProfile);
	}

	default Consumer<IBaseResource> withSource(FhirContext theContext, String theSource) {
		return t -> MetaUtil.setSource(theContext, t.getMeta(), theSource);
	}

	default Consumer<IBaseResource> withLastUpdated(Date theLastUpdated) {
		return t -> t.getMeta().setLastUpdated(theLastUpdated);
	}

	default Consumer<IBaseResource> withLastUpdated(String theIsoDate) {
		return t -> t.getMeta().setLastUpdated(new InstantType(theIsoDate).getValue());
	}

	default IIdType createEncounter(Consumer<IBaseResource>... theModifiers) {
		return createResource("Encounter", theModifiers);
	}

	default IIdType createObservation(Consumer<IBaseResource>... theModifiers) {
		return createResource("Observation", theModifiers);
	}

	default IIdType createObservation(Collection<Consumer<IBaseResource>> theModifiers) {
		return createResource("Observation", theModifiers.toArray(new Consumer[0]));
	}

	default IBaseResource buildPatient(Consumer<IBaseResource>... theModifiers) {
		return buildResource("Patient", theModifiers);
	}
	default IIdType createPatient(Consumer<IBaseResource>... theModifiers) {
		return createResource("Patient", theModifiers);
	}

	default IIdType createOrganization(Consumer<IBaseResource>... theModifiers) {
		return createResource("Organization", theModifiers);
	}

	default IIdType createResource(String theResourceType, Consumer<IBaseResource>... theModifiers) {
		IBaseResource resource = buildResource(theResourceType, theModifiers);

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Creating {}", getFhirContext().newJsonParser().encodeResourceToString(resource));
		}

		if (isNotBlank(resource.getIdElement().getValue())) {
			return doUpdateResource(resource);
		} else {
			return doCreateResource(resource);
		}
	}

	default IIdType createResourceFromJson(String theJson, Consumer<IBaseResource>... theModifiers) {
		IBaseResource resource = getFhirContext().newJsonParser().parseResource(theJson);
		applyElementModifiers(resource, theModifiers);

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Creating {}", getFhirContext().newJsonParser().encodeResourceToString(resource));
		}

		if (isNotBlank(resource.getIdElement().getValue())) {
			return doUpdateResource(resource);
		} else {
			return doCreateResource(resource);
		}
	}

	default IBaseResource buildResource(String theResourceType, Consumer<IBaseResource>... theModifiers) {
		IBaseResource resource = getFhirContext().getResourceDefinition(theResourceType).newInstance();
		applyElementModifiers(resource, theModifiers);
		return resource;
	}


	default Consumer<IBaseResource> withSubject(@Nullable IIdType theSubject) {
		return withReference("subject", theSubject);
	}

	default Consumer<IBaseResource> withSubject(@Nullable String theSubject) {
		return withSubject(new IdType(theSubject));
	}

	default Consumer<IBaseResource> withPatient(@Nullable IIdType theSubject) {
		return withReference("patient", theSubject);
	}

	default Consumer<IBaseResource> withPatient(@Nullable String theSubject) {
		return withSubject(new IdType(theSubject));
	}


	default Consumer<IBaseResource> withEncounter(@Nullable String theEncounter) {
		return withReference("encounter", new IdType(theEncounter));
	}

	@Nonnull
	private Consumer<IBaseResource> withReference(String theReferenceName, @Nullable IIdType theReferenceValue) {
		return t -> {
			if (theReferenceValue != null && theReferenceValue.getValue() != null) {
				IBaseReference reference = (IBaseReference) getFhirContext().getElementDefinition("Reference").newInstance();
				reference.setReference(theReferenceValue.getValue());

				RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(t);
				resourceDef.getChildByName(theReferenceName).getMutator().addValue(t, reference);
			}
		};
	}

	default <T extends IBase> Consumer<T> withPrimitiveAttribute(String thePath, Object theValue) {
		return t->{
			FhirTerser terser = getFhirContext().newTerser();
			terser.addElement(t, thePath, ""+theValue);
		};
	}

	default <T extends IBase, E extends IBase> Consumer<T> withElementAt(String thePath, Consumer<E>... theModifiers) {
		return t->{
			FhirTerser terser = getFhirContext().newTerser();
			E element = terser.addElement(t, thePath);
			applyElementModifiers(element, theModifiers);
		};
	}

	default <T extends IBase> Consumer<T> withQuantityAtPath(String thePath, Number theValue, String theSystem, String theCode) {
		return withElementAt(thePath,
			withPrimitiveAttribute("value", theValue),
			withPrimitiveAttribute("system", theSystem),
			withPrimitiveAttribute("code", theCode)
		);
	}


	/**
	 * Create an Element and apply modifiers
	 * @param theElementType the FHIR Element type to create
	 * @param theModifiers modifiers to apply after construction
	 * @return the Element
	 */
	default IBase withElementOfType(String theElementType, Consumer<IBase>... theModifiers) {
		IBase element = getFhirContext().getElementDefinition(theElementType).newInstance();
		applyElementModifiers(element, theModifiers);
		return element;
	}

	default <E extends IBase> void applyElementModifiers(E element, Consumer<E>[] theModifiers) {
		for (Consumer<E> nextModifier : theModifiers) {
			nextModifier.accept(element);
		}
	}

	default Consumer<IBaseResource> withObservationCode(@Nullable String theSystem, @Nullable String theCode) {
		return withObservationCode(theSystem, theCode, null);
	}

	default Consumer<IBaseResource> withObservationCode(@Nullable String theSystem, @Nullable String theCode, @Nullable String theDisplay) {
		return withCodingAt("code.coding", theSystem, theCode, theDisplay);
	}

	default <T extends IBase> Consumer<T> withCodingAt(String thePath, @Nullable String theSystem, @Nullable String theValue) {
		return withCodingAt(thePath, theSystem, theValue, null);
	}

	default <T extends IBase> Consumer<T> withCodingAt(String thePath, @Nullable String theSystem, @Nullable String theValue, @Nullable String theDisplay) {
		return withElementAt(thePath,
			withPrimitiveAttribute("system", theSystem),
			withPrimitiveAttribute("code", theValue),
			withPrimitiveAttribute("display", theDisplay)
		);
	}

	default <T extends IBaseResource, E extends IBase> Consumer<T> withObservationComponent(Consumer<E>... theModifiers) {
		return withElementAt("component", theModifiers);
	}

	default Consumer<IBaseResource> withObservationHasMember(@Nullable IIdType theHasMember) {
		return withReference("hasMember", theHasMember);
	}

	default Consumer<IBaseResource> withOrganization(@Nullable IIdType theHasMember) {
		return withReference("managingOrganization", theHasMember);
	}

	// todo mb extract these to something like TestDataBuilderBacking.  Maybe split out create* into child interface since people skip it.
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

	interface Support {
		FhirContext getFhirContext();
		IIdType doCreateResource(IBaseResource theResource);
		IIdType doUpdateResource(IBaseResource theResource);
	}

	// todo mb make this the norm.
	interface WithSupport extends ITestDataBuilder {
		Support getTestDataBuilderSupport();

		@Override
		default FhirContext getFhirContext() {
			return getTestDataBuilderSupport().getFhirContext();

		}

		@Override
		default IIdType doCreateResource(IBaseResource theResource) {
			return getTestDataBuilderSupport().doCreateResource(theResource);
		}

		@Override
		default IIdType doUpdateResource(IBaseResource theResource) {
			return getTestDataBuilderSupport().doUpdateResource(theResource);
		}
	}

	/**
	 * Dummy support to use ITestDataBuilder as just a builder, not a DAO
	 * todo mb Maybe we should split out the builder into a super-interface and drop this?
	 */
	class SupportNoDao implements Support {
		final FhirContext myFhirContext;

		public SupportNoDao(FhirContext theFhirContext) {
			myFhirContext = theFhirContext;
		}

		@Override
		public FhirContext getFhirContext() {
			return myFhirContext;
		}

		@Override
		public IIdType doCreateResource(IBaseResource theResource) {
			Validate.isTrue(false, "Create not supported");
			return null;
		}

		@Override
		public IIdType doUpdateResource(IBaseResource theResource) {
			Validate.isTrue(false, "Update not supported");
			return null;
		}
	}
}
