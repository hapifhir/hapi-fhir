package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @since 5.4.0
 */
@Interceptor
public class SearchPreferHandlingInterceptor {

	@Nonnull
	private PreferHandlingEnum myDefaultBehaviour;
	@Nullable
	private ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Constructor that uses the {@link RestfulServer} itself to determine
	 * the allowable search params.
	 */
	public SearchPreferHandlingInterceptor() {
		setDefaultBehaviour(PreferHandlingEnum.STRICT);
	}

	/**
	 * Constructor that uses a dedicated {@link ISearchParamRegistry} instance. This is mainly
	 * intended for the JPA server.
	 */
	public SearchPreferHandlingInterceptor(ISearchParamRegistry theSearchParamRegistry) {
		this();
		mySearchParamRegistry = theSearchParamRegistry;
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLER_SELECTED)
	public void incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		if (!SearchMethodBinding.isPlainSearchRequest(theRequestDetails)) {
			return;
		}

		String resourceName = theRequestDetails.getResourceName();
		if (!theRequestDetails.getFhirContext().getResourceTypes().contains(resourceName)) {
			// This is an error. Let the server handle it normally.
			return;
		}

		String preferHeader = theRequestDetails.getHeader(Constants.HEADER_PREFER);
		PreferHandlingEnum handling = null;
		if (isNotBlank(preferHeader)) {
			PreferHeader parsedPreferHeader = RestfulServerUtils.parsePreferHeader((IRestfulServer<?>) theRequestDetails.getServer(), preferHeader);
			handling = parsedPreferHeader.getHanding();
		}

		// Default behaviour
		if (handling == null) {
			handling = getDefaultBehaviour();
		}

		removeUnwantedParams(handling, theRequestDetails);
	}

	private void removeUnwantedParams(PreferHandlingEnum theHandling, RequestDetails theRequestDetails) {

		ISearchParamRegistry searchParamRetriever = mySearchParamRegistry;
		if (searchParamRetriever == null) {
			searchParamRetriever = ((RestfulServer) theRequestDetails.getServer()).createConfiguration();
		}

		String resourceName = theRequestDetails.getResourceName();
		HashMap<String, String[]> newMap = null;
		for (String paramName : theRequestDetails.getParameters().keySet()) {
			if (paramName.startsWith("_")) {
				continue;
			}

			// Strip modifiers and chains
			for (int i = 0; i < paramName.length(); i++) {
				char nextChar = paramName.charAt(i);
				if (nextChar == '.' || nextChar == ':') {
					paramName = paramName.substring(0, i);
					break;
				}
			}

			RuntimeSearchParam activeSearchParam = searchParamRetriever.getActiveSearchParam(resourceName, paramName);
			if (activeSearchParam == null) {

				if (theHandling == PreferHandlingEnum.LENIENT) {

					if (newMap == null) {
						newMap = new HashMap<>(theRequestDetails.getParameters());
					}

					newMap.remove(paramName);

				} else {

					// Strict handling
					List<String> allowedParams = searchParamRetriever.getActiveSearchParams(resourceName).getSearchParamNames().stream().sorted().distinct().collect(Collectors.toList());
					HapiLocalizer localizer = theRequestDetails.getFhirContext().getLocalizer();
					String msg = localizer.getMessage("ca.uhn.fhir.jpa.dao.BaseStorageDao.invalidSearchParameter", paramName, resourceName, allowedParams);
					throw new InvalidRequestException(Msg.code(323) + msg);

				}
			}

		}

		if (newMap != null) {
			theRequestDetails.setParameters(newMap);
		}
	}

	public PreferHandlingEnum getDefaultBehaviour() {
		return myDefaultBehaviour;
	}

	public void setDefaultBehaviour(@Nonnull PreferHandlingEnum theDefaultBehaviour) {
		Validate.notNull(theDefaultBehaviour, "theDefaultBehaviour must not be null");
		myDefaultBehaviour = theDefaultBehaviour;
	}
}
