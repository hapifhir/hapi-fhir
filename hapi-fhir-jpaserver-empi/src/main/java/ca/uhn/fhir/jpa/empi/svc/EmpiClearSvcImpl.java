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

import ca.uhn.fhir.empi.api.IEmpiResetSvc;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * This class is responsible for clearing out existing EMPI links, as well as deleting all persons related to those EMPI Links.
 *
 */
public class EmpiClearSvcImpl implements IEmpiResetSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	final EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	final EmpiPersonDeletingSvc myEmpiPersonDeletingSvcImpl;

	@Autowired
	public EmpiClearSvcImpl(EmpiLinkDaoSvc theEmpiLinkDaoSvc, EmpiPersonDeletingSvc theEmpiPersonDeletingSvcImpl) {
		myEmpiLinkDaoSvc = theEmpiLinkDaoSvc;
		myEmpiPersonDeletingSvcImpl = theEmpiPersonDeletingSvcImpl;
	}

	@Override
	public long expungeAllEmpiLinksOfTargetType(String theResourceType) {
		throwExceptionIfInvalidTargetType(theResourceType);
		ourLog.info("Clearing all EMPI Links for resource type {}...", theResourceType);
		List<Long> longs = myEmpiLinkDaoSvc.deleteAllEmpiLinksOfTypeAndReturnPersonPids(theResourceType);
		myEmpiPersonDeletingSvcImpl.deletePersonResourcesAndHandleConflicts(longs);
		myEmpiPersonDeletingSvcImpl.expungeHistoricalAndCurrentVersionsOfIds(longs);
		ourLog.info("EMPI clear operation complete.  Removed {} EMPI links.", longs.size());
		return longs.size();
	}

	private void throwExceptionIfInvalidTargetType(String theResourceType) {
		if (!EmpiUtil.supportedTargetType(theResourceType)) {
			throw new InvalidRequestException(ProviderConstants.EMPI_CLEAR + " does not support resource type: " + theResourceType);
		}
	}

	@Override
	public long removeAllEmpiLinks() {
		ourLog.info("Clearing all EMPI Links...");
		List<Long> personPids = myEmpiLinkDaoSvc.deleteAllEmpiLinksAndReturnPersonPids();
		myEmpiPersonDeletingSvcImpl.deletePersonResourcesAndHandleConflicts(personPids);
		myEmpiPersonDeletingSvcImpl.expungeHistoricalAndCurrentVersionsOfIds(personPids);
		ourLog.info("EMPI clear operation complete.  Removed {} EMPI links.", personPids.size());
		return personPids.size();
	}
}

