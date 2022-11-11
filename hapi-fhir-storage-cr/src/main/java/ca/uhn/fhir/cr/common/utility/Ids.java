package ca.uhn.fhir.cr.common.utility;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Ids {

	private Ids() {
	}

	/**
	 * Creates the appropriate IIdType for a given ResourceTypeClass
	 * 
	 * @param <ResourceType>       an IBase type
	 * @param <IdType>             an IIdType type
	 * @param theResourceTypeClass the type of the Resource to create an Id for
	 * @param theId                the String representation of the Id to generate
	 * @return the id
	 */
	public static <ResourceType extends IBaseResource, IdType extends IIdType> IdType newId(
			Class<? extends ResourceType> theResourceTypeClass, String theId) {
		checkNotNull(theResourceTypeClass);
		checkNotNull(theId);

		FhirVersionEnum versionEnum = FhirVersions.forClass(theResourceTypeClass);
		return newId(versionEnum, theResourceTypeClass.getSimpleName(), theId);
	}

	/**
	 * Creates the appropriate IIdType for a given BaseTypeClass
	 * 
	 * @param <BaseType>       an IBase type
	 * @param <IdType>         an IIdType type
	 * @param theBaseTypeClass the BaseTypeClass to use for for determining the FHIR
	 *                         Version
	 * @param theResourceName  the type of the Resource to create an Id for
	 * @param theId            the String representation of the Id to generate
	 * @return the id
	 */
	public static <BaseType extends IBase, IdType extends IIdType> IdType newId(
			Class<? extends BaseType> theBaseTypeClass, String theResourceName, String theId) {
		checkNotNull(theBaseTypeClass);
		checkNotNull(theResourceName);
		checkNotNull(theId);

		FhirVersionEnum versionEnum = FhirVersions.forClass(theBaseTypeClass);
		return newId(versionEnum, theResourceName, theId);
	}

	/**
	 * Creates the appropriate IIdType for a given FhirContext
	 * 
	 * @param <IdType>        an IIdType type
	 * @param theFhirContext  the FhirContext to use for Id generation
	 * @param theResourceType the type of the Resource to create an Id for
	 * @param theId           the String representation of the Id to generate
	 * @return the id
	 */
	public static <IdType extends IIdType> IdType newId(FhirContext theFhirContext, String theResourceType,
			String theId) {
		checkNotNull(theFhirContext);
		checkNotNull(theResourceType);
		checkNotNull(theId);

		return newId(theFhirContext.getVersion().getVersion(), theResourceType, theId);
	}

	/**
	 * Creates the appropriate IIdType for a given FhirVersionEnum
	 * 
	 * @param <IdType>           an IIdType type
	 * @param theFhirVersionEnum the FHIR version to generate an Id for
	 * @param theResourceType    the type of the Resource to create an Id for
	 * @param theIdPart          the String representation of the Id to generate
	 * @return the id
	 */
	public static <IdType extends IIdType> IdType newId(FhirVersionEnum theFhirVersionEnum, String theResourceType,
			String theIdPart) {
		checkNotNull(theFhirVersionEnum);
		checkNotNull(theResourceType);
		checkNotNull(theIdPart);

		return newId(theFhirVersionEnum, theResourceType + "/" + theIdPart);
	}

	/**
	 * Creates the appropriate IIdType for a given FhirContext
	 * 
	 * @param <IdType>       an IIdType type
	 * @param theFhirContext the FhirContext to use for Id generation
	 * @param theId          the String representation of the Id to generate
	 * @return the id
	 */
	public static <IdType extends IIdType> IdType newId(FhirContext theFhirContext, String theId) {
		checkNotNull(theFhirContext);
		checkNotNull(theId);

		return newId(theFhirContext.getVersion().getVersion(), theId);
	}

	/**
	 * The gets the "simple" Id for the Resource, without qualifiers or versions.
	 * For example, "Patient/123".
	 * <p>
	 * This is shorthand for
	 * resource.getIdElement().toUnqualifiedVersionless().getValue()
	 * 
	 * @param resource the Resource to get the Id for
	 * @return the simple Id
	 */
	public static String simple(IBaseResource resource) {
		checkNotNull(resource);
		checkArgument(resource.getIdElement() != null);

		return simple(resource.getIdElement());
	}

	/**
	 * The gets the "simple" Id for the Id, without qualifiers or versions. For
	 * example, "Patient/123".
	 * <p>
	 * This is shorthand for id.toUnqualifiedVersionless().getValue()
	 * 
	 * @param id the IIdType to get the Id for
	 * @return the simple Id
	 */
	public static String simple(IIdType id) {
		checkNotNull(id);
		checkArgument(id.hasResourceType());
		checkArgument(id.hasIdPart());

		return id.toUnqualifiedVersionless().getValue();
	}

	/**
	 * The gets the "simple" Id part for the Id, without qualifiers or versions or
	 * the resource Prefix. For example, "123".
	 * <p>
	 * This is shorthand for
	 * resource.getIdElement().toUnqualifiedVersionless().getIdPart()
	 * 
	 * @param resource the Resource to get the Id for
	 * @return the simple Id part
	 */
	public static String simplePart(IBaseResource resource) {
		checkNotNull(resource);
		checkArgument(resource.getIdElement() != null);

		return simplePart(resource.getIdElement());
	}

	/**
	 * The gets the "simple" Id part for the Id, without qualifiers or versions or
	 * the resource Prefix. For example, "123".
	 * <p>
	 * This is shorthand for id.toUnqualifiedVersionless().getIdPart()
	 * 
	 * @param id the IIdType to get the Id for
	 * @return the simple Id part
	 */
	public static String simplePart(IIdType id) {
		checkNotNull(id);
		checkArgument(id.hasResourceType());
		checkArgument(id.hasIdPart());

		return id.toUnqualifiedVersionless().getIdPart();
	}

	/**
	 * Creates the appropriate IIdType for a given FhirVersionEnum
	 * 
	 * @param <IdType>           an IIdType type
	 * @param theFhirVersionEnum the FHIR version to generate an Id for
	 * @param theId              the String representation of the Id to generate
	 * @return the id
	 */
	@SuppressWarnings("unchecked")
	public static <IdType extends IIdType> IdType newId(FhirVersionEnum theFhirVersionEnum, String theId) {
		checkNotNull(theFhirVersionEnum);
		checkNotNull(theId);

		switch (theFhirVersionEnum) {
			case DSTU2:
				return (IdType) new ca.uhn.fhir.model.primitive.IdDt(theId);
			case DSTU2_1:
				return (IdType) new org.hl7.fhir.dstu2016may.model.IdType(theId);
			case DSTU2_HL7ORG:
				return (IdType) new org.hl7.fhir.dstu2.model.IdType(theId);
			case DSTU3:
				return (IdType) new org.hl7.fhir.dstu3.model.IdType(theId);
			case R4:
				return (IdType) new org.hl7.fhir.r4.model.IdType(theId);
			case R4B:
				return (IdType) new org.hl7.fhir.r4b.model.IdType(theId);
			case R5:
				return (IdType) new org.hl7.fhir.r5.model.IdType(theId);
			default:
				throw new IllegalArgumentException(String.format("newId does not support FHIR version %s",
						theFhirVersionEnum.getFhirVersionString()));
		}
	}
}
