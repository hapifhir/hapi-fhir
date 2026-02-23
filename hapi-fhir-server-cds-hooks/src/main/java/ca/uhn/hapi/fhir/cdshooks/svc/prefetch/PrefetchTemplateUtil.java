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
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrefetchTemplateUtil {
	// Matches any {{...}} placeholder in the template
	private static final Pattern SURROUNDING_CURLY_BRACES_PART = Pattern.compile("\\{\\{([^}]+)}}");
	private static final Pattern DA_VINCI_PART = Pattern.compile("^context\\.(\\w+)\\.(\\w+)\\.id$");
	private static final Pattern DEFAULT_PART = Pattern.compile("^context\\.(\\w+)$");
	private static final Pattern FHIR_PATH_PART = Pattern.compile("^context\\.(\\w+)\\.(.+)$");
	private static final Pattern REFERENCED_PREFETCH_PART = Pattern.compile("^%(\\w+)\\.(.+)$");

	private PrefetchTemplateUtil() {}

	public static String substituteTemplate(
			String theTemplate, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		final Matcher matcher = SURROUNDING_CURLY_BRACES_PART.matcher(theTemplate);
		final StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String rawExpression = matcher.group(1);
			List<String> parts =
					Arrays.stream(rawExpression.split("\\|")).map(String::trim).toList();
			List<String> substitutedPrefetchQueryParts = new ArrayList<>();
			for (String part : parts) {
				Matcher m;
				if ((m = DA_VINCI_PART.matcher(part)).matches()) {
					substitutedPrefetchQueryParts.addAll(handleDaVinciPart(m, theContext, theFhirContext));
				} else if ((m = DEFAULT_PART.matcher(part)).matches()) {
					substitutedPrefetchQueryParts.addAll(handleDefaultPart(m, theContext));
				} else if ((m = FHIR_PATH_PART.matcher(part)).matches()
						|| (m = REFERENCED_PREFETCH_PART.matcher(part)).matches()) {
					substitutedPrefetchQueryParts.addAll(
							convertResultsToIds(handleFhirPathPart(m, theContext, theFhirContext)));
				}
			}
			if (substitutedPrefetchQueryParts.isEmpty()) {
				throw new PreconditionFailedException(
						Msg.code(2377) + "Unable to resolve prefetch template : " + rawExpression + ". No result was found for the prefetch query.");
			}
			matcher.appendReplacement(sb, Matcher.quoteReplacement(String.join(",", substitutedPrefetchQueryParts)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * The below DaVinci Prefetch template support is implemented based on IG specifications described
	 * <a href="http://hl7.org/fhir/us/davinci-crd/hooks.html#additional-prefetch-capabilities">here</a> version 1.0.0 - STU 1
	 * This is subject to change as the IG can be updated by the working committee.
	 */
	private static List<String> handleDaVinciPart(
			Matcher theMatcher, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		final String key = theMatcher.group(1);
		final String resourceType = theMatcher.group(2);
		validateContextKeyExists(key, theContext);
		try {
			final IBaseBundle bundle = (IBaseBundle) theContext.getResource(key);
			return BundleUtil.toListOfResources(theFhirContext, bundle).stream()
					.filter(x -> x.fhirType().equals(resourceType))
					.map(x -> x.getIdElement().getIdPart())
					.filter(id -> id != null && !id.isEmpty())
					.toList();
		} catch (ClassCastException e) {
			throw new InvalidRequestException(Msg.code(2374) + "Request context did not provide valid "
					+ theFhirContext.getVersion().getVersion() + " Bundle resource for template key <" + key + ">");
		}
	}

	@Nonnull
	private static List<String> handleDefaultPart(Matcher theMatcher, CdsServiceRequestContextJson theContext) {
		final String key = theMatcher.group(1);
		validateContextKeyExists(key, theContext);
		return List.of(theContext.getString(key));
	}

	private static List<IBase> handleFhirPathPart(
			Matcher theMatcher, CdsServiceRequestContextJson theContext, FhirContext theFhirContext) {
		final String key = theMatcher.group(1);
		final String fhirPathExpression = theMatcher.group(2);
		return evaluateFhirPathOnContextKey(key, fhirPathExpression, theContext, theFhirContext);
	}

	/**
	 * Core FHIRPath evaluation used by both {@code context.KEY.expression} and {@code %KEY.expression} parts.
	 * Validates the key exists, retrieves the resource, and evaluates the expression.
	 */
	private static List<IBase> evaluateFhirPathOnContextKey(
			String thePrefetchKey,
			String theFhirPathExpression,
			@Nonnull CdsServiceRequestContextJson theContext,
			@Nonnull FhirContext theFhirContext) {
		validateContextKeyExists(thePrefetchKey, theContext);
		try {
			final IBaseResource resource = theContext.getResource(thePrefetchKey);
			final IFhirPath fhirPath = createFhirPathWithReferenceResolution(theFhirContext, resource);
			final String fullExpression = resource.fhirType() + "." + theFhirPathExpression;
			return fhirPath.evaluate(resource, fullExpression, IBase.class);
		} catch (ClassCastException e) {
			throw new InvalidRequestException(Msg.code(2378) + "Request context did not provide valid "
					+ theFhirContext.getVersion().getVersion() + " Bundle resource for FHIRPath template key <"
					+ thePrefetchKey + ">");
		} catch (FhirPathExecutionException e) {
			throw new InvalidRequestException("Unable to evaluate FHIRPath for prefetch template key <" + thePrefetchKey
					+ "> for FHIR version " + theFhirContext.getVersion().getVersion());
		}
	}

	private static void validateContextKeyExists(String theKey, CdsServiceRequestContextJson theContext) {
		if (!theContext.containsKey(theKey)) {
			throw new PreconditionFailedException(Msg.code(2379) + "Request context did not provide a value for key <"
					+ theKey + ">" + ".  Available keys in context are: " + theContext.getKeys());
		}
	}

	/**
	 * Converts a list of FHIRPath evaluation results to a list of ID strings.
	 * Handles IBaseResource, IPrimitiveType, and other IBase types.
	 */
	private static List<String> convertResultsToIds(List<IBase> theResults) {
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
				.toList();
	}

	/**
	 * Creates a FhirPath instance configured with reference resolution within a Bundle or contained resources.
	 */
	@Nonnull
	private static IFhirPath createFhirPathWithReferenceResolution(
			@Nonnull FhirContext theFhirContext, IBaseResource theResource) {
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
