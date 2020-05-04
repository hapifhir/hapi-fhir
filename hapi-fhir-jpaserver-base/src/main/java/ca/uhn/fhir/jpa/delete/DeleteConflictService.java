package ca.uhn.fhir.jpa.delete;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class DeleteConflictService {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteConflictService.class);
	public static final int FIRST_QUERY_RESULT_COUNT = 1;
	public static final int RETRY_QUERY_RESULT_COUNT = 60;
	public static final int MAX_RETRY_ATTEMPTS = 10;

	@Autowired
	DeleteConflictFinderService myDeleteConflictFinderService;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	public int validateOkToDelete(DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, RequestDetails theRequest) {

		// We want the list of resources that are marked to be the same list even as we
		// drill into conflict resolution stacks.. this allows us to not get caught by
		// circular references
		DeleteConflictList newConflicts = new DeleteConflictList(theDeleteConflicts);

		// In most cases, there will be no hooks, and so we only need to check if there is at least FIRST_QUERY_RESULT_COUNT conflict and populate that.
		// Only in the case where there is a hook do we need to go back and collect larger batches of conflicts for processing.

		DeleteConflictOutcome outcome = findAndHandleConflicts(theRequest, newConflicts, theEntity, theForValidate, FIRST_QUERY_RESULT_COUNT);

		int retryCount = 0;
		while (outcome != null) {
			int shouldRetryCount = Math.min(outcome.getShouldRetryCount(), MAX_RETRY_ATTEMPTS);
			if (!(retryCount < shouldRetryCount)) break;
			newConflicts = new DeleteConflictList();
			outcome = findAndHandleConflicts(theRequest, newConflicts, theEntity, theForValidate, RETRY_QUERY_RESULT_COUNT);
			++retryCount;
		}
		theDeleteConflicts.addAll(newConflicts);
		return retryCount;
	}

	private DeleteConflictOutcome findAndHandleConflicts(RequestDetails theRequest, DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, int theMinQueryResultCount) {
		List<ResourceLink> resultList = myDeleteConflictFinderService.findConflicts(theEntity, theMinQueryResultCount);
		if (resultList.isEmpty()) {
			return null;
		}

		return handleConflicts(theRequest, theDeleteConflicts, theEntity, theForValidate, resultList);
	}

	private DeleteConflictOutcome handleConflicts(RequestDetails theRequest, DeleteConflictList theDeleteConflicts, ResourceTable theEntity, boolean theForValidate, List<ResourceLink> theResultList) {
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
			.addIfMatchesType(ServletRequestDetails.class, theRequest);
		return (DeleteConflictOutcome)JpaInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS, hooks);
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

	public static void validateDeleteConflictsEmptyOrThrowException(FhirContext theFhirContext, DeleteConflictList theDeleteConflicts) {
		if (theDeleteConflicts.isEmpty()) {
			return;
		}

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(theFhirContext);
		String firstMsg = null;

		Iterator<DeleteConflict> iterator = theDeleteConflicts.iterator();
		while (iterator.hasNext()) {
			DeleteConflict next = iterator.next();
			StringBuilder b = new StringBuilder();
			b.append("Unable to delete ");
			b.append(next.getTargetId().toUnqualifiedVersionless().getValue());
			b.append(" because at least one resource has a reference to this resource. First reference found was resource ");
			b.append(next.getSourceId().toUnqualifiedVersionless().getValue());
			b.append(" in path ");
			b.append(next.getSourcePath());
			String msg = b.toString();
			if (firstMsg == null) {
				firstMsg = msg;
			}
			OperationOutcomeUtil.addIssue(theFhirContext, oo, BaseHapiFhirDao.OO_SEVERITY_ERROR, msg, null, "processing");
		}

		throw new ResourceVersionConflictException(firstMsg, oo);
	}
}
