/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PrefetchTemplateUtil {
	private static final Pattern ourPlaceholder = Pattern.compile("\\{\\{context\\.(\\w+)}}");
	private static final Pattern daVinciPreFetch = Pattern.compile("\\{\\{context\\.(\\w+)\\.(\\w+)\\.(id)}}");

	private static final int GROUP_WITH_KEY = 1;
	private static final int DAVINCI_RESOURCETYPE_KEY = 2;

	private PrefetchTemplateUtil() {}

	public static String substituteTemplate(
			String theTemplate, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		String parsedDaVinciPrefetchTemplate = handleDaVinciPrefetchTemplate(theTemplate, theContext, theFhirContext);
		return handleDefaultPrefetchTemplate(parsedDaVinciPrefetchTemplate, theContext);
	}

	/**
	 * The below DaVinci Prefetch template support is implemented based on IG specifications described
	 * <a href="http://hl7.org/fhir/us/davinci-crd/hooks.html#additional-prefetch-capabilities">here</a> version 1.0.0 - STU 1
	 * This is subject to change as the IG can be updated by the working committee.
	 */
	private static String handleDaVinciPrefetchTemplate(
			String theTemplate, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		Matcher matcher = daVinciPreFetch.matcher(theTemplate);
		String returnValue = theTemplate;
		while (matcher.find()) {
			String key = matcher.group(GROUP_WITH_KEY);
			if (!theContext.containsKey(key)) {
				throw new InvalidRequestException(Msg.code(2372) + "Request context did not provide a value for key <"
						+ key + ">" + ".  Available keys in context are: " + theContext.getKeys());
			}
			try {
				IBaseBundle bundle = (IBaseBundle) theContext.getResource(key);
				String resourceType = matcher.group(DAVINCI_RESOURCETYPE_KEY);
				String resourceIds = BundleUtil.toListOfResources(theFhirContext, bundle).stream()
						.filter(x -> x.fhirType().equals(resourceType))
						.map(x -> x.getIdElement().getIdPart())
						.collect(Collectors.joining(","));
				if (StringUtils.isEmpty(resourceIds)) {
					throw new InvalidRequestException(Msg.code(2373)
							+ "Request context did not provide for resource(s) matching template. ResourceType missing is: "
							+ resourceType);
				}
				String keyToReplace = key + "." + resourceType + "\\.(id)";
				returnValue = substitute(returnValue, keyToReplace, resourceIds);
			} catch (ClassCastException e) {
				throw new InvalidRequestException(Msg.code(2374) + "Request context did not provide valid "
						+ theFhirContext.getVersion().getVersion() + " Bundle resource for template key <" + key + ">");
			}
		}
		return returnValue;
	}

	private static String handleDefaultPrefetchTemplate(String theTemplate, CdsServiceRequestContextJson theContext) {
		Matcher matcher = ourPlaceholder.matcher(theTemplate);
		String returnValue = theTemplate;
		while (matcher.find()) {
			String key = matcher.group(GROUP_WITH_KEY);
			// Check to see if the context map is empty, or doesn't contain the key.
			// Note we cannot return the keyset as for cases where the map is empty this will throw a
			// NullPointerException.
			if (theContext.getString(key) == null) {
				throw new InvalidRequestException(
						Msg.code(2375) + "Either request context was empty or it did not provide a value for key <"
								+ key
								+ ">.  Please make sure you are including a context with valid keys.");
			}
			String value = theContext.getString(key);
			returnValue = substitute(returnValue, key, value);
		}
		return returnValue;
	}

	private static String substitute(String theString, String theKey, String theValue) {
		return theString.replaceAll("\\{\\{context\\." + theKey + "}}", theValue);
	}
}
