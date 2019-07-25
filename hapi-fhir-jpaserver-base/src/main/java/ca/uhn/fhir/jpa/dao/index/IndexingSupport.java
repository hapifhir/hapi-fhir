package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.ISearchParamExtractor;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;

public interface IndexingSupport {
	public DaoConfig getConfig();
	public ISearchParamExtractor getSearchParamExtractor();
	public ISearchParamRegistry getSearchParamRegistry();
	public FhirContext getContext();
	public EntityManager getEntityManager();
	public <R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType);
	public Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> getResourceTypeToDao();
	public boolean isLogicalReference(IIdType nextId);
	public IForcedIdDao getForcedIdDao();
	public <R extends IBaseResource> Set<Long> processMatchUrl(String theMatchUrl, Class<R> theResourceType);
	public Long translateForcedIdToPid(String theResourceName, String theResourceId);
	public String toResourceName(Class<? extends IBaseResource> theResourceType);
	public IResourceIndexedCompositeStringUniqueDao getResourceIndexedCompositeStringUniqueDao();

}
