package ca.uhn.fhir.jpa.mdm.helper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkEvent;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import ca.uhn.test.concurrency.PointcutLatch;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_GOLDEN_RECORD;
import static ca.uhn.fhir.mdm.api.MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS;

public class MdmHelperR4 extends BaseMdmHelper {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;

	public OutcomeAndLogMessageWrapper createWithLatch(IBaseResource theResource) throws InterruptedException {
		return createWithLatch(theResource, true);
	}

	public OutcomeAndLogMessageWrapper createWithLatch(IBaseResource theBaseResource, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterMdmLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doCreateResource(theBaseResource, isExternalHttpRequest);
		List<HookParams> hookParams = myAfterMdmLatch.awaitExpected();
		return new OutcomeAndLogMessageWrapper(daoMethodOutcome, hookParams);
	}

	public OutcomeAndLogMessageWrapper updateWithLatch(IBaseResource theIBaseResource) throws InterruptedException {
		return updateWithLatch(theIBaseResource, true);
	}

	public OutcomeAndLogMessageWrapper updateWithLatch(IBaseResource theIBaseResource, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterMdmLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doUpdateResource(theIBaseResource, isExternalHttpRequest);
		List<HookParams> hookParams = myAfterMdmLatch.awaitExpected();
		return new OutcomeAndLogMessageWrapper(daoMethodOutcome, hookParams);
	}

	public DaoMethodOutcome doCreateResource(IBaseResource theResource, boolean isExternalHttpRequest) {
		String resourceType = myFhirContext.getResourceType(theResource);

		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);
		return isExternalHttpRequest ? dao.create(theResource, myMockSrd): dao.create(theResource, new SystemRequestDetails());
	}

	public DaoMethodOutcome doUpdateResource(IBaseResource theResource, boolean isExternalHttpRequest) {
		String resourceType = myFhirContext.getResourceType(theResource);
		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);
		return isExternalHttpRequest ? dao.update(theResource, myMockSrd): dao.create(theResource);
	}


	@Nonnull
	public Patient buildGoldenPatient() {
		Patient patient = new Patient();
		patient.getMeta().addTag(SYSTEM_GOLDEN_RECORD_STATUS, CODE_GOLDEN_RECORD, "Golden Record");
		return patient;
	}
	/**
	 * OutcomeAndLogMessageWrapper is a simple wrapper class which is _excellent_. It allows us to skip the fact that java doesn't allow
	 * multiple returns, and wraps both the Method Outcome of the DAO, _and_ the TransactionLogMessages that were passed to the pointcut
	 * by the MDM module.
	 */
	public class OutcomeAndLogMessageWrapper {
		private final DaoMethodOutcome myDaoMethodOutcome;
		private final List<HookParams> myHookParams;

		public OutcomeAndLogMessageWrapper(DaoMethodOutcome theDaoMethodOutcome, List<HookParams> theHookParams) {
			myDaoMethodOutcome = theDaoMethodOutcome;
			myHookParams = theHookParams;
		}

		public DaoMethodOutcome getDaoMethodOutcome() {
			return myDaoMethodOutcome;
		}

		public TransactionLogMessages getLogMessages() {
			return PointcutLatch.getInvocationParameterOfType(myHookParams, TransactionLogMessages.class);
		}

		public List<HookParams> getHookParams() {
			return myHookParams;
		}

		public MdmLinkEvent getMdmLinkEvent() {
			return PointcutLatch.getInvocationParameterOfType(myHookParams, MdmLinkEvent.class);
		}

		public ResourceOperationMessage getResourceOperationMessage() {
			return PointcutLatch.getInvocationParameterOfType(myHookParams, ResourceOperationMessage.class);
		}
	}

}
