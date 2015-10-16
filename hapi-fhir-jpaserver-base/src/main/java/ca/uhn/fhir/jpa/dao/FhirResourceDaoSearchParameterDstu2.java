package ca.uhn.fhir.jpa.dao;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;

public class FhirResourceDaoSearchParameterDstu2 extends FhirResourceDaoDstu2<SearchParameter>implements IFhirResourceDaoSearchParameter<SearchParameter> {

	@Autowired
	private IFhirSystemDao<Bundle> mySystemDao;
	
	/**
	 * This method is called once per minute to perform any required re-indexing. During most passes this will
	 * just check and find that there are no resources requiring re-indexing. In that case the method just returns
	 * immediately. If the search finds that some resources require reindexing, the system will do a bunch of
	 * reindexing and then return.
	 */
	@Override
	@Scheduled(fixedDelay=DateUtils.MILLIS_PER_MINUTE)
	public void performReindexingPass() {
		if (getConfig().isSchedulingDisabled()) {
			return;
		}

		int count = mySystemDao.performReindexingPass(100);
		for (int i = 0; i < 10 && count > 0; i++) {
			count = mySystemDao.performReindexingPass(100);			
		}
		
	}
	
}
