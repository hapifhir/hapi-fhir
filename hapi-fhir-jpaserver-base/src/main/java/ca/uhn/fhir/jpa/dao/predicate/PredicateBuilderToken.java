package ca.uhn.fhir.jpa.dao.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.util.VersionIndependentConcept;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Sets;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.predicate.BooleanStaticAssertionPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

@Component
@Scope("prototype")
class PredicateBuilderToken extends BasePredicateBuilder implements IPredicateBuilder {
	@Autowired
	private ITermReadSvc myTerminologySvc;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	private final PredicateBuilder myPredicateBuilder;

	PredicateBuilderToken(SearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		super(theSearchBuilder);
		myPredicateBuilder = thePredicateBuilder;
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											String theParamName,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation) {

		if (theList.get(0).getMissing() != null) {
			Join<ResourceTable, ResourceIndexedSearchParamToken> join = createJoin(SearchBuilderJoinEnum.TOKEN, theParamName);
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		List<IQueryParameterType> tokens = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof TokenParam) {
				TokenParam id = (TokenParam) nextOr;
				if (id.isText()) {
					myPredicateBuilder.addPredicateString(theResourceName, theParamName, theList);
					break;
				}
			}

			tokens.add(nextOr);
		}

		if (tokens.isEmpty()) {
			return null;
		}

		Join<ResourceTable, ResourceIndexedSearchParamToken> join = createJoin(SearchBuilderJoinEnum.TOKEN, theParamName);
		Collection<Predicate> singleCode = createPredicateToken(tokens, theResourceName, theParamName, myCriteriaBuilder, join, operation);
		assert singleCode != null;
		codePredicates.addAll(singleCode);

