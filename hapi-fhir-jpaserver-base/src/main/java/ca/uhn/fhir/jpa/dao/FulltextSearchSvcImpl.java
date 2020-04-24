package ca.uhn.fhir.jpa.dao;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.*;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FulltextSearchSvcImpl implements IFulltextSearchSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FulltextSearchSvcImpl.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	protected IForcedIdDao myForcedIdDao;

	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired
	private IdHelperService myIdHelperService;

	private Boolean ourDisabled;

	/**
	 * Constructor
	 */
	public FulltextSearchSvcImpl() {
		super();
	}

	private void addTextSearch(QueryBuilder theQueryBuilder, BooleanJunction<?> theBoolean, List<List<IQueryParameterType>> theTerms, String theFieldName, String theFieldNameEdgeNGram, String theFieldNameNGram) {
		if (theTerms == null) {
			return;
		}
		for (List<? extends IQueryParameterType> nextAnd : theTerms) {
			Set<String> terms = new HashSet<>();
			for (IQueryParameterType nextOr : nextAnd) {
				StringParam nextOrString = (StringParam) nextOr;
				String nextValueTrimmed = StringUtils.defaultString(nextOrString.getValue()).trim();
				if (isNotBlank(nextValueTrimmed)) {
					terms.add(nextValueTrimmed);
				}
			}
			if (terms.isEmpty() == false) {
				if (terms.size() == 1) {
					//@formatter:off
					Query textQuery = theQueryBuilder
						.phrase()
						.withSlop(2)
						.onField(theFieldName).boostedTo(4.0f)
//						.andField(theFieldNameEdgeNGram).boostedTo(2.0f)
//						.andField(theFieldNameNGram).boostedTo(1.0f)
						.sentence(terms.iterator().next().toLowerCase()).createQuery();
					//@formatter:on

					theBoolean.must(textQuery);
				} else {
					String joinedTerms = StringUtils.join(terms, ' ');
					theBoolean.must(theQueryBuilder.keyword().onField(theFieldName).matching(joinedTerms).createQuery());
				}
			}
		}
	}

	private List<ResourcePersistentId> doSearch(String theResourceName, SearchParameterMap theParams, ResourcePersistentId theReferencingPid) {
		FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);

		List<ResourcePersistentId> pids = null;
		
		/*
		 * Handle textual params
		 */
		/*
		for (String nextParamName : theParams.keySet()) {
			for (List<? extends IQueryParameterType> nextAndList : theParams.get(nextParamName)) {
				for (Iterator<? extends IQueryParameterType> orIterator = nextAndList.iterator(); orIterator.hasNext();) {
					IQueryParameterType nextParam = orIterator.next();
					if (nextParam instanceof TokenParam) {
						TokenParam nextTokenParam = (TokenParam) nextParam;
						if (nextTokenParam.isText()) {
							orIterator.remove();
							QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(ResourceIndexedSearchParamString.class).get();
							BooleanJunction<?> bool = qb.bool();

							bool.must(qb.keyword().onField("myParamName").matching(nextParamName).createQuery());
							if (isNotBlank(theResourceName)) {
								bool.must(qb.keyword().onField("myResourceType").matching(theResourceName).createQuery());
							}
//							
							//@formatter:off
							String value = nextTokenParam.getValue().toLowerCase();
							bool.must(qb.keyword().onField("myValueTextEdgeNGram").matching(value).createQuery());
							
							//@formatter:on
							
							FullTextQuery ftq = em.createFullTextQuery(bool.createQuery(), ResourceIndexedSearchParamString.class);

							List<?> resultList = ftq.getResultList();
							pids = new ArrayList<Long>();
							for (Object next : resultList) {
								ResourceIndexedSearchParamString nextAsArray = (ResourceIndexedSearchParamString) next;
								pids.add(nextAsArray.getResourcePid());
							}
						}
					}
				}
			}
		}
		
		if (pids != null && pids.isEmpty()) {
			return pids;
		}
		*/

		QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(ResourceTable.class).get();
		BooleanJunction<?> bool = qb.bool();

		/*
		 * Handle _content parameter (resource body content)
		 */
		List<List<IQueryParameterType>> contentAndTerms = theParams.remove(Constants.PARAM_CONTENT);
		addTextSearch(qb, bool, contentAndTerms, "myContentText", "myContentTextEdgeNGram", "myContentTextNGram");

		/*
		 * Handle _text parameter (resource narrative content)
		 */
		List<List<IQueryParameterType>> textAndTerms = theParams.remove(Constants.PARAM_TEXT);
		addTextSearch(qb, bool, textAndTerms, "myNarrativeText", "myNarrativeTextEdgeNGram", "myNarrativeTextNGram");

		if (theReferencingPid != null) {
			bool.must(qb.keyword().onField("myResourceLinksField").matching(theReferencingPid.toString()).createQuery());
		}

		if (bool.isEmpty()) {
			return pids;
		}

		if (isNotBlank(theResourceName)) {
			bool.must(qb.keyword().onField("myResourceType").matching(theResourceName).createQuery());
		}

		Query luceneQuery = bool.createQuery();

		// wrap Lucene query in a javax.persistence.SqlQuery
		FullTextQuery jpaQuery = em.createFullTextQuery(luceneQuery, ResourceTable.class);
		jpaQuery.setProjection("myId");

		// execute search
		List<?> result = jpaQuery.getResultList();

		ArrayList<ResourcePersistentId> retVal = new ArrayList<>();
		for (Object object : result) {
			Object[] nextArray = (Object[]) object;
			Long next = (Long) nextArray[0];
			if (next != null) {
				retVal.add(new ResourcePersistentId(next));
			}
		}

		return retVal;
	}

	@Override
	public List<ResourcePersistentId> everything(String theResourceName, SearchParameterMap theParams, RequestDetails theRequest) {

		ResourcePersistentId pid = null;
		if (theParams.get(IAnyResource.SP_RES_ID) != null) {
			String idParamValue;
			IQueryParameterType idParam = theParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
			if (idParam instanceof TokenParam) {
				TokenParam idParm = (TokenParam) idParam;
				idParamValue = idParm.getValue();
			} else {
				StringParam idParm = (StringParam) idParam;
				idParamValue = idParm.getValue();
			}
//			pid = myIdHelperService.translateForcedIdToPid_(theResourceName, idParamValue, theRequest);
		}

		ResourcePersistentId referencingPid = pid;
		List<ResourcePersistentId> retVal = doSearch(null, theParams, referencingPid);
		if (referencingPid != null) {
			retVal.add(referencingPid);
		}
		return retVal;
	}

	@Override
	public boolean isDisabled() {
		Boolean retVal = ourDisabled;

		if (retVal == null) {
			retVal = new TransactionTemplate(myTxManager).execute(t -> {
				try {
					FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);
					em.getSearchFactory().buildQueryBuilder().forEntity(ResourceTable.class).get();
					return Boolean.FALSE;
				} catch (Exception e) {
					ourLog.trace("FullText test failed", e);
					ourLog.debug("Hibernate Search (Lucene) appears to be disabled on this server, fulltext will be disabled");
					return Boolean.TRUE;
				}
			});
			ourDisabled = retVal;
		}

		assert retVal != null;
		return retVal;
	}

	@Transactional()
	@Override
	public List<ResourcePersistentId> search(String theResourceName, SearchParameterMap theParams) {
		return doSearch(theResourceName, theParams, null);
	}

	@Transactional()
	@Override
	public List<Suggestion> suggestKeywords(String theContext, String theSearchParam, String theText, RequestDetails theRequest) {
		Validate.notBlank(theContext, "theContext must be provided");
		Validate.notBlank(theSearchParam, "theSearchParam must be provided");
		Validate.notBlank(theText, "theSearchParam must be provided");

		long start = System.currentTimeMillis();

		String[] contextParts = StringUtils.split(theContext, '/');
		if (contextParts.length != 3 || "Patient".equals(contextParts[0]) == false || "$everything".equals(contextParts[2]) == false) {
			throw new InvalidRequestException("Invalid context: " + theContext);
		}
		ResourcePersistentId pid = myIdHelperService.resolveResourcePersistentIds(contextParts[0], contextParts[1]);

		FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);

		QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(ResourceTable.class).get();

		Query textQuery = qb
			.phrase()
			.withSlop(2)
			.onField("myContentText").boostedTo(4.0f)
			.andField("myContentTextEdgeNGram").boostedTo(2.0f)
			.andField("myContentTextNGram").boostedTo(1.0f)
			.andField("myContentTextPhonetic").boostedTo(0.5f)
			.sentence(theText.toLowerCase()).createQuery();

		Query query = qb.bool()
