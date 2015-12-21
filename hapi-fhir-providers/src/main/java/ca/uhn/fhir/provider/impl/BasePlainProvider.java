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

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.dao.IDao;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.dao.IDaoFactory;

public class BasePlainProvider extends BaseProvider {

	// ///////////////////////////////////
	// ////////   Spring Wiring   ////////
	// ///////////////////////////////////

	private IDaoFactory myDaoFactory;

	@Required
	public void setSystemDaoFactory(IDaoFactory theDaoFactory) {
		this.myDaoFactory = theDaoFactory;
	}

	// ///////////////////////////////////
	// ///////////////////////////////////
	// ///////////////////////////////////

	private IFhirSystemDao<?,?> myDao = null;

	public IFhirSystemDao<?,?> getDao() {
		synchronized(this) {
			if (null == myDao) {
				myDao = myDaoFactory.getSystemDao();
			}
		}
		return myDao;
	}
	
	@Override
	public IDao getBaseDao() {
		return this.getDao();
	}

	public BasePlainProvider() {
		// nothing
	}
}
