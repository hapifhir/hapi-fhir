package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.BaseSearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class DaoSearchParamProvider implements ISearchParamProvider {
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		return myDaoRegistry.getResourceDao(ResourceTypeEnum.SEARCHPARAMETER.getCode()).search(theParams);
	}

	@Override
	public <SP extends IBaseResource> int refreshCache(BaseSearchParamRegistry<SP> theSearchParamRegistry, long theRefreshInterval) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		return txTemplate.execute(t-> theSearchParamRegistry.doRefresh(theRefreshInterval));
	}
}
