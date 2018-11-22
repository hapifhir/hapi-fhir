package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MatchResourceUrlService {
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private MatchUrlService myMatchUrlService;

	public <R extends IBaseResource> Set<Long> processMatchUrl(String theMatchUrl, Class<R> theResourceType) {
		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceType);

		SearchParameterMap paramMap = myMatchUrlService.translateMatchUrl(theMatchUrl, resourceDef);
		paramMap.setLoadSynchronous(true);

		if (paramMap.isEmpty() && paramMap.getLastUpdated() == null) {
			throw new InvalidRequestException("Invalid match URL[" + theMatchUrl + "] - URL has no search parameters");
		}

		IFhirResourceDao<R> dao = myDaoRegistry.getResourceDao(theResourceType);
		if (dao == null) {
			throw new InternalErrorException("No DAO for resource type: " + theResourceType.getName());
		}

		return dao.searchForIds(paramMap);
	}


}