//			.must(qb.keyword().onField("myResourceLinks.myTargetResourcePid").matching(pid).createQuery())
			.must(qb.keyword().onField("myResourceLinksField").matching(pid.toString()).createQuery())
			.must(textQuery)
			.createQuery();

		FullTextQuery ftq = em.createFullTextQuery(query, ResourceTable.class);
		ftq.setProjection("myContentText");
		ftq.setMaxResults(20);

		List<?> resultList = ftq.getResultList();
		List<Suggestion> suggestions = Lists.newArrayList();
		for (Object next : resultList) {
			Object[] nextAsArray = (Object[]) next;
			String nextValue = (String) nextAsArray[0];

			try {
				MySuggestionFormatter formatter = new MySuggestionFormatter(theText, suggestions);
				Scorer scorer = new QueryScorer(textQuery);
				Highlighter highlighter = new Highlighter(formatter, scorer);
				Analyzer analyzer = em.getSearchFactory().getAnalyzer(ResourceTable.class);

				formatter.setAnalyzer("myContentTextPhonetic");
				highlighter.getBestFragments(analyzer.tokenStream("myContentTextPhonetic", nextValue), nextValue, 10);

				formatter.setAnalyzer("myContentTextNGram");
				highlighter.getBestFragments(analyzer.tokenStream("myContentTextNGram", nextValue), nextValue, 10);

				formatter.setFindPhrasesWith();
				formatter.setAnalyzer("myContentTextEdgeNGram");
				highlighter.getBestFragments(analyzer.tokenStream("myContentTextEdgeNGram", nextValue), nextValue, 10);

			} catch (Exception e) {
				throw new InternalErrorException(e);
			}

		}

		Collections.sort(suggestions);

		Set<String> terms = Sets.newHashSet();
		for (Iterator<Suggestion> iter = suggestions.iterator(); iter.hasNext(); ) {
			String nextTerm = iter.next().getTerm().toLowerCase();
			if (!terms.add(nextTerm)) {
				iter.remove();
			}
		}

		long delay = System.currentTimeMillis() - start;
		ourLog.info("Provided {} suggestions for term {} in {} ms", terms.size(), theText, delay);

		return suggestions;
	}

	public class MySuggestionFormatter implements Formatter {

		private List<Suggestion> mySuggestions;
		private String myAnalyzer;
		private ArrayList<String> myPartialMatchPhrases;
		private ArrayList<Float> myPartialMatchScores;
		private String myOriginalSearch;

		MySuggestionFormatter(String theOriginalSearch, List<Suggestion> theSuggestions) {
			myOriginalSearch = theOriginalSearch;
			mySuggestions = theSuggestions;
		}

		@Override
		public String highlightTerm(String theOriginalText, TokenGroup theTokenGroup) {
			ourLog.debug("{} Found {} with score {}", myAnalyzer, theOriginalText, theTokenGroup.getTotalScore());
			if (theTokenGroup.getTotalScore() > 0) {
				float score = theTokenGroup.getTotalScore();
				if (theOriginalText.equalsIgnoreCase(myOriginalSearch)) {
					score = score + 1.0f;
				}
				mySuggestions.add(new Suggestion(theOriginalText, score));
			} else if (myPartialMatchPhrases != null) {
				if (theOriginalText.length() < 100) {
					for (int i = 0; i < myPartialMatchPhrases.size(); i++) {
						if (theOriginalText.contains(myPartialMatchPhrases.get(i))) {
							mySuggestions.add(new Suggestion(theOriginalText, myPartialMatchScores.get(i) - 0.5f));
						}
					}
				}
			}

			return null;
		}

		void setAnalyzer(String theString) {
			myAnalyzer = theString;
		}

		void setFindPhrasesWith() {
			myPartialMatchPhrases = new ArrayList<>();
			myPartialMatchScores = new ArrayList<>();

			for (Suggestion next : mySuggestions) {
				myPartialMatchPhrases.add(' ' + next.myTerm);
				myPartialMatchScores.add(next.myScore);
			}

			myPartialMatchPhrases.add(myOriginalSearch);
			myPartialMatchScores.add(1.0f);
		}

	}

	public static class Suggestion implements Comparable<Suggestion> {
		private String myTerm;
		private float myScore;

		Suggestion(String theTerm, float theScore) {
			myTerm = theTerm;
			myScore = theScore;
		}

		@Override
		public int compareTo(Suggestion theO) {
			return Float.compare(theO.myScore, myScore);
		}

		public float getScore() {
			return myScore;
		}

		public String getTerm() {
			return myTerm;
		}

		@Override
		public String toString() {
			return "Suggestion[myTerm=" + myTerm + ", myScore=" + myScore + "]";
		}
	}

}
