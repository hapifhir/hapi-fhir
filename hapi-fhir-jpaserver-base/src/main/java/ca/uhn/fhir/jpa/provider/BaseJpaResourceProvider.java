package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.util.CoverageIgnore;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public abstract class BaseJpaResourceProvider<T extends IBaseResource> extends BaseJpaProvider implements IResourceProvider {

	private IFhirResourceDao<T> myDao;

	public BaseJpaResourceProvider() {
		// nothing
	}

	@CoverageIgnore
	public BaseJpaResourceProvider(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}


	protected Parameters doExpunge(IIdType theIdParam, IPrimitiveType<? extends Integer> theLimit, IPrimitiveType<? extends Boolean> theExpungeDeletedResources, IPrimitiveType<? extends Boolean> theExpungeOldVersions, IPrimitiveType<? extends Boolean> theExpungeEverything, RequestDetails theRequest) {

		ExpungeOptions options = createExpungeOptions(theLimit, theExpungeDeletedResources, theExpungeOldVersions, theExpungeEverything);

		ExpungeOutcome outcome;
		if (theIdParam != null) {
			outcome = getDao().expunge(theIdParam, options, theRequest);
		} else {
			outcome = getDao().expunge(options, theRequest);
		}

		return createExpungeResponse(outcome);
	}

	public IFhirResourceDao<T> getDao() {
		return myDao;
	}

	@Required
	public void setDao(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	@History
	public IBundleProvider getHistoryForResourceInstance(
		HttpServletRequest theRequest,
		@IdParam IIdType theId,
		@Since Date theSince,
		@At DateRangeParam theAt,
		RequestDetails theRequestDetails) {

		startRequest(theRequest);
		try {
			DateRangeParam sinceOrAt = processSinceOrAt(theSince, theAt);
			return myDao.history(theId, sinceOrAt.getLowerBoundAsInstant(), sinceOrAt.getUpperBoundAsInstant(), theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

	@History
	public IBundleProvider getHistoryForResourceType(
		HttpServletRequest theRequest,
		@Since Date theSince,
		@At DateRangeParam theAt,
		RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			DateRangeParam sinceOrAt = processSinceOrAt(theSince, theAt);
			return myDao.history(sinceOrAt.getLowerBoundAsInstant(), sinceOrAt.getUpperBoundAsInstant(), theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return myDao.getResourceType();
	}

	@Patch
	public DaoMethodOutcome patch(HttpServletRequest theRequest, @IdParam IIdType theId, @ConditionalUrlParam String theConditionalUrl,  RequestDetails theRequestDetails, @ResourceParam String theBody, PatchTypeEnum thePatchType) {
		startRequest(theRequest);
		try {
			return myDao.patch(theId, theConditionalUrl, thePatchType, theBody, theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

	@Read(version = true)
	public T read(HttpServletRequest theRequest, @IdParam IIdType theId, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			return myDao.read(theId, theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}

}
