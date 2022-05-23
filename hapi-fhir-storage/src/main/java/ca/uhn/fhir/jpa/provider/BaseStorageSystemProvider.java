package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Required;

public class BaseStorageSystemProvider<T, MT> extends BaseJpaProvider {
	protected IFhirSystemDao<T, MT> myDao;

	@Operation(name = ProviderConstants.OPERATION_EXPUNGE, idempotent = false, returnParameters = {
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT, typeName = "integer")
	})
	public IBaseParameters expunge(
		@OperationParam(name = ProviderConstants.OPERATION_EXPUNGE_PARAM_LIMIT, typeName = "integer") IPrimitiveType<Integer> theLimit,
		@OperationParam(name = ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES, typeName = "boolean") IPrimitiveType<Boolean> theExpungeDeletedResources,
		@OperationParam(name = ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS, typeName = "boolean") IPrimitiveType<Boolean> theExpungeOldVersions,
		@OperationParam(name = ProviderConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_EVERYTHING, typeName = "boolean") IPrimitiveType<Boolean> theExpungeEverything,
		RequestDetails theRequestDetails
	) {
		return doExpunge(theLimit, theExpungeDeletedResources, theExpungeOldVersions, theExpungeEverything, theRequestDetails);
	}

	protected IBaseParameters doExpunge(IPrimitiveType<? extends Integer> theLimit, IPrimitiveType<? extends Boolean> theExpungeDeletedResources, IPrimitiveType<? extends Boolean> theExpungeOldVersions, IPrimitiveType<? extends Boolean> theExpungeEverything, RequestDetails theRequestDetails) {
		ExpungeOptions options = createExpungeOptions(theLimit, theExpungeDeletedResources, theExpungeOldVersions, theExpungeEverything);
		ExpungeOutcome outcome = getDao().expunge(options, theRequestDetails);
		return createExpungeResponse(outcome);
	}

	protected IFhirSystemDao<T, MT> getDao() {
		return myDao;
	}

	@Required
	public void setDao(IFhirSystemDao<T, MT> theDao) {
		myDao = theDao;
	}
}
