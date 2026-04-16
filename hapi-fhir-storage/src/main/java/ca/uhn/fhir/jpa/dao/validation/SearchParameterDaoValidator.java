/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.dao.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SearchParameter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum.DATE;
import static ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum.NUMBER;
import static ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum.QUANTITY;
import static ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum.REFERENCE;
import static ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum.STRING;
import static ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum.TOKEN;
import static ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum.URI;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParameterDaoValidator {

	private static final Pattern REGEX_SP_EXPRESSION_HAS_PATH = Pattern.compile("[( ]*([A-Z][a-zA-Z]+\\.)?[a-z].*");
	private static final String RESOURCE = "Resource";
	private static final String DOMAIN_RESOURCE = "DomainResource";

	private final FhirContext myFhirContext;
	private final JpaStorageSettings myStorageSettings;
	private final ISearchParamRegistry mySearchParamRegistry;

	public SearchParameterDaoValidator(
			FhirContext theContext,
			JpaStorageSettings theStorageSettings,
			ISearchParamRegistry theSearchParamRegistry) {
		myFhirContext = theContext;
		myStorageSettings = theStorageSettings;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	public void validate(SearchParameter searchParameter) {
		/*
		 * If overriding built-in SPs is disabled on this server, make sure we aren't
		 * doing that
		 */
		if (myStorageSettings.isDefaultSearchParamsCanBeOverridden() == false) {
			for (IPrimitiveType<?> nextBaseType : searchParameter.getBase()) {
				String nextBase = nextBaseType.getValueAsString();
				RuntimeSearchParam existingSearchParam = mySearchParamRegistry.getActiveSearchParam(
						nextBase, searchParameter.getCode(), ISearchParamRegistry.SearchParamLookupContextEnum.ALL);
				if (existingSearchParam != null) {
					boolean isBuiltIn = existingSearchParam.getId() == null;
					isBuiltIn |= existingSearchParam.getUri().startsWith("http://hl7.org/fhir/SearchParameter/");
					if (isBuiltIn) {
						throw new UnprocessableEntityException(
								Msg.code(1111) + "Can not override built-in search parameter " + nextBase + ":"
										+ searchParameter.getCode() + " because overriding is disabled on this server");
					}
				}
			}
		}

		/*
		 * Everything below is validating that the SP is actually valid. We'll only do that if the
		 * SPO is active, so that we don't block people from uploading works-in-progress
		 */
		if (searchParameter.getStatus() == null) {
			throw new UnprocessableEntityException(Msg.code(1112) + "SearchParameter.status is missing or invalid");
		}
		if (!searchParameter.getStatus().name().equals("ACTIVE")) {
			return;
		}

		// Search parameters must have a base
		if (isCompositeWithoutBase(searchParameter)) {
			throw new UnprocessableEntityException(Msg.code(1113) + "SearchParameter.base is missing");
		}

		// Do we have a valid expression
		if (isCompositeWithoutExpression(searchParameter)) {

			// this is ok

		} else if (isBlank(searchParameter.getExpression())) {

			if (!Constants.PARAM_CONTENT.equals(searchParameter.getCode())
					&& !Constants.PARAM_TEXT.equals(searchParameter.getCode())) {
				throw new UnprocessableEntityException(Msg.code(1114) + "SearchParameter.expression is missing");
			}

		} else {

			FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();
			if (fhirVersion.isOlderThan(FhirVersionEnum.DSTU3)) {
				// omitting validation for DSTU2_HL7ORG, DSTU2_1 and DSTU2
			} else {
				maybeValidateCompositeSpForUniqueIndexing(searchParameter);
				maybeValidateSearchParameterExpressionsOnSave(searchParameter);
				maybeValidateCompositeWithComponent(searchParameter);
			}
		}
	}

	private boolean isCompositeSp(SearchParameter theSearchParameter) {
		return theSearchParameter.getType() != null
				&& theSearchParameter.getType().equals(Enumerations.SearchParamType.COMPOSITE);
	}

	private boolean isCompositeWithoutBase(SearchParameter searchParameter) {
		return ElementUtil.isEmpty(searchParameter.getBase())
				&& ElementUtil.isEmpty(
						searchParameter.getExtensionsByUrl(HapiExtensions.EXTENSION_SEARCHPARAM_CUSTOM_BASE_RESOURCE))
				&& !isCompositeSp(searchParameter);
	}

	private boolean isCompositeWithoutExpression(SearchParameter searchParameter) {
		return isCompositeSp(searchParameter) && isBlank(searchParameter.getExpression());
	}

	private boolean isCompositeWithComponent(SearchParameter theSearchParameter) {
		return isCompositeSp(theSearchParameter) && theSearchParameter.hasComponent();
	}

	private boolean isCompositeSpForUniqueIndexing(SearchParameter theSearchParameter) {
		return isCompositeSp(theSearchParameter) && hasAnyExtensionUniqueSetTo(theSearchParameter, true);
	}

	private void maybeValidateCompositeSpForUniqueIndexing(SearchParameter theSearchParameter) {
		if (isCompositeSpForUniqueIndexing(theSearchParameter)) {
			if (!theSearchParameter.hasComponent()) {
				throw new UnprocessableEntityException(
						Msg.code(1115) + "SearchParameter is marked as unique but has no components");
			}
			for (SearchParameter.SearchParameterComponentComponent next : theSearchParameter.getComponent()) {
				if (isBlank(next.getDefinition())) {
					throw new UnprocessableEntityException(
							Msg.code(1116) + "SearchParameter is marked as unique but is missing component.definition");
				}
			}
		}
	}

	private void maybeValidateSearchParameterExpressionsOnSave(SearchParameter theSearchParameter) {
		if (myStorageSettings.isValidateSearchParameterExpressionsOnSave()) {
			validateExpressionPath(theSearchParameter);
			validateExpressionIsParsable(theSearchParameter);
		}
	}

	private void validateExpressionPath(SearchParameter theSearchParameter) {
		String expression = getExpression(theSearchParameter);

		boolean isResourceOfTypeComposite = theSearchParameter.getType() == Enumerations.SearchParamType.COMPOSITE;
		boolean isResourceOfTypeSpecial = theSearchParameter.getType() == Enumerations.SearchParamType.SPECIAL;
		boolean expressionHasPath =
				REGEX_SP_EXPRESSION_HAS_PATH.matcher(expression).matches();

		boolean isUnique = hasAnyExtensionUniqueSetTo(theSearchParameter, true);

		if (!isUnique && !isResourceOfTypeComposite && !isResourceOfTypeSpecial && !expressionHasPath) {
			throw new UnprocessableEntityException(Msg.code(1120) + "SearchParameter.expression value \"" + expression
					+ "\" is invalid due to missing/incorrect path");
		}

		if (!isResourceOfTypeComposite && !isResourceOfTypeSpecial) {
			validateExpressionAgainstBase(theSearchParameter);
		}
	}

	/**
	 * Validates that the FHIRPath expression's resource type prefix is consistent with the declared
	 * base resource types. If base is a specific resource type (e.g. PractitionerRole), at least one
	 * expression path segment must start with that resource type. If base is Resource or DomainResource,
	 * the expression must use a Resource. or DomainResource. prefix — a specific type prefix (e.g. Person.)
	 * is rejected as a misconfiguration since the parameter would be registered against all resource types
	 * but only extract values from that one specific type.
	 */
	private void validateExpressionAgainstBase(SearchParameter theSearchParameter) {
		String expression = getExpression(theSearchParameter);
		List<String> bases = theSearchParameter.getBase().stream()
				.map(IPrimitiveType::getValueAsString)
				.filter(Objects::nonNull)
				.toList();

		if (bases.isEmpty()) {
			return;
		}

		String[] paths = expression.split("\\|");

		boolean allBasesAreGeneric = bases.stream().allMatch(b -> RESOURCE.equals(b) || DOMAIN_RESOURCE.equals(b));

		if (allBasesAreGeneric) {
			for (String path : paths) {
				String prefix = extractTypePrefix(path.trim());
				if (prefix != null && !RESOURCE.equals(prefix) && !DOMAIN_RESOURCE.equals(prefix)) {
					throw new UnprocessableEntityException(Msg.code(2910) + "SearchParameter.expression '" + expression
							+ "' uses type-specific prefix '" + prefix
							+ "' but base is [" + String.join(", ", bases)
							+ "]. Expression must use Resource or DomainResource prefix when base is generic.");
				}
			}
		} else {
			for (String base : bases) {
				if (RESOURCE.equals(base) || DOMAIN_RESOURCE.equals(base)) {
					continue;
				}
				boolean anyPathMatchesBase = false;
				for (String path : paths) {
					String prefix = extractTypePrefix(path.trim());
					if (prefix == null
							|| base.equals(prefix)
							|| RESOURCE.equals(prefix)
							|| DOMAIN_RESOURCE.equals(prefix)) {
						anyPathMatchesBase = true;
						break;
					}
				}
				if (!anyPathMatchesBase) {
					throw new UnprocessableEntityException(Msg.code(2911) + "No path in expression '" + expression
							+ "' matches the base [" + base + "].");
				}
			}
		}
	}

	/**
	 * Extracts the resource type prefix from a FHIRPath expression path segment.
	 * Returns null if no type-qualified prefix is present.
	 */
	private static String extractTypePrefix(String thePath) {
		// Strip any leading parentheses or spaces
		int start = 0;
		while (start < thePath.length() && (thePath.charAt(start) == '(' || thePath.charAt(start) == ' ')) {
			start++;
		}
		String trimmed = thePath.substring(start);
		int dotIndex = trimmed.indexOf('.');
		if (dotIndex > 0) {
			String candidate = trimmed.substring(0, dotIndex);
			if (Character.isUpperCase(candidate.charAt(0))) {
				return candidate;
			}
		}
		return null;
	}

	private void validateExpressionIsParsable(SearchParameter theSearchParameter) {
		String expression = getExpression(theSearchParameter);

		try {
			myFhirContext.newFhirPath().parse(expression);
		} catch (Exception exception) {
			throw new UnprocessableEntityException(
					Msg.code(1121) + "Invalid FHIRPath format for SearchParameter.expression \"" + expression + "\": "
							+ exception.getMessage());
		}
	}

	private String getExpression(SearchParameter theSearchParameter) {
		return theSearchParameter.getExpression().trim();
	}

	private boolean hasAnyExtensionUniqueSetTo(SearchParameter theSearchParameter, boolean theValue) {
		String theValueAsString = Boolean.toString(theValue);

		return theSearchParameter.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE).stream()
				.anyMatch(t -> theValueAsString.equals(t.getValueAsPrimitive().getValueAsString()));
	}

	private void maybeValidateCompositeWithComponent(SearchParameter theSearchParameter) {
		if (isCompositeWithComponent(theSearchParameter)) {
			validateCompositeSearchParameterComponents(theSearchParameter);
		}
	}

	private void validateCompositeSearchParameterComponents(SearchParameter theSearchParameter) {
		theSearchParameter.getComponent().stream()
				.filter(SearchParameter.SearchParameterComponentComponent::hasDefinition)
				.map(SearchParameter.SearchParameterComponentComponent::getDefinition)
				.filter(Objects::nonNull)
				.map((String url) -> mySearchParamRegistry.getActiveSearchParamByUrl(
						url, ISearchParamRegistry.SearchParamLookupContextEnum.ALL))
				.filter(Objects::nonNull)
				.forEach(theRuntimeSp -> validateComponentSpTypeAgainstWhiteList(
						theRuntimeSp, getAllowedSearchParameterTypes(theSearchParameter)));
	}

	private void validateComponentSpTypeAgainstWhiteList(
			RuntimeSearchParam theRuntimeSearchParam,
			Collection<RestSearchParameterTypeEnum> theAllowedSearchParamTypes) {
		if (!theAllowedSearchParamTypes.contains(theRuntimeSearchParam.getParamType())) {
			throw new UnprocessableEntityException(String.format(
					"%sInvalid component search parameter type: %s in component.definition: %s, supported types: %s",
					Msg.code(2347),
					theRuntimeSearchParam.getParamType().name(),
					theRuntimeSearchParam.getUri(),
					theAllowedSearchParamTypes.stream().map(Enum::name).collect(Collectors.joining(", "))));
		}
	}

	/*
	 * Returns allowed Search Parameter Types for a given composite or combo search parameter
	 * This prevents the creation of search parameters that would fail during runtime (during a GET request)
	 * Below you can find references to runtime usage for each parameter type:
	 *
	 * For Composite Search Parameters without HSearch indexing enabled (JPA only):
	 * @see QueryStack#createPredicateCompositePart() and SearchBuilder#createCompositeSort()
	 *
	 * For Composite Search Parameters with HSearch indexing enabled:
	 * @see HSearchCompositeSearchIndexDataImpl#writeIndexEntry()
	 *
	 * For Combo Search Parameters:
	 * @see BaseSearchParamExtractor.extractParameterCombinationsForComboParam()
	 */
	private Set<RestSearchParameterTypeEnum> getAllowedSearchParameterTypes(SearchParameter theSearchParameter) {
		// combo unique search parameter
		if (hasAnyExtensionUniqueSetTo(theSearchParameter, true)) {
			return Set.of(STRING, TOKEN, DATE, QUANTITY, URI, NUMBER, REFERENCE);
			// combo non-unique search parameter or composite Search Parameter with HSearch indexing
		} else if (hasAnyExtensionUniqueSetTo(theSearchParameter, false)
				|| // combo non-unique search parameter
				myStorageSettings.isAdvancedHSearchIndexing()) { // composite Search Parameter with HSearch indexing
			return Set.of(STRING, TOKEN, DATE, QUANTITY, URI, NUMBER, REFERENCE);
		} else { // composite Search Parameter (JPA only)
			return Set.of(STRING, TOKEN, DATE, QUANTITY);
		}
	}
}
