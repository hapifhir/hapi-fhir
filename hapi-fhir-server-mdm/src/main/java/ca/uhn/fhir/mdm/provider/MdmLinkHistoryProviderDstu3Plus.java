package ca.uhn.fhir.mdm.provider;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.model.mdmevents.MdmHistoryEvent;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class MdmLinkHistoryProviderDstu3Plus extends BaseMdmProvider {
	private static final Logger ourLog = getLogger(MdmLinkHistoryProviderDstu3Plus.class);

	private final IMdmControllerSvc myMdmControllerSvc;

	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	public MdmLinkHistoryProviderDstu3Plus(
			FhirContext theFhirContext,
			IMdmControllerSvc theMdmControllerSvc,
			IInterceptorBroadcaster theIInterceptorBroadcaster) {
		super(theFhirContext);
		myMdmControllerSvc = theMdmControllerSvc;
		myInterceptorBroadcaster = theIInterceptorBroadcaster;
	}

	@Operation(name = ProviderConstants.MDM_LINK_HISTORY, idempotent = true)
	public IBaseParameters historyLinks(
			@Description(value = "The id of the Golden Resource (e.g. Golden Patient Resource).")
					@OperationParam(
							name = ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theMdmGoldenResourceIds,
			@Description(value = "The id of the source resource (e.g. Patient resource).")
					@OperationParam(
							name = ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theResourceIds,
			ServletRequestDetails theRequestDetails) {
		validateMdmLinkHistoryParameters(theMdmGoldenResourceIds, theResourceIds);

		final List<String> goldenResourceIdsToUse =
				convertToStringsIncludingCommaDelimitedIfNotNull(theMdmGoldenResourceIds);
		final List<String> resourceIdsToUse = convertToStringsIncludingCommaDelimitedIfNotNull(theResourceIds);

		final IBaseParameters retVal = ParametersUtil.newInstance(myFhirContext);

		final MdmHistorySearchParameters mdmHistorySearchParameters = new MdmHistorySearchParameters()
				.setGoldenResourceIds(goldenResourceIdsToUse)
				.setSourceIds(resourceIdsToUse);

		final List<MdmLinkWithRevisionJson> mdmLinkRevisionsFromSvc =
				myMdmControllerSvc.queryLinkHistory(mdmHistorySearchParameters, theRequestDetails);

		parametersFromMdmLinkRevisions(retVal, mdmLinkRevisionsFromSvc);

		if (myInterceptorBroadcaster.hasHooks(Pointcut.MDM_POST_LINK_HISTORY)) {
			// MDM_POST_LINK_HISTORY hook
			MdmHistoryEvent historyEvent = new MdmHistoryEvent();
			historyEvent.setMdmLinkRevisions(mdmLinkRevisionsFromSvc);
			if (isNotEmpty(theResourceIds)) {
				historyEvent.setSourceIds(theResourceIds.stream()
						.map(IPrimitiveType::getValueAsString)
						.collect(Collectors.toList()));
			}
			if (isNotEmpty(theMdmGoldenResourceIds)) {
				historyEvent.setGoldenResourceIds(theMdmGoldenResourceIds.stream()
						.map(IPrimitiveType::getValueAsString)
						.collect(Collectors.toList()));
			}

			HookParams params = new HookParams();
			params.add(RequestDetails.class, theRequestDetails);
			params.add(MdmHistoryEvent.class, historyEvent);
			myInterceptorBroadcaster.callHooks(Pointcut.MDM_POST_LINK_HISTORY, params);
		}

		return retVal;
	}
}
