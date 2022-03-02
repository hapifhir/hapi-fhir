package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.collect.Sets;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.toAndPredicate;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.toEqualToOrInPredicate;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.toOrPredicate;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TokenPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashSystemAndValue;
	private final DbColumn myColumnHashSystem;
	private final DbColumn myColumnHashValue;
	private final DbColumn myColumnSystem;
	private final DbColumn myColumnValue;

	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private ITermReadSvc myTerminologySvc;
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private FhirContext myContext;

	/**
	 * Constructor
	 */
	public TokenPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_TOKEN"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnHashSystem = getTable().addColumn("HASH_SYS");
		myColumnHashSystemAndValue = getTable().addColumn("HASH_SYS_AND_VALUE");
		myColumnHashValue = getTable().addColumn("HASH_VALUE");
		myColumnSystem = getTable().addColumn("SP_SYSTEM");
		myColumnValue = getTable().addColumn("SP_VALUE");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public Condition createPredicateToken(Collection<IQueryParameterType> theParameters,
													  String theResourceName,
													  String theSpnamePrefix,
													  RuntimeSearchParam theSearchParam,
													  RequestPartitionId theRequestPartitionId) {
		return createPredicateToken(
			theParameters,
			theResourceName,
			theSpnamePrefix,
			theSearchParam,
			null,
			theRequestPartitionId);
	}

	public Condition createPredicateToken(Collection<IQueryParameterType> theParameters,
													  String theResourceName,
													  String theSpnamePrefix,
													  RuntimeSearchParam theSearchParam,
													  SearchFilterParser.CompareOperation theOperation,
													  RequestPartitionId theRequestPartitionId) {


		final List<FhirVersionIndependentConcept> codes = new ArrayList<>();

		String paramName = QueryStack.getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

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
				code = (id.getValueElement().getValue());
			} else if (nextParameter instanceof BaseCodingDt) {
				BaseCodingDt id = (BaseCodingDt) nextParameter;
				system = id.getSystemElement().getValueAsString();
				code = (id.getCodeElement().getValue());
			} else if (nextParameter instanceof NumberParam) {
				NumberParam number = (NumberParam) nextParameter;
				system = null;
				code = number.getValueAsQueryToken(getFhirContext());
			} else {
				throw new IllegalArgumentException(Msg.code(1236) + "Invalid token type: " + nextParameter.getClass());
			}

			if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException(Msg.code(1237) + "Parameter[" + paramName + "] has system (" + system.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + system);
			}

			if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException(Msg.code(1238) + "Parameter[" + paramName + "] has code (" + code.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + code);
			}

			/*
			 * Process token modifiers (:in, :below, :above)
			 */

			if (modifier == TokenParamModifier.IN || modifier == TokenParamModifier.NOT_IN) {
				if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
					IValidationSupport.ValueSetExpansionOutcome expanded = myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), code);
					codes.addAll(extractValueSetCodes(expanded.getValueSet()));
				} else {
					codes.addAll(myTerminologySvc.expandValueSetIntoConceptList(null, code));
				}
				if (modifier == TokenParamModifier.NOT_IN) {
					operation = SearchFilterParser.CompareOperation.ne;
				}
			} else if (modifier == TokenParamModifier.ABOVE) {
				system = determineSystemIfMissing(theSearchParam, code, system);
				validateHaveSystemAndCodeForToken(paramName, code, system);
				codes.addAll(myTerminologySvc.findCodesAbove(system, code));
			} else if (modifier == TokenParamModifier.BELOW) {
				system = determineSystemIfMissing(theSearchParam, code, system);
				validateHaveSystemAndCodeForToken(paramName, code, system);
				codes.addAll(myTerminologySvc.findCodesBelow(system, code));
			} else if (modifier == TokenParamModifier.OF_TYPE) {
				if (!myModelConfig.isIndexIdentifierOfType()) {
					throw new MethodNotAllowedException(Msg.code(2012) + "The :of-type modifier is not enabled on this server");
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

		List<FhirVersionIndependentConcept> sortedCodesList = codes
			.stream()
			.filter(t -> t.getCode() != null || t.getSystem() != null)
			.sorted()
			.distinct()
			.collect(Collectors.toList());

		if (codes.isEmpty()) {
			// This will never match anything
			setMatchNothing();
			return null;
		}


		Condition predicate;
		if (operation == SearchFilterParser.CompareOperation.ne) {

			/*
			 * For a token :not search, we look for index rows that have the right identity (i.e. it's the right resource and
			 * param name) but not the actual provided token value.
			 */

			long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(getPartitionSettings(), theRequestPartitionId, theResourceName, paramName);
			Condition hashIdentityPredicate = BinaryCondition.equalTo(getColumnHashIdentity(), generatePlaceholder(hashIdentity));

			Condition hashValuePredicate = createPredicateOrList(theResourceName, paramName, sortedCodesList, false);
			predicate = toAndPredicate(hashIdentityPredicate, hashValuePredicate);

		} else {

			predicate = createPredicateOrList(theResourceName, paramName, sortedCodesList, true);

		}

		return predicate;
	}

	private List<FhirVersionIndependentConcept> extractValueSetCodes(IBaseResource theValueSet) {
		List<FhirVersionIndependentConcept> retVal = new ArrayList<>();

		RuntimeResourceDefinition vsDef = myContext.getResourceDefinition("ValueSet");
		BaseRuntimeChildDefinition expansionChild = vsDef.getChildByName("expansion");
		Optional<IBase> expansionOpt = expansionChild.getAccessor().getFirstValueOrNull(theValueSet);
		if (expansionOpt.isPresent()) {
			IBase expansion = expansionOpt.get();
			BaseRuntimeElementCompositeDefinition<?> expansionDef = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(expansion.getClass());
			BaseRuntimeChildDefinition containsChild = expansionDef.getChildByName("contains");
			List<IBase> contains = containsChild.getAccessor().getValues(expansion);
				
			BaseRuntimeChildDefinition.IAccessor systemAccessor = null;
			BaseRuntimeChildDefinition.IAccessor codeAccessor = null;
			for (IBase nextContains : contains) {
				if (systemAccessor == null) {
					systemAccessor = myContext.getElementDefinition(nextContains.getClass()).getChildByName("system").getAccessor();
				}
				if (codeAccessor == null) {
					codeAccessor = myContext.getElementDefinition(nextContains.getClass()).getChildByName("code").getAccessor();
				}
				String system = systemAccessor
					.getFirstValueOrNull(nextContains)
					.map(t->(IPrimitiveType<?>)t)
					.map(t->t.getValueAsString())
					.orElse(null);
				String code = codeAccessor
					.getFirstValueOrNull(nextContains)
					.map(t->(IPrimitiveType<?>)t)
					.map(t->t.getValueAsString())
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
					Class<? extends IBaseResource> type = getFhirContext().getResourceDefinition(getResourceType()).getImplementingClass();
					BaseRuntimeChildDefinition def = getFhirContext().newTerser().getDefinition(type, nextPath);
					if (def instanceof BaseRuntimeDeclaredChildDefinition) {
						String valueSet = ((BaseRuntimeDeclaredChildDefinition) def).getBindingValueSet();
						if (isNotBlank(valueSet)) {
							valueSetUris.add(valueSet);
						}
					}
				}
				if (valueSetUris.size() == 1) {
					String valueSet = valueSetUris.iterator().next();
					ValueSetExpansionOptions options = new ValueSetExpansionOptions()
						.setFailOnMissingCodeSystem(false);
					List<FhirVersionIndependentConcept> candidateCodes = myTerminologySvc.expandValueSetIntoConceptList(options, valueSet);
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

	public DbColumn getColumnSystem() {
		return myColumnSystem;
	}

	public DbColumn getColumnValue() {
		return myColumnValue;
	}

	private void validateHaveSystemAndCodeForToken(String theParamName, String theCode, String theSystem) {
		String systemDesc = defaultIfBlank(theSystem, "(missing)");
		String codeDesc = defaultIfBlank(theCode, "(missing)");
		if (isBlank(theCode)) {
			String msg = getFhirContext().getLocalizer().getMessage(LegacySearchBuilder.class, "invalidCodeMissingSystem", theParamName, systemDesc, codeDesc);
			throw new InvalidRequestException(Msg.code(1239) + msg);
		}
		if (isBlank(theSystem)) {
			String msg = getFhirContext().getLocalizer().getMessage(LegacySearchBuilder.class, "invalidCodeMissingCode", theParamName, systemDesc, codeDesc);
			throw new InvalidRequestException(Msg.code(1240) + msg);
		}
	}


	private Condition createPredicateOrList(String theResourceType, String theSearchParamName, List<FhirVersionIndependentConcept> theCodes, boolean theWantEquals) {
		Condition[] conditions = new Condition[theCodes.size()];

		Long[] hashes = new Long[theCodes.size()];
		DbColumn[] columns = new DbColumn[theCodes.size()];
		boolean haveMultipleColumns = false;
		for (int i = 0; i < conditions.length; i++) {

			FhirVersionIndependentConcept nextToken = theCodes.get(i);
			long hash;
			DbColumn column;
			if (nextToken.getSystem() == null) {
				hash = ResourceIndexedSearchParamToken.calculateHashValue(getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, nextToken.getCode());
				column = myColumnHashValue;
			} else if (isBlank(nextToken.getCode())) {
				hash = ResourceIndexedSearchParamToken.calculateHashSystem(getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, nextToken.getSystem());
				column = myColumnHashSystem;
			} else {
				hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, nextToken.getSystem(), nextToken.getCode());
				column = myColumnHashSystemAndValue;
			}
			hashes[i] = hash;
			columns[i] = column;
			if (i > 0 && columns[0] != columns[i]) {
				haveMultipleColumns = true;
			}
		}

		if (!haveMultipleColumns && conditions.length > 1) {
			List<Long> values = Arrays.asList(hashes);
			return toEqualToOrInPredicate(columns[0], generatePlaceholders(values), !theWantEquals);
		}

		for (int i = 0; i < conditions.length; i++) {
			String valuePlaceholder = generatePlaceholder(hashes[i]);
			if (theWantEquals) {
				conditions[i] = BinaryCondition.equalTo(columns[i], valuePlaceholder);
			} else {
				conditions[i] = BinaryCondition.notEqualTo(columns[i], valuePlaceholder);
			}
		}
		if (conditions.length > 1) {
			if (theWantEquals) {
				return toOrPredicate(conditions);
			} else {
				return toAndPredicate(conditions);
			}
		} else {
			return conditions[0];
		}
	}
}
