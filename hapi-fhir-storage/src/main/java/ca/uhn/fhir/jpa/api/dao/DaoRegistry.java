/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DaoRegistry implements IDaoRegistry {
	@Autowired
	private FhirContext myFhirContext;

	private final Map<String, IFhirResourceDao<?>> myResourceNameToResourceDao = new HashMap<>();
	private volatile IFhirSystemDao<?, ?> mySystemDao;
	private Set<String> mySupportedResourceTypes;

	/**
	 * Constructor
	 */
	public DaoRegistry() {
		this(null);
	}

	/**
	 * Constructor
	 */
	public DaoRegistry(FhirContext theFhirContext) {
		super();
		myFhirContext = theFhirContext;
	}

	public void setSupportedResourceTypes(Collection<String> theSupportedResourceTypes) {
		HashSet<String> supportedResourceTypes = new HashSet<>();
		if (theSupportedResourceTypes != null) {
			supportedResourceTypes.addAll(theSupportedResourceTypes);
		}
		mySupportedResourceTypes = supportedResourceTypes;
	}

	@SuppressWarnings("unchecked")
	public <T, MT> IFhirSystemDao<T, MT> getSystemDao() {
		return (IFhirSystemDao<T, MT>) mySystemDao;
	}

	/**
	 * @throws InvalidRequestException If the given resource type is not supported
	 */
	@SuppressWarnings("unchecked")
	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getResourceDao(String theResourceName) {
		IFhirResourceDao<IBaseResource> retVal = getResourceDaoOrNull(theResourceName);
		if (retVal == null) {
			List<String> supportedResourceTypes =
					myResourceNameToResourceDao.keySet().stream().sorted().toList();
			throw new InvalidRequestException(Msg.code(572)
					+ "Unable to process request, this server does not know how to handle resources of type "
					+ theResourceName + " - Can handle: " + supportedResourceTypes);
		}
		return (D) retVal;
	}

	@SuppressWarnings("unchecked")
	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getResourceDao(R theResource) {
		return (D) getResourceDao(theResource.getClass());
	}

	@SuppressWarnings("unchecked")
	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getResourceDao(Class<R> theResourceType) {
		IFhirResourceDao<R> retVal = getResourceDaoIfExists(theResourceType);
		Validate.notNull(
				retVal, "No DAO exists for resource type %s - Have: %s", theResourceType, myResourceNameToResourceDao);
		return (D) retVal;
	}

	/**
	 * Use getResourceDaoOrNull
	 */
	@Deprecated
	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getResourceDaoIfExists(Class<R> theResourceType) {
		return getResourceDaoOrNull(theResourceType);
	}

	@Nullable
	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getResourceDaoOrNull(Class<R> theResourceType) {
		String resourceName = myFhirContext.getResourceType(theResourceType);
		try {
			return getResourceDao(resourceName);
		} catch (InvalidRequestException e) {
			return null;
		}
	}

	/**
	 * Use getResourceDaoOrNull
	 */
	@Deprecated
	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getResourceDaoIfExists(String theResourceType) {
		return getResourceDaoOrNull(theResourceType);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getResourceDaoOrNull(String theResourceName) {
		return (D) myResourceNameToResourceDao.get(theResourceName);
	}

	@Override
	public boolean isResourceTypeSupported(String theResourceType) {
		if (mySupportedResourceTypes == null) {
			return getResourceDaoOrNull(theResourceType) != null;
		}
		return mySupportedResourceTypes.contains(theResourceType);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void register(@Nonnull IFhirResourceDao theResourceDao) {
		Validate.notNull(theResourceDao, "theResourceDao must not be null");
		Validate.notNull(theResourceDao.getResourceType(), "theResourceDao.getResourceType() must not be null");

		RuntimeResourceDefinition resourceDef =
				myFhirContext.getResourceDefinition((Class<? extends IBaseResource>) theResourceDao.getResourceType());
		String resourceName = resourceDef.getName();

		Validate.isTrue(
				myResourceNameToResourceDao.get(resourceName) == null,
				"Resource type %s is already registered",
				resourceName);
		myResourceNameToResourceDao.put(resourceName, theResourceDao);
	}

	/**
	 * Adds a system DAO to the registry. This method may only be called once.
	 * @param theSystemDao The system DAO (must not be null)
	 */
	public void register(@Nonnull IFhirSystemDao<?, ?> theSystemDao) {
		Validate.notNull(theSystemDao, "theSystemDao must not be null");
		Validate.isTrue(mySystemDao == null, "A system DAO has already been registered");
		mySystemDao = theSystemDao;
	}

	/**
	 * @deprecated use getDaoOrThrow
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public IFhirResourceDao getDaoOrThrowException(Class<? extends IBaseResource> theClass) {
		return getDaoOrThrow(theClass);
	}

	public <R extends IBaseResource, D extends IFhirResourceDao<R>> D getDaoOrThrow(Class<R> theClass) {
		D retVal = getResourceDao(theClass);
		if (retVal == null) {
			List<String> supportedResourceNames = myResourceNameToResourceDao.keySet().stream()
					.map(t -> myFhirContext.getResourceType(t))
					.sorted()
					.toList();
			throw new InvalidRequestException(Msg.code(573)
					+ "Unable to process request, this server does not know how to handle resources of type "
					+ myFhirContext.getResourceType(theClass) + " - Can handle: " + supportedResourceNames);
		}
		return retVal;
	}

	@SuppressWarnings("rawtypes")
	public IFhirResourceDao getSubscriptionDao() {
		return getResourceDao(ResourceTypeEnum.SUBSCRIPTION.getCode());
	}

	public void setSupportedResourceTypes(String... theResourceTypes) {
		setSupportedResourceTypes(toCollection(theResourceTypes));
	}

	private List<String> toCollection(String[] theResourceTypes) {
		List<String> retVal = null;
		if (theResourceTypes != null && theResourceTypes.length > 0) {
			retVal = Arrays.asList(theResourceTypes);
		}
		return retVal;
	}

	public Set<String> getRegisteredDaoTypes() {
		return Collections.unmodifiableSet(myResourceNameToResourceDao.keySet());
	}

	// TODO KHS find all the places where FhirContext and DaoRegistry are both passed into constructors and
	// remove the FhirContext parameter and pull it from the DaoRegistry parameter
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	/**
	 * This method is primarily intended for unit tests. It clears all previously
	 * registered resource providers and any registered system provider.
	 *
	 * @since 8.12.0
	 */
	public void unregisterAll() {
		myResourceNameToResourceDao.clear();
		mySystemDao = null;
	}
}
