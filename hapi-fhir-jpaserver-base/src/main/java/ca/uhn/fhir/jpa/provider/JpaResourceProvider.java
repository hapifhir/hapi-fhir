package ca.uhn.fhir.jpa.provider;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class JpaResourceProvider<T extends IResource> implements IResourceProvider {

	private FhirContext myContext=new FhirContext();
	private IFhirResourceDao<T> myDao;

	public JpaResourceProvider() {
		//nothing
	}

	public JpaResourceProvider(IFhirResourceDao<T> theDao) {
		myDao=theDao;
	}

	@Create
	public MethodOutcome create(@ResourceParam T theResource) {
		return myDao.create(theResource);
	}

	public FhirContext getContext() {
		return myContext;
	}
	
	public IFhirResourceDao<T> getDao() {
		return myDao;
	}

	@History
	public List<T> history(@IdParam IdDt theId) {
		return myDao.history(theId);
	}

	@Read()
	public T read(@IdParam IdDt theId) {
		return myDao.read(theId);
	}
	
	@Search
	public List<T> search() {
		return myDao.search(new HashMap<String, IQueryParameterType>());
	}

	@Required
	public void setDao(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	@Update
	public MethodOutcome update(@ResourceParam T theResource, @IdParam IdDt theId) {
		return myDao.update(theResource, theId);
	}

	@Override
	public Class<? extends IResource> getResourceType() {
		return myDao.getResourceType();
	}

	@History
	public List<IResource> getHistoryServerWithCriteria(@Since Date theDate, @Count Integer theCount) {
		return myDao.history(theDate, theCount);
	}

	@History
	public List<IResource> getHistoryServerWithCriteria(@IdParam IdDt theId, @Since Date theDate, @Count Integer theCount) {
		return myDao.history(theId.asLong(), theDate, theCount);
	}

}
