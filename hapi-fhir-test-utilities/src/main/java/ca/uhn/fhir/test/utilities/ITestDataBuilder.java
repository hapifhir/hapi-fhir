/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.MetaUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an experiment to see if we can make test data creation for storage unit tests a bit more readable.
 */
@SuppressWarnings({"unchecked", "ConstantConditions"})
public interface ITestDataBuilder {
	Logger ourLog = LoggerFactory.getLogger(ITestDataBuilder.class);

	/**
	 * Name chosen to avoid potential for conflict. This is an internal API to this interface.
	 */
	static void __setPrimitiveChild(FhirContext theFhirContext, IBase theTarget, String theElementName, String theElementType, String theValue) {
		BaseRuntimeElementCompositeDefinition def = (BaseRuntimeElementCompositeDefinition) theFhirContext.getElementDefinition(theTarget.getClass());
		BaseRuntimeChildDefinition activeChild = def.getChildByName(theElementName);

		IPrimitiveType<?> booleanType = (IPrimitiveType<?>) activeChild.getChildByName(theElementName).newInstance();
		booleanType.setValueAsString(theValue);
		activeChild.getMutator().addValue(theTarget, booleanType);
	}

	/**
	 * Set Patient.active = true
	 */
	default ICreationArgument withActiveTrue() {
		return t -> __setPrimitiveChild(getFhirContext(), t, "active", "boolean", "true");
	}

	/**
	 * Set Patient.active = false
	 */
	default ICreationArgument withActiveFalse() {
		return t -> __setPrimitiveChild(getFhirContext(), t, "active", "boolean", "false");
	}

