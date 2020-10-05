package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpungeResourceService {
	@Autowired
	IResourceLinkDao myResourceLinkDao;
	@Autowired
	IResourceIndexedSearchParamTokenDao myResourceIndexedSearchParamTokenDao;

	@Autowired
	IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	IResourceTableDao myResourceTableDao;

	public long deleteByResourcePids(List<Long> thePids) {
		long deleteCount = 0;
		// FIXME KHS add the rest
		deleteCount += myResourceLinkDao.deleteBySourcePids(thePids);
		deleteCount += myResourceIndexedSearchParamTokenDao.deleteByResIds(thePids);

		deleteCount += myResourceHistoryTableDao.deleteByResIds(thePids);
		deleteCount += myResourceTableDao.deleteByPids(thePids);
		return deleteCount;
	}
}
