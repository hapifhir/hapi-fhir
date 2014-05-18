package ca.uhn.fhir.jpa.dao;

import java.util.HashMap;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;

public abstract class BaseJpaResourceProvider<T extends IResource> implements IResourceProvider {

	private FhirContext myContext=new FhirContext();
	private IFhirResourceDao<T> myDao;

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

	public void setDao(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	@Update
	public MethodOutcome update(@ResourceParam T theResource, @IdParam IdDt theId) {
		return myDao.update(theResource, theId);
	}

}
