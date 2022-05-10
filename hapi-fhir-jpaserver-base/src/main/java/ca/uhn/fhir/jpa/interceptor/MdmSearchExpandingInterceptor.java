package ca.uhn.fhir.jpa.interceptor;

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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkExpandSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This interceptor replaces the auto-generated CapabilityStatement that is generated
 * by the HAPI FHIR Server with a static hard-coded resource.
 */
@Interceptor
public class MdmSearchExpandingInterceptor {
	// A simple interface to turn ids into some form of IQueryParameterTypes
	private interface Creator<T extends IQueryParameterType> {
		T create(String id);
	}

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmLinkExpandSvc myMdmLinkExpandSvc;

	@Autowired
	private DaoConfig myDaoConfig;

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void hook(SearchParameterMap theSearchParameterMap) {
		if (myDaoConfig.isAllowMdmExpansion()) {
			for (Map.Entry<String, List<List<IQueryParameterType>>> set : theSearchParameterMap.entrySet()) {
				String paramName = set.getKey();
				List<List<IQueryParameterType>> andList = set.getValue();
				for (List<IQueryParameterType> orList : andList) {
					// here we will know if it's an _id param or not
					// from theSearchParameterMap.keySet()
					expandAnyReferenceParameters(paramName, orList);
				}
			}
		}
	}

	/**
	 * If a Parameter is a reference parameter, and it has been set to expand MDM, perform the expansion.
	 */
	private void expandAnyReferenceParameters(String theParamName, List<IQueryParameterType> orList) {
		List<IQueryParameterType> toRemove = new ArrayList<>();
		List<IQueryParameterType> toAdd = new ArrayList<>();
		for (IQueryParameterType iQueryParameterType : orList) {
			if (iQueryParameterType instanceof ReferenceParam) {
				ReferenceParam refParam = (ReferenceParam) iQueryParameterType;
				if (refParam.isMdmExpand()) {
					ourLog.debug("Found a reference parameter to expand: {}", refParam);
					//First, attempt to expand as a source resource.
					Set<String> expandedResourceIds = myMdmLinkExpandSvc.expandMdmBySourceResourceId(new IdDt(refParam.getValue()));

					// If we failed, attempt to expand as a golden resource
					if (expandedResourceIds.isEmpty()) {
						expandedResourceIds = myMdmLinkExpandSvc.expandMdmByGoldenResourceId(new IdDt(refParam.getValue()));
					}

					//Rebuild the search param list.
					if (!expandedResourceIds.isEmpty()) {
						ourLog.debug("Parameter has been expanded to: {}", String.join(", ", expandedResourceIds));
						toRemove.add(refParam);
						expandedResourceIds.stream()
							.map(resourceId -> new ReferenceParam(refParam.getResourceType() + "/" + resourceId))
							.forEach(toAdd::add);
					}
				}
			} else if (theParamName.equalsIgnoreCase("_id")) {
				expandIdParameter(iQueryParameterType, toAdd, toRemove);
			}
		}

		orList.removeAll(toRemove);
		orList.addAll(toAdd);
	}

	/**
	 * Expands out the provided _id parameter into all the various
	 * ids of linked resources.
	 *
	 * @param theIdParameter
	 * @param theAddList
	 * @param theRemoveList
	 */
	private void expandIdParameter(IQueryParameterType theIdParameter,
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
			// in case the _id paramter type is different from the above
			ourLog.warn("_id parameter of incorrect type. Expected StringParam or TokenParam, but got {}. No expansion will be done!",
				theIdParameter.getClass().getSimpleName());
		} else if (mdmExpand) {
			ourLog.debug("_id parameter must be expanded out from: {}", id.getValue());

			Set<String> expandedResourceIds = myMdmLinkExpandSvc.expandMdmBySourceResourceId(id);

			if (expandedResourceIds.isEmpty()) {
				expandedResourceIds = myMdmLinkExpandSvc.expandMdmByGoldenResourceId(id.getIdPartAsLong());
			}

			//Rebuild
			if (!expandedResourceIds.isEmpty()) {
				ourLog.debug("_id parameter has been expanded to: {}", String.join(", ", expandedResourceIds));

				// remove the original
				theRemoveList.add(theIdParameter);

				// add in all the linked values
				expandedResourceIds.stream()
					.map(creator::create)
					.forEach(theAddList::add);
			}
		}
		// else - no expansion required
	}
}
