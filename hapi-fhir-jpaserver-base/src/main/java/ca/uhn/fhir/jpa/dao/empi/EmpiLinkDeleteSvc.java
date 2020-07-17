package ca.uhn.fhir.jpa.dao.empi;

import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiLinkDeleteSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLinkDeleteSvc.class);

	@Autowired
	private IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	private IdHelperService myIdHelperService;

	/**
	 * Delete all EmpiLink records with any reference to this resource.  (Used by Expunge.)
	 * @param theResource
	 * @return the number of records deleted
	 */
	public int deleteWithAnyReferenceTo(IBaseResource theResource) {
		Long pid = myIdHelperService.getPidOrThrowException(theResource.getIdElement(), null);
		int removed =  myEmpiLinkDao.deleteWithAnyReferenceToPid(pid);
		if (removed > 0) {
			ourLog.info("Removed {} EMPI links with references to {}", removed, theResource.getIdElement().toVersionless());
		}
		return removed;
	}
}
