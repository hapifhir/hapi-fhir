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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

	/**
	* Given a resource, remove its Golden Resource tag.
	* @param theGoldenResource the {@link IAnyResource} to remove the tag from.
	* @param theResourcetype the type of that resource
	*/
	public void removeGoldenResourceTag(IAnyResource theGoldenResource, String theResourcetype) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourcetype);
		resourceDao.removeTag(theGoldenResource.getIdElement(), TagTypeEnum.TAG, EmpiConstants.SYSTEM_GOLDEN_RECORD_STATUS, EmpiConstants.CODE_GOLDEN_RECORD);
	}

	public IAnyResource readSourceResourceByPid(ResourcePersistentId theSourceResourcePid, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		return (IAnyResource) resourceDao.readByPid(theSourceResourcePid);
	}

	//TODO GGG MDM address this
	public Optional<IAnyResource> searchSourceResourceByEID(String theEid, String theResourceType) {
		SearchParameterMap map = buildEidSearchParameterMap(theEid);

		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		IBundleProvider search = resourceDao.search(map);
		List<IBaseResource> resources = search.getResources(0, MAX_MATCHING_PERSONS);

		if (resources.isEmpty()) {
			return Optional.empty();
		} else if (resources.size() > 1) {
			throw new InternalErrorException("Found more than one active " +
				EmpiConstants.CODE_HAPI_MDM_MANAGED +
				" Person with EID " +
				theEid +
				": " +
				resources.get(0).getIdElement().getValue() +
				", " +
				resources.get(1).getIdElement().getValue()
			);
		} else {
			return Optional.of((IAnyResource) resources.get(0));
		}
	}

	@NotNull
	private SearchParameterMap buildEidSearchParameterMap(String theTheEid) {
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("identifier", new TokenParam(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem(), theTheEid));
		map.add("_tag", new TokenParam(EmpiConstants.SYSTEM_GOLDEN_RECORD_STATUS, EmpiConstants.CODE_GOLDEN_RECORD));
		return map;
	}
}
