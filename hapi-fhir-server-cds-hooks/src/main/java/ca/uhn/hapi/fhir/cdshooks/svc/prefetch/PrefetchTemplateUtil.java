/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.prefetch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PrefetchTemplateUtil {
	private static final Pattern ourPlaceholder = Pattern.compile("\\{\\{context\\.(\\w+)}}");
	private static final Pattern daVinciPreFetch = Pattern.compile("\\{\\{context\\.(\\w+)\\.(\\w+)\\.(id)}}");
	private static final Pattern fhirPathPrefetch = Pattern.compile("\\{\\{context\\.(\\w+)\\.([^}]+)}}");

	private static final int GROUP_WITH_KEY = 1;
	private static final int DAVINCI_RESOURCETYPE_KEY = 2;

	private PrefetchTemplateUtil() {}

	public static String substituteTemplate(
			String theTemplate, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		final String parsedDaVinciPrefetchTemplate =
				handleDaVinciPrefetchTemplate(theTemplate, theContext, theFhirContext);
		String parsedDefaultPrefetchTemplate = handleDefaultPrefetchTemplate(parsedDaVinciPrefetchTemplate, theContext);
		return handleFhirPathPrefetchTemplate(parsedDefaultPrefetchTemplate, theContext, theFhirContext);
	}

	private static String handleFhirPathPrefetchTemplate(
			String theTemplate, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		Matcher matcher = fhirPathPrefetch.matcher(theTemplate);
		String returnValue = theTemplate;
		while (matcher.find()) {
			String key = matcher.group(GROUP_WITH_KEY);
			if (!theContext.containsKey(key)) {
				throw new InvalidRequestException(Msg.code(2376) + "Request context did not provide a value for key <"
						+ key + ">" + ".  Available keys in context are: " + theContext.getKeys());
			}
			try {
				final IBaseResource resource = theContext.getResource(key);
				final String fhirPathExpression = matcher.group(2);
				final IFhirPath fhirPath = theFhirContext.newFhirPath();
				fhirPath.setEvaluationContext(new IFhirPathEvaluationContext() {
					@Override
					public IBase resolveReference(@Nonnull IIdType theReference, @Nullable IBase theContext) {
						return BundleUtil.getResourceByReferenceAndResourceType(theFhirContext, (IBaseBundle) resource, (IBaseReference) theContext);
					}
				});
				final List<IBase> results = fhirPath.evaluate(resource, resource.fhirType() + "." + fhirPathExpression, IBase.class);
				final String resourceIds = results.stream()
						.map(result -> {
							if (result instanceof IBaseResource baseResource) {
								return  baseResource.getIdElement().getIdPart();
							} else if (result instanceof IPrimitiveType) {
								return ((IPrimitiveType<?>) result).getValueAsString();
							} else {
								return result.toString();
							}
						})
						.filter(id -> id != null && !id.isEmpty())
						.collect(Collectors.joining(","));
				if (StringUtils.isEmpty(resourceIds)) {
					throw new InvalidRequestException(
							Msg.code(2377) + "FHIRPath expression did not return any results: " + fhirPathExpression);
				}
				String templateToReplace = "{{context." + key + "." + fhirPathExpression + "}}";
				returnValue = returnValue.replace(templateToReplace, resourceIds);
			} catch (ClassCastException e) {
				throw new InvalidRequestException(Msg.code(2378) + "Request context did not provide valid "
						+ theFhirContext.getVersion().getVersion() + " Bundle resource for FHIRPath template key <"
						+ key + ">");
			}
		}
		return returnValue;
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
