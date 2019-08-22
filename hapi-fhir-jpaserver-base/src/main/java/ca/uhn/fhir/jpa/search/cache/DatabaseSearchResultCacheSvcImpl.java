package ca.uhn.fhir.jpa.search.cache;

import ca.uhn.fhir.jpa.dao.data.IAAAAAAAAASearchDao;
import ca.uhn.fhir.jpa.entity.Search;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

public class DatabaseSearchResultCacheSvcImpl extends BaseSearchResultCacheSvcImpl {

	@Autowired
	private IAAAAAAAAASearchDao mySearchDao;

	@Transactional(Transactional.TxType.MANDATORY)
	@Override
	public Search saveNew(Search theSearch) {
		return mySearchDao.save(theSearch);
	}
}
