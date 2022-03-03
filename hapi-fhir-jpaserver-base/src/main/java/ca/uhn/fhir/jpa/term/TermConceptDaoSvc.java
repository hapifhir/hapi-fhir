package ca.uhn.fhir.jpa.term;

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

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;

public class TermConceptDaoSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemStorageSvcImpl.class);

	@Autowired
	protected ITermConceptPropertyDao myConceptPropertyDao;

	@Autowired
	protected ITermConceptDao myConceptDao;

	@Autowired
	protected ITermConceptDesignationDao myConceptDesignationDao;

	public int saveConcept(TermConcept theConcept) {
		int retVal = 0;

		/*
		 * If the concept has an ID, we're reindexing, so there's no need to
		 * save parent concepts first (it's way too slow to do that)
		 */
		if (theConcept.getId() == null) {
			boolean needToSaveParents = false;
			for (TermConceptParentChildLink next : theConcept.getParents()) {
				if (next.getParent().getId() == null) {
					needToSaveParents = true;
					break;
				}
			}
			if (needToSaveParents) {
				retVal += ensureParentsSaved(theConcept.getParents());
			}
		}

		if (theConcept.getId() == null || theConcept.getIndexStatus() == null) {
			retVal++;
			theConcept.setIndexStatus(BaseHapiFhirDao.INDEX_STATUS_INDEXED);
			theConcept.setUpdated(new Date());
			myConceptDao.save(theConcept);

			for (TermConceptProperty next : theConcept.getProperties()) {
				myConceptPropertyDao.save(next);
			}

			for (TermConceptDesignation next : theConcept.getDesignations()) {
				myConceptDesignationDao.save(next);
			}

		}

		ourLog.trace("Saved {} and got PID {}", theConcept.getCode(), theConcept.getId());
		return retVal;
	}

	private int ensureParentsSaved(Collection<TermConceptParentChildLink> theParents) {
		ourLog.trace("Checking {} parents", theParents.size());
		int retVal = 0;

		for (TermConceptParentChildLink nextLink : theParents) {
			if (nextLink.getRelationshipType() == TermConceptParentChildLink.RelationshipTypeEnum.ISA) {
				TermConcept nextParent = nextLink.getParent();
				retVal += ensureParentsSaved(nextParent.getParents());
				if (nextParent.getId() == null) {
					nextParent.setUpdated(new Date());
					myConceptDao.saveAndFlush(nextParent);
					retVal++;
					ourLog.debug("Saved parent code {} and got id {}", nextParent.getCode(), nextParent.getId());
				}
			}
		}

		return retVal;
	}
}

