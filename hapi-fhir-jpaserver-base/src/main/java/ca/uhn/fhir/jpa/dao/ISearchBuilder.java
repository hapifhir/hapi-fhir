package ca.uhn.fhir.jpa.dao;

/*-
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.DateRangeParam;

public interface ISearchBuilder {

	Iterator<Long> createQuery(SearchParameterMap theParams, String theSearchUuid);

	void setType(Class<? extends IBaseResource> theResourceType, String theResourceName);

	void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation, EntityManager theEntityManager,
			FhirContext theContext, IDao theDao);

	Set<Long> loadReverseIncludes(IDao theCallingDao, FhirContext theContext, EntityManager theEntityManager, Collection<Long> theMatches, Set<Include> theRevIncludes, boolean theReverseMode,
			DateRangeParam theLastUpdated);

}
