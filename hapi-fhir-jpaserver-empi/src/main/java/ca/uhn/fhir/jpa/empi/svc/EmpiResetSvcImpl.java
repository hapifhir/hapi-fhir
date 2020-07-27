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
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.expunge.IResourceExpungeService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class is in charge of Clearing out existing EMPI links, as well as deleting all persons related to those EMPI Links.
 *
 */
public class EmpiResetSvcImpl implements IEmpiResetSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiResetSvcImpl.class);

	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@Autowired
	private IResourceExpungeService myResourceExpungeService;

	@Autowired
	private IResourceTableDao myResourceTable;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private HapiTransactionService myTransactionService;

	@Override
	public long expungeAllEmpiLinksOfTargetType(String theResourceType) {
		throwExceptionIfInvalidTargetType(theResourceType);

		List<Long> longs = myEmpiLinkDaoSvc.deleteAllEmpiLinksOfTypeAndReturnPersonPids(theResourceType);
		myResourceExpungeService.expungeCurrentVersionOfResources(null, longs, new AtomicInteger(longs.size()));
		return longs.size();
	}

	private void throwExceptionIfInvalidTargetType(String theResourceType) {
		if (!EmpiUtil.supportedTargetType(theResourceType)) {
			throw new InvalidRequestException(ProviderConstants.EMPI_CLEAR + " does not support resource type: " + theResourceType);
		}
	}

	/**
	 * TODO GGG this operation likely won't scale very well. Consider adding slicing
	 */
	@Override
	public long expungeAllEmpiLinks() {
		List<Long> longs = myEmpiLinkDaoSvc.deleteAllEmpiLinksAndReturnPersonPids();
		longs = longs.stream()
			.distinct().collect(Collectors.toList());

		DeleteConflictList dlc = new DeleteConflictList();
		List<Long> finalLongs = longs;
		myTransactionService.execute(null, tx -> {
			finalLongs.stream().forEach(pid -> {
				deleteCascade(pid, dlc);
			});
			return null;
		});

		IFhirResourceDao personDao = myDaoRegistry.getResourceDao("Person");
		while (!dlc.isEmpty()) {
			myTransactionService.execute(null, tx -> {
				deleteConflictBatch(dlc, personDao);
				return null;
			});
		}
		myResourceExpungeService.expungeHistoricalVersionsOfIds(null, longs, new AtomicInteger(longs.size()));
		//myResourceExpungeService.expungeCurrentVersionOfResources(null, longs, new AtomicInteger(longs.size()));
		return longs.size();
	}

	private void deleteConflictBatch(DeleteConflictList theDcl, IFhirResourceDao<IBaseResource> theDao) {
		DeleteConflictList newBatch = new DeleteConflictList();
		for (DeleteConflict next: theDcl) {
			IdDt nextSource = next.getSourceId();
			ourLog.info("Have delete conflict {} - Cascading delete", next);
			theDao.delete(nextSource.toVersionless(), newBatch, null, null);
		}
		theDcl.removeIf(x -> true);
		theDcl.addAll(newBatch);
	}

	private void deleteCascade(Long pid, DeleteConflictList theDcl) {
		ourLog.debug("About to cascade delete: " + pid);
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao("Person");
			resourceDao.delete(new IdType("Person/" + pid), theDcl, null, null);
	}
}

