package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

@Service
public class MdmSearchParamSvc {
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private DaoRegistry myDaoRegistry;

	public SearchParameterMap mapFromCriteria(String theResourceType, String theResourceCriteria) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceType);
		return myMatchUrlService.translateMatchUrl(theResourceCriteria, resourceDef);
	}

	public List<String> getValueFromResourceForSearchParam(IBaseResource theResource, String theSearchParam) {
		String resourceType = myFhirContext.getResourceType(theResource);
		String searchParam = SearchParameterUtil.stripModifier(theSearchParam);
		RuntimeSearchParam activeSearchParam = mySearchParamRegistry.getActiveSearchParam(resourceType, searchParam);
		return mySearchParamExtractorService.extractParamValuesAsStrings(activeSearchParam, theResource);
	}

	/**
	 * Given a source type, and a criteria string of the shape name=x&birthDate=y, generate a {@link SearchParameterMap}
	 * that represents this query.
	 *
	 * @param theSourceType the resource type to execute the search on
	 * @param theCriteria   the string search criteria.
	 * @return the generated SearchParameterMap, or an empty one if there is no criteria.
	 */
	public SearchParameterMap getSearchParameterMapFromCriteria(String theSourceType, @Nullable String theCriteria) {
		SearchParameterMap spMap;
		if (StringUtils.isBlank(theCriteria)) {
			spMap = new SearchParameterMap();
		} else {
			spMap = mapFromCriteria(theSourceType, theCriteria);
		}
		return spMap;
	}

	public ISearchBuilder generateSearchBuilderForType(String theSourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theSourceType);
		return mySearchBuilderFactory.newSearchBuilder(resourceDao, theSourceType, resourceDao.getResourceType());
	}

	/**
	 * Will return true if the types match, or the search param type is '*', otherwise false.
	 *
	 * @param theSearchParamType
	 * @param theResourceType
	 * @return
	 */
	public boolean searchParamTypeIsValidForResourceType(String theSearchParamType, String theResourceType) {
		return theSearchParamType.equalsIgnoreCase(theResourceType) || theSearchParamType.equalsIgnoreCase("*");
	}
}
