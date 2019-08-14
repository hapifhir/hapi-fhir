package ca.uhn.fhir.jpa.provider.dstu3;

/*
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

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.http.HttpServletRequest;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_META;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_META_ADD;
import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_META_DELETE;

public class JpaResourceProviderDstu3<T extends IAnyResource> extends BaseJpaResourceProvider<T> {

	public JpaResourceProviderDstu3() {
		// nothing
	}

	public JpaResourceProviderDstu3(IFhirResourceDao<T> theDao) {
		super(theDao);
	}

	@Create
	public MethodOutcome create(HttpServletRequest theRequest, @ResourceParam T theResource, @ConditionalUrlParam String theConditional, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().create(theResource, theConditional, theRequestDetails);
			} else {
				return getDao().create(theResource, theRequestDetails);
			}
		} finally {
			endRequest(theRequest);
		}
	}

	@Delete()
	public MethodOutcome delete(HttpServletRequest theRequest, @IdParam IdType theResource, @ConditionalUrlParam(supportsMultiple = true) String theConditional, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().deleteByUrl(theConditional, theRequestDetails);
			} else {
				return getDao().delete(theResource, theRequestDetails);
			}
		} finally {
			endRequest(theRequest);
		}
	}

	@Operation(name = JpaConstants.OPERATION_EXPUNGE, idempotent = false, returnParameters = {
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT, type = IntegerType.class)
	})
	public Parameters expunge(
		@IdParam IIdType theIdParam,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT) IntegerType theLimit,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES) BooleanType theExpungeDeletedResources,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS) BooleanType theExpungeOldVersions,
		RequestDetails theRequest) {
		org.hl7.fhir.r4.model.Parameters retVal = super.doExpunge(theIdParam, theLimit, theExpungeDeletedResources, theExpungeOldVersions, null, theRequest);
		try {
			return VersionConvertor_30_40.convertParameters(retVal);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}

	@Operation(name = JpaConstants.OPERATION_EXPUNGE, idempotent = false, returnParameters = {
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT, type = IntegerType.class)
	})
	public Parameters expunge(
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT) IntegerType theLimit,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES) BooleanType theExpungeDeletedResources,
		@OperationParam(name = JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS) BooleanType theExpungeOldVersions,
		RequestDetails theRequest) {
		org.hl7.fhir.r4.model.Parameters retVal = super.doExpunge(null, theLimit, theExpungeDeletedResources, theExpungeOldVersions, null, theRequest);
		try {
			return VersionConvertor_30_40.convertParameters(retVal);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
	}

	@Operation(name = OPERATION_META, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	public Parameters meta(RequestDetails theRequestDetails) {
		Parameters parameters = new Parameters();
		Meta metaGetOperation = getDao().metaGetOperation(Meta.class, theRequestDetails);
		parameters.addParameter().setName("return").setValue(metaGetOperation);
		return parameters;
	}

	@Operation(name = OPERATION_META, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	public Parameters meta(@IdParam IdType theId, RequestDetails theRequestDetails) {
		Parameters parameters = new Parameters();
		Meta metaGetOperation = getDao().metaGetOperation(Meta.class, theId, theRequestDetails);
		parameters.addParameter().setName("return").setValue(metaGetOperation);
		return parameters;
	}

	@Operation(name = OPERATION_META_ADD, idempotent = true, returnParameters = {
		@OperationParam(name = "return", type = Meta.class)
	})
	public Parameters metaAdd(@IdParam IdType theId, @OperationParam(name = "meta") Meta theMeta, RequestDetails theRequestDetails) {
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
	public Parameters metaDelete(@IdParam IdType theId, @OperationParam(name = "meta") Meta theMeta, RequestDetails theRequestDetails) {
		if (theMeta == null) {
			throw new InvalidRequestException("Input contains no parameter with name 'meta'");
		}
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("return").setValue(getDao().metaDeleteOperation(theId, theMeta, theRequestDetails));
		return parameters;
	}

	@Update
	public MethodOutcome update(HttpServletRequest theRequest, @ResourceParam T theResource, @IdParam IdType theId, @ConditionalUrlParam String theConditional, RequestDetails theRequestDetails) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().update(theResource, theConditional, theRequestDetails);
			} else {
				return getDao().update(theResource, theRequestDetails);
			}
		} finally {
			endRequest(theRequest);
		}
	}

	@Validate
	public MethodOutcome validate(@ResourceParam T theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
											@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
		return validate(theResource, null, theRawResource, theEncoding, theMode, theProfile, theRequestDetails);
	}

	@Validate
	public MethodOutcome validate(@ResourceParam T theResource, @IdParam IdType theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
											@Validate.Profile String theProfile, RequestDetails theRequestDetails) {
		return getDao().validate(theResource, theId, theRawResource, theEncoding, theMode, theProfile, theRequestDetails);
	}

}
