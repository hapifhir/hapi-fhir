package ca.uhn.fhir.jpa.provider;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;

public class JpaResourceProvider<T extends IResource> implements IResourceProvider {

	@Autowired(required=true)
	private FhirContext myContext;
	
	private IFhirResourceDao<T> myDao;

	public JpaResourceProvider() {
		// nothing
	}

	public JpaResourceProvider(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	@Create
	public MethodOutcome create(@ResourceParam T theResource) {
		return myDao.create(theResource);
	}

	@Delete
	public MethodOutcome delete(@IdParam IdDt theResource) {
		return myDao.delete(theResource);
	}

	public FhirContext getContext() {
		return myContext;
	}

	public IFhirResourceDao<T> getDao() {
		return myDao;
	}

	@History
	public IBundleProvider getHistoryForResourceInstance(@IdParam IdDt theId, @Since Date theDate) {
		return myDao.history(theId.getIdPartAsLong(), theDate);
	}

	@History
	public IBundleProvider getHistoryForResourceType(@Since Date theDate) {
		return myDao.history(theDate);
	}

	@Override
	public Class<? extends IResource> getResourceType() {
		return myDao.getResourceType();
	}

	@GetTags
	public TagList getTagsForResourceInstance(@IdParam IdDt theResourceId) {
		return myDao.getTags(theResourceId);
	}

	@GetTags
	public TagList getTagsForResourceType() {
		return myDao.getAllResourceTags();
	}

	@Read(version=true)
	public T read(@IdParam IdDt theId) {
		return myDao.read(theId);
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	@Required
	public void setDao(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	@Update
	public MethodOutcome update(@ResourceParam T theResource, @IdParam IdDt theId) {
		return myDao.update(theResource, theId);
	}

	@Validate
	public MethodOutcome validate(@ResourceParam T theResource) {
		MethodOutcome retVal = new MethodOutcome();
		retVal.setOperationOutcome(new OperationOutcome());
		retVal.getOperationOutcome().addIssue().setSeverity(IssueSeverityEnum.INFORMATION).setDetails("Resource validates successfully");
		return retVal;
	}

}
