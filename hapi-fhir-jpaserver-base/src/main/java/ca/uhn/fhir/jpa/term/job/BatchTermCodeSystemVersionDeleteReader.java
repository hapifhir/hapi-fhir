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

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_ID;

/**
 *
 */
public class BatchTermCodeSystemVersionDeleteReader implements ItemReader<Long> {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchTermCodeSystemVersionDeleteReader.class);

	@Autowired
	private ITermCodeSystemVersionDao myTermCodeSystemVersionDao;

	@Value("#{jobParameters['" + JOB_PARAM_CODE_SYSTEM_ID  + "']}")
	private Long myTermCodeSystemPid;

	private List<Long> myTermCodeSystemVersionPidList;
	private int myCurrentIdx = 0;


	@Override
	public Long read() throws Exception {
		if (myTermCodeSystemVersionPidList == null) {
			myTermCodeSystemVersionPidList = myTermCodeSystemVersionDao.findSortedPidsByCodeSystemPid(myTermCodeSystemPid);
		}

		if (myTermCodeSystemVersionPidList.isEmpty())  {
			// nothing to process
			ourLog.info("Nothing to process");
			return null;
		}

		if (myCurrentIdx >= myTermCodeSystemVersionPidList.size())  {
			// nothing else to process
			ourLog.info("No more versions to process");
			return null;
		}

		// still processing elements
		long TermCodeSystemVersionPid = myTermCodeSystemVersionPidList.get(myCurrentIdx++);
		ourLog.info("Passing termCodeSystemVersionPid: {} to writer", TermCodeSystemVersionPid);
		return TermCodeSystemVersionPid;
	}


}
