package ca.uhn.fhir.jpa.empi.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpiResourceDaoSvc {

	private static final int MAX_MATCHING_PERSONS = 1000;

	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	IEmpiSettings myEmpiConfig;

	public DaoMethodOutcome upsertSourceResource(IAnyResource theSourceResource, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		if (theSourceResource.getIdElement().hasIdPart()) {
			return resourceDao.update(theSourceResource);
		} else {
			return resourceDao.create(theSourceResource);
		}
	}

	public IAnyResource readSourceResourceByPid(ResourcePersistentId theSourceResourcePid, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		return (IAnyResource) resourceDao.readByPid(theSourceResourcePid);
	}

	public Optional<IAnyResource> searchSourceResourceByEID(String theEid, String theResourceType) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("identifier", new TokenParam(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(), theEid));
		// TODO NG - During person dedup do we set this to false? We might be setting a person to inactive...
		// map.add("active", new TokenParam("true"));
		map.add("_tag", new TokenParam(EmpiConstants.SYSTEM_EMPI_MANAGED, EmpiConstants.CODE_HAPI_EMPI_MANAGED));

		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		IBundleProvider search = resourceDao.search(map);

		// Could add the meta tag to the query, but it's probably more efficient to filter on it afterwards since in practice
		// it will always be present.
		List<IBaseResource> list = search.getResources(0, MAX_MATCHING_PERSONS).stream()
			.filter(EmpiUtil::isEmpiManaged)
			.collect(Collectors.toList());

		if (list.isEmpty()) {
			return Optional.empty();
		} else if (list.size() > 1) {
			throw new InternalErrorException("Found more than one active " +
				EmpiConstants.CODE_HAPI_EMPI_MANAGED +
				" Person with EID " +
				theEid +
				": " +
				list.get(0).getIdElement().getValue() +
				", " +
				list.get(1).getIdElement().getValue()
			);
		} else {
			return Optional.of((IAnyResource) list.get(0));
		}
	}
}
