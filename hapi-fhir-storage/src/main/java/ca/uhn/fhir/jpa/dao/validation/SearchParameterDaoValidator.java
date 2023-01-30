package ca.uhn.fhir.jpa.dao.validation;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SearchParameter;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParameterDaoValidator {

	private static final Pattern REGEX_SP_EXPRESSION_HAS_PATH = Pattern.compile("[( ]*([A-Z][a-zA-Z]+\\.)?[a-z].*");

	private final FhirContext myFhirContext;
	private final DaoConfig myDaoConfig;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final ModelConfig myModelConfig;

	public SearchParameterDaoValidator(FhirContext theContext, ModelConfig theModelConfig, DaoConfig theDaoConfig, ISearchParamRegistry theSearchParamRegistry) {
		myFhirContext = theContext;
		myModelConfig = theModelConfig;
		myDaoConfig = theDaoConfig;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	public void validate(SearchParameter searchParameter) {
		/*
		 * If overriding built-in SPs is disabled on this server, make sure we aren't
		 * doing that
		 */
		if (myModelConfig.isDefaultSearchParamsCanBeOverridden() == false) {
			for (IPrimitiveType<?> nextBaseType : searchParameter.getBase()) {
				String nextBase = nextBaseType.getValueAsString();
				RuntimeSearchParam existingSearchParam = mySearchParamRegistry.getActiveSearchParam(nextBase, searchParameter.getCode());
				if (existingSearchParam != null) {
					boolean isBuiltIn = existingSearchParam.getId() == null;
					isBuiltIn |= existingSearchParam.getUri().startsWith("http://hl7.org/fhir/SearchParameter/");
					if (isBuiltIn) {
						throw new UnprocessableEntityException(Msg.code(1111) + "Can not override built-in search parameter " + nextBase + ":" + searchParameter.getCode() + " because overriding is disabled on this server");
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

		if (isCompositeWithoutBase(searchParameter)) {
			throw new UnprocessableEntityException(Msg.code(1113) + "SearchParameter.base is missing");
		}

		boolean isUnique = hasAnyExtensionUniqueSetTo(searchParameter, true);

		if (isCompositeWithoutExpression(searchParameter)) {

			// this is ok

		} else if (isBlank(searchParameter.getExpression())) {

			throw new UnprocessableEntityException(Msg.code(1114) + "SearchParameter.expression is missing");

		} else {

			if (isUnique) {
				if (searchParameter.getComponent().size() == 0) {
					throw new UnprocessableEntityException(Msg.code(1115) + "SearchParameter is marked as unique but has no components");
				}
				for (SearchParameter.SearchParameterComponentComponent next : searchParameter.getComponent()) {
					if (isBlank(next.getDefinition())) {
						throw new UnprocessableEntityException(Msg.code(1116) + "SearchParameter is marked as unique but is missing component.definition");
					}
				}
			}

			FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();
			if (fhirVersion.isOlderThan(FhirVersionEnum.DSTU3)) {
				// omitting validation for DSTU2_HL7ORG, DSTU2_1 and DSTU2
			} else {

				if (myDaoConfig.isValidateSearchParameterExpressionsOnSave()) {

					validateExpressionPath(searchParameter);

					String expression = getExpression(searchParameter);

					try {
						myFhirContext.newFhirPath().parse(expression);
					} catch (Exception exception) {
						throw new UnprocessableEntityException(Msg.code(1121) + "Invalid FHIRPath format for SearchParameter.expression \"" + expression + "\": " + exception.getMessage());
					}
				}
			}
		}
	}

	private boolean isCompositeWithoutBase(SearchParameter searchParameter) {
		return ElementUtil.isEmpty(searchParameter.getBase()) && (searchParameter.getType() == null || !Enumerations.SearchParamType.COMPOSITE.name().equals(searchParameter.getType().name()));
	}

	private boolean isCompositeWithoutExpression(SearchParameter searchParameter) {
		return searchParameter.getType() != null && searchParameter.getType().name().equals(Enumerations.SearchParamType.COMPOSITE.name()) && isBlank(searchParameter.getExpression());
	}

	private void validateExpressionPath(SearchParameter theSearchParameter) {
		String expression = getExpression(theSearchParameter);

		boolean isResourceOfTypeComposite = theSearchParameter.getType() == Enumerations.SearchParamType.COMPOSITE;
		boolean isResourceOfTypeSpecial = theSearchParameter.getType() == Enumerations.SearchParamType.SPECIAL;
		boolean expressionHasPath = REGEX_SP_EXPRESSION_HAS_PATH.matcher(expression).matches();

		boolean isUnique = hasAnyExtensionUniqueSetTo(theSearchParameter, true);

		if (!isUnique && !isResourceOfTypeComposite && !isResourceOfTypeSpecial && !expressionHasPath) {
			throw new UnprocessableEntityException(Msg.code(1120) + "SearchParameter.expression value \"" + expression + "\" is invalid due to missing/incorrect path");
		}
	}

	private String getExpression(SearchParameter theSearchParameter) {
		return theSearchParameter.getExpression().trim();
	}

	private boolean hasAnyExtensionUniqueSetTo(SearchParameter theSearchParameter, boolean theValue) {
		String theValueAsString = Boolean.toString(theValue);

		return theSearchParameter
			.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE)
			.stream()
			.anyMatch(t -> theValueAsString.equals(t.getValueAsPrimitive().getValueAsString()));
	}
}
