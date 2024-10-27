/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.search;

import jakarta.annotation.Nonnull;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.ExistsPredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.MatchAllPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.MatchIdPredicateMatchingStep;
import org.hibernate.search.engine.search.predicate.dsl.MatchNonePredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.MatchPredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.NamedPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.NestedPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.NestedPredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.NestedPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.NotPredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.PhrasePredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.RangePredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.RegexpPredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactoryExtension;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactoryExtensionIfSupportedStep;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.SimpleQueryStringPredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.SpatialPredicateInitialStep;
import org.hibernate.search.engine.search.predicate.dsl.TermsPredicateFieldStep;
import org.hibernate.search.engine.search.predicate.dsl.WildcardPredicateFieldStep;
import org.hibernate.search.util.common.annotation.Incubating;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static ca.uhn.fhir.jpa.dao.search.ExtendedHSearchClauseBuilder.PATH_JOINER;
import static ca.uhn.fhir.jpa.model.search.HSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;

/**
 * Holds current query path, boolean clause accumulating AND clauses, and a factory for new predicates.
 *
 * The Hibernate Search SearchPredicateFactory is "smart", and knows to wrap references to nested fields
 * in a nested clause.  This is a problem if we want to accumulate them in a single boolean before nesting.
 * Instead, we keep track of the current query path (e.g. "nsp.value-quantity"), and the right SearchPredicateFactory
 * to use.
 */
class PathContext implements SearchPredicateFactory {
	private final String myPathPrefix;
	private final BooleanPredicateClausesStep<?> myRootClause;
	private final SearchPredicateFactory myPredicateFactory;

	PathContext(
			String thePrefix, BooleanPredicateClausesStep<?> theClause, SearchPredicateFactory thePredicateFactory) {
		myRootClause = theClause;
		myPredicateFactory = thePredicateFactory;
		myPathPrefix = thePrefix;
	}

	@Nonnull
	static PathContext buildRootContext(
			BooleanPredicateClausesStep<?> theRootClause, SearchPredicateFactory thePredicateFactory) {
		return new PathContext("", theRootClause, thePredicateFactory);
	}

	public String getContextPath() {
		return myPathPrefix;
	}

	public PathContext getSubComponentContext(String theName) {
		return new PathContext(joinPath(myPathPrefix, theName), myRootClause, myPredicateFactory);
	}

	@Nonnull
	PathContext forAbsolutePath(String path) {
		return new PathContext(path, myRootClause, myPredicateFactory);
	}

	public PredicateFinalStep buildPredicateInNestedContext(
			String theSubPath, Function<PathContext, PredicateFinalStep> f) {
		String nestedRootPath = joinPath(NESTED_SEARCH_PARAM_ROOT, theSubPath);
		NestedPredicateOptionsStep<?> orListPredicate = myPredicateFactory
				.nested()
				.objectField(nestedRootPath)
				.nest(nestedRootPredicateFactory -> {
					PathContext nestedCompositeSPContext =
							new PathContext(nestedRootPath, myRootClause, nestedRootPredicateFactory);
					return f.apply(nestedCompositeSPContext);
				});
		return orListPredicate;
	}

	/**
	 * Provide an OR wrapper around a list of predicates.
	 *
	 * Wrap the predicates under a bool as should clauses with minimumShouldMatch=1 for OR semantics.
	 * As an optimization, when there is only one clause, we avoid the redundant boolean wrapper
	 * and return the first item as is.
	 *
	 * @param theOrList a list containing at least 1 predicate
	 * @return a predicate providing or-semantics over the list.
	 */
	public PredicateFinalStep orPredicateOrSingle(List<? extends PredicateFinalStep> theOrList) {
		PredicateFinalStep finalClause;
		if (theOrList.size() == 1) {
			finalClause = theOrList.get(0);
		} else {
			BooleanPredicateClausesStep<?> orClause = myPredicateFactory.bool();
			orClause.minimumShouldMatchNumber(1);
			theOrList.forEach(orClause::should);
			finalClause = orClause;
		}
		return finalClause;
	}

	// implement SearchPredicateFactory

	@Override
	public MatchAllPredicateOptionsStep<?> matchAll() {
		return myPredicateFactory.matchAll();
	}

	@Override
	public MatchNonePredicateFinalStep matchNone() {
		return myPredicateFactory.matchNone();
	}

	@Override
	public MatchIdPredicateMatchingStep<?> id() {
		return myPredicateFactory.id();
	}

	@Override
	public BooleanPredicateClausesStep<?> bool() {
		return myPredicateFactory.bool();
	}

