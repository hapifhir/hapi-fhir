package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import joptsimple.internal.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This interceptor replaces the auto-generated CapabilityStatement that is generated
 * by the HAPI FHIR Server with a static hard-coded resource.
 */
@Interceptor
public class MdmSearchExpandingInterceptor {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmLinkExpandSvc myMdmLinkExpandSvc;

	@Autowired
	private DaoConfig myDaoConfig;

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void hook(SearchParameterMap theSearchParameterMap) {
		if (myDaoConfig.isAllowMdmExpansion()) {
			for (List<List<IQueryParameterType>> andList : theSearchParameterMap.values()) {
				for (List<IQueryParameterType> orList : andList) {
					expandAnyReferenceParameters(orList);
				}
			}
		}
	}

	/**
	 * If a Parameter is a reference parameter, and it has been set to expand MDM, perform the expansion.
	 */
	private void expandAnyReferenceParameters(List<IQueryParameterType> orList) {
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
							expandedResourceIds =  myMdmLinkExpandSvc.expandMdmByGoldenResourceId(new IdDt(refParam.getValue()));
					}

					//Rebuild the search param list.
					if (!expandedResourceIds.isEmpty()) {
						ourLog.debug("Parameter has been expanded to: {}", String.join(", ", expandedResourceIds));
						toRemove.add(refParam);
						expandedResourceIds.stream().map(resourceId -> new ReferenceParam(refParam.getResourceType() + "/" + resourceId)).forEach(toAdd::add);
					}
				}
			}
		}
		orList.removeAll(toRemove);
		orList.addAll(toAdd);
	}
}
