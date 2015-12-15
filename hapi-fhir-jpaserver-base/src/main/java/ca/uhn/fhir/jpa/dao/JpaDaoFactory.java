/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

package ca.uhn.fhir.jpa.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.dao.ISearchDao;

/**
 * @author Bill.Denton
 *
 */
public class JpaDaoFactory implements IDaoFactory {
	
	// ///////////////////////////////////////
	// //////////   Spring Wiring   //////////
	// ///////////////////////////////////////
	
	private FhirContext myFhirContext;

	public void setFhirContext(FhirContext myFhirContext) {
		this.myFhirContext = myFhirContext;
	}

	// ///////////////////////////////////////
	// ///////////////////////////////////////
	// ///////////////////////////////////////
	
	private static final Logger ourLog = LoggerFactory.getLogger(JpaDaoFactory.class);
	private Map<Class<? extends IBaseResource>, IFhirResourceDao<? extends IBaseResource>> myResourceTypeToDao = new LinkedHashMap<Class<? extends IBaseResource>, IFhirResourceDao<? extends IBaseResource>>();
	private IFhirSystemDao<?,?> mySystemDao;
	private ISearchDao mySearchDao;

	public void addResourceDao (Class<? extends IBaseResource> theType, IFhirResourceDao<? extends IBaseResource> theProvider) {
		ourLog.debug("Register Resource DAO: {} for {}", new Object[]{theProvider.getClass().getSimpleName(), theType.getName()});
		this.myResourceTypeToDao.put(theType,  theProvider);
	}
	public void setSystemDao(IFhirSystemDao<?,?> mySystemDao) {
		ourLog.debug("Register System DAO: {}", new Object[]{mySystemDao.getClass().getSimpleName()});
		this.mySystemDao = mySystemDao;
	}
	public void setSearchDao(ISearchDao mySearchDao) {
		ourLog.debug("Register Search DAO: {}", new Object[]{mySearchDao.getClass().getSimpleName()});
		this.mySearchDao = mySearchDao;
	}
	
	
	
	@Override
	public FhirContext getFhirContext () {
		return myFhirContext;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBaseResource> IFhirResourceDao<T> getResourceDao (Class<T> theResourceClass) {
		IFhirResourceDao<T> result = (IFhirResourceDao<T>) myResourceTypeToDao.get(theResourceClass);
		ourLog.debug("Fetch Resource DAO for {} -> {}", new Object[]{theResourceClass.getName(), (null==result? "<null>":result.getClass().getSimpleName())});
		return result;
	}


	@Override
	public IFhirSystemDao<?,?> getSystemDao () {
		ourLog.debug("Fetch System DAO -> {}", new Object[]{(null==mySystemDao? "<null>":mySystemDao.getClass().getSimpleName())});
		return mySystemDao;
	}

	
	@Override
	public ISearchDao getSearchDao () {
		ourLog.debug("Fetch Search DAO -> {}", new Object[]{(null==mySearchDao? "<null>":mySearchDao.getClass().getSimpleName())});
		return mySearchDao;
	}
	
	
	@Override
	public String getValidResourceTypes () {
		return myResourceTypeToDao.keySet().toString();
	}

}
