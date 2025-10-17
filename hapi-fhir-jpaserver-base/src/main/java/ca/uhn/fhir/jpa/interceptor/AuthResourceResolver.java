package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthResourceResolver;

import java.util.Arrays;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

@Service
public class AuthResourceResolver implements IAuthResourceResolver {
	private final DaoRegistry myDaoRegistry;

	public AuthResourceResolver(DaoRegistry myDaoRegistry) {
		this.myDaoRegistry = myDaoRegistry;
	}

	public IBaseResource resolveCompartmentById(IIdType theCompartmentId) {
		return myDaoRegistry.getResourceDao(theCompartmentId.getResourceType()).read(theCompartmentId, new SystemRequestDetails());
	}

	public List<IBaseResource> resolveCompartmentByIds(List<String> theCompartmentId, String theResourceType) {
		TokenOrListParam t = new TokenOrListParam(null, theCompartmentId.toArray(String[]::new));

		SearchParameterMap m = new SearchParameterMap();
		m.add(Constants.PARAM_ID, t);
		return myDaoRegistry.getResourceDao(theResourceType).searchForResources(m, new SystemRequestDetails());
	}
	//translateMatchUrl

}
