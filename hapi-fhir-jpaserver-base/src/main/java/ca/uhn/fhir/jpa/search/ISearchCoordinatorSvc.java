package ca.uhn.fhir.jpa.search;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.rest.server.IBundleProvider;

public interface ISearchCoordinatorSvc {

	List<Long> getResources(String theUuid, int theFrom, int theTo);

	IBundleProvider registerSearch(IDao theCallingDao, SearchParameterMap theParams, String theResourceType);

}
