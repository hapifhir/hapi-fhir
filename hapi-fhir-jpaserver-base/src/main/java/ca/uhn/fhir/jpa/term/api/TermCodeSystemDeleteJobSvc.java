/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.term.api;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.term.models.CodeSystemConceptsDeleteResult;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Transactional
public class TermCodeSystemDeleteJobSvc implements ITermCodeSystemDeleteJobSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemDeleteJobSvc.class);

	private static final DecimalFormat ourDecimalFormat = new DecimalFormat("#,###");

	@Autowired
	private ITermConceptDao myConceptDao;

	@Autowired
	private ITermCodeSystemDao myCodeSystemDao;

	@Autowired
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;

	@Autowired
	private ITermConceptPropertyDao myConceptPropertyDao;

	@Autowired
	private ITermConceptDesignationDao myConceptDesignationDao;

	@Autowired
	private ITermCodeSystemDao myTermCodeSystemDao;

	@Autowired
	private ITermDeferredStorageSvc myDeferredStorageSvc;

	@Override
	public Iterator<Long> getAllCodeSystemVersionForCodeSystemPid(long thePid) {
		// TODO - make this a pageable iterator
		List<Long> pids = myTermCodeSystemVersionDao.findSortedPidsByCodeSystemPid(thePid);

		if (pids == null) {
			return new ArrayIterator<>(new Long[0]);
		}

		return pids.iterator();
	}

	@Override
	public CodeSystemConceptsDeleteResult deleteCodeSystemConceptsByCodeSystemVersionPid(long theCodeSystemVersionPid) {
		CodeSystemConceptsDeleteResult result = new CodeSystemConceptsDeleteResult();

		// code system links delete
		ourLog.info("Deleting term code links");
		int deletedLinks = myConceptParentChildLinkDao.deleteByCodeSystemVersion(theCodeSystemVersionPid);
		ourLog.info("Deleted {} term code links", ourDecimalFormat.format(deletedLinks));
		result.setDeletedLinks(deletedLinks);

		// code system concept properties
		ourLog.info("Deleting term code properties");
		int deletedProperties = myConceptPropertyDao.deleteByCodeSystemVersion(theCodeSystemVersionPid);
		ourLog.info("Deleted {} term code properties", ourDecimalFormat.format(deletedProperties));
		result.setDeletedProperties(deletedProperties);

		// code system concept designations
		ourLog.info("Deleting concept designations");
		int deletedDesignations = myConceptDesignationDao.deleteByCodeSystemVersion(theCodeSystemVersionPid);
		ourLog.info("Deleted {} concept designations", ourDecimalFormat.format(deletedDesignations));
		result.setDeletedDesignations(deletedDesignations);

		// code system concept
		ourLog.info("Deleting concepts");
		int deletedConcepts = myConceptDao.deleteByCodeSystemVersion(theCodeSystemVersionPid);
		ourLog.info("Deleted {} concepts", ourDecimalFormat.format(deletedConcepts));
		result.setCodeSystemConceptDelete(deletedConcepts);

		return result;
	}

	@Override
	public void deleteCodeSystemVersion(long theVersionPid) {
		ourLog.debug("Executing for codeSystemVersionId: {}", theVersionPid);

		// if TermCodeSystemVersion being deleted is current, disconnect it form TermCodeSystem
		Optional<TermCodeSystem> codeSystemOpt =
				myCodeSystemDao.findWithCodeSystemVersionAsCurrentVersion(theVersionPid);
		if (codeSystemOpt.isPresent()) {
			TermCodeSystem codeSystem = codeSystemOpt.get();
			ourLog.info(
					"Removing code system version: {} as current version of code system: {}",
					theVersionPid,
					codeSystem.getPid());
			codeSystem.setCurrentVersion(null);
			myCodeSystemDao.save(codeSystem);
		}

		ourLog.info("Deleting code system version: {}", theVersionPid);
		Optional<TermCodeSystemVersion> csv = myTermCodeSystemVersionDao.findById(theVersionPid);
		csv.ifPresent(theTermCodeSystemVersion -> {
			myTermCodeSystemVersionDao.delete(theTermCodeSystemVersion);
			ourLog.info("Code system version: {} deleted", theVersionPid);
		});
	}

	@Override
	public void deleteCodeSystem(long thePid) {
		ourLog.info("Deleting code system by id : {}", thePid);

		Optional<TermCodeSystem> csop = myTermCodeSystemDao.findById(thePid);
		if (csop.isPresent()) {
			TermCodeSystem cs = csop.get();

			ourLog.info("Deleting code system {} / {}", thePid, cs.getCodeSystemUri());

			myTermCodeSystemDao.deleteById(thePid);

			ourLog.info("Code system {} deleted", thePid);
		}
	}

	@Override
	public void notifyJobComplete(String theJobId) {
		myDeferredStorageSvc.notifyJobEnded(theJobId);
	}
}
