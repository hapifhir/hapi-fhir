package ca.uhn.fhir.jpa.term.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BatchTermCodeSystemVersionDeleteWriter implements ItemWriter<Long> {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchTermCodeSystemVersionDeleteWriter.class);

	@Autowired
	private ITermCodeSystemDao myCodeSystemDao;

	@Autowired
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;



	@Override
	public void write(List<? extends Long> theTermCodeSystemVersionPidList) throws Exception {
		// receives input in chunks of size one
		long codeSystemVersionId = theTermCodeSystemVersionPidList.get(0);

		ourLog.debug("Executing for codeSystemVersionId: {}", codeSystemVersionId);

		// if TermCodeSystemVersion being deleted is current, disconnect it form TermCodeSystem
		Optional<TermCodeSystem> codeSystemOpt = myCodeSystemDao.findWithCodeSystemVersionAsCurrentVersion(codeSystemVersionId);
		if (codeSystemOpt.isPresent()) {
			TermCodeSystem codeSystem = codeSystemOpt.get();
			ourLog.info("Removing code system version: {} as current version of code system: {}", codeSystemVersionId, codeSystem.getPid());
			codeSystem.setCurrentVersion(null);
			myCodeSystemDao.save(codeSystem);
		}

		ourLog.info("Deleting code system version: {}", codeSystemVersionId);
		Optional<TermCodeSystemVersion> csv = myTermCodeSystemVersionDao.findById(codeSystemVersionId);
		csv.ifPresent(theTermCodeSystemVersion -> {
			myTermCodeSystemVersionDao.delete(theTermCodeSystemVersion);
			ourLog.info("Code system version: {} deleted", codeSystemVersionId);
		});

	}
}
