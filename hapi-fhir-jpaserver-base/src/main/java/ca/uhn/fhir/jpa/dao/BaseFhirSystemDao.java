package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import com.google.common.collect.ArrayListMultimap;

public abstract class BaseFhirSystemDao<T> extends BaseFhirDao implements IFhirSystemDao<T> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseFhirSystemDao.class);

	@PersistenceContext()
	protected EntityManager myEntityManager;

	protected boolean hasValue(InstantDt theInstantDt) {
		return theInstantDt != null && theInstantDt.isEmpty() == false;
	}

	protected ResourceTable tryToLoadEntity(IdDt nextId) {
		ResourceTable entity;
		try {
			Long pid = translateForcedIdToPid(nextId);
			entity = myEntityManager.find(ResourceTable.class, pid);
		} catch (ResourceNotFoundException e) {
			entity = null;
		}
		return entity;
	}

	protected ResourceTable loadFirstEntityFromCandidateMatches(Set<Long> candidateMatches) {
		return myEntityManager.find(ResourceTable.class, candidateMatches.iterator().next());
	}

	protected Set<Long> processMatchUrl(String theMatchUrl, Class<? extends IBaseResource> theResourceType) {
		RuntimeResourceDefinition resourceDef = getContext().getResourceDefinition(theResourceType);

		SearchParameterMap paramMap = new SearchParameterMap();
		List<NameValuePair> parameters;
		try {
			parameters = URLEncodedUtils.parse(new URI(theMatchUrl), "UTF-8");
		} catch (URISyntaxException e) {
			throw new InvalidRequestException("Failed to parse match URL[" + theMatchUrl + "] - Error was: " + e.toString());
		}

		ArrayListMultimap<String, QualifiedParamList> nameToParamLists = ArrayListMultimap.create();
		for (NameValuePair next : parameters) {
			String paramName = next.getName();
			String qualifier = null;
			for (int i = 0; i < paramMap.size(); i++) {
				switch (paramName.charAt(i)) {
				case '.':
				case ':':
					qualifier = paramName.substring(i);
					paramName = paramName.substring(0, i);
					i = Integer.MAX_VALUE;
					break;
				}
			}

			QualifiedParamList paramList = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(qualifier, next.getValue());
			nameToParamLists.put(paramName, paramList);
		}

		for (String nextParamName : nameToParamLists.keySet()) {
			RuntimeSearchParam paramDef = resourceDef.getSearchParam(nextParamName);
			if (paramDef == null) {
				throw new InvalidRequestException("Failed to parse match URL[" + theMatchUrl + "] - Resource type " + resourceDef.getName() + " does not have a parameter with name: " + nextParamName);
			}

			List<QualifiedParamList> paramList = nameToParamLists.get(nextParamName);
			IQueryParameterAnd<?> param = MethodUtil.parseQueryParams(paramDef, nextParamName, paramList);
			paramMap.add(nextParamName, param);
		}

		IFhirResourceDao<? extends IResource> dao = getDao(theResourceType);
		Set<Long> ids = dao.searchForIdsWithAndOr(paramMap);

		return ids;
	}

	@Override
	public IBundleProvider history(Date theSince) {
		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(null, null, theSince);
		ourLog.info("Processed global history in {}ms", w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public TagList getAllTags() {
		StopWatch w = new StopWatch();
		TagList retVal = super.getTags(null, null);
		ourLog.info("Processed getAllTags in {}ms", w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public Map<String, Long> getResourceCounts() {
		CriteriaBuilder builder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();
		Root<?> from = cq.from(ResourceTable.class);
		cq.multiselect(from.get("myResourceType").as(String.class), builder.count(from.get("myResourceType")).as(Long.class));
		cq.groupBy(from.get("myResourceType"));

		TypedQuery<Tuple> q = myEntityManager.createQuery(cq);

		Map<String, Long> retVal = new HashMap<String, Long>();
		for (Tuple next : q.getResultList()) {
			String resourceName = next.get(0, String.class);
			Long count = next.get(1, Long.class);
			retVal.put(resourceName, count);
		}
		return retVal;
	}

}
