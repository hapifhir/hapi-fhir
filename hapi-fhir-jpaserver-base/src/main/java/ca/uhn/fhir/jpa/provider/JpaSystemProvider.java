package ca.uhn.fhir.jpa.provider;

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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class JpaSystemProvider extends BaseJpaProvider {

	private IFhirSystemDao myDao;

	public JpaSystemProvider() {
		// nothing
	}

	public JpaSystemProvider(IFhirSystemDao theDao) {
		myDao = theDao;
	}

	@Required
	public void setDao(IFhirSystemDao theDao) {
		myDao = theDao;
	}

	@Transaction
	public List<IResource> transaction(HttpServletRequest theRequest, @TransactionParam List<IResource> theResources) {
		startRequest(theRequest);
		try {
			return myDao.transaction(theResources);
		} finally {
			endRequest(theRequest);
		}
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
