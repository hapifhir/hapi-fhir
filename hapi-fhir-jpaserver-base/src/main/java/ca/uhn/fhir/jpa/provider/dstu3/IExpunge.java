package ca.uhn.fhir.jpa.provider.dstu3;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.IExtendedResourceProvider;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

interface IExpunge<T extends IAnyResource> extends IExtendedResourceProvider<T> {

	@Operation(name = JpaConstants.OPERATION_EXPUNGE, idempotent = false, returnParameters = {
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT, type = IntegerType.class)
	})
	default Parameters expunge(
		@IdParam IIdType theIdParam,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT) IntegerType theLimit,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES) BooleanType theExpungeDeletedResources,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS) BooleanType theExpungeOldVersions,
		RequestDetails theRequest) {
		org.hl7.fhir.r4.model.Parameters retVal = doExpunge(theIdParam, theLimit, theExpungeDeletedResources, theExpungeOldVersions, null, theRequest);
		try {
			return VersionConvertor_30_40.convertParameters(retVal);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}

	@Operation(name = JpaConstants.OPERATION_EXPUNGE, idempotent = false, returnParameters = {
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT, type = IntegerType.class)
	})
	default Parameters expunge(
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT) IntegerType theLimit,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES) BooleanType theExpungeDeletedResources,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS) BooleanType theExpungeOldVersions,
		RequestDetails theRequest) {
		org.hl7.fhir.r4.model.Parameters retVal = doExpunge(null, theLimit, theExpungeDeletedResources, theExpungeOldVersions, null, theRequest);
		try {
			return VersionConvertor_30_40.convertParameters(retVal);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}

	default org.hl7.fhir.r4.model.Parameters doExpunge(IIdType theIdParam, IPrimitiveType<? extends Integer> theLimit, IPrimitiveType<? extends Boolean> theExpungeDeletedResources, IPrimitiveType<? extends Boolean> theExpungeOldVersions, IPrimitiveType<? extends Boolean> theExpungeEverything, RequestDetails theRequest) {

		ExpungeOptions options = createExpungeOptions(theLimit, theExpungeDeletedResources, theExpungeOldVersions, theExpungeEverything);

		ExpungeOutcome outcome;
		if (theIdParam != null) {
			outcome = getDao().expunge(theIdParam, options, theRequest);
		} else {
			outcome = getDao().expunge(options, theRequest);
		}

		return createExpungeResponse(outcome);
	}

	static ExpungeOptions createExpungeOptions(IPrimitiveType<? extends Integer> theLimit, IPrimitiveType<? extends Boolean> theExpungeDeletedResources, IPrimitiveType<? extends Boolean> theExpungeOldVersions, IPrimitiveType<? extends Boolean> theExpungeEverything) {
		ExpungeOptions options = new ExpungeOptions();
		if (theLimit != null && theLimit.getValue() != null) {
			options.setLimit(theLimit.getValue());
		}

		if (theExpungeOldVersions != null && theExpungeOldVersions.getValue() != null) {
			options.setExpungeOldVersions(theExpungeOldVersions.getValue());
		}

		if (theExpungeDeletedResources != null && theExpungeDeletedResources.getValue() != null) {
			options.setExpungeDeletedResources(theExpungeDeletedResources.getValue());
		}

		if (theExpungeEverything != null && theExpungeEverything.getValue() != null) {
			options.setExpungeEverything(theExpungeEverything.getValue());
		}
		return options;
	}

	static org.hl7.fhir.r4.model.Parameters createExpungeResponse(ExpungeOutcome theOutcome) {
		org.hl7.fhir.r4.model.Parameters retVal = new org.hl7.fhir.r4.model.Parameters();
		retVal
			.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT)
			.setValue(new org.hl7.fhir.r4.model.IntegerType(theOutcome.getDeletedCount()));
		return retVal;
	}
}
