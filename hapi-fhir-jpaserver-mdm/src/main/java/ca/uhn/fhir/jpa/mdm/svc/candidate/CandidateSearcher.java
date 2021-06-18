package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.mdm.svc.MdmSearchParamSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CandidateSearcher {
	private static final Logger ourLog = LoggerFactory.getLogger(CandidateSearcher.class);
	private final DaoRegistry myDaoRegistry;
	private final IMdmSettings myMdmSettings;
	private final MdmSearchParamSvc myMdmSearchParamSvc;

	@Autowired
	public CandidateSearcher(DaoRegistry theDaoRegistry, IMdmSettings theMdmSettings, MdmSearchParamSvc theMdmSearchParamSvc) {
		myDaoRegistry = theDaoRegistry;
		myMdmSettings = theMdmSettings;
		myMdmSearchParamSvc = theMdmSearchParamSvc;
	}

	/**
	 * Perform a search for mdm candidates.
	 *
	 * @param theResourceType     the type of resources searched on
	 * @param theResourceCriteria the criteria used to search for the candidates
	 * @return Optional.empty() if >= IMdmSettings.getCandidateSearchLimit() candidates are found, otherwise
	 * return the bundle provider for the search results.
	 */
	public Optional<IBundleProvider> search(String theResourceType, String theResourceCriteria) {
		SearchParameterMap searchParameterMap = myMdmSearchParamSvc.mapFromCriteria(theResourceType, theResourceCriteria);

		searchParameterMap.setLoadSynchronousUpTo(myMdmSettings.getCandidateSearchLimit());

		IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		IBundleProvider retval = resourceDao.search(searchParameterMap);

		if (retval.size() != null && retval.size() >= myMdmSettings.getCandidateSearchLimit()) {
			return Optional.empty();
		}
		return Optional.of(retval);
	}
}
