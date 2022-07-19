package ca.uhn.fhir.jpa.term.job;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.DeleteLinksPropertiesAndDesignationsResult;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Transactional()
public class TermCodeSystemSvc implements ITermCodeSystemSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemSvc.class);

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
	public DeleteLinksPropertiesAndDesignationsResult deleteLinksPropertiesAndDesignationsByCodeSystemVersionPID(long theCodeSystemVersionPid) {
		DeleteLinksPropertiesAndDesignationsResult result = new DeleteLinksPropertiesAndDesignationsResult();

		ourLog.info("Deleting term code links");
		int deletedLinks = myConceptParentChildLinkDao.deleteByCodeSystemVersion(theCodeSystemVersionPid);
		ourLog.info("Deleted {} term code links", ourDecimalFormat.format(deletedLinks));
		result.setDeletedLinks(deletedLinks);

		ourLog.info("Deleting term code properties");
		int deletedProperties = myConceptPropertyDao.deleteByCodeSystemVersion(theCodeSystemVersionPid);
		ourLog.info("Deleted {} term code properties", ourDecimalFormat.format(deletedProperties));
		result.setDeletedProperties(deletedProperties);

		ourLog.info("Deleting concept designations");
		int deletedDesignations = myConceptDesignationDao.deleteByCodeSystemVersion(theCodeSystemVersionPid);
		ourLog.info("Deleted {} concept designations", ourDecimalFormat.format(deletedDesignations));
		result.setDeletedDesignations(deletedDesignations);

		return result;
	}

	@Override
	public void deleteCodeSystemConceptsByVersion(long theVersionPid) {
		ourLog.info("Deleting concepts");
		int deletedConcepts = myConceptDao.deleteByCodeSystemVersion(theVersionPid);
		ourLog.info("Deleted {} concepts", ourDecimalFormat.format(deletedConcepts));
	}

	@Override
	public void deleteCodeSystemVersion(long theVersionPid) {
		ourLog.debug("Executing for codeSystemVersionId: {}", theVersionPid);

		// if TermCodeSystemVersion being deleted is current, disconnect it form TermCodeSystem
		Optional<TermCodeSystem> codeSystemOpt = myCodeSystemDao.findWithCodeSystemVersionAsCurrentVersion(theVersionPid);
		if (codeSystemOpt.isPresent()) {
			TermCodeSystem codeSystem = codeSystemOpt.get();
			ourLog.info("Removing code system version: {} as current version of code system: {}", theVersionPid, codeSystem.getPid());
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
}
