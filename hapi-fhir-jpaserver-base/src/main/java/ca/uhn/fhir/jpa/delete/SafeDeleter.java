package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// TODO LUKE should this be a service bean?
public class SafeDeleter {
	private static final Logger ourLog = LoggerFactory.getLogger(SafeDeleter.class);
	private final DaoRegistry myDaoRegistry;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final HapiTransactionService myHapiTransactionService;

	public SafeDeleter(DaoRegistry theDaoRegistry, IInterceptorBroadcaster theInterceptorBroadcaster, HapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myHapiTransactionService = theHapiTransactionService;
	}

	public Integer delete(RequestDetails theRequest, DeleteConflictList theConflictList, TransactionDetails theTransactionDetails) {
		Integer retVal = 0;

		List<String> cascadeDeleteIdCache = CascadingDeleteInterceptor.getCascadedDeletesList(theRequest, true);
		for (DeleteConflict next : theConflictList) {
			IdDt nextSource = next.getSourceId();
			String nextSourceId = nextSource.toUnqualifiedVersionless().getValue();

			if (!cascadeDeleteIdCache.contains(nextSourceId)) {
				cascadeDeleteIdCache.add(nextSourceId);

				IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextSource.getResourceType());

				// Interceptor call: STORAGE_CASCADE_DELETE
				IBaseResource resource = dao.read(nextSource, theRequest);
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(DeleteConflictList.class, theConflictList)
					.add(IBaseResource.class, resource);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_CASCADE_DELETE, params);

				// Actually perform the delete
				ourLog.info("Have delete conflict {} - Cascading delete", next);
				// TODO:  add a transaction checkpoint here and then try-catch to handle this
//				final TransactionStatus savepoint = myHapiTransactionService.savepoint();
				// FIXME LUKE: do we need this?
//				theTransactionDetails.setSavepoint(savepoint);
				DaoMethodOutcome result = dao.delete(nextSource, theConflictList, theRequest, theTransactionDetails);
				++retVal;
			}
		}

		return retVal;
	}
}
