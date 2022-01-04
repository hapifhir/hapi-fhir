package ca.uhn.fhir.jpa.term.job;

/*
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

import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.List;

public class BatchConceptRelationsDeleteWriter implements ItemWriter<Long> {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchConceptRelationsDeleteWriter.class);

	private static final DecimalFormat ourDecimalFormat = new DecimalFormat("#,###");

	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;

	@Autowired
	private ITermConceptPropertyDao myConceptPropertyDao;

	@Autowired
	private ITermConceptDesignationDao myConceptDesignationDao;


	@Override
	public void write(List<? extends Long> theTermCodeSystemVersionPidList) throws Exception {
		// receives input in chunks of size one
		long codeSystemVersionId = theTermCodeSystemVersionPidList.get(0);

		ourLog.info("Deleting term code links");
		int deletedLinks = myConceptParentChildLinkDao.deleteByCodeSystemVersion(codeSystemVersionId);
		ourLog.info("Deleted {} term code links", ourDecimalFormat.format(deletedLinks));

		ourLog.info("Deleting term code properties");
		int deletedProperties = myConceptPropertyDao.deleteByCodeSystemVersion(codeSystemVersionId);
		ourLog.info("Deleted {} term code properties", ourDecimalFormat.format(deletedProperties));

		ourLog.info("Deleting concept designations");
		int deletedDesignations = myConceptDesignationDao.deleteByCodeSystemVersion(codeSystemVersionId);
		ourLog.info("Deleted {} concept designations", ourDecimalFormat.format(deletedDesignations));
	}
}
