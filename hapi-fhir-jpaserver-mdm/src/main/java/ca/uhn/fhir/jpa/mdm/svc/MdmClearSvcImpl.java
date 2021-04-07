package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.mdm.api.IMdmExpungeSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * This class is responsible for clearing out existing MDM links, as well as deleting all Golden Resources related to those MDM Links.
 */
public class MdmClearSvcImpl implements IMdmExpungeSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	final MdmLinkDaoSvc myMdmLinkDaoSvc;
	final MdmGoldenResourceDeletingSvc myMdmGoldenResourceDeletingSvcImpl;
	final IMdmSettings myMdmSettings;

	@Autowired
	public MdmClearSvcImpl(MdmLinkDaoSvc theMdmLinkDaoSvc, MdmGoldenResourceDeletingSvc theMdmGoldenResourceDeletingSvcImpl, IMdmSettings theIMdmSettings) {
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
		myMdmGoldenResourceDeletingSvcImpl = theMdmGoldenResourceDeletingSvcImpl;
		myMdmSettings = theIMdmSettings;
	}

	@Override
	public long expungeAllMdmLinksOfSourceType(String theSourceResourceType, ServletRequestDetails theRequestDetails) {
		throwExceptionIfInvalidSourceResourceType(theSourceResourceType);
		ourLog.info("Clearing all MDM Links for resource type {}...", theSourceResourceType);
		List<Long> goldenResourcePids = myMdmLinkDaoSvc.deleteAllMdmLinksOfTypeAndReturnGoldenResourcePids(theSourceResourceType);
		DeleteMethodOutcome deleteOutcome = myMdmGoldenResourceDeletingSvcImpl.expungeGoldenResourcePids(goldenResourcePids, theSourceResourceType, theRequestDetails);
		ourLog.info("MDM clear operation complete.  Removed {} MDM links and {} Golden Resources.", goldenResourcePids.size(), deleteOutcome.getExpungedResourcesCount());
		return goldenResourcePids.size();
	}

	private void throwExceptionIfInvalidSourceResourceType(String theResourceType) {
		if (!myMdmSettings.isSupportedMdmType(theResourceType)) {
			throw new InvalidRequestException(ProviderConstants.MDM_CLEAR + " does not support resource type: " + theResourceType);
		}
	}

	@Override
	public long expungeAllMdmLinks(ServletRequestDetails theRequestDetails) {
		ourLog.info("Clearing all MDM Links...");
		long retVal = 0;

		for(String mdmType : myMdmSettings.getMdmRules().getMdmTypes()) {
			List<Long> goldenResourcePids = myMdmLinkDaoSvc.deleteAllMdmLinksAndReturnGoldenResourcePids();
			DeleteMethodOutcome deleteOutcome = myMdmGoldenResourceDeletingSvcImpl.expungeGoldenResourcePids(goldenResourcePids, null, theRequestDetails);
			ourLog.info("MDM clear operation on type {} complete.  Removed {} MDM links and expunged {} Golden resources.", mdmType, goldenResourcePids.size(), deleteOutcome.getExpungedResourcesCount());
			retVal += goldenResourcePids.size();
		}
		ourLog.info("MDM clear completed expunged with a total of {} golden resources cleared.", retVal);
		return retVal;
	}
}

