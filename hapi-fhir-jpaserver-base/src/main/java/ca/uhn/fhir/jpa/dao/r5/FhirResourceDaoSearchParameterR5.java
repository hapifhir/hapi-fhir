package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.utils.FHIRLexer;
import org.hl7.fhir.r5.utils.FHIRPathEngine;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class FhirResourceDaoSearchParameterR5 extends FhirResourceDaoR5<SearchParameter> implements IFhirResourceDaoSearchParameter<SearchParameter> {

	public static final DefaultProfileValidationSupport VALIDATION_SUPPORT = new DefaultProfileValidationSupport();
	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDao;

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
		String expression = theResource.getExpression();
		FhirContext context = getContext();
		Enum<?> type = theResource.getType();

		FhirResourceDaoSearchParameterR5.validateSearchParam(type, status, base, expression, context, getConfig());
	}

	public static void validateSearchParam(Enum<?> theType, Enum<?> theStatus, List<? extends IPrimitiveType> theBase, String theExpression, FhirContext theContext, DaoConfig theDaoConfig) {
		if (theStatus == null) {
			throw new UnprocessableEntityException("SearchParameter.status is missing or invalid");
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

			if (!theContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R5)) {
				String[] expressionSplit = BaseSearchParamExtractor.SPLIT.split(theExpression);
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
								String msg = theContext.getLocalizer().getMessage(FhirResourceDaoSearchParameterR5.class, "invalidSearchParamExpression", nextPath, e.getMessage());
								throw new UnprocessableEntityException(msg, e);
							}
						}
					}
				}

			} else {

				FHIRPathEngine fhirPathEngine = new FHIRPathEngine(new HapiWorkerContext(theContext, VALIDATION_SUPPORT));
				try {
					fhirPathEngine.parse(theExpression);
				} catch (FHIRLexer.FHIRLexerException e) {
					throw new UnprocessableEntityException("Invalid SearchParameter.expression value \"" + theExpression + "\": " + e.getMessage());
				}

			}
		} // if have expression
	}

}
