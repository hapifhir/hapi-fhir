package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.delete.DeleteConflictList;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Interceptor that allows for cascading deletes (deletes that resolve constraint issues).
 * <p>
 * For example, if <code>DiagnosticReport/A</code> has a reference to <code>Observation/B</code>
 * it is not normally possible to delete <code>Observation/B</code> without first deleting
 * <code>DiagnosticReport/A</code>. With this interceptor in place, it is.
 * </p>
 * <p>
 * When using this interceptor, client requests must include the parameter
 * <code>_cascade=true</code> on the DELETE URL in order to activate
 * cascading delete.
 * </p>
 */
@Interceptor
public class CascadingDeleteInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(CascadingDeleteInterceptor.class);
	private static final String CASCADED_DELETES_KEY = CascadingDeleteInterceptor.class.getName() + "_CASCADED_DELETES_KEY";

	private final DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 *
	 * @param theDaoRegistry The DAO registry (must not be null)
	 */
	public CascadingDeleteInterceptor(DaoRegistry theDaoRegistry) {
		Validate.notNull(theDaoRegistry, "theDaoRegistry must not be null");
		myDaoRegistry = theDaoRegistry;
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_DELETE_CONFLICTS)
	public boolean delete(DeleteConflictList theConflictList, RequestDetails theRequest) {
		ourLog.debug("Have delete conflicts: {}", theConflictList);

		if (!shouldCascade(theRequest)) {
			return false;
		}

		List<String> cascadedDeletes = getCascadedDeletesMapOrNull(theRequest);
		if (cascadedDeletes == null) {
			cascadedDeletes = new ArrayList<>();
			theRequest.getUserData().put(CASCADED_DELETES_KEY, cascadedDeletes);
		}

		for (Iterator<DeleteConflict> iter = theConflictList.iterator(); iter.hasNext(); ) {
			DeleteConflict next = iter.next();
			IdDt nextSource = next.getSourceId();

			ourLog.info("Have delete conflict {} - Cascading delete", next);

			IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextSource.getResourceType());
			dao.delete(nextSource, theRequest);

			cascadedDeletes.add(nextSource.getValue());
		}

		return true;
	}

	private List<String> getCascadedDeletesMapOrNull(RequestDetails theRequest) {
		return (List<String>) theRequest.getUserData().get(CASCADED_DELETES_KEY);
	}

	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void outgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails, IBaseResource theResponse) {
		List<String> deleteList = getCascadedDeletesMapOrNull(theRequestDetails);
		if (deleteList != null) {
			if (theResponseDetails.getResponseCode() == 200) {
				if (theResponse instanceof IBaseOperationOutcome) {
					FhirContext ctx = theRequestDetails.getFhirContext();
					IBaseOperationOutcome oo = (IBaseOperationOutcome) theResponse;
					String severity = OperationOutcome.IssueSeverity.INFORMATION.toCode();
					String code = OperationOutcome.IssueType.INFORMATIONAL.toCode();
					String details = "Cascaded delete to " + deleteList.size() + " resources: " + deleteList;
					OperationOutcomeUtil.addIssue(ctx, oo, severity, details, null, code);
				}
			}
		}
	}


	/**
	 * Subclasses may override
	 *
	 * @param theRequest The REST request
	 * @return Returns true if cascading delete should be allowed
	 */
	protected boolean shouldCascade(RequestDetails theRequest) {
		if (theRequest != null) {
			String[] cascadeParameters = theRequest.getParameters().get("_cascade");
			if (cascadeParameters != null && Arrays.asList(cascadeParameters).contains("true")) {
				return true;
			}
		}

		return false;
	}


}
