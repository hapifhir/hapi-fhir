package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceVersionCacheSvc {
	@Autowired
	DaoRegistry myDaoRegistry;

	public IResourceVersionMap getVersionLookup(String theResourceType, SearchParameterMap theSearchParamMap) {
		// FIXME KHS
		// Optimized database search
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceType);
		IBundleProvider result = dao.search(theSearchParamMap);
		ResourceVersionMap retval = new ResourceVersionMap();
		// FIXME KHS fix this
		List<IBaseResource> resources = result.getResources(0, 999);
		resources.stream().map(IBaseResource::getIdElement).forEach(retval::add);

//		resourceIds.stream().map(id -> myIdHelperService
//			.translatePidIdToForcedId(id))
//			.map(Optional::get)
//			.map(IdDt::new)
//			.forEach(retval.add());
		return retval;
	}
}
