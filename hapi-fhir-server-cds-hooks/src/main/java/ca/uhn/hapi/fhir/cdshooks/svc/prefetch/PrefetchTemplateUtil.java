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

public class PrefetchTemplateUtil {
	private static final Pattern SURROUNDING_CURLY_BRACES_PART = Pattern.compile("\\{\\{([^}]+)}}");
	private static final Pattern DA_VINCI_PART = Pattern.compile("^context\\.(\\w+)\\.(\\w+)\\.id$");
	private static final Pattern DEFAULT_PART = Pattern.compile("^context\\.(\\w+)$");
	private static final Pattern FHIR_PATH_PART = Pattern.compile("^context\\.(\\w+)\\.(.+)$");
	private static final Pattern REFERENCED_PREFETCH_PART = Pattern.compile("^%(\\w+)\\.(.+)$");
	private static final String UNION_OPERATOR_REGEX = "\\|";

	private PrefetchTemplateUtil() {}

	private interface PartResolutionResult {
		record Proceed() implements PartResolutionResult {}

		record Success(List<String> values) implements PartResolutionResult {}

		record MissingContextKey(String key, String availableKeys) implements PartResolutionResult {}

		record NoMatch() implements PartResolutionResult {}
	}

	@Nonnull
	public static String substituteTemplate(
			@Nonnull String theTemplate,
			@Nonnull CdsServiceRequestContextJson theContext,
			@Nonnull FhirContext theFhirContext) {
		return SURROUNDING_CURLY_BRACES_PART
				.matcher(theTemplate)
				.replaceAll(match ->
						Matcher.quoteReplacement(resolveExpression(match.group(1), theContext, theFhirContext)));
	}

	private static String resolveExpression(
			String theRawExpression,
			@Nonnull CdsServiceRequestContextJson theContext,
			@Nonnull FhirContext theFhirContext) {
		final List<String> results = new ArrayList<>();
		PartResolutionResult.MissingContextKey firstMissingKey = null;
		for (String rawPart : theRawExpression.split(UNION_OPERATOR_REGEX)) {
			final PartResolutionResult partResult = resolvePartResults(rawPart.trim(), theContext, theFhirContext);
			if (partResult instanceof PartResolutionResult.Success s) {
				results.addAll(s.values());
			} else if (partResult instanceof PartResolutionResult.MissingContextKey m && firstMissingKey == null) {
				firstMissingKey = m;
			}
		}
		if (!results.isEmpty()) {
			return String.join(",", results);
		}
		if (firstMissingKey != null) {
			throw new InvalidRequestException(Msg.code(2372) + "Request context did not provide a value for key <"
					+ firstMissingKey.key() + ">.  Available keys in context are: " + firstMissingKey.availableKeys());
		}
		throw new InvalidRequestException(Msg.code(2856) + "Unable to resolve prefetch template : " + theRawExpression
				+ ". No result was found for the prefetch query.");
	}

	@Nonnull
	private static PartResolutionResult resolvePartResults(
			String thePart, @Nonnull CdsServiceRequestContextJson theContext, @Nonnull FhirContext theFhirContext) {
		PartResolutionResult result = handleDaVinciPart(thePart, theContext, theFhirContext);
		if (result instanceof PartResolutionResult.NoMatch) {
			result = handleDefaultPart(thePart, theContext);
		}
		if (result instanceof PartResolutionResult.NoMatch) {
			result = handleFhirPathAndReferencedPrefetchPart(thePart, theContext, theFhirContext);
		}
		return result;
	}

	/**
	 * The below DaVinci Prefetch template support is implemented based on IG specifications described
	 * <a href="http://hl7.org/fhir/us/davinci-crd/hooks.html#additional-prefetch-capabilities">here</a> version 1.0.0 - STU 1
	 * This is subject to change as the IG can be updated by the working committee.
	 */
	@Nonnull
	private static PartResolutionResult handleDaVinciPart(
			String thePart, @Nonnull CdsServiceRequestContextJson theContext, @Nonnull FhirContext theFhirContext) {
		final Matcher m = DA_VINCI_PART.matcher(thePart);
		final PartResolutionResult partResolutionResult = matchAndCheckKey(m, theContext);
		if (!(partResolutionResult instanceof PartResolutionResult.Proceed)) {
			return partResolutionResult;
		}
		final String key = m.group(1);
		final String resourceType = m.group(2);
		try {
			final IBaseBundle bundle = (IBaseBundle) theContext.getResource(key);
			final List<String> ids = BundleUtil.toListOfResources(theFhirContext, bundle).stream()
					.filter(x -> x.fhirType().equals(resourceType))
					.map(x -> x.getIdElement().getIdPart())
					.filter(StringUtils::isNotBlank)
					.toList();
			if (ids.isEmpty()) {
				throw new InvalidRequestException(Msg.code(2373)
						+ "Request context did not provide for resource(s) matching template. ResourceType missing is: "
						+ resourceType);
			}
			return new PartResolutionResult.Success(ids);
		} catch (ClassCastException e) {
			throw new InvalidRequestException(Msg.code(2374) + "Request context did not provide valid "
					+ theFhirContext.getVersion().getVersion() + " Bundle resource for template key <" + key + ">");
		}
	}

