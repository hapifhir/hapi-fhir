package ca.uhn.fhir.provider.impl;

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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.cobertura.CoverageIgnore;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.dao.IDao;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.provider.IResourceProvider;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.server.IBundleProvider;

public abstract class BaseResourceProvider<T extends IResource> extends BaseProvider implements IResourceProvider<T> {

	// ///////////////////////////////////
	// ////////   Spring Wiring   ////////
	// ///////////////////////////////////

	private Class<? extends IBaseResource> myResourceType;
	private IDaoFactory myDaoFactory;

	@Required
	public void setResourceType(Class<? extends IBaseResource> myResourceType) {
		this.myResourceType = myResourceType;
	}
	@Required
	public void setResourceDaoFactory(IDaoFactory theDaoFactory) {
		this.myDaoFactory = theDaoFactory;
	}

	// ///////////////////////////////////
	// ///////////////////////////////////
	// ///////////////////////////////////

	private IFhirResourceDao<T> myDao;

	@SuppressWarnings("unchecked")
	public IFhirResourceDao<T> getDao() {
		synchronized(this) {
			if (null == myDao) {
				myDao = (IFhirResourceDao<T>)myDaoFactory.getResourceDao(getResourceType());
			}
		}
		return myDao;
	}
	
	@Override
	public IDao getBaseDao() {
		return this.getDao();
	}

	public BaseResourceProvider() {
		// nothing
	}

	@CoverageIgnore
	public BaseResourceProvider(IDaoFactory theDaoFactory) {
		setResourceDaoFactory(theDaoFactory);
	}

	@History
	public IBundleProvider getHistoryForResourceInstance(HttpServletRequest theRequest, @IdParam IdDt theId, @Since Date theDate) {
		startRequest(theRequest);
		try {
			return getDao().history(theId, theDate);
		} finally {
			endRequest(theRequest);
		}
	}

	@History
	public IBundleProvider getHistoryForResourceType(HttpServletRequest theRequest, @Since Date theDate) {
		startRequest(theRequest);
		try {
			return getDao().history(theDate);
		} finally {
			endRequest(theRequest);
		}
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return myResourceType;
	}

	@GetTags
	public TagList getTagsForResourceInstance(HttpServletRequest theRequest, @IdParam IdDt theResourceId) {
		startRequest(theRequest);
		try {
			return getDao().getTags(theResourceId);
		} finally {
			endRequest(theRequest);
		}
	}

	@GetTags
	public TagList getTagsForResourceType(HttpServletRequest theRequest) {
		startRequest(theRequest);
		try {
			return getDao().getAllResourceTags();
		} finally {
			endRequest(theRequest);
		}
	}

	@Read(version = true)
	public T read(HttpServletRequest theRequest, @IdParam IdDt theId) {
		startRequest(theRequest);
		try {
			return getDao().read(theId);
		} finally {
			endRequest(theRequest);
		}
	}

}
