package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ca.uhn.fhir.jpa.dao.FhirSearchDao.MySuggestionFormatter;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.resource.BaseResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class FhirSearchDao extends BaseHapiFhirDao<IBaseResource> implements ISearchDao {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSearchDao.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	private void addTextSearch(QueryBuilder qb, BooleanJunction<?> bool, List<List<? extends IQueryParameterType>> contentAndTerms, String field) {
		if (contentAndTerms == null) {
			return;
		}
		for (List<? extends IQueryParameterType> nextAnd : contentAndTerms) {
			Set<String> terms = new HashSet<String>();
			for (IQueryParameterType nextOr : nextAnd) {
				StringParam nextOrString = (StringParam) nextOr;
				String nextValueTrimmed = StringUtils.defaultString(nextOrString.getValue()).trim();
				if (isNotBlank(nextValueTrimmed)) {
					terms.add(nextValueTrimmed);
				}
			}
			if (terms.isEmpty() == false) {
				String joinedTerms = StringUtils.join(terms, ' ');
				bool.must(qb.keyword().onField(field).matching(joinedTerms).createQuery());
			}
		}
	}

	private List<Long> doSearch(String theResourceName, SearchParameterMap theParams, Long theReferencingPid) {
		FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);

		QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(ResourceTable.class).get();

		BooleanJunction<?> bool = qb.bool();

		List<List<? extends IQueryParameterType>> contentAndTerms = theParams.remove(Constants.PARAM_CONTENT);
		addTextSearch(qb, bool, contentAndTerms, "myContentText");

		List<List<? extends IQueryParameterType>> textAndTerms = theParams.remove(Constants.PARAM_TEXT);
		addTextSearch(qb, bool, textAndTerms, "myNarrativeText");

		if (theReferencingPid != null) {
			bool.must(qb.keyword().onField("myResourceLinks.myTargetResourcePid").matching(theReferencingPid).createQuery());
		}

		if (bool.isEmpty()) {
			return null;
		}

		if (isNotBlank(theResourceName)) {
			bool.must(qb.keyword().onField("myResourceType").matching(theResourceName).createQuery());
		}

		Query luceneQuery = bool.createQuery();

		// wrap Lucene query in a javax.persistence.Query
		FullTextQuery jpaQuery = em.createFullTextQuery(luceneQuery, ResourceTable.class);
		jpaQuery.setProjection("myId");

		// execute search
		List<?> result = jpaQuery.getResultList();

		ArrayList<Long> retVal = new ArrayList<Long>();
		for (Object object : result) {
			Object[] nextArray = (Object[]) object;
			Long next = (Long) nextArray[0];
			if (next != null) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	@Override
	public List<Long> everything(String theResourceName, SearchParameterMap theParams) {

		Long pid = null;
		if (theParams.get(BaseResource.SP_RES_ID) != null) {
			StringParam idParm = (StringParam) theParams.get(BaseResource.SP_RES_ID).get(0).get(0);
			pid = BaseHapiFhirDao.translateForcedIdToPid(new IdDt(idParm.getValue()), myEntityManager);
		}

		Long referencingPid = pid;
		List<Long> retVal = doSearch(null, theParams, referencingPid);
		if (referencingPid != null) {
			retVal.add(referencingPid);
		}
		return retVal;
	}

	@Transactional()
	@Override
	public List<Long> search(String theResourceName, SearchParameterMap theParams) {
		return doSearch(theResourceName, theParams, null);
	}

	@Override
	public List<Suggestion> suggestKeywords(String theContext, String theSearchParam, String theText) {
		Validate.notBlank(theContext, "theContext must be provided");
		Validate.notBlank(theSearchParam, "theSearchParam must be provided");
		Validate.notBlank(theText, "theSearchParam must be provided");
		
		long start = System.currentTimeMillis();
		
		String[] contextParts = StringUtils.split(theContext, '/');
		if (contextParts.length != 3 || "Patient".equals(contextParts[0]) == false || "$everything".equals(contextParts[2]) == false) {
			throw new InvalidRequestException("Invalid context: " + theContext);
		}
		IdDt contextId = new IdDt(contextParts[0], contextParts[1]);
		Long pid = BaseHapiFhirDao.translateForcedIdToPid(contextId, myEntityManager);
		
		FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);

		QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(ResourceTable.class).get();

		//@formatter:off
		Query textQuery = qb
				.phrase()
				.withSlop(2)
				.onField("myContentText").boostedTo(4.0f)
				.andField("myContentTextEdgeNGram").boostedTo(2.0f)
				.andField("myContentTextNGram").boostedTo(1.0f)
				.andField("myContentTextPhonetic").boostedTo(0.5f)
				.sentence(theText.toLowerCase()).createQuery();
		
		Query query = qb.bool()
				.must(qb.keyword().onField("myResourceLinks.myTargetResourcePid").matching(pid).createQuery())
				.must(textQuery)
				.createQuery();
		//@formatter:on

		FullTextQuery ftq = em.createFullTextQuery(query, ResourceTable.class);
		ftq.setProjection("myContentText");
		ftq.setMaxResults(20);

		List<?> resultList = ftq.getResultList();
		List<Suggestion> suggestions = Lists.newArrayList();
		for (Object next : resultList) {
			Object[] nextAsArray = (Object[]) next;
			String nextValue = (String) nextAsArray[0];

			try {
				MySuggestionFormatter formatter = new MySuggestionFormatter(suggestions);

				Scorer scorer = new QueryScorer(textQuery);
				Highlighter highlighter = new Highlighter(formatter, scorer);

				Analyzer analyzer = em.getSearchFactory().getAnalyzer(ResourceTable.class);
				highlighter.getBestFragment(analyzer.tokenStream("myContentText", nextValue), nextValue);
				highlighter.getBestFragment(analyzer.tokenStream("myContentTextNGram", nextValue), nextValue);
				highlighter.getBestFragment(analyzer.tokenStream("myContentTextEdgeNGram", nextValue), nextValue);
				highlighter.getBestFragment(analyzer.tokenStream("myContentTextPhonetic", nextValue), nextValue);
			} catch (Exception e) {
				throw new InternalErrorException(e);
			}

		}

		Collections.sort(suggestions);
		
		Set<String> terms = Sets.newHashSet();
		for (Iterator<Suggestion> iter = suggestions.iterator(); iter.hasNext(); ) {
			if (!terms.add(iter.next().getTerm())) {
				iter.remove();
			}
		}
		
		long delay = System.currentTimeMillis()- start;
		ourLog.info("Provided {} suggestions for term {} in {} ms", new Object[] {terms.size(), theText, delay});
		
		return suggestions;
	}

	public static class Suggestion implements Comparable<Suggestion> {
		public Suggestion(String theTerm, float theScore) {
			myTerm = theTerm;
			myScore = theScore;
		}

		public String getTerm() {
			return myTerm;
		}

		public float getScore() {
			return myScore;
		}

		private String myTerm;
		private float myScore;

		@Override
		public int compareTo(Suggestion theO) {
			return Float.compare(theO.myScore, myScore);
		}

		@Override
		public String toString() {
			return "Suggestion[myTerm=" + myTerm + ", myScore=" + myScore + "]";
		}
	}

	public class MySuggestionFormatter implements Formatter {

		private List<Suggestion> mySuggestions;

		public MySuggestionFormatter(List<Suggestion> theSuggestions) {
			mySuggestions = theSuggestions;
		}

		@Override
		public String highlightTerm(String theOriginalText, TokenGroup theTokenGroup) {
			if (theTokenGroup.getTotalScore() > 0) {
				mySuggestions.add(new Suggestion(theOriginalText, theTokenGroup.getTotalScore()));
			}
			return null;
		}

	}

}
