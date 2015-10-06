package ca.uhn.fhir.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class SearchDao extends BaseHapiFhirDao<IBaseResource> implements ISearchDao {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchDao.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Transactional()
	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		
		FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);
		
		for (String nextParamName : theParams.keySet()) {
			if (nextParamName.equals(FULL_TEXT_PARAM_NAME)) {
				QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(ResourceIndexedSearchParamString.class).get();
				org.apache.lucene.search.Query luceneQuery = qb
				  .keyword()
				  .onFields("myValueComplete")
				  .matching("AAAS")
				  .createQuery();
	
				// wrap Lucene query in a javax.persistence.Query
				FullTextQuery jpaQuery = em.createFullTextQuery(luceneQuery, ResourceIndexedSearchParamString.class);
	
				// execute search
				List<?> result = jpaQuery.getResultList();
				for (Object object : result) {
					ourLog.info(""+ object);
				}
			}
		}
		
		return null;
	}

}
