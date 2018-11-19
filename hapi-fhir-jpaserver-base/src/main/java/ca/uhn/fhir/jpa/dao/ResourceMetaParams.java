package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.*;
import org.hl7.fhir.r4.model.BaseResource;

import java.util.*;

public class ResourceMetaParams {
	/**
	 * These are parameters which are supported by {@link BaseHapiFhirResourceDao#searchForIds(SearchParameterMap)}
	 */
	public static final Map<String, Class<? extends IQueryParameterAnd<?>>> RESOURCE_META_AND_PARAMS;
	/**
	 * These are parameters which are supported by {@link BaseHapiFhirResourceDao#searchForIds(SearchParameterMap)}
	 */
	public static final Map<String, Class<? extends IQueryParameterType>> RESOURCE_META_PARAMS;
	public static final Set<String> EXCLUDE_ELEMENTS_IN_ENCODED;

	static {
		Map<String, Class<? extends IQueryParameterType>> resourceMetaParams = new HashMap<String, Class<? extends IQueryParameterType>>();
		Map<String, Class<? extends IQueryParameterAnd<?>>> resourceMetaAndParams = new HashMap<String, Class<? extends IQueryParameterAnd<?>>>();
		resourceMetaParams.put(BaseResource.SP_RES_ID, StringParam.class);
		resourceMetaAndParams.put(BaseResource.SP_RES_ID, StringAndListParam.class);
		resourceMetaParams.put(BaseResource.SP_RES_LANGUAGE, StringParam.class);
		resourceMetaAndParams.put(BaseResource.SP_RES_LANGUAGE, StringAndListParam.class);
		resourceMetaParams.put(Constants.PARAM_TAG, TokenParam.class);
		resourceMetaAndParams.put(Constants.PARAM_TAG, TokenAndListParam.class);
		resourceMetaParams.put(Constants.PARAM_PROFILE, UriParam.class);
		resourceMetaAndParams.put(Constants.PARAM_PROFILE, UriAndListParam.class);
		resourceMetaParams.put(Constants.PARAM_SECURITY, TokenParam.class);
		resourceMetaAndParams.put(Constants.PARAM_SECURITY, TokenAndListParam.class);
		RESOURCE_META_PARAMS = Collections.unmodifiableMap(resourceMetaParams);
		RESOURCE_META_AND_PARAMS = Collections.unmodifiableMap(resourceMetaAndParams);

		HashSet<String> excludeElementsInEncoded = new HashSet<String>();
		excludeElementsInEncoded.add("id");
		excludeElementsInEncoded.add("*.meta");
		EXCLUDE_ELEMENTS_IN_ENCODED = Collections.unmodifiableSet(excludeElementsInEncoded);
	}
}
