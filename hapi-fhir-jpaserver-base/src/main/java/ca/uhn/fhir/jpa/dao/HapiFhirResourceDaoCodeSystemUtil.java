package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HapiFhirResourceDaoCodeSystemUtil {

	static public void deleteCodeSystemEntities(ITermCodeSystemDao theCsDao, ITermCodeSystemVersionDao theCsvDao,
															  ITermDeferredStorageSvc theTermDeferredStorageSvc, String theCodeSystemUrl,
															  String theCodeSystemVersion) {
		if (isNotBlank(theCodeSystemUrl)) {
			TermCodeSystem persCs = theCsDao.findByCodeSystemUri(theCodeSystemUrl);
			if (persCs != null) {
				if (theCodeSystemVersion != null) {
					TermCodeSystemVersion persCsVersion = theCsvDao.findByCodeSystemPidAndVersion(persCs.getPid(), theCodeSystemVersion);
					theTermDeferredStorageSvc.deleteCodeSystemVersion(persCsVersion);
				} else {
					theTermDeferredStorageSvc.deleteCodeSystem(persCs);
				}
			}
		}
	}
}
