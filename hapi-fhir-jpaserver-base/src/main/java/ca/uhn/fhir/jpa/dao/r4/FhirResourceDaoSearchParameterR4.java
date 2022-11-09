package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class FhirResourceDaoSearchParameterR4 extends BaseHapiFhirResourceDao<SearchParameter> implements IFhirResourceDaoSearchParameter<SearchParameter> {

	private static final Pattern REGEX_SP_EXPRESSION_HAS_PATH = Pattern.compile("[( ]*([A-Z][a-zA-Z]+\\.)?[a-z].*");
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	protected void reindexAffectedResources(SearchParameter theResource, RequestDetails theRequestDetails) {
		Boolean reindex = theResource != null ? CURRENTLY_REINDEXING.get(theResource) : null;
		String expression = theResource != null ? theResource.getExpression() : null;
		List<String> base = theResource != null ? theResource.getBase().stream().map(CodeType::getCode).collect(Collectors.toList()) : null;
		requestReindexForRelatedResources(reindex, base, theRequestDetails);
	}


	@Override
	protected void postPersist(ResourceTable theEntity, SearchParameter theResource, RequestDetails theRequestDetails) {
		super.postPersist(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void postUpdate(ResourceTable theEntity, SearchParameter theResource, RequestDetails theRequestDetails) {
		super.postUpdate(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void preDelete(SearchParameter theResourceToDelete, ResourceTable theEntityToDelete, RequestDetails theRequestDetails) {
		super.preDelete(theResourceToDelete, theEntityToDelete, theRequestDetails);
		reindexAffectedResources(theResourceToDelete, theRequestDetails);
	}

	@Override
	protected void validateResourceForStorage(SearchParameter theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

		validateSearchParam(theResource, getContext(), getConfig(), mySearchParamRegistry, mySearchParamExtractor);
	}

	public static void validateSearchParam(SearchParameter theResource, FhirContext theContext, DaoConfig theDaoConfig, ISearchParamRegistry theSearchParamRegistry, ISearchParamExtractor theSearchParamExtractor) {

		/*
		 * If overriding built-in SPs is disabled on this server, make sure we aren't
		 * doing that
		 */
		if (theDaoConfig.getModelConfig().isDefaultSearchParamsCanBeOverridden() == false) {
			for (IPrimitiveType<?> nextBaseType : theResource.getBase()) {
				String nextBase = nextBaseType.getValueAsString();
				RuntimeSearchParam existingSearchParam = theSearchParamRegistry.getActiveSearchParam(nextBase, theResource.getCode());
				if (existingSearchParam != null) {
					boolean isBuiltIn = existingSearchParam.getId() == null;
					isBuiltIn |= existingSearchParam.getUri().startsWith("http://hl7.org/fhir/SearchParameter/");
					if (isBuiltIn) {
						throw new UnprocessableEntityException(Msg.code(1111) + "Can not override built-in search parameter " + nextBase + ":" + theResource.getCode() + " because overriding is disabled on this server");
					}
				}
			}
		}

		/*
		 * Everything below is validating that the SP is actually valid. We'll only do that if the
		 * SPO is active, so that we don't block people from uploading works-in-progress
		 */
		if (theResource.getStatus() == null) {
			throw new UnprocessableEntityException(Msg.code(1112) + "SearchParameter.status is missing or invalid");
		}
		if (!theResource.getStatus().name().equals("ACTIVE")) {
			return;
		}

		if (ElementUtil.isEmpty(theResource.getBase()) && (theResource.getType() == null || !Enumerations.SearchParamType.COMPOSITE.name().equals(theResource.getType().name()))) {
			throw new UnprocessableEntityException(Msg.code(1113) + "SearchParameter.base is missing");
		}

		boolean isUnique = hasAnyExtensionUniqueSetTo(theResource, true);

		if (theResource.getType() != null && theResource.getType().name().equals(Enumerations.SearchParamType.COMPOSITE.name()) && isBlank(theResource.getExpression())) {

			// this is ok

		} else if (isBlank(theResource.getExpression())) {

			throw new UnprocessableEntityException(Msg.code(1114) + "SearchParameter.expression is missing");

		} else {

			if (isUnique) {
				if (theResource.getComponent().size() == 0) {
					throw new UnprocessableEntityException(Msg.code(1115) + "SearchParameter is marked as unique but has no components");
				}
				for (SearchParameter.SearchParameterComponentComponent next : theResource.getComponent()) {
					if (isBlank(next.getDefinition())) {
						throw new UnprocessableEntityException(Msg.code(1116) + "SearchParameter is marked as unique but is missing component.definition");
					}
				}
			}

			FhirVersionEnum fhirVersion = theContext.getVersion().getVersion();
			if (fhirVersion.isOlderThan(FhirVersionEnum.DSTU3)) {
				// omitting validation for DSTU2_HL7ORG, DSTU2_1 and DSTU2
			}
			else {

				if (theDaoConfig.isValidateSearchParameterExpressionsOnSave()) {

					validateExpressionPath(theResource);

					String expression = getExpression(theResource);

					try {
						theContext.newFhirPath().parse(expression);
					} catch (Exception exception) {
						throw new UnprocessableEntityException(Msg.code(1121) + "Invalid FHIRPath format for SearchParameter.expression \"" + expression + "\": " + exception.getMessage());
					}
				}
			}
		}

	}

	private static void validateExpressionPath(SearchParameter theSearchParameter){
		String expression = getExpression(theSearchParameter);

		boolean isResourceOfTypeComposite = theSearchParameter.getType() == Enumerations.SearchParamType.COMPOSITE;
		boolean isResourceOfTypeSpecial = theSearchParameter.getType() == Enumerations.SearchParamType.SPECIAL;
		boolean expressionHasPath = REGEX_SP_EXPRESSION_HAS_PATH.matcher(expression).matches();

		boolean isUnique = hasAnyExtensionUniqueSetTo(theSearchParameter, true);

		if ( !isUnique && !isResourceOfTypeComposite && !isResourceOfTypeSpecial && !expressionHasPath ) {
			throw new UnprocessableEntityException(Msg.code(1120) + "SearchParameter.expression value \"" + expression + "\" is invalid due to missing/incorrect path");
		}

	}

	private static String getExpression(SearchParameter theSearchParameter){
		return theSearchParameter.getExpression().trim();
	}

	private static boolean hasAnyExtensionUniqueSetTo(SearchParameter theSearchParameter, boolean theValue){
		String theValueAsString = Boolean.toString(theValue);

		return theSearchParameter
			.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE)
			.stream()
			.anyMatch(t-> theValueAsString.equals(t.getValueAsPrimitive().getValueAsString()));
	}

}
