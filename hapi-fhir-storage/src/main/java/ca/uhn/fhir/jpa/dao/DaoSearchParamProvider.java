package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

public class DaoSearchParamProvider implements ISearchParamProvider {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		return getSearchParamDao().search(theParams);
	}

	private IFhirResourceDao getSearchParamDao() {
		return myDaoRegistry.getResourceDao(ResourceTypeEnum.SEARCHPARAMETER.getCode());
	}

	@Override
	public IBaseResource read(IIdType theSearchParamId) {
		return getSearchParamDao().read(theSearchParamId);
	}
}