	@Nonnull
	private static PartResolutionResult handleDefaultPart(
			String thePart, @Nonnull CdsServiceRequestContextJson theContext) {
		final Matcher m = DEFAULT_PART.matcher(thePart);
		final PartResolutionResult partResolutionResult = matchAndCheckKey(m, theContext);
		if (!(partResolutionResult instanceof PartResolutionResult.Proceed)) {
			return partResolutionResult;
		}
		final String key = m.group(1);
		try {
			final String value = theContext.getString(key);
			if (value == null) {
				throw new InvalidRequestException(
						Msg.code(2375) + "Request context value for key <" + key + "> is null or not a string.");
			}
			return new PartResolutionResult.Success(List.of(value));
		} catch (ClassCastException e) {
			throw new InvalidRequestException(
					Msg.code(2857) + "Request context value for key <" + key + "> is null or not a string.");
		}
	}

	@Nonnull
	private static PartResolutionResult handleFhirPathAndReferencedPrefetchPart(
			String thePart, @Nonnull CdsServiceRequestContextJson theContext, @Nonnull FhirContext theFhirContext) {
		Matcher m = FHIR_PATH_PART.matcher(thePart);
		PartResolutionResult partResolutionResult = matchAndCheckKey(m, theContext);
		if (partResolutionResult instanceof PartResolutionResult.NoMatch) {
			m = REFERENCED_PREFETCH_PART.matcher(thePart);
			partResolutionResult = matchAndCheckKey(m, theContext);
		}
		if (!(partResolutionResult instanceof PartResolutionResult.Proceed)) {
			return partResolutionResult;
		}
		final String key = m.group(1);
		final String expression = m.group(2);
		return new PartResolutionResult.Success(convertPrimitiveResultsToString(
				evaluateFhirPathOnContextKey(key, expression, theContext, theFhirContext), key));
	}

	@Nonnull
	private static List<IBase> evaluateFhirPathOnContextKey(
			String thePrefetchKey,
			String theFhirPathExpression,
			@Nonnull CdsServiceRequestContextJson theContext,
			@Nonnull FhirContext theFhirContext) {
		try {
			final IBaseResource resource = theContext.getResource(thePrefetchKey);
			final IFhirPath fhirPath = createFhirPathWithReferenceLocalResolution(theFhirContext, resource);
			final String fullExpression = resource.fhirType() + "." + theFhirPathExpression;
			return fhirPath.evaluate(resource, fullExpression, IBase.class);
		} catch (ClassCastException e) {
			throw new InvalidRequestException(Msg.code(2858) + "Request context did not provide valid "
					+ theFhirContext.getVersion().getVersion() + " Bundle resource for FHIRPath template key <"
					+ thePrefetchKey + ">");
		} catch (FhirPathExecutionException e) {
			throw new InvalidRequestException(Msg.code(2859)
					+ "Unable to evaluate FHIRPath for prefetch template key <" + thePrefetchKey + "> for FHIR version "
					+ theFhirContext.getVersion().getVersion());
		}
	}

	@Nonnull
	private static PartResolutionResult matchAndCheckKey(
			Matcher theMatcher, @Nonnull CdsServiceRequestContextJson theContext) {
		if (!theMatcher.matches()) {
			return new PartResolutionResult.NoMatch();
		}
		final String key = theMatcher.group(1);
		if (!theContext.containsKey(key)) {
			return new PartResolutionResult.MissingContextKey(
					key, theContext.getKeys().toString());
		}
		return new PartResolutionResult.Proceed();
	}

	@Nonnull
	private static List<String> convertPrimitiveResultsToString(
			@Nonnull List<IBase> theResults, String thePrefetchKey) {
		return theResults.stream()
				.map(result -> {
					if (result instanceof IPrimitiveType) {
						return ((IPrimitiveType<?>) result).getValueAsString();
					} else {
						throw new InvalidRequestException(Msg.code(2860)
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
