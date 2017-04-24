package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.SearchParameter;

public class FhirResourceDaoSearchParameterDstu2 extends FhirResourceDaoDstu2<SearchParameter>implements IFhirResourceDaoSearchParameter<SearchParameter> {

	@Autowired
	private IFhirSystemDao<Bundle, MetaDt> mySystemDao;
	
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

		Integer count = mySystemDao.performReindexingPass(100);
		for (int i = 0; i < 50 && count != null && count != 0; i++) {
			count = mySystemDao.performReindexingPass(100);
			try {
				Thread.sleep(DateUtils.MILLIS_PER_SECOND);
			} catch (InterruptedException e) {
				break;
			}
		}
		
	}
	
}
