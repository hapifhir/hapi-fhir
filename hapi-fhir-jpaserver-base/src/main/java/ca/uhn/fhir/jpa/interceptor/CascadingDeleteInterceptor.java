package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.*;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.delete.DeleteConflictList;
import ca.uhn.fhir.jpa.delete.DeleteConflictOutcome;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static ca.uhn.fhir.jpa.delete.DeleteConflictService.MAX_RETRY_ATTEMPTS;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Interceptor that allows for cascading deletes (deletes that resolve constraint issues).
 * <p>
 * For example, if <code>DiagnosticReport/A</code> has a reference to <code>Observation/B</code>
 * it is not normally possible to delete <code>Observation/B</code> without first deleting
 * <code>DiagnosticReport/A</code>. With this interceptor in place, it is.
 * </p>
 * <p>
 * When using this interceptor, client requests must include the parameter
 * <code>_cascade=delete</code> on the DELETE URL in order to activate
 * cascading delete, or include the request header <code>X-Cascade-Delete: delete</code>
 * </p>
 */
@Interceptor
public class CascadingDeleteInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(CascadingDeleteInterceptor.class);
	private static final String CASCADED_DELETES_KEY = CascadingDeleteInterceptor.class.getName() + "_CASCADED_DELETES_KEY";
	private static final String CASCADED_DELETES_FAILED_KEY = CascadingDeleteInterceptor.class.getName() + "_CASCADED_DELETES_FAILED_KEY";

	private final DaoRegistry myDaoRegistry;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Constructor
	 *
	 * @param theDaoRegistry The DAO registry (must not be null)
	 */
	public CascadingDeleteInterceptor(DaoRegistry theDaoRegistry, IInterceptorBroadcaster theInterceptorBroadcaster) {
		Validate.notNull(theDaoRegistry, "theDaoRegistry must not be null");
		Validate.notNull(theInterceptorBroadcaster, "theInterceptorBroadcaster must not be null");
		myDaoRegistry = theDaoRegistry;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS)
	public DeleteConflictOutcome handleDeleteConflicts(DeleteConflictList theConflictList, RequestDetails theRequest) {
		ourLog.debug("Have delete conflicts: {}", theConflictList);

		if (!shouldCascade(theRequest)) {
			return null;
		}

		List<String> cascadedDeletes = getCascadedDeletesMap(theRequest, true);
		for (Iterator<DeleteConflict> iter = theConflictList.iterator(); iter.hasNext(); ) {
			DeleteConflict next = iter.next();
			IdDt nextSource = next.getSourceId();
			String nextSourceId = nextSource.toUnqualifiedVersionless().getValue();

			if (!cascadedDeletes.contains(nextSourceId)) {

				IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextSource.getResourceType());

				// Interceptor call: STORAGE_CASCADE_DELETE
				IBaseResource resource = dao.read(nextSource);
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(DeleteConflictList.class, theConflictList)
					.add(IBaseResource.class, resource);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_CASCADE_DELETE, params);

				// Actually perform the delete
				ourLog.info("Have delete conflict {} - Cascading delete", next);
				dao.delete(nextSource, theConflictList, theRequest);

				cascadedDeletes.add(nextSourceId);

			}
		}

		return new DeleteConflictOutcome().setShouldRetryCount(MAX_RETRY_ATTEMPTS);
	}

	@SuppressWarnings("unchecked")
	private List<String> getCascadedDeletesMap(RequestDetails theRequest, boolean theCreate) {
		List<String> retVal = (List<String>) theRequest.getUserData().get(CASCADED_DELETES_KEY);
		if (retVal == null && theCreate) {
			retVal = new ArrayList<>();
			theRequest.getUserData().put(CASCADED_DELETES_KEY, retVal);
		}
		return retVal;
	}

	@Hook(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME)
	public void outgoingFailureOperationOutcome(RequestDetails theRequestDetails, IBaseOperationOutcome theResponse) {
		if (theRequestDetails != null) {

			String failedDeleteMessage = (String) theRequestDetails.getUserData().get(CASCADED_DELETES_FAILED_KEY);
			if (isNotBlank(failedDeleteMessage)) {
				FhirContext ctx = theRequestDetails.getFhirContext();
				String severity = OperationOutcome.IssueSeverity.INFORMATION.toCode();
				String code = OperationOutcome.IssueType.INFORMATIONAL.toCode();
				String details = failedDeleteMessage;
				OperationOutcomeUtil.addIssue(ctx, theResponse, severity, details, null, code);
			}

		}
	}


	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void outgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails, IBaseResource theResponse) {
		if (theRequestDetails != null) {

			// Successful delete list
			List<String> deleteList = getCascadedDeletesMap(theRequestDetails, false);
			if (deleteList != null) {
				if (theResponseDetails.getResponseCode() == 200) {
					if (theResponse instanceof IBaseOperationOutcome) {
						FhirContext ctx = theRequestDetails.getFhirContext();
						IBaseOperationOutcome oo = (IBaseOperationOutcome) theResponse;
						String severity = OperationOutcome.IssueSeverity.INFORMATION.toCode();
						String code = OperationOutcome.IssueType.INFORMATIONAL.toCode();
						String details = ctx.getLocalizer().getMessage(CascadingDeleteInterceptor.class, "successMsg", deleteList.size(), deleteList);
						OperationOutcomeUtil.addIssue(ctx, oo, severity, details, null, code);
					}
				}
			}

		}
	}


	/**
	 * Subclasses may override
	 *
	 * @param theRequest The REST request
	 * @return Returns true if cascading delete should be allowed
	 */
	@SuppressWarnings("WeakerAccess")
	protected boolean shouldCascade(RequestDetails theRequest) {
		if (theRequest != null) {
			String[] cascadeParameters = theRequest.getParameters().get(Constants.PARAMETER_CASCADE_DELETE);
			if (cascadeParameters != null && Arrays.asList(cascadeParameters).contains(Constants.CASCADE_DELETE)) {
				return true;
			}

			String cascadeHeader = theRequest.getHeader(Constants.HEADER_CASCADE);
			if (Constants.CASCADE_DELETE.equals(cascadeHeader)) {
				return true;
			}

			// Add a message to the response
			String message = theRequest.getFhirContext().getLocalizer().getMessage(CascadingDeleteInterceptor.class, "noParam");
			theRequest.getUserData().put(CASCADED_DELETES_FAILED_KEY, message);
		}

		return false;
	}


}
