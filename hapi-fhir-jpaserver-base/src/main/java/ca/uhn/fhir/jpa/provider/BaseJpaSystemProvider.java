package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.util.ParametersUtil;

public class BaseJpaSystemProvider<T, MT> extends BaseJpaProvider {

	public static final String MARK_ALL_RESOURCES_FOR_REINDEXING = "$mark-all-resources-for-reindexing";
	
	private IFhirSystemDao<T, MT> myDao;

	public BaseJpaSystemProvider() {
		// nothing
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

	protected IFhirSystemDao<T, MT> getDao() {
		return myDao;
	}

	@History
	public IBundleProvider historyServer(HttpServletRequest theRequest, @Since Date theDate, @At DateRangeParam theAt, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			DateRangeParam range = super.processSinceOrAt(theDate, theAt);
			return myDao.history(range.getLowerBoundAsInstant(), range.getUpperBoundAsInstant(), theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

	@Required
	public void setDao(IFhirSystemDao<T, MT> theDao) {
		myDao = theDao;
	}

}
