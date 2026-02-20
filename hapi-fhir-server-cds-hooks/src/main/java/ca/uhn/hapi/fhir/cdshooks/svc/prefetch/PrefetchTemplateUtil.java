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
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.BundleUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
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
	private static final Pattern referencedPrefetch = Pattern.compile("\\{\\{([^}]*%[^}]+)}}");

	private static final int GROUP_WITH_KEY = 1;
	private static final int DAVINCI_RESOURCETYPE_KEY = 2;

	private PrefetchTemplateUtil() {}

	public static String substituteTemplate(
			String theTemplate, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		final String parsedDaVinciPrefetchTemplate =
				handleDaVinciPrefetchTemplate(theTemplate, theContext, theFhirContext);
		String parsedDefaultPrefetchTemplate = handleDefaultPrefetchTemplate(parsedDaVinciPrefetchTemplate, theContext);
		String parsedReferencedTemplate =
				handleReferencedPrefetchTemplate(parsedDefaultPrefetchTemplate, theContext, theFhirContext);
		return handleFhirPathPrefetchTemplate(parsedReferencedTemplate, theContext, theFhirContext);
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
				// Handle OR operator in FHIRPath expressions by evaluating each part separately
				final List<IBase> results;
				if (fhirPathExpression.contains("|")) {
					String resourceType = resource.fhirType();
					String[] orParts = fhirPathExpression.split("\\|");
					results = new java.util.ArrayList<>();
					// Pattern to match "context.KEY." at the start of an OR part
					String contextPrefix = "context\\." + key + "\\.";
					for (String theOrPart : orParts) {
						String orPart = theOrPart.trim();
						// Strip "context.KEY." from the beginning of each OR part if present
						orPart = orPart.replaceFirst("^" + contextPrefix, "");
						// Create a fresh FhirPath instance for each OR part to avoid state issues
						final IFhirPath fhirPathPart = createFhirPathWithReferenceResolution(theFhirContext, resource);
						String fullExpression = resourceType + "." + orPart;
						List<IBase> partResults = fhirPathPart.evaluate(resource, fullExpression, IBase.class);
						results.addAll(partResults);
					}
				} else {
					final IFhirPath fhirPath = createFhirPathWithReferenceResolution(theFhirContext, resource);
					String fullFhirPathExpression = resource.fhirType() + "." + fhirPathExpression;
					results = fhirPath.evaluate(resource, fullFhirPathExpression, IBase.class);
				}
				final String resourceIds = convertResultsToIds(results);
				if (StringUtils.isEmpty(resourceIds)) {
					throw new InvalidRequestException(Msg.code(2377)
							+ "FHIRPath expression did not return any results for query: " + fhirPathExpression);
				}
				String templateToReplace = "{{context." + key + "." + fhirPathExpression + "}}";
				returnValue = returnValue.replace(templateToReplace, resourceIds);
			} catch (ClassCastException e) {
				throw new InvalidRequestException(Msg.code(2378) + "Request context did not provide valid "
						+ theFhirContext.getVersion().getVersion() + " Bundle resource for FHIRPath template key <"
						+ key + ">");
			} catch (FhirPathExecutionException e) {
				throw new InvalidRequestException("Unable to evaluate FHIRPath for prefetch template with expression "
						+ theTemplate + " for FHIR version "
						+ theFhirContext.getVersion().getVersion());
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

	private static String handleReferencedPrefetchTemplate(
			String theTemplate, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		Matcher matcher = referencedPrefetch.matcher(theTemplate);
		String returnValue = theTemplate;
		while (matcher.find()) {
			String expression = matcher.group(1);
			try {
				// Handle OR operator by splitting on |
				final List<IBase> results = new java.util.ArrayList<>();
				String[] orParts = expression.split("\\|");

				for (String theOrPart : orParts) {
					String orPart = theOrPart.trim();

					String key;
					String fhirPathExpression;
					IBaseResource resource;

					if (orPart.startsWith("%")) {
						// Extract key name and path from %KEY.path
						int dotIndex = orPart.indexOf('.', 1); // Start from 1 to skip the %
						if (dotIndex == -1) {
							continue; // Invalid format - no path after key
						}

						key = orPart.substring(1, dotIndex); // Extract KEY from %KEY
						fhirPathExpression = orPart.substring(dotIndex + 1); // Extract path after the dot
					} else if (orPart.startsWith("context.")) {
						// Handle context.KEY.path format (mixing with % format in OR expression)
						String withoutContext = orPart.substring(8); // Remove "context."
						int dotIndex = withoutContext.indexOf('.');
						if (dotIndex == -1) {
							continue; // Invalid format
						}
						key = withoutContext.substring(0, dotIndex);
						fhirPathExpression = withoutContext.substring(dotIndex + 1);
					} else {
						continue; // Invalid format
					}

					if (!theContext.containsKey(key)) {
						throw new PreconditionFailedException(
								Msg.code(2379) + "Request context did not provide a value for key <" + key + ">"
										+ ".  Available keys in context are: " + theContext.getKeys());
					}

					resource = theContext.getResource(key);
					// Create a fresh FhirPath instance for each OR part
					final IFhirPath fhirPath = createFhirPathWithReferenceResolution(theFhirContext, resource);
					String fullExpression = resource.fhirType() + "." + fhirPathExpression;
					List<IBase> partResults = fhirPath.evaluate(resource, fullExpression, IBase.class);
					results.addAll(partResults);
				}

				final String resourceIds = convertResultsToIds(results);
				if (StringUtils.isEmpty(resourceIds)) {
					throw new InvalidRequestException(
							Msg.code(2380) + "FHIRPath expression did not return any results for query: " + expression);
				}
				String templateToReplace = "{{" + expression + "}}";
				returnValue = returnValue.replace(templateToReplace, resourceIds);
			} catch (ClassCastException e) {
				throw new InvalidRequestException(Msg.code(2381) + "Request context did not provide valid "
						+ theFhirContext.getVersion().getVersion() + " resource for referenced prefetch template");
			} catch (FhirPathExecutionException e) {
				throw new InvalidRequestException(
						"Unable to evaluate FHIRPath for referenced prefetch template with expression "
								+ theTemplate + " for FHIR version "
								+ theFhirContext.getVersion().getVersion());
			}
		}
		return returnValue;
	}

	private static String substitute(String theString, String theKey, String theValue) {
		return theString.replaceAll("\\{\\{context\\." + theKey + "}}", theValue);
	}

	/**
	 * Converts a list of FHIRPath evaluation results to a comma-separated string of IDs.
	 * Handles IBaseResource, IPrimitiveType, and other IBase types.
	 */
	private static String convertResultsToIds(List<IBase> theResults) {
		return theResults.stream()
				.map(result -> {
					if (result instanceof IBaseResource baseResource) {
						return baseResource.getIdElement().getIdPart();
					} else if (result instanceof IPrimitiveType) {
						return ((IPrimitiveType<?>) result).getValueAsString();
					} else {
						return result.toString();
					}
				})
				.filter(id -> id != null && !id.isEmpty())
				.collect(Collectors.joining(","));
	}

	/**
	 * Creates a FhirPath instance configured with reference resolution within a Bundle or contained resources.
	 */
	private static IFhirPath createFhirPathWithReferenceResolution(
			FhirContext theFhirContext, IBaseResource theResource) {
		final IFhirPath fhirPath = theFhirContext.newFhirPath();
		fhirPath.setEvaluationContext(new IFhirPathEvaluationContext() {
			@Override
			public IBase resolveReference(@Nonnull IIdType theReference, @Nullable IBase theContext) {
				return BundleUtil.getReferenceInBundle(theFhirContext, theReference.getValue(), theResource);
			}
		});
		return fhirPath;
	}
}
