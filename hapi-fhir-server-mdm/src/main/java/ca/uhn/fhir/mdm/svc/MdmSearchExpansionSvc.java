/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.MdmSearchExpansionResults;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.BaseParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class MdmSearchExpansionSvc {
	private static final String RESOURCE_NAME = MdmSearchExpansionSvc.class.getName() + "_RESOURCE_NAME";
	private static final String QUERY_STRING = MdmSearchExpansionSvc.class.getName() + "_QUERY_STRING";
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private MdmExpandersHolder myMdmExpandersHolder;

	private IMdmSettings myMdmSettings;

	/**
	 * This method looks through all the reference parameters within a {@link SearchParameterMap}
	 * and performs MDM expansion. This means looking for any subject/patient parameters, and
	 * expanding them to include any linked and golden resource patients. So, for example, a
	 * search for <code>Encounter?subject=Patient/1</code> might be modified to be a search
	 * for <code>Encounter?subject=Patient/1,Patient/999</code> if 999 is linked to 1 by MDM.
	 * <p>
	 * This is an internal MDM service and its API is subject to change. Use with caution!
	 * </p>
	 *
	 * @param theRequestDetails     The incoming request details
	 * @param theSearchParameterMap The parameter map to modify
	 * @param theParamTester        Determines which parameters should be expanded
	 * @return Returns the results of the expansion, which are also stored in the {@link RequestDetails} userdata map, so this service will only be invoked a maximum of once per request.
	 * @since 8.0.0
	 */
	public MdmSearchExpansionResults expandSearchAndStoreInRequestDetails(
			String theResourceName,
			@Nullable RequestDetails theRequestDetails,
			@Nonnull SearchParameterMap theSearchParameterMap,
			IParamTester theParamTester) {

		if (theRequestDetails == null) {
			return null;
		}

		// Try to detect if the RequestDetails is being reused across multiple different queries, which
		// can happen during CQL measure evaluation
		{
			String resourceName = theRequestDetails.getResourceName();
			String queryString = theSearchParameterMap.toNormalizedQueryString(myFhirContext);
			if (!Objects.equals(resourceName, theRequestDetails.getUserData().get(RESOURCE_NAME))
					|| !Objects.equals(
							queryString, theRequestDetails.getUserData().get(QUERY_STRING))) {
				MdmSearchExpansionResults.clearCachedExpansionResults(theRequestDetails);
			}
		}

		MdmSearchExpansionResults expansionResults =
				MdmSearchExpansionResults.getCachedExpansionResults(theRequestDetails);
		if (expansionResults != null) {
			return expansionResults;
		}

		expansionResults = new MdmSearchExpansionResults();

		final RequestPartitionId requestPartitionId =
				determineReadPartitionForSearch(theRequestDetails, theSearchParameterMap);

		for (Map.Entry<String, List<List<IQueryParameterType>>> set : theSearchParameterMap.entrySet()) {
			String paramName = set.getKey();
			List<List<IQueryParameterType>> andList = set.getValue();
			for (List<IQueryParameterType> orList : andList) {
				// here we will know if it's an _id param or not
				// from theSearchParameterMap.keySet()
				expandAnyReferenceParameters(
						requestPartitionId, theResourceName, paramName, orList, theParamTester, expansionResults);
			}
		}

		MdmSearchExpansionResults.cacheExpansionResults(theRequestDetails, expansionResults);

		/*
		 * Note: Do this at the end so that the query string reflects the post-translated
		 * query string
		 */
		String queryString = theSearchParameterMap.toNormalizedQueryString(myFhirContext);
		theRequestDetails.getUserData().put(RESOURCE_NAME, theResourceName);
		theRequestDetails.getUserData().put(QUERY_STRING, queryString);

		return expansionResults;
	}

	private RequestPartitionId determineReadPartitionForSearch(
			RequestDetails theRequestDetails, SearchParameterMap theSearchParameterMap) {
		RequestPartitionId retVal;

		// When search_all_partition is enabled, MDM expansion should search across all partitions
		// This is essential for PATIENT_ID partition mode where each patient exists in its own partition
		if (shouldSearchAllPartitionForMatch()) {
			retVal = RequestPartitionId.allPartitions();
		} else {
			retVal = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(
					theRequestDetails, theRequestDetails.getResourceName(), theSearchParameterMap);
		}

		return retVal;
	}

	private boolean shouldSearchAllPartitionForMatch() {
		return nonNull(myMdmSettings) && myMdmSettings.getSearchAllPartitionForMatch();
	}

	private void expandAnyReferenceParameters(
			RequestPartitionId theRequestPartitionId,
			String theResourceName,
			String theParamName,
			List<IQueryParameterType> orList,
			IParamTester theParamTester,
			MdmSearchExpansionResults theResultsToPopulate) {

		IMdmLinkExpandSvc mdmLinkExpandSvc = myMdmExpandersHolder.getLinkExpandSvcInstance();

		List<IQueryParameterType> toRemove = new ArrayList<>();
		List<IQueryParameterType> toAdd = new ArrayList<>();
		for (IQueryParameterType iQueryParameterType : orList) {
			if (iQueryParameterType instanceof ReferenceParam refParam) {
				if (theParamTester.shouldExpand(theParamName, refParam)) {
					if (refParam.hasChain()) {
						throw new InvalidRequestException(
								Msg.code(2822) + "MDM Expansion can not be applied to chained reference search.");
					}
					ourLog.debug("Found a reference parameter to expand: {}", refParam);
					// First, attempt to expand as a source resource.
					IIdType sourceId = newId(refParam.getValue());
					Set<String> expandedResourceIds =
							mdmLinkExpandSvc.expandMdmBySourceResourceId(theRequestPartitionId, sourceId);

					// If we failed, attempt to expand as a golden resource
					if (expandedResourceIds.isEmpty()) {
						expandedResourceIds =
								mdmLinkExpandSvc.expandMdmByGoldenResourceId(theRequestPartitionId, sourceId);
					}

					// Rebuild the search param list.
					if (!expandedResourceIds.isEmpty()) {
						ourLog.atLevel(Level.DEBUG)
								.log("Parameter has been expanded to: {}", String.join(", ", expandedResourceIds));
						toRemove.add(refParam);
						for (String resourceId : expandedResourceIds) {
							IIdType nextReference =
									newId(addResourceTypeIfNecessary(refParam.getResourceType(), resourceId));
							toAdd.add(new ReferenceParam(nextReference));
							theResultsToPopulate.addExpandedId(sourceId, nextReference);
						}
					}
				}
			} else if (theParamName.equalsIgnoreCase(IAnyResource.SP_RES_ID)) {
				expandIdParameter(
						theRequestPartitionId,
						iQueryParameterType,
						toAdd,
						toRemove,
						theParamTester,
						theResourceName,
						theResultsToPopulate);
			}
		}

		orList.removeAll(toRemove);
		orList.addAll(toAdd);
	}

	private IIdType newId(String value) {
		return myFhirContext.getVersion().newIdType(value);
	}

	private String addResourceTypeIfNecessary(String theResourceType, String theResourceId) {
		if (theResourceId.contains("/") || isBlank(theResourceType)) {
			return theResourceId;
		} else {
			return theResourceType + "/" + theResourceId;
		}
	}

	/**
	 * Expands out the provided _id parameter into all the various
	 * ids of linked resources.
	 */
	private void expandIdParameter(
			RequestPartitionId theRequestPartitionId,
			IQueryParameterType theIdParameter,
			List<IQueryParameterType> theAddList,
			List<IQueryParameterType> theRemoveList,
			IParamTester theParamTester,
			String theResourceName,
			MdmSearchExpansionResults theResultsToPopulate) {
		// id parameters can either be StringParam (for $everything operation)
		// or TokenParam (for searches)
		// either case, we want to expand it out and grab all related resources
		IIdType id;
		Creator<? extends IQueryParameterType> creator;
		boolean mdmExpand = false;
		if (theIdParameter instanceof TokenParam param) {
			mdmExpand = theParamTester.shouldExpand(IAnyResource.SP_RES_ID, param);
			String value = param.getValue();
			value = addResourceTypeIfNecessary(theResourceName, value);
			id = newId(value);
			creator = TokenParam::new;
		} else {
			creator = null;
			id = null;
		}

		if (id == null) {
			// in case the _id parameter type is different from the above
			ourLog.warn(
					"_id parameter of incorrect type. Expected StringParam or TokenParam, but got {}. No expansion will be done!",
					theIdParameter.getClass().getSimpleName());
		} else if (mdmExpand) {
			ourLog.debug("_id parameter must be expanded out from: {}", id.getValue());

			IMdmLinkExpandSvc mdmLinkExpandSvc = myMdmExpandersHolder.getLinkExpandSvcInstance();
			Set<String> expandedResourceIds = mdmLinkExpandSvc.expandMdmBySourceResourceId(theRequestPartitionId, id);

			if (expandedResourceIds.isEmpty()) {
				expandedResourceIds = mdmLinkExpandSvc.expandMdmByGoldenResourceId(theRequestPartitionId, id);
			}

			// Rebuild
			if (!expandedResourceIds.isEmpty()) {
				ourLog.debug("_id parameter has been expanded to: {}", expandedResourceIds);

				// remove the original
				theRemoveList.add(theIdParameter);

				// add in all the linked values
				expandedResourceIds.stream().map(creator::create).forEach(theAddList::add);

				for (String expandedId : expandedResourceIds) {
					theResultsToPopulate.addExpandedId(
							id, newId(addResourceTypeIfNecessary(theResourceName, expandedId)));
				}
			}
		}
		// else - no expansion required
	}

	// A simple interface to turn ids into some form of IQueryParameterTypes
	private interface Creator<T extends IQueryParameterType> {
		T create(String id);
	}

	@FunctionalInterface
	public interface IParamTester {
		boolean shouldExpand(String theParamName, BaseParam theParam);
	}

	public void setMdmSettings(IMdmSettings theMdmSettings) {
		myMdmSettings = theMdmSettings;
	}
}
