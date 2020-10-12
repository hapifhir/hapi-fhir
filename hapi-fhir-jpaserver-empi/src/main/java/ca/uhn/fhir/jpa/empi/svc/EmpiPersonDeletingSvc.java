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

import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.IResourceExpungeService;
import ca.uhn.fhir.jpa.dao.expunge.ResourceExpungeService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpiPersonDeletingSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	/**
	 * This is here for the case of possible infinite loops. Technically batch conflict deletion should handle this, but this is an escape hatch.
	 */
	private static final int MAXIMUM_DELETE_ATTEMPTS = 100000;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private ExpungeService myExpungeService;
	@Autowired
	IResourceExpungeService myResourceExpungeService;

	public DeleteMethodOutcome expungePersonPids(List<Long> thePersonPids) {
		return myResourceExpungeService.expungeByResourcePids(new SliceImpl<>(thePersonPids));
	}
}
