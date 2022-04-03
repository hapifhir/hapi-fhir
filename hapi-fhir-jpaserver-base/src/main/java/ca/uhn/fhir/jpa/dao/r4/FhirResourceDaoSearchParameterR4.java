package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

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

		boolean isUnique = theResource.getExtensionsByUrl(HapiExtensions.EXT_SP_UNIQUE).stream().anyMatch(t-> "true".equals(t.getValueAsPrimitive().getValueAsString()));

		if (theResource.getType() != null && theResource.getType().name().equals(Enumerations.SearchParamType.COMPOSITE.name()) && isBlank(theResource.getExpression())) {

			// this is ok

		} else if (isBlank(theResource.getExpression())) {

			throw new UnprocessableEntityException(Msg.code(1114) + "SearchParameter.expression is missing");

		} else {

			String expression = theResource.getExpression().trim();

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

			if (!theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {

				// DSTU3 and below
				String[] expressionSplit = theSearchParamExtractor.split(expression);
				for (String nextPath : expressionSplit) {
					nextPath = nextPath.trim();

					int dotIdx = nextPath.indexOf('.');
					if (dotIdx == -1) {
						throw new UnprocessableEntityException(Msg.code(1117) + "Invalid SearchParameter.expression value \"" + nextPath + "\". Must start with a resource name.");
					}

					String resourceName = nextPath.substring(0, dotIdx);
					try {
						theContext.getResourceDefinition(resourceName);
					} catch (DataFormatException e) {
						throw new UnprocessableEntityException(Msg.code(1118) + "Invalid SearchParameter.expression value \"" + nextPath + "\": " + e.getMessage());
					}

					if (theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
						if (theDaoConfig.isValidateSearchParameterExpressionsOnSave()) {
							IBaseResource temporaryInstance = theContext.getResourceDefinition(resourceName).newInstance();
							try {
								theContext.newFluentPath().evaluate(temporaryInstance, nextPath, IBase.class);
							} catch (Exception e) {
								String msg = theContext.getLocalizer().getMessageSanitized(FhirResourceDaoSearchParameterR4.class, "invalidSearchParamExpression", nextPath, e.getMessage());
								throw new UnprocessableEntityException(Msg.code(1119) + msg, e);
							}
						}
					}
				}
			} else {
				if (!isUnique && theResource.getType() != Enumerations.SearchParamType.COMPOSITE && theResource.getType() != Enumerations.SearchParamType.SPECIAL && !REGEX_SP_EXPRESSION_HAS_PATH.matcher(expression).matches()) {
					throw new UnprocessableEntityException(Msg.code(1120) + "SearchParameter.expression value \"" + expression + "\" is invalid");
				}

				// R4 and above
				try {
					theContext.newFluentPath().parse(expression);
				} catch (Exception e) {
					throw new UnprocessableEntityException(Msg.code(1121) + "Invalid SearchParameter.expression value \"" + expression + "\": " + e.getMessage());
				}

			}
		} // if have expression

	}

}