	@Override
	public PredicateFinalStep bool(Consumer<? super BooleanPredicateClausesStep<?>> clauseContributor) {
		return myPredicateFactory.bool(clauseContributor);
	}

	@Override
	public SimpleBooleanPredicateClausesStep<?> and() {
		return myPredicateFactory.and();
	}

	@Override
	public SimpleBooleanPredicateOptionsStep<?> and(
			SearchPredicate theSearchPredicate, SearchPredicate... theSearchPredicates) {
		return myPredicateFactory.and(theSearchPredicate, theSearchPredicates);
	}

	@Override
	public SimpleBooleanPredicateOptionsStep<?> and(
			PredicateFinalStep thePredicateFinalStep, PredicateFinalStep... thePredicateFinalSteps) {
		return myPredicateFactory.and(thePredicateFinalStep, thePredicateFinalSteps);
	}

	@Override
	public SimpleBooleanPredicateClausesStep<?> or() {
		return myPredicateFactory.or();
	}

	@Override
	public SimpleBooleanPredicateOptionsStep<?> or(
			SearchPredicate theSearchPredicate, SearchPredicate... theSearchPredicates) {
		return myPredicateFactory.or(theSearchPredicate, theSearchPredicates);
	}

	@Override
	public SimpleBooleanPredicateOptionsStep<?> or(
			PredicateFinalStep thePredicateFinalStep, PredicateFinalStep... thePredicateFinalSteps) {
		return myPredicateFactory.or(thePredicateFinalStep, thePredicateFinalSteps);
	}

	@Override
	public NotPredicateFinalStep not(SearchPredicate theSearchPredicate) {
		return myPredicateFactory.not(theSearchPredicate);
	}

	@Override
	public NotPredicateFinalStep not(PredicateFinalStep thePredicateFinalStep) {
		return myPredicateFactory.not(thePredicateFinalStep);
	}

	@Override
	public MatchPredicateFieldStep<?> match() {
		return myPredicateFactory.match();
	}

	@Override
	public RangePredicateFieldStep<?> range() {
		return myPredicateFactory.range();
	}

	@Override
	public PhrasePredicateFieldStep<?> phrase() {
		return myPredicateFactory.phrase();
	}

	@Override
	public WildcardPredicateFieldStep<?> wildcard() {
		return myPredicateFactory.wildcard();
	}

	@Override
	public RegexpPredicateFieldStep<?> regexp() {
		return myPredicateFactory.regexp();
	}

	@Override
	public TermsPredicateFieldStep<?> terms() {
		return myPredicateFactory.terms();
	}

	@Override
	public NestedPredicateFieldStep<?> nested() {
		return myPredicateFactory.nested();
	}

	@Override
	public NestedPredicateClausesStep<?> nested(String theObjectFieldPath) {
		return myPredicateFactory.nested(theObjectFieldPath);
	}

	@Override
	public SimpleQueryStringPredicateFieldStep<?> simpleQueryString() {
		return myPredicateFactory.simpleQueryString();
	}

	@Override
	public ExistsPredicateFieldStep<?> exists() {
		return myPredicateFactory.exists();
	}

	@Override
	public SpatialPredicateInitialStep spatial() {
		return myPredicateFactory.spatial();
	}

	@Override
	@Incubating
	public NamedPredicateOptionsStep named(String path) {
		return myPredicateFactory.named(path);
	}

	@Override
	public <T> T extension(SearchPredicateFactoryExtension<T> extension) {
		return myPredicateFactory.extension(extension);
	}

	@Override
	public SearchPredicateFactoryExtensionIfSupportedStep extension() {
		return myPredicateFactory.extension();
	}

	@Override
	@Incubating
	public SearchPredicateFactory withRoot(String objectFieldPath) {
		return myPredicateFactory.withRoot(objectFieldPath);
	}

	@Override
	@Incubating
	public String toAbsolutePath(String relativeFieldPath) {
		return myPredicateFactory.toAbsolutePath(relativeFieldPath);
	}

	// HSearch uses a dotted path
	// Some private static helpers that can be inlined.
	@Nonnull
	public static String joinPath(String thePath0, String thePath1) {
		return thePath0 + PATH_JOINER + thePath1;
	}

	public static String joinPath(String thePath0, String thePath1, String thePath2) {
		return thePath0 + PATH_JOINER + thePath1 + PATH_JOINER + thePath2;
	}

	@Nonnull
	public static String joinPath(String thePath0, String thePath1, String thePath2, String thePath3) {
		return thePath0 + PATH_JOINER + thePath1 + PATH_JOINER + thePath2 + PATH_JOINER + thePath3;
	}
}
