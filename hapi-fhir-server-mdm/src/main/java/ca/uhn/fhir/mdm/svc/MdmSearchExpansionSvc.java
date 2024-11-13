package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class MdmSearchExpansionSvc {
	// A simple interface to turn ids into some form of IQueryParameterTypes
	private interface Creator<T extends IQueryParameterType> {
		T create(String id);
	}

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IMdmLinkExpandSvc myMdmLinkExpandSvc;

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
	 * @param theRequestDetails The incoming request details
	 * @param theSearchParameterMap The parameter map to modify
	 * @param theExpansionCandidateTester Each {@link ReferenceParam} in the map will be first tested
	 *                                    by this function to determine whether it should be expanded.
	 * @since 8.0.0
	 */
	public void expandSearch(
			RequestDetails theRequestDetails,
			SearchParameterMap theSearchParameterMap,
			Function<ReferenceParam, Boolean> theExpansionCandidateTester) {
		final RequestDetails requestDetailsToUse =
				theRequestDetails == null ? new SystemRequestDetails() : theRequestDetails;
		final RequestPartitionId requestPartitionId =
				myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(
						requestDetailsToUse, requestDetailsToUse.getResourceName(), theSearchParameterMap);
		for (Map.Entry<String, List<List<IQueryParameterType>>> set : theSearchParameterMap.entrySet()) {
			String paramName = set.getKey();
			List<List<IQueryParameterType>> andList = set.getValue();
			for (List<IQueryParameterType> orList : andList) {
				// here we will know if it's an _id param or not
				// from theSearchParameterMap.keySet()
				expandAnyReferenceParameters(requestPartitionId, paramName, orList, theExpansionCandidateTester);
			}
		}
	}

	private void expandAnyReferenceParameters(
			RequestPartitionId theRequestPartitionId,
			String theParamName,
			List<IQueryParameterType> orList,
			Function<ReferenceParam, Boolean> theExpansionCandidateTester) {
		List<IQueryParameterType> toRemove = new ArrayList<>();
		List<IQueryParameterType> toAdd = new ArrayList<>();
		for (IQueryParameterType iQueryParameterType : orList) {
			if (iQueryParameterType instanceof ReferenceParam) {
				ReferenceParam refParam = (ReferenceParam) iQueryParameterType;
				if (theExpansionCandidateTester.apply(refParam)) {
					ourLog.debug("Found a reference parameter to expand: {}", refParam);
					// First, attempt to expand as a source resource.
					Set<String> expandedResourceIds = myMdmLinkExpandSvc.expandMdmBySourceResourceId(
							theRequestPartitionId, new IdDt(refParam.getValue()));

					// If we failed, attempt to expand as a golden resource
					if (expandedResourceIds.isEmpty()) {
						expandedResourceIds = myMdmLinkExpandSvc.expandMdmByGoldenResourceId(
								theRequestPartitionId, new IdDt(refParam.getValue()));
					}

					// Rebuild the search param list.
					if (!expandedResourceIds.isEmpty()) {
						ourLog.debug("Parameter has been expanded to: {}", String.join(", ", expandedResourceIds));
						toRemove.add(refParam);
						expandedResourceIds.stream()
								.map(resourceId -> addResourceTypeIfNecessary(refParam.getResourceType(), resourceId))
								.map(ReferenceParam::new)
								.forEach(toAdd::add);
					}
				}
			} else if (theParamName.equalsIgnoreCase("_id")) {
				expandIdParameter(theRequestPartitionId, iQueryParameterType, toAdd, toRemove);
			}
		}

		orList.removeAll(toRemove);
		orList.addAll(toAdd);
	}

	private String addResourceTypeIfNecessary(String theResourceType, String theResourceId) {
		if (theResourceId.contains("/")) {
			return theResourceId;
		} else {
			return theResourceType + "/" + theResourceId;
		}
	}

	/**
	 * Expands out the provided _id parameter into all the various
	 * ids of linked resources.
	 *
	 * @param theRequestPartitionId
	 * @param theIdParameter
	 * @param theAddList
	 * @param theRemoveList
	 */
	private void expandIdParameter(
			RequestPartitionId theRequestPartitionId,
			IQueryParameterType theIdParameter,
			List<IQueryParameterType> theAddList,
			List<IQueryParameterType> theRemoveList) {
		// id parameters can either be StringParam (for $everything operation)
		// or TokenParam (for searches)
		// either case, we want to expand it out and grab all related resources
		IIdType id;
		Creator<? extends IQueryParameterType> creator;
		boolean mdmExpand = false;
		if (theIdParameter instanceof TokenParam) {
			TokenParam param = (TokenParam) theIdParameter;
			mdmExpand = param.isMdmExpand();
			id = new IdDt(param.getValue());
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

			Set<String> expandedResourceIds = myMdmLinkExpandSvc.expandMdmBySourceResourceId(theRequestPartitionId, id);

			if (expandedResourceIds.isEmpty()) {
				expandedResourceIds = myMdmLinkExpandSvc.expandMdmByGoldenResourceId(theRequestPartitionId, (IdDt) id);
			}

			// Rebuild
			if (!expandedResourceIds.isEmpty()) {
				ourLog.debug("_id parameter has been expanded to: {}", String.join(", ", expandedResourceIds));

				// remove the original
				theRemoveList.add(theIdParameter);

				// add in all the linked values
				expandedResourceIds.stream().map(creator::create).forEach(theAddList::add);
			}
		}
		// else - no expansion required
	}
}
