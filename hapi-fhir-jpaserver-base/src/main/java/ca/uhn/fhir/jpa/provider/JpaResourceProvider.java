package ca.uhn.fhir.jpa.provider;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome.BaseIssue;
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
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public class JpaResourceProvider<T extends IResource> extends BaseJpaProvider implements IResourceProvider {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaResourceProvider.class);

	@Autowired(required = true)
	private FhirContext myContext;

	private IFhirResourceDao<T> myDao;

	public JpaResourceProvider() {
		// nothing
	}

	public JpaResourceProvider(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	@Create
	public MethodOutcome create(HttpServletRequest theRequest, @ResourceParam T theResource) {
		startRequest(theRequest);
		try {
			return myDao.create(theResource);
		} finally {
			endRequest(theRequest);
		}
	}

	@Delete
	public MethodOutcome delete(HttpServletRequest theRequest, @IdParam IdDt theResource) {
		startRequest(theRequest);
		try {
			return myDao.delete(theResource);
		} finally {
			endRequest(theRequest);
		}
	}

	public FhirContext getContext() {
		return myContext;
	}

	public IFhirResourceDao<T> getDao() {
		return myDao;
	}

	@History
	public IBundleProvider getHistoryForResourceInstance(HttpServletRequest theRequest, @IdParam IdDt theId, @Since Date theDate) {
		startRequest(theRequest);
		try {
			return myDao.history(theId, theDate);
		} finally {
			endRequest(theRequest);
		}
	}

	@History
	public IBundleProvider getHistoryForResourceType(HttpServletRequest theRequest, @Since Date theDate) {
		startRequest(theRequest);
		try {
			return myDao.history(theDate);
		} finally {
			endRequest(theRequest);
		}
	}

	@Override
	public Class<? extends IResource> getResourceType() {
		return myDao.getResourceType();
	}

	@GetTags
	public TagList getTagsForResourceInstance(HttpServletRequest theRequest, @IdParam IdDt theResourceId) {
		startRequest(theRequest);
		try {
			return myDao.getTags(theResourceId);
		} finally {
			endRequest(theRequest);
		}
	}

	@GetTags
	public TagList getTagsForResourceType(HttpServletRequest theRequest) {
		startRequest(theRequest);
		try {
			return myDao.getAllResourceTags();
		} finally {
			endRequest(theRequest);
		}
	}

	@Read(version = true)
	public T read(HttpServletRequest theRequest, @IdParam IdDt theId) {
		startRequest(theRequest);
		try {
			return myDao.read(theId);
		} finally {
			endRequest(theRequest);
		}
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	@Required
	public void setDao(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	@Update
	public MethodOutcome update(HttpServletRequest theRequest, @ResourceParam T theResource, @IdParam IdDt theId) {
		startRequest(theRequest);
		try {
			return myDao.update(theResource, theId);
		} catch (ResourceNotFoundException e) {
			ourLog.info("Can't update resource with ID[" + theId.getValue() + "] because it doesn't exist, going to create it instead");
			theResource.setId(theId);
			MethodOutcome retVal = myDao.create(theResource);
			retVal.setCreated(true);
			return retVal;
		} finally {
			endRequest(theRequest);
		}
	}

	@Validate
	public MethodOutcome validate(HttpServletRequest theRequest, @ResourceParam T theResource) {
		startRequest(theRequest);
		try {
			MethodOutcome retVal = new MethodOutcome();
			retVal.setOperationOutcome(new OperationOutcome());
			BaseIssue issue = retVal.getOperationOutcome().addIssue();
			issue.getSeverityElement().setValue("information");
			issue.setDetails("Resource validates successfully");
			return retVal;
		} finally {
			endRequest(theRequest);
		}
	}

}
