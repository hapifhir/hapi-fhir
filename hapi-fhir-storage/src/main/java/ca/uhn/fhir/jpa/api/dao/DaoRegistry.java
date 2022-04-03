package ca.uhn.fhir.jpa.api.dao;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DaoRegistry implements ApplicationContextAware, IDaoRegistry {
	private ApplicationContext myAppCtx;

	@Autowired
	private FhirContext myContext;
	private volatile Map<String, IFhirResourceDao<?>> myResourceNameToResourceDao;
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
		myContext = theFhirContext;
	}

	public void setSupportedResourceTypes(Collection<String> theSupportedResourceTypes) {
		HashSet<String> supportedResourceTypes = new HashSet<>();
		if (theSupportedResourceTypes != null) {
			supportedResourceTypes.addAll(theSupportedResourceTypes);
		}
		mySupportedResourceTypes = supportedResourceTypes;
		myResourceNameToResourceDao = null;

	}

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		myAppCtx = theApplicationContext;
	}

	public IFhirSystemDao getSystemDao() {
		IFhirSystemDao retVal = mySystemDao;
		if (retVal == null) {
			retVal = myAppCtx.getBean(IFhirSystemDao.class);
			mySystemDao = retVal;
		}
		return retVal;
	}

	/**
	 * @throws InvalidRequestException If the given resource type is not supported
	 */
	public IFhirResourceDao getResourceDao(String theResourceName) {
		IFhirResourceDao<IBaseResource> retVal = getResourceDaoOrNull(theResourceName);
		if (retVal == null) {
			List<String> supportedResourceTypes = myResourceNameToResourceDao
				.keySet()
				.stream()
				.sorted()
				.collect(Collectors.toList());
			throw new InvalidRequestException(Msg.code(572) + "Unable to process request, this server does not know how to handle resources of type " + theResourceName + " - Can handle: " + supportedResourceTypes);
		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	public <R extends IBaseResource> IFhirResourceDao<R> getResourceDao(R theResource) {
		return (IFhirResourceDao<R>) getResourceDao(theResource.getClass());
	}

	public <R extends IBaseResource> IFhirResourceDao<R> getResourceDao(Class<R> theResourceType) {
		IFhirResourceDao<R> retVal = getResourceDaoIfExists(theResourceType);
		Validate.notNull(retVal, "No DAO exists for resource type %s - Have: %s", theResourceType, myResourceNameToResourceDao);
		return retVal;
	}

	/**
	 * Use getResourceDaoOrNull
	 */
	@Deprecated
	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDaoIfExists(Class<T> theResourceType) {
		return getResourceDaoOrNull(theResourceType);
	}

	@Nullable
	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDaoOrNull(Class<T> theResourceType) {
		String resourceName = myContext.getResourceType(theResourceType);
		try {
			return (IFhirResourceDao<T>) getResourceDao(resourceName);
		} catch (InvalidRequestException e) {
			return null;
		}
	}

	/**
	 * Use getResourceDaoOrNull
	 */
	@Deprecated
	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDaoIfExists(String theResourceType) {
		return getResourceDaoOrNull(theResourceType);
	}

	@Nullable
	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDaoOrNull(String theResourceName) {
		init();
		return (IFhirResourceDao<T>) myResourceNameToResourceDao.get(theResourceName);
	}

	@Override
	public boolean isResourceTypeSupported(String theResourceType) {
		if (mySupportedResourceTypes == null) {
			return getResourceDaoOrNull(theResourceType) != null;
		}
		return mySupportedResourceTypes.contains(theResourceType);
	}

	private void init() {
		if (myResourceNameToResourceDao != null && !myResourceNameToResourceDao.isEmpty()) {
			return;
		}

		Map<String, IFhirResourceDao> resourceDaos = myAppCtx.getBeansOfType(IFhirResourceDao.class);

		initializeMaps(resourceDaos.values());
	}

	private void initializeMaps(Collection<IFhirResourceDao> theResourceDaos) {

		myResourceNameToResourceDao = new HashMap<>();

		for (IFhirResourceDao nextResourceDao : theResourceDaos) {
			Class resourceType = nextResourceDao.getResourceType();
			assert resourceType != null;
			RuntimeResourceDefinition nextResourceDef = myContext.getResourceDefinition(resourceType);
			if (mySupportedResourceTypes == null || mySupportedResourceTypes.contains(nextResourceDef.getName())) {
				myResourceNameToResourceDao.put(nextResourceDef.getName(), nextResourceDao);
			}
		}
	}

	public void register(IFhirResourceDao theResourceDao) {
		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceDao.getResourceType());
		String resourceName = resourceDef.getName();
		myResourceNameToResourceDao.put(resourceName, theResourceDao);
	}

	public IFhirResourceDao getDaoOrThrowException(Class<? extends IBaseResource> theClass) {
		IFhirResourceDao retVal = getResourceDao(theClass);
		if (retVal == null) {
			List<String> supportedResourceNames = myResourceNameToResourceDao
				.keySet()
				.stream()
				.map(t -> myContext.getResourceType(t))
				.sorted()
				.collect(Collectors.toList());
			throw new InvalidRequestException(Msg.code(573) + "Unable to process request, this server does not know how to handle resources of type " + myContext.getResourceType(theClass) + " - Can handle: " + supportedResourceNames);
		}
		return retVal;
	}

	public void setResourceDaos(Collection<IFhirResourceDao> theResourceDaos) {
		initializeMaps(theResourceDaos);
	}

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
}
