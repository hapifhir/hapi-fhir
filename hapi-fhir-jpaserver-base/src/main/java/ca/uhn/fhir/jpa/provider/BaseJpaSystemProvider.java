package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ReindexTerminologyResult;
import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.Offset;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public abstract class BaseJpaSystemProvider<T, MT> extends BaseStorageSystemProvider<T, MT> implements IJpaSystemProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseJpaSystemProvider.class);

	public static final String RESP_PARAM_SUCCESS = "success";

	/**
	 * @see ProviderConstants#OPERATION_REINDEX
	 * @deprecated
	 */
	@Deprecated
	public static final String MARK_ALL_RESOURCES_FOR_REINDEXING = ProviderConstants.MARK_ALL_RESOURCES_FOR_REINDEXING;
	/**
	 * @see ProviderConstants#OPERATION_REINDEX
	 * @deprecated
	 */
	@Deprecated
	public static final String PERFORM_REINDEXING_PASS = ProviderConstants.PERFORM_REINDEXING_PASS;

	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;

	@Autowired
	private ITermReadSvc myTermReadSvc;


	public BaseJpaSystemProvider() {
		// nothing
	}

	protected IResourceReindexingSvc getResourceReindexingSvc() {
		return myResourceReindexingSvc;
	}

	@History
	public IBundleProvider historyServer(
		HttpServletRequest theRequest,
		@Offset Integer theOffset,
		@Since Date theDate,
		@At DateRangeParam theAt,
		RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			DateRangeParam range = super.processSinceOrAt(theDate, theAt);
			return myDao.history(range.getLowerBoundAsInstant(), range.getUpperBoundAsInstant(), theOffset, theRequestDetails);
		} finally {
			endRequest(theRequest);
		}
	}


	@Operation(name = ProviderConstants.OPERATION_REINDEX_TERMINOLOGY, idempotent = false)
	public IBaseParameters reindexTerminology(RequestDetails theRequestDetails) {

		ReindexTerminologyResult result;
		StopWatch sw = new StopWatch();
		try {
			result = myTermReadSvc.reindexTerminology();

		} catch (Exception theE) {
			throw new InternalErrorException(Msg.code(2072) +
				"Re-creating terminology freetext indexes failed with exception: " + theE.getMessage() +
				NL +  "With trace:" + NL + ExceptionUtils.getStackTrace(theE));
		}

		IBaseParameters retVal = ParametersUtil.newInstance(getContext());
		if ( ! result.equals(ReindexTerminologyResult.SUCCESS) ) {
			ParametersUtil.addParameterToParametersBoolean(getContext(), retVal, RESP_PARAM_SUCCESS, false);
			String msg = result.equals(ReindexTerminologyResult.SEARCH_SVC_DISABLED)
				? "Freetext service is not configured. Operation didn't run."
				: "Operation was cancelled because other terminology background tasks are currently running. Try again in a few minutes.";
			ParametersUtil.addParameterToParametersString(getContext(), retVal, "message", msg);
			return retVal;
		}

		ParametersUtil.addParameterToParametersBoolean(getContext(), retVal, RESP_PARAM_SUCCESS, true);
		ourLog.info("Re-creating terminology freetext indexes took {}", sw);
		return retVal;
	}


	public static final String NL = System.getProperty("line.separator");

}
