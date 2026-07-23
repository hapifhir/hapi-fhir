/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import com.google.common.collect.Sets;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Base class for predicate builders that handle token search parameters.
 */
public abstract class BaseTokenPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseTokenPredicateBuilder.class);

	@Autowired
	protected IValidationSupport myValidationSupport;

	@Autowired
	protected ITermReadSvc myTerminologySvc;

	@Autowired
	protected FhirContext myContext;

	@Autowired
	protected JpaStorageSettings myStorageSettings;

	protected BaseTokenPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder, DbTable theTable) {
		super(theSearchSqlBuilder, theTable);
	}

	/**
	 * Builds a SQL predicate for a token search parameter for standard equality searches.
	 * @return a SQL condition matching resources that have the given token values
	 */
	public Condition createPredicateToken(
			Collection<IQueryParameterType> theParameters,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateToken(
				theParameters, theResourceName, theSpnamePrefix, theSearchParam, null, theRequestPartitionId);
	}

	/**
	 * Builds a SQL predicate for a token search parameter. Pass {@code theOperation} to use a
	 * compare operation from a {@code _filter} search, or {@code null} for a standard equality match.
	 */
	public final Condition createPredicateToken(
			Collection<IQueryParameterType> theParameters,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {

		final List<FhirVersionIndependentConcept> codes = new ArrayList<>();
		String paramName = QueryParameterUtils.getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());
		SearchFilterParser.CompareOperation operation = theOperation;
		TokenParamModifier modifier = null;

		for (IQueryParameterType nextParameter : theParameters) {
			String code;
			String system;
			if (nextParameter instanceof TokenParam) {
				TokenParam id = (TokenParam) nextParameter;
				system = id.getSystem();
				code = id.getValue();
				modifier = id.getModifier();
			} else if (nextParameter instanceof BaseIdentifierDt) {
				BaseIdentifierDt id = (BaseIdentifierDt) nextParameter;
				system = id.getSystemElement().getValueAsString();
				code = id.getValueElement().getValue();
			} else if (nextParameter instanceof BaseCodingDt) {
				BaseCodingDt id = (BaseCodingDt) nextParameter;
				system = id.getSystemElement().getValueAsString();
				code = id.getCodeElement().getValue();
			} else if (nextParameter instanceof NumberParam) {
				NumberParam number = (NumberParam) nextParameter;
				system = null;
				code = number.getValueAsQueryToken(getFhirContext());
			} else {
				throw new IllegalArgumentException(Msg.code(1236) + "Invalid token type: " + nextParameter.getClass());
			}

			if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				ourLog.info(
						"Parameter[{}] has system ({}) that is longer than maximum ({}) so will truncate: {} ",
						paramName,
						system.length(),
						ResourceIndexedSearchParamToken.MAX_LENGTH,
						system);
			}
			if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				ourLog.info(
						"Parameter[{}] has code ({}) that is longer than maximum ({}) so will truncate: {} ",
						paramName,
						code.length(),
						ResourceIndexedSearchParamToken.MAX_LENGTH,
						code);
			}

			// process token modifiers (:in, :not-in, :above, :below, :of-type, :not)
			if (modifier == TokenParamModifier.IN || modifier == TokenParamModifier.NOT_IN) {
				if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
					ValueSetExpansionOptions valueSetExpansionOptions = new ValueSetExpansionOptions();
					valueSetExpansionOptions.setCount(myStorageSettings.getMaximumExpansionSize());
					IValidationSupport.ValueSetExpansionOutcome expanded = myValidationSupport.expandValueSet(
							new ValidationSupportContext(myValidationSupport), valueSetExpansionOptions, code);
					codes.addAll(extractValueSetCodes(expanded.getValueSet()));
				} else {
					codes.addAll(myTerminologySvc.expandValueSetIntoConceptList(null, code));
				}
				if (modifier == TokenParamModifier.NOT_IN) {
					operation = SearchFilterParser.CompareOperation.ne;
				}
			} else if (modifier == TokenParamModifier.ABOVE) {
				system = determineSystemIfMissing(theSearchParam, code, system);
				validateHaveSystemAndCode(paramName, code, system);
				codes.addAll(myTerminologySvc.findCodesAbove(system, code));
			} else if (modifier == TokenParamModifier.BELOW) {
				system = determineSystemIfMissing(theSearchParam, code, system);
				validateHaveSystemAndCode(paramName, code, system);
				codes.addAll(myTerminologySvc.findCodesBelow(system, code));
			} else if (modifier == TokenParamModifier.OF_TYPE) {
				if (!myStorageSettings.isIndexIdentifierOfType()) {
					throw new MethodNotAllowedException(
							Msg.code(2012) + "The :of-type modifier is not enabled on this server");
				}
				if (isBlank(system) || isBlank(code)) {
					throw new InvalidRequestException(Msg.code(2013) + "Invalid parameter value for :of-type query");
				}
				int pipeIdx = code.indexOf('|');
				if (pipeIdx < 1 || pipeIdx == code.length() - 1) {
					throw new InvalidRequestException(Msg.code(2014) + "Invalid parameter value for :of-type query");
				}
				paramName = paramName + Constants.PARAMQUALIFIER_TOKEN_OF_TYPE;
				codes.add(new FhirVersionIndependentConcept(system, code));
			} else {
				if (modifier == TokenParamModifier.NOT && operation == null) {
					operation = SearchFilterParser.CompareOperation.ne;
				}
				codes.add(new FhirVersionIndependentConcept(system, code));
			}
		}

		List<FhirVersionIndependentConcept> sortedCodesList = codes.stream()
				.filter(t -> t.getCode() != null || t.getSystem() != null)
				.sorted()
				.distinct()
				.collect(Collectors.toList());

		if (sortedCodesList.isEmpty()) {
			// This will never match anything
			setMatchNothing();
			return null;
		}

		if (operation == SearchFilterParser.CompareOperation.ne) {
			/*
			 * For a token :not search, we look for index rows that have the right identity (i.e. it's the right resource and
			 * param name) but not the actual provided token value.
			 */
			Condition hashIdentityPredicate =
					buildNeHashIdentityCondition(theRequestPartitionId, theResourceName, paramName);
			Condition hashValuePredicate = createPredicateOrList(theResourceName, paramName, sortedCodesList, false);
			return QueryParameterUtils.toAndPredicate(hashIdentityPredicate, hashValuePredicate);
		}

		Condition predicate = createPredicateOrList(theResourceName, paramName, sortedCodesList, true);
		Condition optionalHashIdentity =
				buildOptionalHashIdentityForEquals(theRequestPartitionId, theResourceName, paramName);
		if (optionalHashIdentity != null) {
			predicate = QueryParameterUtils.toAndPredicate(optionalHashIdentity, predicate);
		}
		return predicate;
	}

	/**
	 * Builds the hash-identity predicate for the {@code :not} (ne) branch.
	 * Subclasses targeting tables without a direct {@code HASH_IDENTITY} column should override.
	 */
	protected Condition buildNeHashIdentityCondition(
			RequestPartitionId theRequestPartitionId, String theResourceName, String theParamName) {
		return createHashIdentityPredicate(theRequestPartitionId, theResourceName, theParamName);
	}

	/**
	 * Returns an optional hash-identity predicate to combine with the equality condition.
	 */
	protected Condition buildOptionalHashIdentityForEquals(
			RequestPartitionId theRequestPartitionId, String theResourceName, String theParamName) {
		return null;
	}

	/**
	 * Converts the resolved list of concepts into a table-specific SQL predicate.
	 * Called by {@link #createPredicateToken} for both the equality and ne branches.
	 */
	protected abstract Condition createPredicateOrList(
			String theResourceType,
			String theSearchParamName,
			List<FhirVersionIndependentConcept> theCodes,
			boolean theWantEquals);

	private List<FhirVersionIndependentConcept> extractValueSetCodes(IBaseResource theValueSet) {
		List<FhirVersionIndependentConcept> retVal = new ArrayList<>();

		RuntimeResourceDefinition vsDef = myContext.getResourceDefinition("ValueSet");
		BaseRuntimeChildDefinition expansionChild = vsDef.getChildByName("expansion");
		Optional<IBase> expansionOpt = expansionChild.getAccessor().getFirstValueOrNull(theValueSet);
		if (expansionOpt.isPresent()) {
			IBase expansion = expansionOpt.get();
			BaseRuntimeElementCompositeDefinition<?> expansionDef =
					(BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(expansion.getClass());
			BaseRuntimeChildDefinition containsChild = expansionDef.getChildByName("contains");
			List<IBase> contains = containsChild.getAccessor().getValues(expansion);

			BaseRuntimeChildDefinition.IAccessor systemAccessor = null;
			BaseRuntimeChildDefinition.IAccessor codeAccessor = null;
			for (IBase nextContains : contains) {
				if (systemAccessor == null) {
					systemAccessor = myContext
							.getElementDefinition(nextContains.getClass())
							.getChildByName("system")
							.getAccessor();
				}
				if (codeAccessor == null) {
					codeAccessor = myContext
							.getElementDefinition(nextContains.getClass())
							.getChildByName("code")
							.getAccessor();
				}
				String system = systemAccessor
						.getFirstValueOrNull(nextContains)
						.map(t -> (IPrimitiveType<?>) t)
						.map(IPrimitiveType::getValueAsString)
						.orElse(null);
				String code = codeAccessor
						.getFirstValueOrNull(nextContains)
						.map(t -> (IPrimitiveType<?>) t)
						.map(IPrimitiveType::getValueAsString)
						.orElse(null);
				if (isNotBlank(system) && isNotBlank(code)) {
					retVal.add(new FhirVersionIndependentConcept(system, code));
				}
			}
		}

		return retVal;
	}

	private String determineSystemIfMissing(RuntimeSearchParam theSearchParam, String code, String theSystem) {
		String retVal = theSystem;
		if (retVal == null) {
			if (theSearchParam != null) {
				Set<String> valueSetUris = Sets.newHashSet();
				for (String nextPath : theSearchParam.getPathsSplitForResourceType(getResourceType())) {
					Class<? extends IBaseResource> type = getFhirContext()
							.getResourceDefinition(getResourceType())
							.getImplementingClass();
					BaseRuntimeChildDefinition def =
							getFhirContext().newTerser().getDefinition(type, nextPath);
					if (def instanceof BaseRuntimeDeclaredChildDefinition) {
						String valueSet = ((BaseRuntimeDeclaredChildDefinition) def).getBindingValueSet();
						if (isNotBlank(valueSet)) {
							valueSetUris.add(valueSet);
						}
					}
				}
				if (valueSetUris.size() == 1) {
					String valueSet = valueSetUris.iterator().next();
					ValueSetExpansionOptions options = new ValueSetExpansionOptions().setFailOnMissingCodeSystem(false);
					List<FhirVersionIndependentConcept> candidateCodes =
							myTerminologySvc.expandValueSetIntoConceptList(options, valueSet);
					for (FhirVersionIndependentConcept nextCandidate : candidateCodes) {
						if (nextCandidate.getCode().equals(code)) {
							retVal = nextCandidate.getSystem();
							break;
						}
					}
				}
			}
		}
		return retVal;
	}

	protected void validateHaveSystemAndCode(String theParamName, String theCode, String theSystem) {
		String systemDesc = defaultIfBlank(theSystem, "(missing)");
		String codeDesc = defaultIfBlank(theCode, "(missing)");
		if (isBlank(theCode)) {
			String msg = getFhirContext()
					.getLocalizer()
					.getMessage(
							TokenPredicateBuilder.class,
							"invalidCodeMissingSystem",
							theParamName,
							systemDesc,
							codeDesc);
			throw new InvalidRequestException(Msg.code(1239) + msg);
		}
		if (isBlank(theSystem)) {
			String msg = getFhirContext()
					.getLocalizer()
					.getMessage(
							TokenPredicateBuilder.class, "invalidCodeMissingCode", theParamName, systemDesc, codeDesc);
			throw new InvalidRequestException(Msg.code(1240) + msg);
		}
	}
}
