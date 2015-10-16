package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;

public class FhirSearchDao extends BaseHapiFhirDao<IBaseResource> implements ISearchDao {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSearchDao.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Transactional()
	@Override
	public List<Long> search(String theResourceName, SearchParameterMap theParams) {
		FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);
		
		QueryBuilder qb = em
				.getSearchFactory()
				.buildQueryBuilder()
				.forEntity(ResourceTable.class).get();
		
		BooleanJunction<?> bool = qb.bool();
		
		List<List<? extends IQueryParameterType>> contentAndTerms = theParams.remove(Constants.PARAM_CONTENT);
		addTextSearch(qb, bool, contentAndTerms, "myContentText");
		
		List<List<? extends IQueryParameterType>> textAndTerms = theParams.remove(Constants.PARAM_TEXT);
		addTextSearch(qb, bool, textAndTerms, "myNarrativeText");

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
			retVal.add((Long)nextArray[0]);
		}

		return retVal;
	}

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

}
