package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;

import java.util.Set;

public class ComboParamUtil {

	/**
	 * Non instantiable
	 */
	private ComboParamUtil() {
		super();
	}

	/**
	 * Given an index value, applies the combo param allowlist and returns <code>true</code> if the specific value
	 * should be included.
	 */
	public static boolean applyAllowListLogic(Set<RuntimeSearchParam.ComboInclude> theValueAllowList, BaseResourceIndexedSearchParam theSearchIndex) {
		if (theValueAllowList.isEmpty()) {
			return true;
		}

		/*
		 * If you update the logic in this method, also update the other "applyAllowListLogic" method in this class
		 */

		boolean include = false;
		if (theSearchIndex instanceof ResourceIndexedSearchParamToken token) {
			for (RuntimeSearchParam.ComboInclude nextValueAllow : theValueAllowList) {
				if (nextValueAllow.matchesSystemAndValue(token.getSystem(), token.getValue())) {
					include = true;
					break;
				}
			}
		} else if (theSearchIndex instanceof ResourceIndexedSearchParamUri uri) {
			for (RuntimeSearchParam.ComboInclude nextValueAllow : theValueAllowList) {
				if (nextValueAllow.matchesValue(uri.getUri())) {
					include = true;
					break;
				}
			}
		}
		return include;
	}

	/**
	 * Given an index value, applies the combo param allowlist and returns <code>true</code> if the specific value
	 * should be included.
	 */
	public static boolean applyAllowListLogic(Set<RuntimeSearchParam.ComboInclude> theValueAllowList, IQueryParameterType theQueryParameter) {
		if (theValueAllowList.isEmpty()) {
			return true;
		}

		/*
		 * If you update the logic in this method, also update the other "applyAllowListLogic" method in this class
		 */

		boolean include = false;
		if (theQueryParameter instanceof TokenParam token) {
			for (RuntimeSearchParam.ComboInclude nextValueAllow :
					theValueAllowList) {
				if (nextValueAllow.matchesSystemAndValue(token.getSystem(), token.getValue())) {
					include = true;
					break;
				}
			}
		} else if (theQueryParameter instanceof UriParam uri) {
			for (RuntimeSearchParam.ComboInclude nextValueAllow :
				theValueAllowList) {
				if (nextValueAllow.matchesValue(uri.getValue())) {
					include = true;
					break;
				}
			}
		}
		return include;
	}
}
