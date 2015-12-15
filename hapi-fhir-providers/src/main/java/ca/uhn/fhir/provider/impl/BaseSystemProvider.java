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

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.dao.IDao;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.provider.ISystemProvider;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.server.IBundleProvider;

public abstract class BaseSystemProvider<T, MT> extends BaseProvider implements ISystemProvider<T, MT> {

	// ///////////////////////////////////
	// ////////   Spring Wiring   ////////
	// ///////////////////////////////////

	private IDaoFactory myDaoFactory;

	@Required
	public void setSystemDaoFactory(IDaoFactory theDaoFactory) {
		this.myDaoFactory = theDaoFactory;
	}
	public IDaoFactory getDaoFactory() {
		return myDaoFactory;
	}

	// ///////////////////////////////////
	// ///////////////////////////////////
	// ///////////////////////////////////

	private IFhirSystemDao<T, MT> myDao = null;

	@SuppressWarnings("unchecked")
	public IFhirSystemDao<T, MT> getDao() {
		synchronized(this) {
			if (null == myDao) {
				myDao = (IFhirSystemDao<T, MT>)myDaoFactory.getSystemDao();
			}
		}
		return myDao;
	}
	
	@Override
	public IDao getBaseDao() {
		return this.getDao();
	}

	public BaseSystemProvider() {
		// nothing
	}

	@CoverageIgnore
	public BaseSystemProvider(IDaoFactory theDaoFactory) {
		setSystemDaoFactory(theDaoFactory);
	}

	@History
	public IBundleProvider historyServer(HttpServletRequest theRequest, @Since Date theDate) {
		startRequest(theRequest);
		try {
			return myDao.history(theDate);
		} finally {
			endRequest(theRequest);
		}
	}

	@GetTags
	public TagList getAllTagsOnServer(HttpServletRequest theRequest) {
		startRequest(theRequest);
		try {
		return myDao.getAllTags();
		} finally {
			endRequest(theRequest);
		}
	}

}
