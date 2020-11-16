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
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * This class is responsible for clearing out existing EMPI links, as well as deleting all persons related to those EMPI Links.
 *
 */
public class EmpiClearSvcImpl implements IEmpiExpungeSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	final EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	final EmpiPersonDeletingSvc myEmpiPersonDeletingSvcImpl;
	final IEmpiSettings myEmpiSettings;

	@Autowired
	public EmpiClearSvcImpl(EmpiLinkDaoSvc theEmpiLinkDaoSvc, EmpiPersonDeletingSvc theEmpiPersonDeletingSvcImpl, IEmpiSettings theIEmpiSettings) {
		myEmpiLinkDaoSvc = theEmpiLinkDaoSvc;
		myEmpiPersonDeletingSvcImpl = theEmpiPersonDeletingSvcImpl;
		myEmpiSettings = theIEmpiSettings;
	}

	@Override
	public long expungeAllMdmLinksOfTargetType(String theResourceType, ServletRequestDetails theRequestDetails) {
		throwExceptionIfInvalidTargetType(theResourceType);
		ourLog.info("Clearing all EMPI Links for resource type {}...", theResourceType);
		List<Long> personPids = myEmpiLinkDaoSvc.deleteAllEmpiLinksOfTypeAndReturnPersonPids(theResourceType);
		DeleteMethodOutcome deleteOutcome = myEmpiPersonDeletingSvcImpl.expungePersonPids(personPids, theRequestDetails);
		ourLog.info("EMPI clear operation complete.  Removed {} EMPI links and {} Person resources.", personPids.size(), deleteOutcome.getExpungedResourcesCount());
		return personPids.size();
	}

	private void throwExceptionIfInvalidTargetType(String theResourceType) {
		if (!myEmpiSettings.isSupportedMdmType(theResourceType)) {
			throw new InvalidRequestException(ProviderConstants.MDM_CLEAR + " does not support resource type: " + theResourceType);
		}
	}

	@Override
	public long expungeAllEmpiLinks(ServletRequestDetails theRequestDetails) {
		ourLog.info("Clearing all EMPI Links...");
		List<Long> personPids = myEmpiLinkDaoSvc.deleteAllEmpiLinksAndReturnGoldenResourcePids();
		DeleteMethodOutcome deleteOutcome = myEmpiPersonDeletingSvcImpl.expungePersonPids(personPids, theRequestDetails);
		ourLog.info("EMPI clear operation complete.  Removed {} EMPI links and expunged {} Person resources.", personPids.size(), deleteOutcome.getExpungedResourcesCount());
		return personPids.size();
	}
}

