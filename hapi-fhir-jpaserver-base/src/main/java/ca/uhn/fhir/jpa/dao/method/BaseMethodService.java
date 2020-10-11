package ca.uhn.fhir.jpa.dao.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.OO_SEVERITY_ERROR;
import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.OO_SEVERITY_INFO;

public abstract class BaseMethodService<T extends IBaseResource> {
	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected HapiTransactionService myTransactionService;
	@Autowired
	protected IdHelperService myIdHelperService;
	@Autowired
	protected IRequestPartitionHelperSvc myRequestPartitionHelperService;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected IResourceTableDao myResourceTableDao;

	protected final BaseHapiFhirResourceDao<T> myDao;

	protected BaseMethodService(BaseHapiFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	protected String getResourceName() {
		return myDao.getResourceName();
	}

	protected Class<T> getResourceType() {
		return myDao.getResourceType();
	}

	public IBaseOperationOutcome createInfoOperationOutcome(String theMessage) {
		return createOperationOutcome(OO_SEVERITY_INFO, theMessage, "informational");
	}

	public IBaseOperationOutcome createErrorOperationOutcome(String theMessage, String theCode) {
		return createOperationOutcome(OO_SEVERITY_ERROR, theMessage, theCode);
	}

	private IBaseOperationOutcome createOperationOutcome(String theSeverity, String theMessage, String theCode) {
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);
		OperationOutcomeUtil.addIssue(myFhirContext, oo, theSeverity, theMessage, null, theCode);
		return oo;
	}

	public String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myFhirContext.getResourceType(theResourceType);
	}

	String toResourceName(IBaseResource theResource) {
		return myFhirContext.getResourceType(theResource);
	}

	protected DaoMethodOutcome toMethodOutcome(RequestDetails theRequest, @Nonnull final IBasePersistedResource theEntity, @Nonnull IBaseResource theResource) {
		DaoMethodOutcome outcome = new DaoMethodOutcome();

		if (theEntity instanceof ResourceTable) {
			if (((ResourceTable) theEntity).isUnchangedInCurrentOperation()) {
				outcome.setNop(true);
			}
		}

		IIdType id = null;
		if (theResource.getIdElement().getValue() != null) {
			id = theResource.getIdElement();
		}
		if (id == null) {
			id = theEntity.getIdDt();
			if (myFhirContext.getVersion().getVersion().isRi()) {
				id = myFhirContext.getVersion().newIdType().setValue(id.getValue());
			}
		}

		outcome.setId(id);
		if (theEntity.isDeleted() == false) {
			outcome.setResource(theResource);
		}
		outcome.setEntity(theEntity);

		// Interceptor broadcast: STORAGE_PREACCESS_RESOURCES
		if (outcome.getResource() != null) {
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(outcome.getResource());
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			if (accessDetails.isDontReturnResourceAtIndex(0)) {
				outcome.setResource(null);
			}
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		// Note that this will only fire if someone actually goes to use the
		// resource in a response (it's their responsibility to call
		// outcome.fireResourceViewCallback())
		outcome.registerResourceViewCallback(() -> {
			if (outcome.getResource() != null) {
				SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(outcome.getResource());
				HookParams params = new HookParams()
					.add(IPreResourceShowDetails.class, showDetails)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
				outcome.setResource(showDetails.getResource(0));
			}
		});

		return outcome;
	}

	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}
}
