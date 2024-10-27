/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.util.MdmSearchParamBuildingUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MdmResourceDaoSvcImpl implements IMdmResourceDaoSvc {

	private static final int MAX_MATCHING_GOLDEN_RESOURCES = 1000;

	@Autowired
	DaoRegistry myDaoRegistry;

	@Autowired
	IMdmSettings myMdmSettings;

	@Override
	public DaoMethodOutcome upsertGoldenResource(IAnyResource theGoldenResource, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		RequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId((RequestPartitionId)
				theGoldenResource.getUserData(Constants.RESOURCE_PARTITION_ID));
		if (theGoldenResource.getIdElement().hasIdPart()) {
			return resourceDao.update(theGoldenResource, requestDetails);
		} else {
			return resourceDao.create(theGoldenResource, requestDetails);
		}
	}

	@Override
	public void removeGoldenResourceTag(IAnyResource theGoldenResource, String theResourcetype) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourcetype);
		RequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId((RequestPartitionId)
				theGoldenResource.getUserData(Constants.RESOURCE_PARTITION_ID));
		resourceDao.removeTag(
				theGoldenResource.getIdElement(),
				TagTypeEnum.TAG,
				MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS,
				MdmConstants.CODE_GOLDEN_RECORD,
				requestDetails);
	}

	@Override
	public IAnyResource readGoldenResourceByPid(IResourcePersistentId theGoldenResourcePid, String theResourceType) {
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		return (IAnyResource) resourceDao.readByPid(theGoldenResourcePid);
	}

	@Override
	public Optional<IAnyResource> searchGoldenResourceByEID(String theEid, String theResourceType) {
		return this.searchGoldenResourceByEID(theEid, theResourceType, null);
	}

	@Override
	public Optional<IAnyResource> searchGoldenResourceByEID(
			String theEid, String theResourceType, RequestPartitionId thePartitionId) {
		SearchParameterMap map = MdmSearchParamBuildingUtils.buildEidSearchParameterMap(
				theEid, theResourceType, myMdmSettings.getMdmRules());

		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		systemRequestDetails.setRequestPartitionId(thePartitionId);
		IBundleProvider search = resourceDao.search(map, systemRequestDetails);
		List<IBaseResource> resources = search.getResources(0, MAX_MATCHING_GOLDEN_RESOURCES);

		if (resources.isEmpty()) {
			return Optional.empty();
		} else if (resources.size() > 1) {
			throw new InternalErrorException(
					Msg.code(737) + "Found more than one active " + MdmConstants.CODE_HAPI_MDM_MANAGED
							+ " Golden Resource with EID "
							+ theEid
							+ ": "
							+ resources.get(0).getIdElement().getValue()
							+ ", "
							+ resources.get(1).getIdElement().getValue());
		} else {
			return Optional.of((IAnyResource) resources.get(0));
		}
	}
}
