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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PrefetchTemplateUtil {
	private static final Pattern SURROUNDING_CURLY_BRACES_PART = Pattern.compile("\\{\\{([^}]+)}}");
	private static final Pattern DA_VINCI_PART = Pattern.compile("^context\\.(\\w+)\\.(\\w+)\\.id$");
	private static final Pattern DEFAULT_PART = Pattern.compile("^context\\.(\\w+)$");
	private static final Pattern FHIR_PATH_PART = Pattern.compile("^context\\.(\\w+)\\.(.+)$");
	private static final Pattern REFERENCED_PREFETCH_PART = Pattern.compile("^%(\\w+)\\.(.+)$");
	private static final String UNION_OPERATOR = "\\|";

	private PrefetchTemplateUtil() {}

	public static String substituteTemplate(
			String theTemplate, @Nonnull CdsServiceRequestContextJson theContext, @Nonnull FhirContext theFhirContext) {
		return SURROUNDING_CURLY_BRACES_PART
				.matcher(theTemplate)
				.replaceAll(match ->
						Matcher.quoteReplacement(resolveExpression(match.group(1), theContext, theFhirContext)));
	}

	private static String resolveExpression(
			String theRawExpression,
			@Nonnull CdsServiceRequestContextJson theContext,
			@Nonnull FhirContext theFhirContext) {
		final List<String> parts = Stream.of(theRawExpression.split(UNION_OPERATOR))
				.map(String::trim)
				.toList();
		final List<String> results = new ArrayList<>();
		for (String part : parts) {
			results.addAll(handleDaVinciPart(part, theContext, theFhirContext));
			results.addAll(handleDefaultPart(part, theContext));
			results.addAll(handleFhirPathAndReferencedPrefetchPart(part, theContext, theFhirContext));
		}
		if (results.isEmpty()) {
			throw new PreconditionFailedException(Msg.code(2856) + "Unable to resolve prefetch template : "
					+ theRawExpression + ". No result was found for the prefetch query.");
		}
		return String.join(",", results);
	}

	/**
	 * The below DaVinci Prefetch template support is implemented based on IG specifications described
	 * <a href="http://hl7.org/fhir/us/davinci-crd/hooks.html#additional-prefetch-capabilities">here</a> version 1.0.0 - STU 1
	 * This is subject to change as the IG can be updated by the working committee.
	 */
	private static List<String> handleDaVinciPart(
			String thePart, @Nonnull CdsServiceRequestContextJson theContext, @Nonnull FhirContext theFhirContext) {
		final Matcher m = DA_VINCI_PART.matcher(thePart);
		if (!m.matches()) return List.of();
		final String key = m.group(1);
		final String resourceType = m.group(2);
		validateContextKeyExists(key, theContext);
		try {
			final IBaseBundle bundle = (IBaseBundle) theContext.getResource(key);
			return BundleUtil.toListOfResources(theFhirContext, bundle).stream()
					.filter(x -> x.fhirType().equals(resourceType))
					.map(x -> x.getIdElement().getIdPart())
					.filter(StringUtils::isNotBlank)
					.toList();
		} catch (ClassCastException e) {
			throw new PreconditionFailedException(Msg.code(2374) + "Request context did not provide valid "
					+ theFhirContext.getVersion().getVersion() + " Bundle resource for template key <" + key + ">");
		}
	}

	@Nonnull
	private static List<String> handleDefaultPart(String thePart, @Nonnull CdsServiceRequestContextJson theContext) {
		final Matcher m = DEFAULT_PART.matcher(thePart);
		if (!m.matches()) return List.of();
		final String key = m.group(1);
		validateContextKeyExists(key, theContext);
		try {//
			return List.of(theContext.getString(key));
		} catch (ClassCastException e) {
			throw new PreconditionFailedException(
					Msg.code(2857) + "Request context value for key <" + key + "> is not a string.");
		}
	}

	@Nonnull
	private static List<String> handleFhirPathAndReferencedPrefetchPart(
			String thePart, @Nonnull CdsServiceRequestContextJson theContext, @Nonnull FhirContext theFhirContext) {
		Matcher m = FHIR_PATH_PART.matcher(thePart);
		if (!m.matches()) m = REFERENCED_PREFETCH_PART.matcher(thePart);
		if (!m.matches()) return List.of();
		return convertPrimitiveResultsToString(
				evaluateFhirPathOnContextKey(m.group(1), m.group(2), theContext, theFhirContext), m.group(1));
	}

	@Nonnull
	private static List<IBase> evaluateFhirPathOnContextKey(
			String thePrefetchKey,
			String theFhirPathExpression,
			@Nonnull CdsServiceRequestContextJson theContext,
			@Nonnull FhirContext theFhirContext) {
		validateContextKeyExists(thePrefetchKey, theContext);
		try {
			final IBaseResource resource = theContext.getResource(thePrefetchKey);
			final IFhirPath fhirPath = createFhirPathWithReferenceLocalResolution(theFhirContext, resource);
			final String fullExpression = resource.fhirType() + "." + theFhirPathExpression;
			return fhirPath.evaluate(resource, fullExpression, IBase.class);
		} catch (ClassCastException e) {
			throw new PreconditionFailedException(Msg.code(2858) + "Request context did not provide valid "
					+ theFhirContext.getVersion().getVersion() + " Bundle resource for FHIRPath template key <"
					+ thePrefetchKey + ">");
		} catch (FhirPathExecutionException e) {
			throw new PreconditionFailedException(Msg.code(2859)
					+ "Unable to evaluate FHIRPath for prefetch template key <" + thePrefetchKey + "> for FHIR version "
					+ theFhirContext.getVersion().getVersion());
		}
	}

	private static void validateContextKeyExists(String theKey, @Nonnull CdsServiceRequestContextJson theContext) {
		if (!theContext.containsKey(theKey)) {
			throw new PreconditionFailedException(Msg.code(2372) + "Request context did not provide a value for key <"
					+ theKey + ">.  Available keys in context are: " + theContext.getKeys());
		}
	}

	@Nonnull
	private static List<String> convertPrimitiveResultsToString(
			@Nonnull List<IBase> theResults, String thePrefetchKey) {
		return theResults.stream()
				.map(result -> {
					if (result instanceof IPrimitiveType) {
						return ((IPrimitiveType<?>) result).getValueAsString();
					} else {
						throw new PreconditionFailedException(Msg.code(2860)
								+ "FHIR path expression returned a non-primitive result: "
								+ result.getClass().getSimpleName() + " for Prefetch Key : <" + thePrefetchKey + ">");
					}
				})
				.filter(StringUtils::isNotBlank)
				.toList();
	}

	private static IFhirPath createFhirPathWithReferenceLocalResolution(
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
