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

import ca.uhn.fhir.empi.api.IEmpiExpungeSvc;
import ca.uhn.fhir.jpa.dao.expunge.IResourceExpungeService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiTargetType;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is in charge of Clearing out existing EMPI links, as well as deleting all persons related to those EMPI Links.
 *
 */
public class EmpiExpungeSvcImpl implements IEmpiExpungeSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiExpungeSvcImpl.class);

	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@Autowired
	private IResourceExpungeService myResourceExpungeService;


	@Override
	public void expungeEmpiLinks(String theResourceType) {
		EmpiTargetType targetType = getTargetTypeOrThrowException(theResourceType);
		List<Long> longs = myEmpiLinkDaoSvc.deleteAllEmpiLinksOfTypeAndReturnPersonPids(targetType);
		myResourceExpungeService.expungeCurrentVersionOfResources(null, longs, new AtomicInteger(longs.size()));
	}

	private EmpiTargetType getTargetTypeOrThrowException(String theResourceType) {
		if (theResourceType.equalsIgnoreCase("Patient")) {
			return EmpiTargetType.PATIENT;
		} else if(theResourceType.equalsIgnoreCase("Practitioner")) {
			return EmpiTargetType.PRACTITIONER;
		} else {
			throw new InvalidRequestException(ProviderConstants.EMPI_CLEAR + " does not support resource type: " + theResourceType);
		}
	}

	@Override
	public void expungeEmpiLinks() {
		List<Long> longs = myEmpiLinkDaoSvc.deleteAllEmpiLinksAndReturnPersonPids();
		myResourceExpungeService.expungeCurrentVersionOfResources(null, longs, new AtomicInteger(longs.size()));
	}
}