	/**
	 * Set Resource.language
	 */
	default ICreationArgument withLanguage(String theLanguage) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "language", "string", theLanguage);
	}

	/**
	 * Set Patient.gender
	 */
	default ICreationArgument withGender(String theGender) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "gender", "code", theGender);
	}

	default ICreationArgument withFamily(String theFamily) {
		return t -> {
			IPrimitiveType<?> family = (IPrimitiveType<?>) getFhirContext().getElementDefinition("string").newInstance();
			family.setValueAsString(theFamily);

			BaseRuntimeElementCompositeDefinition<?> humanNameDef = (BaseRuntimeElementCompositeDefinition<?>) getFhirContext().getElementDefinition("HumanName");
			ICompositeType humanName = (ICompositeType) humanNameDef.newInstance();
			humanNameDef.getChildByName("family").getMutator().addValue(humanName, family);

			BaseRuntimeElementCompositeDefinition resourceDef = (BaseRuntimeElementCompositeDefinition) getFhirContext().getElementDefinition(t.getClass());
			resourceDef.getChildByName("name").getMutator().addValue(t, humanName);
		};
	}

	/**
	 * Patient.name.given
	 */
	default ICreationArgument withGiven(String theName) {
		return withResourcePrimitiveAttribute("name.given", theName);
	}

	/**
	 * Set Patient.birthdate
	 */
	default ICreationArgument withBirthdate(String theBirthdate) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "birthDate", "dateTime", theBirthdate);
	}

	/**
	 * Set Observation.status
	 */
	default ICreationArgument withStatus(String theStatus) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "status", "code", theStatus);
	}

	/**
	 * Set Observation.effectiveDate
	 */
	default ICreationArgument withEffectiveDate(String theDate) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "effectiveDateTime", "dateTime", theDate);
	}

	/**
	 * Set Observation.effectiveDate
	 */
	default ICreationArgument withDateTimeAt(String thePath, String theDate) {
		return t -> __setPrimitiveChild(getFhirContext(), t, thePath, "dateTime", theDate);
	}

	/**
	 * Set [Resource].identifier.system and [Resource].identifier.value
	 */
	default ICreationArgument withIdentifier(String theSystem, String theValue) {
		return t -> {
			IPrimitiveType<?> system = (IPrimitiveType<?>) getFhirContext().getElementDefinition("uri").newInstance();
			system.setValueAsString(theSystem);

			IPrimitiveType<?> value = (IPrimitiveType<?>) getFhirContext().getElementDefinition("string").newInstance();
			value.setValueAsString(theValue);

			BaseRuntimeElementCompositeDefinition<?> identifierDef = (BaseRuntimeElementCompositeDefinition<?>) getFhirContext().getElementDefinition("Identifier");
			ICompositeType identifier = (ICompositeType) identifierDef.newInstance();
			identifierDef.getChildByName("system").getMutator().addValue(identifier, system);
			identifierDef.getChildByName("value").getMutator().addValue(identifier, value);

			RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition((Class<? extends IBaseResource>) t.getClass());
			resourceDef.getChildByName("identifier").getMutator().addValue(t, identifier);
		};
	}

	/**
	 * Set Organization.name
	 */
	default ICreationArgument withName(String theStatus) {
		return t -> __setPrimitiveChild(getFhirContext(), t, "name", "string", theStatus);
	}

	default ICreationArgument withId(String theId) {
		return t -> {
			assertThat(theId).matches("[a-zA-Z0-9-]+");
			((IBaseResource)t).setId(theId);
		};
	}

	default ICreationArgument withId(IIdType theId) {
		return t -> ((IBaseResource)t).setId(theId.toUnqualifiedVersionless());
	}

	default ICreationArgument withTag(String theSystem, String theCode, Consumer<IBaseCoding>... theModifiers) {
		return t -> {
			IBaseCoding coding = ((IBaseResource) t).getMeta().addTag().setSystem(theSystem).setCode(theCode);
			applyElementModifiers(coding, theModifiers);
		};
	}

	default ICreationArgument withSecurity(String theSystem, String theCode) {
		return t -> ((IBaseResource)t).getMeta().addSecurity().setSystem(theSystem).setCode(theCode);
	}

	default ICreationArgument withProfile(String theProfile) {
		return t -> ((IBaseResource)t).getMeta().addProfile(theProfile);
	}

	default ICreationArgument withSource(FhirContext theContext, String theSource) {
		return t -> MetaUtil.setSource(theContext, ((IBaseResource)t).getMeta(), theSource);
	}

	default ICreationArgument withSource(String theSource) {
		return t -> MetaUtil.setSource(getFhirContext(), ((IBaseResource)t).getMeta(), theSource);
	}

	default ICreationArgument withLastUpdated(Date theLastUpdated) {
		return t -> ((IBaseResource)t).getMeta().setLastUpdated(theLastUpdated);
	}

	default ICreationArgument withLastUpdated(String theIsoDate) {
		return t -> ((IBaseResource)t).getMeta().setLastUpdated(new InstantType(theIsoDate).getValue());
	}

	default IIdType createEncounter(ICreationArgument... theModifiers) {
		return createResource("Encounter", theModifiers);
	}

	default IIdType createGroup(ICreationArgument... theModifiers) {
		return createResource("Group", theModifiers);
	}

	default IIdType createObservation(ICreationArgument... theModifiers) {
		return createResource("Observation", theModifiers);
	}

	default IIdType createObservation(Collection<ICreationArgument> theModifiers) {
		return createResource("Observation", theModifiers.toArray(new ICreationArgument[0]));
	}

	default IBaseResource buildPatient(ICreationArgument... theModifiers) {
		return buildResource("Patient", theModifiers);
	}

	default IIdType createPatient(ICreationArgument... theModifiers) {
		return createResource("Patient", theModifiers);
	}

	default IIdType createOrganization(ICreationArgument... theModifiers) {
		return createResource("Organization", theModifiers);
	}

	default IIdType createPractitioner(ICreationArgument... theModifiers) {
		return createResource("Practitioner", theModifiers);
	}

	default IIdType createResource(String theResourceType, ICreationArgument... theModifiers) {
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

	default IIdType createResourceFromJson(String theJson, ICreationArgument... theModifiers) {
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

	default <T extends IBaseResource> T buildResource(String theResourceType, ICreationArgument... theModifiers) {
		IBaseResource resource = getFhirContext().getResourceDefinition(theResourceType).newInstance();
		applyElementModifiers(resource, theModifiers);
		return (T) resource;
	}

	default ICreationArgument withSubject(@Nullable IIdType theSubject) {
		return withReference("subject", theSubject);
	}

	default ICreationArgument withSubject(@Nullable String theSubject) {
		return withSubject(new IdType(theSubject));
	}

	default ICreationArgument withPatient(@Nullable IIdType theSubject) {
		return withReference("patient", theSubject);
	}

	default ICreationArgument withPatient(@Nullable String theSubject) {
		return withSubject(new IdType(theSubject));
	}

	default ICreationArgument withGroupMember(@Nullable IIdType theMember) {
		return withResourcePrimitiveAttribute("member.entity.reference", theMember);
	}

	default ICreationArgument withGroupMember(@Nullable String theMember) {
		return withGroupMember(new IdType(theMember));
	}

	default ICreationArgument withEncounter(@Nullable String theEncounter) {
		return withReference("encounter", new IdType(theEncounter));
	}

	@Nonnull
	default ICreationArgument withReference(String theReferenceName, @Nullable IIdType theReferenceValue) {
		return t -> {
			if (theReferenceValue != null && theReferenceValue.getValue() != null) {
				IBaseReference reference = (IBaseReference) getFhirContext().getElementDefinition("Reference").newInstance();
				reference.setReference(theReferenceValue.getValue());

				RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition((IBaseResource) t);
				resourceDef.getChildByName(theReferenceName).getMutator().addValue(t, reference);
			}
		};
	}

	default Consumer<IBase> withPrimitiveAttribute(String thePath, Object theValue) {
		return t -> {
			FhirTerser terser = getFhirContext().newTerser();
			terser.addElement(t, thePath, "" + theValue);
		};
	}

	default ICreationArgument withResourcePrimitiveAttribute(String thePath, Object theValue) {
		return t -> {
			FhirTerser terser = getFhirContext().newTerser();
			terser.addElement(t, thePath, "" + theValue);
		};
	}

	default <E extends IBase> ICreationArgument withElementAt(String thePath, Consumer<E>... theModifiers) {
		return t -> {
			FhirTerser terser = getFhirContext().newTerser();
			E element = terser.addElement(t, thePath);
			applyElementModifiers(element, theModifiers);
		};
	}

	default ICreationArgument withQuantityAtPath(String thePath, Number theValue, String theSystem, String theCode) {
		return withElementAt(thePath,
			withPrimitiveAttribute("value", theValue),
			withPrimitiveAttribute("system", theSystem),
			withPrimitiveAttribute("code", theCode)
		);
	}

	/**
	 * Create an Element and apply modifiers
	 *
	 * @param theElementType the FHIR Element type to create
	 * @param theModifiers   modifiers to apply after construction
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

	default ICreationArgument withObservationCode(@Nullable String theSystem, @Nullable String theCode) {
		return withObservationCode(theSystem, theCode, null);
	}

	default ICreationArgument withObservationCode(@Nullable String theSystem, @Nullable String theCode, @Nullable String theDisplay) {
		return withCodingAt("code.coding", theSystem, theCode, theDisplay);
	}

	default <T extends IBase> ICreationArgument withCodingAt(String thePath, @Nullable String theSystem, @Nullable String theValue) {
		return withCodingAt(thePath, theSystem, theValue, null);
	}

	default <T extends IBase> ICreationArgument withCodingAt(String thePath, @Nullable String theSystem, @Nullable String theValue, @Nullable String theDisplay) {
		return withElementAt(thePath,
			withPrimitiveAttribute("system", theSystem),
			withPrimitiveAttribute("code", theValue),
			withPrimitiveAttribute("display", theDisplay)
		);
	}

	default ICreationArgument withObservationComponent(ICreationArgument... theModifiers) {
		return withElementAt("component", theModifiers);
	}

	default ICreationArgument withObservationHasMember(@Nullable IIdType theHasMember) {
		return withReference("hasMember", theHasMember);
	}

	default ICreationArgument withOrganization(@Nullable IIdType theHasMember) {
		return withReference("managingOrganization", theHasMember);
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

	default ICreationArgument[] asArray(ICreationArgument theIBaseResourceConsumer) {
		return new ICreationArgument[]{theIBaseResourceConsumer};
	}

	interface Support {
		void setRequestId(String theRequestId);

		FhirContext getFhirContext();

		IIdType doCreateResource(IBaseResource theResource);

		IIdType doUpdateResource(IBaseResource theResource);
	}

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

	interface ICreationArgument extends Consumer<IBase> {
		// nothing
	}

	/**
	 * Dummy support to use ITestDataBuilder as just a builder, not a DAO
	 */
	class SupportNoDao implements Support {
		final FhirContext myFhirContext;

		public SupportNoDao(FhirContext theFhirContext) {
			myFhirContext = theFhirContext;
		}

		@Override
		public void setRequestId(String theRequestId) {
			// do nothing
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
