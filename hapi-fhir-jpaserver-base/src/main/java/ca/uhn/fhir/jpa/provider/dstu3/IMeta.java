package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.provider.IExtendedResourceProvider;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.instance.model.api.IAnyResource;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.*;

public interface IMeta<T extends IAnyResource> extends IExtendedResourceProvider<T> {

	@Operation(name = OPERATION_META, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	default Parameters meta(RequestDetails theRequestDetails) {
		Parameters parameters = new Parameters();
		Meta metaGetOperation = getDao().metaGetOperation(Meta.class, theRequestDetails);
		parameters.addParameter().setName("return").setValue(metaGetOperation);
		return parameters;
	}

	@Operation(name = OPERATION_META, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	default Parameters meta(@IdParam IdType theId, RequestDetails theRequestDetails) {
		Parameters parameters = new Parameters();
		Meta metaGetOperation = getDao().metaGetOperation(Meta.class, theId, theRequestDetails);
		parameters.addParameter().setName("return").setValue(metaGetOperation);
		return parameters;
	}

	@Operation(name = OPERATION_META_ADD, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	default Parameters metaAdd(@IdParam IdType theId, @OperationParam(name = "meta") Meta theMeta, RequestDetails theRequestDetails) {
		if (theMeta == null) {
			throw new InvalidRequestException("Input contains no parameter with name 'meta'");
		}
		Parameters parameters = new Parameters();
		Meta metaAddOperation = getDao().metaAddOperation(theId, theMeta, theRequestDetails);
		parameters.addParameter().setName("return").setValue(metaAddOperation);
		return parameters;
	}

	@Operation(name = OPERATION_META_DELETE, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	default Parameters metaDelete(@IdParam IdType theId, @OperationParam(name = "meta") Meta theMeta, RequestDetails theRequestDetails) {
		if (theMeta == null) {
			throw new InvalidRequestException("Input contains no parameter with name 'meta'");
		}
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("return").setValue(getDao().metaDeleteOperation(theId, theMeta, theRequestDetails));
		return parameters;
	}
}