		Predicate spPredicate = myCriteriaBuilder.or(toArray(codePredicates));
		myQueryRoot.addPredicate(spPredicate);
		return spPredicate;
	}

	public Collection<Predicate> createPredicateToken(Collection<IQueryParameterType> theParameters,
																	  String theResourceName,
																	  String theParamName,
																	  CriteriaBuilder theBuilder,
																	  From<?, ResourceIndexedSearchParamToken> theFrom) {
		return createPredicateToken(
			theParameters,
			theResourceName,
			theParamName,
			theBuilder,
			theFrom,
			null);
	}

	private Collection<Predicate> createPredicateToken(Collection<IQueryParameterType> theParameters,
																		String theResourceName,
																		String theParamName,
																		CriteriaBuilder theBuilder,
																		From<?, ResourceIndexedSearchParamToken> theFrom,
																		SearchFilterParser.CompareOperation operation) {
		final List<VersionIndependentConcept> codes = new ArrayList<>();

		TokenParamModifier modifier = null;
		for (IQueryParameterType nextParameter : theParameters) {

			String code;
			String system;
			if (nextParameter instanceof TokenParam) {
				TokenParam id = (TokenParam) nextParameter;
				system = id.getSystem();
				code = (id.getValue());
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
				code = number.getValueAsQueryToken(myContext);
			} else {
				throw new IllegalArgumentException("Invalid token type: " + nextParameter.getClass());
			}

			if (system != null && system.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException(
					"Parameter[" + theParamName + "] has system (" + system.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + system);
			}

			if (code != null && code.length() > ResourceIndexedSearchParamToken.MAX_LENGTH) {
				throw new InvalidRequestException(
					"Parameter[" + theParamName + "] has code (" + code.length() + ") that is longer than maximum allowed (" + ResourceIndexedSearchParamToken.MAX_LENGTH + "): " + code);
			}

			/*
			 * Process token modifiers (:in, :below, :above)
			 */

			if (modifier == TokenParamModifier.IN) {
				codes.addAll(myTerminologySvc.expandValueSet(null, code));
			} else if (modifier == TokenParamModifier.ABOVE) {
				system = determineSystemIfMissing(theParamName, code, system);
				validateHaveSystemAndCodeForToken(theParamName, code, system);
				codes.addAll(myTerminologySvc.findCodesAbove(system, code));
			} else if (modifier == TokenParamModifier.BELOW) {
				system = determineSystemIfMissing(theParamName, code, system);
				validateHaveSystemAndCodeForToken(theParamName, code, system);
				codes.addAll(myTerminologySvc.findCodesBelow(system, code));
			} else {
				codes.add(new VersionIndependentConcept(system, code));
			}

		}

		List<VersionIndependentConcept> sortedCodesList = codes
			.stream()
			.filter(t -> t.getCode() != null || t.getSystem() != null)
			.sorted()
			.distinct()
			.collect(Collectors.toList());

		if (codes.isEmpty()) {
			// This will never match anything
			return Collections.singletonList(new BooleanStaticAssertionPredicate((CriteriaBuilderImpl) theBuilder, false));
		}

		List<Predicate> retVal = new ArrayList<>();

		// System only
		List<VersionIndependentConcept> systemOnlyCodes = sortedCodesList.stream().filter(t -> isBlank(t.getCode())).collect(Collectors.toList());
		if (!systemOnlyCodes.isEmpty()) {
			retVal.add(addPredicate(theResourceName, theParamName, theBuilder, theFrom, systemOnlyCodes, modifier, SearchBuilderTokenModeEnum.SYSTEM_ONLY));
		}

		// Code only
		List<VersionIndependentConcept> codeOnlyCodes = sortedCodesList.stream().filter(t -> t.getSystem() == null).collect(Collectors.toList());
		if (!codeOnlyCodes.isEmpty()) {
			retVal.add(addPredicate(theResourceName, theParamName, theBuilder, theFrom, codeOnlyCodes, modifier, SearchBuilderTokenModeEnum.VALUE_ONLY));
		}

		// System and code
		List<VersionIndependentConcept> systemAndCodeCodes = sortedCodesList.stream().filter(t -> isNotBlank(t.getCode()) && t.getSystem() != null).collect(Collectors.toList());
		if (!systemAndCodeCodes.isEmpty()) {
			retVal.add(addPredicate(theResourceName, theParamName, theBuilder, theFrom, systemAndCodeCodes, modifier, SearchBuilderTokenModeEnum.SYSTEM_AND_VALUE));
		}

		return retVal;
	}

	private String determineSystemIfMissing(String theParamName, String code, String theSystem) {
		String retVal = theSystem;
		if (retVal == null) {
			RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(myResourceName);
			RuntimeSearchParam param = mySearchParamRegistry.getSearchParamByName(resourceDef, theParamName);
			if (param != null) {
				Set<String> valueSetUris = Sets.newHashSet();
				for (String nextPath : param.getPathsSplit()) {
					BaseRuntimeChildDefinition def = myContext.newTerser().getDefinition(myResourceType, nextPath);
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
					List<VersionIndependentConcept> candidateCodes = myTerminologySvc.expandValueSet(options, valueSet);
					for (VersionIndependentConcept nextCandidate : candidateCodes) {
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

	private void validateHaveSystemAndCodeForToken(String theParamName, String theCode, String theSystem) {
		String systemDesc = defaultIfBlank(theSystem, "(missing)");
		String codeDesc = defaultIfBlank(theCode, "(missing)");
		if (isBlank(theCode)) {
			String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, "invalidCodeMissingSystem", theParamName, systemDesc, codeDesc);
			throw new InvalidRequestException(msg);
		}
		if (isBlank(theSystem)) {
			String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, "invalidCodeMissingCode", theParamName, systemDesc, codeDesc);
			throw new InvalidRequestException(msg);
		}
	}

	private Predicate addPredicate(String theResourceName, String theParamName, CriteriaBuilder theBuilder, From<?, ResourceIndexedSearchParamToken> theFrom, List<VersionIndependentConcept> theTokens, TokenParamModifier theModifier, SearchBuilderTokenModeEnum theTokenMode) {
		if (myDontUseHashesForSearch) {
			final Path<String> systemExpression = theFrom.get("mySystem");
			final Path<String> valueExpression = theFrom.get("myValue");

			List<Predicate> orPredicates = new ArrayList<>();
			switch (theTokenMode) {
				case SYSTEM_ONLY: {
					List<String> systems = theTokens.stream().map(t -> t.getSystem()).collect(Collectors.toList());
					Predicate orPredicate = systemExpression.in(systems);
					orPredicates.add(orPredicate);
					break;
				}
				case VALUE_ONLY:
					List<String> codes = theTokens.stream().map(t -> t.getCode()).collect(Collectors.toList());
					Predicate orPredicate = valueExpression.in(codes);
					orPredicates.add(orPredicate);
					break;
				case SYSTEM_AND_VALUE:
					for (VersionIndependentConcept next : theTokens) {
						orPredicates.add(theBuilder.and(
							toEqualOrIsNullPredicate(systemExpression, next.getSystem()),
							toEqualOrIsNullPredicate(valueExpression, next.getCode())
						));
					}
					break;
			}

			Predicate or = theBuilder.or(orPredicates.toArray(new Predicate[0]));
			if (theModifier == TokenParamModifier.NOT) {
				or = theBuilder.not(or);
			}

			return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, or);
		}

		/*
		 * Note: A null system value means "match any system", but
		 * an empty-string system value means "match values that
		 * explicitly have no system".
		 */
		Expression<Long> hashField;
		List<Long> values;
		switch (theTokenMode) {
			case SYSTEM_ONLY:
				hashField = theFrom.get("myHashSystem").as(Long.class);
				values = theTokens
					.stream()
					.map(t -> ResourceIndexedSearchParamToken.calculateHashSystem(theResourceName, theParamName, t.getSystem()))
					.collect(Collectors.toList());
				break;
			case VALUE_ONLY:
				hashField = theFrom.get("myHashValue").as(Long.class);
				values = theTokens
					.stream()
					.map(t -> ResourceIndexedSearchParamToken.calculateHashValue(theResourceName, theParamName, t.getCode()))
					.collect(Collectors.toList());
				break;
			case SYSTEM_AND_VALUE:
			default:
				hashField = theFrom.get("myHashSystemAndValue").as(Long.class);
				values = theTokens
					.stream()
					.map(t -> ResourceIndexedSearchParamToken.calculateHashSystemAndValue(theResourceName, theParamName, t.getSystem(), t.getCode()))
					.collect(Collectors.toList());
				break;
		}

		Predicate predicate = hashField.in(values);
		if (theModifier == TokenParamModifier.NOT) {
			Predicate identityPredicate = theBuilder.equal(theFrom.get("myHashIdentity").as(Long.class), BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName));
			Predicate disjunctionPredicate = theBuilder.not(predicate);
			predicate = theBuilder.and(identityPredicate, disjunctionPredicate);
		}
		return predicate;
	}

	private <T> Expression<Boolean> toEqualOrIsNullPredicate(Path<T> theExpression, T theCode) {
		if (theCode == null) {
			return myCriteriaBuilder.isNull(theExpression);
		}
		return myCriteriaBuilder.equal(theExpression, theCode);
	}
}
