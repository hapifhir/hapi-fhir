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

import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class BaseJpaSystemProvider<T, MT> extends BaseJpaProvider implements IJpaSystemProvider {

	public static final String MARK_ALL_RESOURCES_FOR_REINDEXING = "$mark-all-resources-for-reindexing";
	public static final String PERFORM_REINDEXING_PASS = "$perform-reindexing-pass";

	private IFhirSystemDao<T, MT> myDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;

	public BaseJpaSystemProvider() {
		// nothing
	}

	protected IResourceReindexingSvc getResourceReindexingSvc() {
		return myResourceReindexingSvc;
	}

	protected Parameters doExpunge(IPrimitiveType<? extends Integer> theLimit, IPrimitiveType<? extends Boolean> theExpungeDeletedResources, IPrimitiveType<? extends Boolean> theExpungeOldVersions, IPrimitiveType<? extends Boolean> theExpungeEverything, RequestDetails theRequestDetails) {
		ExpungeOptions options = createExpungeOptions(theLimit, theExpungeDeletedResources, theExpungeOldVersions, theExpungeEverything);
		ExpungeOutcome outcome = getDao().expunge(options, theRequestDetails);
		return createExpungeResponse(outcome);
	}

	protected IFhirSystemDao<T, MT> getDao() {
		return myDao;
	}

	@Required
	public void setDao(IFhirSystemDao<T, MT> theDao) {
		myDao = theDao;
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

}
