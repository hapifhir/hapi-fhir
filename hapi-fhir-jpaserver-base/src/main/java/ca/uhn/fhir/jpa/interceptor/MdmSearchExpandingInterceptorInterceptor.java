package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkExpandSvc;
import ca.uhn.fhir.jpa.search.helper.SearchParamHelper;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This interceptor replaces the auto-generated CapabilityStatement that is generated
 * by the HAPI FHIR Server with a static hard-coded resource.
 */
@Interceptor
public class MdmSearchExpandingInterceptorInterceptor {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private MdmLinkExpandSvc myMdmLinkExpandSvc;
	@Autowired
	private SearchParamHelper mySearchParamHelper;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IdHelperService myIdHelperService;

	@Hook(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH)
	public boolean hook(RequestDetails theRequestDetails, SearchParameterMap theSearchParameterMap) {
		Map<String, String[]> parameters =theRequestDetails.getParameters();
		boolean shouldExpandMdm = false;
		if (parameters.containsKey("_mdm")) {
			shouldExpandMdm = parameters.get("_mdm").length == 1 && parameters.get("_mdm")[0].equalsIgnoreCase("true");
		}
		if (shouldExpandMdm) {
			ourLog.debug("Detected that incoming request has _mdm=true. The request was: {}", theRequestDetails.getRequestPath());
			String resourceName = theRequestDetails.getResourceName();
			Collection<RuntimeSearchParam> patientSearchParams = mySearchParamHelper.getPatientSearchParamsForResourceType(resourceName);
			ourLog.debug("Resource type {} has patient search parameters [{}]", resourceName, patientSearchParams.stream().map(RuntimeSearchParam::getName).collect(Collectors.joining(", ")));
			for (RuntimeSearchParam patientSearchParam: patientSearchParams) {
				if (!theSearchParameterMap.containsKey(patientSearchParam.getName())) {
					continue;
				}
				List<List<IQueryParameterType>> lists = theSearchParameterMap.get(patientSearchParam.getName());
				for (List<IQueryParameterType> list : lists) {
					List<IQueryParameterType> toAdd = new ArrayList<>();
					for (IQueryParameterType paramVal : list) {
						if (!paramVal.getMissing() && paramVal.getQueryParameterQualifier().equalsIgnoreCase("equals")){
							String valueAsQueryToken = paramVal.getValueAsQueryToken(myFhirContext);
							Long pidOrThrowException = myIdHelperService.getPidOrThrowException(new IdDt(valueAsQueryToken));
							Set<String> expandedIds= myMdmLinkExpandSvc.expandMdmBySourceResourcePid(pidOrThrowException);
							ourLog.info("Expanded to resource ids: [{}]", String.join(",", expandedIds));
							toAdd.addAll(expandedIds.stream().map(StringParam::new).collect(Collectors.toList()));
						}
					}
					list.addAll(toAdd);
				}
			}
		}
		return true;
	}
}
