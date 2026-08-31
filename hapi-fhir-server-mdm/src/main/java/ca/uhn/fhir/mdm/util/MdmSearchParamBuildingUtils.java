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
package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;

import java.util.Collection;

import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static org.hl7.fhir.dstu2016may.model.Basic.SP_IDENTIFIER;

public class MdmSearchParamBuildingUtils {

	/**
	 * Builds a search parameter map that can be used to find the
	 * golden resources associated with MDM blocked resources (ie, those
	 * resources that were omitted from MDM matching).
	 */
	public static SearchParameterMap buildSearchParameterForBlockedResourceCount(String theResourceType) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		TokenAndListParam tagsToSearch = new TokenAndListParam();
		tagsToSearch.addAnd(new TokenParam(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD));
		tagsToSearch.addAnd(new TokenParam(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_BLOCKED));

		map.add(PARAM_TAG, tagsToSearch);
		return map;
	}

	/**
	 * Creates a SearchParameterMap used for searching for golden resources
	 * by EID specifically.
	 *
	 * @deprecated use {@link #buildEidSearchParameterMap(Collection)}, which matches on the EID system as
	 * well as the value and can search several EIDs at once.
	 */
	@Deprecated
	public static SearchParameterMap buildEidSearchParameterMap(
			String theEid, String theResourceType, MdmRulesJson theMdmRules) {
		SearchParameterMap map = buildBasicGoldenResourceSearchParameterMap(theResourceType);
		map.add(
				SP_IDENTIFIER,
				new TokenParam(theMdmRules.getEnterpriseEIDSystemForResourceType(theResourceType), theEid));
		return map;
	}

	/**
	 * Creates a SearchParameterMap that finds the golden resources carrying any of the given EIDs, as a
	 * single OR query. Each EID is matched on its own system as well as its value, so that the same value
	 * issued by two different EID systems is not conflated.
	 *
	 * @param theEids the EIDs to search for
	 * @return a search parameter map restricted to golden records
	 */
	public static SearchParameterMap buildEidSearchParameterMap(Collection<CanonicalEID> theEids) {
		SearchParameterMap map = buildBasicGoldenResourceSearchParameterMap(null);
		TokenOrListParam eidsToSearch = new TokenOrListParam();
		theEids.forEach(eid -> eidsToSearch.addOr(new TokenParam(eid.getSystem(), eid.getValue())));
		map.add(SP_IDENTIFIER, eidsToSearch);
		return map;
	}

	/**
	 * Creates a SearchParameterMap that can be used to find golden resources.
	 */
	public static SearchParameterMap buildBasicGoldenResourceSearchParameterMap(String theResourceType) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(PARAM_TAG, new TokenParam(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD));
		return map;
	}
}
