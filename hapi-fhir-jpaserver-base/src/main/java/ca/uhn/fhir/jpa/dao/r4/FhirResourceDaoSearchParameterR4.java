package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

	protected void markAffectedResources(SearchParameter theResource) {
		Boolean reindex = theResource != null ? CURRENTLY_REINDEXING.get(theResource) : null;
		String expression = theResource != null ? theResource.getExpression() : null;
		markResourcesMatchingExpressionAsNeedingReindexing(reindex, expression);
	}


	@Override
	protected void postPersist(ResourceTable theEntity, SearchParameter theResource) {
		super.postPersist(theEntity, theResource);
		markAffectedResources(theResource);
	}

	@Override
	protected void postUpdate(ResourceTable theEntity, SearchParameter theResource) {
		super.postUpdate(theEntity, theResource);
		markAffectedResources(theResource);
	}

	@Override
	protected void preDelete(SearchParameter theResourceToDelete, ResourceTable theEntityToDelete) {
		super.preDelete(theResourceToDelete, theEntityToDelete);
		markAffectedResources(theResourceToDelete);
	}

	@Override
	protected void validateResourceForStorage(SearchParameter theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

		validateSearchParam(theResource, getContext(), getConfig(), mySearchParamRegistry, mySearchParamExtractor, myDaoRegistry.getResourceDao("SearchParameter"));
	}

	public static void validateSearchParam(SearchParameter theResource, FhirContext theContext, DaoConfig theDaoConfig, ISearchParamRegistry theSearchParamRegistry, ISearchParamExtractor theSearchParamExtractor, IFhirResourceDao<?> theSearchParameterDao) {

		/*
		 * If overriding built-in SPs is disabled on this server, make sure we aren't
		 * doing that
		 */
		String code = theResource.getCode();
		if (theDaoConfig.getModelConfig().isDefaultSearchParamsCanBeOverridden() == false) {
			for (IPrimitiveType<?> nextBaseType : theResource.getBase()) {
				String nextBase = nextBaseType.getValueAsString();
				RuntimeSearchParam existingSearchParam = theSearchParamRegistry.getActiveSearchParam(nextBase, code);
				if (existingSearchParam != null && existingSearchParam.getId() == null) {
					throw new UnprocessableEntityException("Can not override built-in search parameter " + nextBase + ":" + code + " because overriding is disabled on this server");
				}
			}
		}

		/*
		 * Everything below is validating that the SP is actually valid. We'll only do that if the
		 * SPO is active, so that we don't block people from uploading works-in-progress
		 */
		if (theResource.getStatus() == null) {
			throw new UnprocessableEntityException("SearchParameter.status is missing or invalid");
		}
		if (!theResource.getStatus().name().equals("ACTIVE")) {
			return;
		}

		// Require SearchParameter.base
		if (ElementUtil.isEmpty(theResource.getBase()) && (theResource.getType() == null || !Enumerations.SearchParamType.COMPOSITE.name().equals(theResource.getType().name()))) {
			throw new UnprocessableEntityException("SearchParameter.base is missing");
		}

		// Require SearchParameter.code
		if (ElementUtil.isEmpty(code) && (theResource.getType() == null || !Enumerations.SearchParamType.COMPOSITE.name().equals(theResource.getType().name()))) {
			throw new UnprocessableEntityException("SearchParameter.code is missing");
		}

		/*
		 * Make sure we don't already have a SearchParameter matching the base and code
		 */
		List<String> baseValues = theResource.getBase().stream().map(t -> t.getCode()).filter(t -> isNotBlank(t)).collect(Collectors.toList());
		if (baseValues.size() > 0 && isNotBlank(code)) {
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(SearchParameter.SP_BASE, new TokenOrListParam(null, baseValues));
			map.add(SearchParameter.SP_CODE, new TokenParam(code));
			map.add(SearchParameter.SP_STATUS, new TokenParam(Enumerations.PublicationStatus.ACTIVE.toCode()));
			boolean alredyExists = false;
			IBundleProvider outcome = theSearchParameterDao.search(map);
			for (IBaseResource next : outcome.getResources(0, outcome.sizeOrThrowNpe())) {
				if (theResource.getIdElement().hasIdPart()) {
					if (theResource.getIdElement().getIdPart().equals(next.getIdElement().getIdPart())) {
						alredyExists = true;
						break;
					}
				}
			}

			if (outcome.sizeOrThrowNpe() > 0 && !alredyExists) {
				String firstExistingId = outcome.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue();
				throw new UnprocessableEntityException("SearchParameter resource [" + firstExistingId + "] already exists matching at least one base in " + baseValues + " and/or code [" + code + "]");
			}
		}

		boolean isUnique = theResource.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE).stream().anyMatch(t -> "true".equals(t.getValueAsPrimitive().getValueAsString()));

		if (theResource.getType() != null && theResource.getType().name().equals(Enumerations.SearchParamType.COMPOSITE.name()) && isBlank(theResource.getExpression())) {

			// this is ok

		} else if (isBlank(theResource.getExpression())) {

			throw new UnprocessableEntityException("SearchParameter.expression is missing");

		} else {

			String expression = theResource.getExpression().trim();

			if (isUnique) {
				if (theResource.getComponent().size() == 0) {
					throw new UnprocessableEntityException("SearchParameter is marked as unique but has no components");
				}
				for (SearchParameter.SearchParameterComponentComponent next : theResource.getComponent()) {
					if (isBlank(next.getDefinition())) {
						throw new UnprocessableEntityException("SearchParameter is marked as unique but is missing component.definition");
					}
				}
			}

			if (!theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {

				// DSTU3 and below
				String[] expressionSplit = theSearchParamExtractor.split(expression);
				for (String nextPath : expressionSplit) {
					nextPath = nextPath.trim();

					int dotIdx = nextPath.indexOf('.');
					if (dotIdx == -1) {
						throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + nextPath + "\". Must start with a resource name.");
					}

					String resourceName = nextPath.substring(0, dotIdx);
					try {
						theContext.getResourceDefinition(resourceName);
					} catch (DataFormatException e) {
						throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + nextPath + "\": " + e.getMessage());
					}

					if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
						if (theDaoConfig.isValidateSearchParameterExpressionsOnSave()) {
							IBaseResource temporaryInstance = theContext.getResourceDefinition(resourceName).newInstance();
							try {
								theContext.newFluentPath().evaluate(temporaryInstance, nextPath, IBase.class);
							} catch (Exception e) {
								String msg = theContext.getLocalizer().getMessage(FhirResourceDaoSearchParameterR4.class, "invalidSearchParamExpression", nextPath, e.getMessage());
								throw new UnprocessableEntityException(msg, e);
							}
						}
					}
				}

			} else {

				if (!isUnique && theResource.getType() != Enumerations.SearchParamType.COMPOSITE && theResource.getType() != Enumerations.SearchParamType.SPECIAL && !REGEX_SP_EXPRESSION_HAS_PATH.matcher(expression).matches()) {
					throw new UnprocessableEntityException("SearchParameter.expression value \"" + expression + "\" is invalid");
				}

				// R4 and above
				try {
					theContext.newFluentPath().parse(expression);
				} catch (Exception e) {
					throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + expression + "\": " + e.getMessage());
				}

			}
		} // if have expression

	}

}
