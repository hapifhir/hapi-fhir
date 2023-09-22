package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;

public class MdmSearchParamBuildingUtils {

	private static final String IDENTIFIER = "identifier";

	private static final String TAG = "_tag";

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

		map.add(TAG, tagsToSearch);
		return map;
	}

	/**
	 * Creates a SearchParameterMap used for searching for golden resources
	 * by EID specifically.
	 */
	public static SearchParameterMap buildEidSearchParameterMap(
			String theEid, String theResourceType, MdmRulesJson theMdmRules) {
		SearchParameterMap map = buildBasicGoldenResourceSearchParameterMap(theEid);
		map.add(IDENTIFIER, new TokenParam(theMdmRules.getEnterpriseEIDSystemForResourceType(theResourceType), theEid));
		return map;
	}

	/**
	 * Creates a SearchParameterMap that can be used to find golden resources.
	 */
	public static SearchParameterMap buildBasicGoldenResourceSearchParameterMap(String theResourceType) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(TAG, new TokenParam(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD));
		return map;
	}
}
