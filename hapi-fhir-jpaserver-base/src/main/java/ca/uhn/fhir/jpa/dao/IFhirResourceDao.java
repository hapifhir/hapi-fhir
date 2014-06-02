package ca.uhn.fhir.jpa.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public interface IFhirResourceDao<T extends IResource> {

	MethodOutcome create(T theResource);

	TagList getAllResourceTags();

	Class<T> getResourceType();

	TagList getTags(IdDt theResourceId);

	List<T> history();

	List<IResource> history(Date theDate, Integer theLimit);

	List<T> history(IdDt theId);

	List<IResource> history(Long theId, Date theSince, Integer theLimit);

	/**
	 * 
	 * @param theId
	 * @return
	 * @throws ResourceNotFoundException If the ID is not known to the server
	 */
	T read(IdDt theId);

	BaseHasResource readEntity(IdDt theId);

	List<T> search(Map<String, IQueryParameterType> theParams);

	List<T> search(SearchParameterMap theMap);

	List<T> search(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIds(Map<String, IQueryParameterType> theParams);

	Set<Long> searchForIds(String theParameterName, IQueryParameterType theValue);

	Set<Long> searchForIdsWithAndOr(Map<String, List<List<IQueryParameterType>>> theMap);

	MethodOutcome update(T theResource, IdDt theId);

	void removeTag(IdDt theId, String theScheme, String theTerm);

	void addTag(IdDt theId, String theScheme, String theTerm, String theLabel);
	
}
