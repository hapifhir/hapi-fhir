package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DaoRegistry implements ApplicationContextAware {
	private ApplicationContext myAppCtx;

	@Autowired
	private FhirContext myCtx;
	private volatile Map<String, IFhirResourceDao<?>> myResourceNameToResourceDao;
	private volatile IFhirSystemDao<?, ?> mySystemDao;

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		myAppCtx = theApplicationContext;
	}

	public IFhirSystemDao<?, ?> getSystemDao() {
		IFhirSystemDao<?, ?> retVal = mySystemDao;
		if (retVal == null) {
			retVal = myAppCtx.getBean(IFhirSystemDao.class);
			mySystemDao = retVal;
		}
		return retVal;
	}

	public IFhirResourceDao<?> getResourceDao(String theResourceName) {
		IFhirResourceDao<?> retVal = getResourceNameToResourceDao().get(theResourceName);
		if (retVal == null) {
			List<String> supportedResourceTypes = getResourceNameToResourceDao()
				.keySet()
				.stream()
				.sorted()
				.collect(Collectors.toList());
			throw new InvalidRequestException("Unable to process request, this server does not know how to handle resources of type " + theResourceName + " - Can handle: " + supportedResourceTypes);
		}
		return retVal;

	}

	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDao(Class<T> theResourceType) {
		String resourceName = myCtx.getResourceDefinition(theResourceType).getName();
		return (IFhirResourceDao<T>) getResourceDao(resourceName);
	}

	private Map<String, IFhirResourceDao<?>> getResourceNameToResourceDao() {
		Map<String, IFhirResourceDao<?>> retVal = myResourceNameToResourceDao;
		if (retVal == null || retVal.isEmpty()) {
			retVal = new HashMap<>();
			Map<String, IFhirResourceDao> resourceDaos = myAppCtx.getBeansOfType(IFhirResourceDao.class);
			for (IFhirResourceDao nextResourceDao : resourceDaos.values()) {
				RuntimeResourceDefinition nextResourceDef = myCtx.getResourceDefinition(nextResourceDao.getResourceType());
				retVal.put(nextResourceDef.getName(), nextResourceDao);
			}
			myResourceNameToResourceDao = retVal;
		}
		return retVal;
	}

}
