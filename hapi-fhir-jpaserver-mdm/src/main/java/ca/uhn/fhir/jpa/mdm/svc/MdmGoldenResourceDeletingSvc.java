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

import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.expunge.DeleteExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MdmGoldenResourceDeletingSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	/**
	 * This is here for the case of possible infinite loops. Technically batch conflict deletion should handle this, but this is an escape hatch.
	 */
	private static final int MAXIMUM_DELETE_ATTEMPTS = 100000;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private ExpungeService myExpungeService;
	@Autowired
	DeleteExpungeService myDeleteExpungeService;

	public DeleteMethodOutcome expungeGoldenResourcePids(List<Long> theGoldenResourcePids, String theResourceType, ServletRequestDetails theRequestDetails) {
		return myDeleteExpungeService.expungeByResourcePids(ProviderConstants.MDM_CLEAR, theResourceType, new SliceImpl<>(theGoldenResourcePids), theRequestDetails);
	}
}
