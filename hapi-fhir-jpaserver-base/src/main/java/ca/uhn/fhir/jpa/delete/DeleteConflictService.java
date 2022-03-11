package ca.uhn.fhir.jpa.delete;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteConflictService {
	public static final int FIRST_QUERY_RESULT_COUNT = 1;
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteConflictService.class);
	public static int MAX_RETRY_ATTEMPTS = 10;
	public static String MAX_RETRY_ATTEMPTS_EXCEEDED_MSG = "Requested delete operation stopped before all conflicts were handled. May need to increase the configured Maximum Delete Conflict Query Count.";
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	DeleteConflictFinderService myDeleteConflictFinderService;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myFhirContext;

	private DeleteConflictOutcome findAndHandleConflicts(RequestDetails theRequest, DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, int theMinQueryResultCount, TransactionDetails theTransactionDetails) {
		List<ResourceLink> resultList = myDeleteConflictFinderService.findConflicts(theEntity, theMinQueryResultCount);
		if (resultList.isEmpty()) {
			return null;
		}

		return handleConflicts(theRequest, theDeleteConflicts, theEntity, theForValidate, resultList, theTransactionDetails);
	}

	private DeleteConflictOutcome handleConflicts(RequestDetails theRequest, DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, List<ResourceLink> theResultList, TransactionDetails theTransactionDetails) {
		if (!myDaoConfig.isEnforceReferentialIntegrityOnDelete() && !theForValidate) {
			ourLog.debug("Deleting {} resource dependencies which can no longer be satisfied", theResultList.size());
			myResourceLinkDao.deleteAll(theResultList);
			return null;
		}

		addConflictsToList(theDeleteConflicts, theEntity, theResultList);

		if (theDeleteConflicts.isEmpty()) {
			return new DeleteConflictOutcome();
		}

		// Notify Interceptors about pre-action call
		HookParams hooks = new HookParams()
			.add(DeleteConflictList.class, theDeleteConflicts)
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(TransactionDetails.class, theTransactionDetails);
		return (DeleteConflictOutcome) CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS, hooks);
	}

	private void addConflictsToList(DeleteConflictList theDeleteConflicts, ResourceTable theEntity, List<ResourceLink> theResultList) {
		for (ResourceLink link : theResultList) {
			IdDt targetId = theEntity.getIdDt();
			IdDt sourceId = link.getSourceResource().getIdDt();
			String sourcePath = link.getSourcePath();
			if (theDeleteConflicts.isResourceIdMarkedForDeletion(sourceId)) {
				if (theDeleteConflicts.isResourceIdMarkedForDeletion(targetId)) {
					continue;
				}
			}

			theDeleteConflicts.add(new DeleteConflict(sourceId, sourcePath, targetId));
		}
	}

	public int validateOkToDelete(DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, RequestDetails theRequest, TransactionDetails theTransactionDetails) {

		// We want the list of resources that are marked to be the same list even as we
		// drill into conflict resolution stacks.. this allows us to not get caught by
		// circular references
		DeleteConflictList newConflicts = new DeleteConflictList(theDeleteConflicts);

		// In most cases, there will be no hooks, and so we only need to check if there is at least FIRST_QUERY_RESULT_COUNT conflict and populate that.
		// Only in the case where there is a hook do we need to go back and collect larger batches of conflicts for processing.

		DeleteConflictOutcome outcome = findAndHandleConflicts(theRequest, newConflicts, theEntity, theForValidate, FIRST_QUERY_RESULT_COUNT, theTransactionDetails);

		int retryCount = 0;
		while (outcome != null) {
			int shouldRetryCount = Math.min(outcome.getShouldRetryCount(), MAX_RETRY_ATTEMPTS);
			if (!(retryCount < shouldRetryCount)) break;
			newConflicts = new DeleteConflictList(newConflicts);
			outcome = findAndHandleConflicts(theRequest, newConflicts, theEntity, theForValidate, myDaoConfig.getMaximumDeleteConflictQueryCount(), theTransactionDetails);
			++retryCount;
		}
		theDeleteConflicts.addAll(newConflicts);
		if (retryCount >= MAX_RETRY_ATTEMPTS && !theDeleteConflicts.isEmpty()) {
			IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);
			OperationOutcomeUtil.addIssue(myFhirContext, oo, BaseStorageDao.OO_SEVERITY_ERROR, MAX_RETRY_ATTEMPTS_EXCEEDED_MSG, null, "processing");
			throw new ResourceVersionConflictException(Msg.code(821) + MAX_RETRY_ATTEMPTS_EXCEEDED_MSG, oo);
		}
		return retryCount;
	}

	@VisibleForTesting
	static void setMaxRetryAttempts(Integer theMaxRetryAttempts) {
		MAX_RETRY_ATTEMPTS = theMaxRetryAttempts;
	}
}
