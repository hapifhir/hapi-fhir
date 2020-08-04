package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

		Enum<?> status = theResource.getStatus();
		List<CodeType> base = theResource.getBase();
		String code = theResource.getCode();
		String expression = theResource.getExpression();
		FhirContext context = getContext();
		Enum<?> type = theResource.getType();

		FhirResourceDaoSearchParameterR4.validateSearchParam(mySearchParamRegistry, mySearchParamExtractor, code, type, status, base, expression, context, getConfig());
	}

	public static void validateSearchParam(ISearchParamRegistry theSearchParamRegistry, ISearchParamExtractor theSearchParamExtractor, String theCode, Enum<?> theType, Enum<?> theStatus, List<? extends IPrimitiveType> theBase, String theExpression, FhirContext theContext, DaoConfig theDaoConfig) {
		/*
		 * If overriding built-in SPs is disabled on this server, make sure we aren't
		 * doing that
		 */
		if (theDaoConfig.getModelConfig().isDefaultSearchParamsCanBeOverridden() == false) {
			for (IPrimitiveType<?> nextBaseType : theBase) {
				String nextBase = nextBaseType.getValueAsString();
				RuntimeSearchParam existingSearchParam = theSearchParamRegistry.getActiveSearchParam(nextBase, theCode);
				if (existingSearchParam != null && existingSearchParam.getId() == null) {
					throw new UnprocessableEntityException("Can not override built-in search parameter " + nextBase + ":" + theCode + " because overriding is disabled on this server");
				}
			}
		}

		/*
		 * Everything below is validating that the SP is actually valid. We'll only do that if the
		 * SPO is active, so that we don't block people from uploading works-in-progress
		 */
		if (theStatus == null) {
			throw new UnprocessableEntityException("SearchParameter.status is missing or invalid");
		}
		if (!theStatus.name().equals("ACTIVE")) {
			return;
		}

		if (ElementUtil.isEmpty(theBase) && (theType == null || !Enumerations.SearchParamType.COMPOSITE.name().equals(theType.name()))) {
			throw new UnprocessableEntityException("SearchParameter.base is missing");
		}

		if (theType != null && theType.name().equals(Enumerations.SearchParamType.COMPOSITE.name()) && isBlank(theExpression)) {

			// this is ok

		} else if (isBlank(theExpression)) {

			throw new UnprocessableEntityException("SearchParameter.expression is missing");

		} else {

			theExpression = theExpression.trim();

			if (!theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4)) {
				String[] expressionSplit = theSearchParamExtractor.split(theExpression);
				for (String nextPath : expressionSplit) {
					nextPath = nextPath.trim();

					int dotIdx = nextPath.indexOf('.');
					if (dotIdx == -1) {
						throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + nextPath + "\". Must start with a resource name");
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

				try {
					theContext.newFluentPath().parse(theExpression);
				} catch (Exception e) {
					throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + theExpression + "\": " + e.getMessage());
				}

			}
		} // if have expression

	}

}
