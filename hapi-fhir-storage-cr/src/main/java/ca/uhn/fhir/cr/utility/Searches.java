package ca.uhn.fhir.cr.utility;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriOrListParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for parameter search
 */
public class Searches {
	private static final String VERSION_SP = "version";
	private static final String URL_SP = "url";
	private static final String NAME_SP = "name";
	private static final String ID_SP = "_id";

	/**
	 * Constructor
	 */
	private Searches() {
	}

	/**
	 * Creates and returns Parameter search results
	 */
	public static SearchParameterMap all() {
		return sync();
	}

	/**
	 * Creates and returns Parameter search results
	 */
	public static SearchParameterMap sync() {
		return SearchParameterMap.newSynchronous();
	}

	/**
	 * Creates and returns Parameter search results
	 */
	public static SearchParameterMap async() {
		return new SearchParameterMap();
	}

	/**
	 * Method to return a parameter and search by parameter name and parameter type
	 *
	 * @param theParamName String of parameter name
	 * @param theParam     IQuery parameter type
	 * @return a parameter
	 */
	public static SearchParameterMap byParam(String theParamName, IQueryParameterType theParam) {
		checkNotNull(theParamName);
		checkNotNull(theParam);

		return sync().add(theParamName, theParam);
	}

	/**
	 * Method to return a parameter and search by parameter name
	 *
	 * @param theName String of parameter name
	 * @return a parameter
	 */
	public static SearchParameterMap byName(String theName) {
		checkNotNull(theName);

		return byParam(NAME_SP, new StringParam(theName, true));
	}

	/**
	 * Method to return a parameter and search by parameter name and version
	 *
	 * @param theName    String of parameter name
	 * @param theVersion String of code version
	 * @return a parameter
	 */
	public static SearchParameterMap byName(String theName, String theVersion) {
		checkNotNull(theName);
		checkNotNull(theVersion);

		return byName(theName).add(VERSION_SP, new TokenParam(theVersion));
	}

	/**
	 * Method to return a parameter and search by url and parameter version
	 *
	 * @param theUrl     String of the url
	 * @param theVersion String of code version
	 * @return a parameter
	 */
	public static SearchParameterMap byUrl(String theUrl, String theVersion) {
		checkNotNull(theUrl);
		checkNotNull(theVersion);

		return byParam(URL_SP, new UriParam(theUrl)).add(VERSION_SP, new TokenParam(theVersion));
	}

	/**
	 * Method to return a parameter and search by url
	 *
	 * @param theUrl String of the url
	 * @return a parameter
	 */
	public static SearchParameterMap byUrl(String theUrl) {
		checkNotNull(theUrl);

		return byParam(URL_SP, new UriParam(theUrl));
	}

	/**
	 * Method to return a parameter and search by list of url's
	 *
	 * @param theUrls list of URL strings
	 * @return list of parameters
	 */
	// TODO: versioned version
	public static SearchParameterMap byUrls(List<String> theUrls) {
		checkNotNull(theUrls);
		UriOrListParam params = new UriOrListParam();

		theUrls.forEach(theUrl -> {
			checkNotNull(theUrl);
			params.addOr(new UriParam(theUrl));
		});

		return sync().add(ID_SP, params);
	}

	/**
	 * Method to return a parameter and search by canonical
	 *
	 * @param theCanonical a string representing the canonical
	 * @return parameter
	 */
	public static SearchParameterMap byCanonical(String theCanonical) {
		checkNotNull(theCanonical);

		SearchParameterMap search = byUrl(Canonicals.getUrl(theCanonical));
		String version = Canonicals.getVersion(theCanonical);
		if (version != null) {
			search.add(VERSION_SP, new TokenParam(version));
		}

		return search;
	}

	/**
	 * Method to return a parameter and search by canonicaltype
	 *
	 * @param theCanonicalType a variable representing the canonical type
	 * @return parameters by canonical type
	 */
	public static <C extends IPrimitiveType<String>> SearchParameterMap byCanonical(C theCanonicalType) {
		checkNotNull(theCanonicalType);
		checkArgument(theCanonicalType.hasValue());

		return byCanonical(theCanonicalType.getValue());
	}

	/**
	 * Method to return a parameter and search by canonical and version
	 *
	 * @param theCanonical a string representing the canonical
	 * @param version      a string representing the version
	 * @return parameter
	 */
	public static SearchParameterMap byCanonical(String theCanonical, String version) {
		checkNotNull(theCanonical);

		SearchParameterMap search = byUrl(Canonicals.getUrl(theCanonical));
		if (version != null) {
			search.add(VERSION_SP, new TokenParam(version));
		}

		return search;
	}

	/**
	 * Method to return a parameter and search by canonicaltype list
	 *
	 * @param theCanonicalTypes a variable representing list of the canonical type
	 * @return parameters by canonical type
	 */
	// TODO: use versioned version
	public static <C extends IPrimitiveType<String>> SearchParameterMap byCanonicals(List<C> theCanonicalTypes) {
		checkNotNull(theCanonicalTypes);

		List<String> urls = new ArrayList<>();
		theCanonicalTypes.forEach(canonicalType -> {
			checkArgument(canonicalType.hasValue());
			urls.add(canonicalType.getValue());
		});

		return byUrls(urls);
	}

	/**
	 * Method to return a parameter and search by IIDType
	 *
	 * @param theId IID Type variable representing measure ID
	 * @return parameter matching Id
	 */
	public static SearchParameterMap byId(IIdType theId) {
		checkNotNull(theId);
		return byParam(ID_SP, new TokenParam(theId.getIdPart()));
	}

	/**
	 * Method to return a parameter and search by IIDType
	 *
	 * @param theIdPart String representing theID
	 * @return parameter matching theID
	 */
	public static SearchParameterMap byId(String theIdPart) {
		checkNotNull(theIdPart);
		return byParam(ID_SP, new TokenParam(theIdPart));
	}

	/**
	 * Method to return a parameter and search by list of IDpart strings
	 *
	 * @param theIdParts String representing theID
	 * @return parameter matching theID
	 */
	public static SearchParameterMap byIds(List<String> theIdParts) {
		checkNotNull(theIdParts);
		TokenOrListParam params = new TokenOrListParam();

		theIdParts.forEach(theIdPart -> {
			checkNotNull(theIdPart);
			params.addOr(new TokenParam(theIdPart));
		});

		return sync().add(ID_SP, params);
	}
}
