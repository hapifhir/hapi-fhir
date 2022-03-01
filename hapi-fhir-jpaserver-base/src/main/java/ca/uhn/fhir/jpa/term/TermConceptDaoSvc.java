package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
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

//			for (TermConceptProperty next : theConcept.getProperties()) {
//				myConceptPropertyDao.save(next);
//			}
//
//			for (TermConceptDesignation next : theConcept.getDesignations()) {
//				myConceptDesignationDao.save(next);
//			}

			myConceptPropertyDao.saveAll(theConcept.getProperties());
			myConceptDesignationDao.saveAll(theConcept.getDesignations());
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

