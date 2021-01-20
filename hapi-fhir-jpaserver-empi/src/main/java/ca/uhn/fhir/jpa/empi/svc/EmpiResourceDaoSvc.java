package ca.uhn.fhir.jpa.empi.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
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

	private IFhirResourceDao<IBaseResource> myPatientDao;
	private IFhirResourceDao<IBaseResource> myPersonDao;
	private IFhirResourceDao<IBaseResource> myPractitionerDao;

	@PostConstruct
	public void postConstruct() {
		myPatientDao = myDaoRegistry.getResourceDao("Patient");
		myPersonDao = myDaoRegistry.getResourceDao("Person");
		myPractitionerDao = myDaoRegistry.getResourceDao("Practitioner");
	}

	public IAnyResource readPatient(IIdType theId) {
		return (IAnyResource) myPatientDao.read(theId);
	}

	public IAnyResource readPerson(IIdType theId) {
		return (IAnyResource) myPersonDao.read(theId);
	}

	public IAnyResource readPractitioner(IIdType theId) {
		return (IAnyResource) myPractitionerDao.read(theId);
	}

	public DaoMethodOutcome updatePerson(IAnyResource thePerson) {
		if (thePerson.getIdElement().hasIdPart()) {
			return myPersonDao.update(thePerson);
		} else {
			return myPersonDao.create(thePerson);
		}
	}

	public IAnyResource readPersonByPid(ResourcePersistentId thePersonPid) {
		return (IAnyResource) myPersonDao.readByPid(thePersonPid);
	}

	public Optional<IAnyResource> searchPersonByEid(String theEid) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("identifier", new TokenParam(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(), theEid));
		map.add("active", new TokenParam("true"));
		IBundleProvider search = myPersonDao.search(map);

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
