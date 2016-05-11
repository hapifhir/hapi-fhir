package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class BaseJpaSystemProvider<T, MT> extends BaseJpaProvider {

	private IFhirSystemDao<T, MT> myDao;

	public BaseJpaSystemProvider() {
		// nothing
	}

	@Required
	public void setDao(IFhirSystemDao<T, MT> theDao) {
		myDao = theDao;
	}

	@History
	public IBundleProvider historyServer(HttpServletRequest theRequest, @Since Date theDate, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			return myDao.history(theDate, theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

	protected IFhirSystemDao<T, MT> getDao() {
		return myDao;
	}

	@GetTags
	public TagList getAllTagsOnServer(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
		return myDao.getAllTags(theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

}
