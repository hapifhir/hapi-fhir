package ca.uhn.fhir.jpa.empi.helper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.server.TransactionLogMessages;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

public class EmpiHelperR4 extends BaseEmpiHelper {
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private DaoRegistry myDaoRegistry;

	public OutcomeAndLogMessageWrapper createWithLatch(IBaseResource theResource) throws InterruptedException {
		return createWithLatch(theResource, true);
	}

	public OutcomeAndLogMessageWrapper createWithLatch(IBaseResource theBaseResource, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doCreateResource(theBaseResource, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return new OutcomeAndLogMessageWrapper(daoMethodOutcome, myAfterEmpiLatch.getLatchInvocationParameterOfType(TransactionLogMessages.class));
	}

	public OutcomeAndLogMessageWrapper updateWithLatch(IBaseResource theIBaseResource) throws InterruptedException {
		return updateWithLatch(theIBaseResource, true);
	}

	public OutcomeAndLogMessageWrapper updateWithLatch(IBaseResource theIBaseResource, boolean isExternalHttpRequest) throws InterruptedException {
		myAfterEmpiLatch.setExpectedCount(1);
		DaoMethodOutcome daoMethodOutcome = doUpdateResource(theIBaseResource, isExternalHttpRequest);
		myAfterEmpiLatch.awaitExpected();
		return new OutcomeAndLogMessageWrapper(daoMethodOutcome, myAfterEmpiLatch.getLatchInvocationParameterOfType(TransactionLogMessages.class));
	}

	public DaoMethodOutcome doCreateResource(IBaseResource theResource, boolean isExternalHttpRequest) {
		String resourceType = myFhirContext.getResourceType(theResource);

		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);
		return isExternalHttpRequest ? dao.create(theResource, myMockSrd): dao.create(theResource);
	}

	public DaoMethodOutcome doUpdateResource(IBaseResource theResource, boolean isExternalHttpRequest) {
		String resourceType = myFhirContext.getResourceType(theResource);
		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);
		return isExternalHttpRequest ? dao.update(theResource, myMockSrd): dao.create(theResource);
	}

	/**
	 * OutcomeAndLogMessageWrapper is a simple wrapper class which is _excellent_. It allows us to skip the fact that java doesn't allow
	 * multiple returns, and wraps both the Method Outcome of the DAO, _and_ the TransactionLogMessages that were passed to the pointcut
	 * by the EMPI module.
	 */
	public class OutcomeAndLogMessageWrapper {
		DaoMethodOutcome myDaoMethodOutcome;
		TransactionLogMessages myLogMessages;

		private OutcomeAndLogMessageWrapper(DaoMethodOutcome theDaoMethodOutcome, TransactionLogMessages theTransactionLogMessages) {
			myDaoMethodOutcome = theDaoMethodOutcome;
			myLogMessages = theTransactionLogMessages;
		}

		public DaoMethodOutcome getDaoMethodOutcome() {
			return myDaoMethodOutcome;
		}

		public TransactionLogMessages getLogMessages() {
			return myLogMessages;
		}
	}

}
