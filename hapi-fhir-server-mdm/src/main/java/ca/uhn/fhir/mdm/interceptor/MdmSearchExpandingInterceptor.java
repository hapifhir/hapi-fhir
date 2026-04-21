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
package ca.uhn.fhir.mdm.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This interceptor replaces the auto-generated CapabilityStatement that is generated
 * by the HAPI FHIR Server with a static hard-coded resource.
 */
@Interceptor
public class MdmSearchExpandingInterceptor {

	private static final MdmSearchExpansionSvc.IParamTester PARAM_TESTER = (paramName, param) -> {
		boolean retVal = false;
		if (param instanceof ReferenceParam) {
			retVal = ((ReferenceParam) param).isMdmExpand();
		} else if (param instanceof TokenParam) {
			retVal = ((TokenParam) param).isMdmExpand();
		}
		return retVal;
	};

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private MdmSearchExpansionSvc myMdmSearchExpansionSvc;

	@Hook(
			value = Pointcut.STORAGE_PRESEARCH_PARTITION_SELECTED,
			order = MdmConstants.STORAGE_PRESEARCH_PARTITION_SELECTED_MDM_SEARCH_EXPANDING_INTERCEPTOR)
	public void hook(
			RequestDetails theRequestDetails,
			SearchParameterMap theSearchParameterMap,
			ICachedSearchDetails theSearchDetails) {

		if (myStorageSettings.isAllowMdmExpansion()) {
			String resourceType = theSearchDetails.getResourceType();
			myMdmSearchExpansionSvc.expandSearchAndStoreInRequestDetails(
					resourceType, theRequestDetails, theSearchParameterMap, PARAM_TESTER);
		}
	}
}
