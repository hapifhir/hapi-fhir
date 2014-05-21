package ca.uhn.fhir.jpa.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public interface IFhirResourceDao<T extends IResource> {

	MethodOutcome create(T theResource);

	/**
	 * 
	 * @param theId
	 * @return
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 */
	T read(IdDt theId);

	MethodOutcome update(T theResource, IdDt theId);

	List<T> history(IdDt theId);

	List<T> search(Map<String, IQueryParameterType> theParams);

	List<T> search(String theParameterName, IQueryParameterType theValue);

	List<T> search(SearchParameterMap theMap);

	Class<T> getResourceType();

	Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIds(Map<String, IQueryParameterType> theParams);

	Set<Long> searchForIdsWithAndOr(Map<String, List<List<IQueryParameterType>>> theMap);
	
}
