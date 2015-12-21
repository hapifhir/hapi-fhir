package ca.uhn.fhir.provider.impl;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.cobertura.CoverageIgnore;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ResourceProviderDstu2<T extends IResource> extends BaseResourceProvider<T> {

	public static final String OPERATION_NAME_META = "$meta";
	public static final String OPERATION_NAME_META_DELETE = "$meta-delete";
	public static final String OPERATION_NAME_META_ADD = "$meta-add";

	public ResourceProviderDstu2() {
		// nothing
	}

	@CoverageIgnore
	public ResourceProviderDstu2(IDaoFactory theDaoFactory) {
		super(theDaoFactory);
	}

	@Create
	public MethodOutcome create(HttpServletRequest theRequest, @ResourceParam T theResource, @ConditionalUrlParam String theConditional) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().create(theResource, theConditional);
			} else {
				return getDao().create(theResource);
			}
		} finally {
			endRequest(theRequest);
		}
	}

	@Delete()
	public MethodOutcome delete(HttpServletRequest theRequest, @IdParam IdDt theResource, @ConditionalUrlParam(supportsMultiple=true) String theConditional) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().deleteByUrl(theConditional);
			} else {
				return getDao().delete(theResource);
			}
		} finally {
			endRequest(theRequest);
		}
	}

	//@formatter:off
	@Operation(name=OPERATION_NAME_META, idempotent=true, returnParameters= {
		@OperationParam(name="return", type=MetaDt.class)
	})
	//@formatter:on
	public Parameters meta() {
		Parameters parameters = new Parameters();
		MetaDt metaGetOperation = getDao().metaGetOperation(MetaDt.class);
		parameters.addParameter().setName("return").setValue(metaGetOperation);
		return parameters;
	}

	//@formatter:off
	@Operation(name=OPERATION_NAME_META, idempotent=true, returnParameters= {
		@OperationParam(name="return", type=MetaDt.class)
	})
	//@formatter:on
	public Parameters meta(@IdParam IdDt theId) {
		Parameters parameters = new Parameters();
		MetaDt metaGetOperation = getDao().metaGetOperation(MetaDt.class, theId);
		parameters.addParameter().setName("return").setValue(metaGetOperation);
		return parameters;
	}

	//@formatter:off
	@Operation(name=OPERATION_NAME_META_ADD, idempotent=true, returnParameters= {
		@OperationParam(name="return", type=MetaDt.class)
	})
	//@formatter:on
	public Parameters metaAdd(@IdParam IdDt theId, @OperationParam(name = "meta") MetaDt theMeta) {
		if (theMeta == null) {
			throw new InvalidRequestException("Input contains no parameter with name 'meta'");
		}
		Parameters parameters = new Parameters();
		MetaDt metaAddOperation = getDao().metaAddOperation(theId, theMeta);
		parameters.addParameter().setName("return").setValue(metaAddOperation);
		return parameters;
	}

	//@formatter:off
	@Operation(name=OPERATION_NAME_META_DELETE, idempotent=true, returnParameters= {
		@OperationParam(name="return", type=MetaDt.class)
	})
	//@formatter:on
	public Parameters metaDelete(@IdParam IdDt theId, @OperationParam(name = "meta") MetaDt theMeta) {
		if (theMeta == null) {
			throw new InvalidRequestException("Input contains no parameter with name 'meta'");
		}
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("return").setValue(getDao().metaDeleteOperation(theId, theMeta));
		return parameters;
	}

	@Update
	public MethodOutcome update(HttpServletRequest theRequest, @ResourceParam T theResource, @IdParam IdDt theId, @ConditionalUrlParam String theConditional) {
		startRequest(theRequest);
		try {
			if (theConditional != null) {
				return getDao().update(theResource, theConditional);
			} else {
				theResource.setId(theId);
				return getDao().update(theResource);
			}
		} finally {
			endRequest(theRequest);
		}
	}

	@Validate
	public MethodOutcome validate(@ResourceParam T theResource, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
			@Validate.Profile String theProfile) {
		return validate(theResource, null, theRawResource, theEncoding, theMode, theProfile);
	}
		
	@Validate
	public MethodOutcome validate(@ResourceParam T theResource, @IdParam IdDt theId, @ResourceParam String theRawResource, @ResourceParam EncodingEnum theEncoding, @Validate.Mode ValidationModeEnum theMode,
			@Validate.Profile String theProfile) {
		return getDao().validate(theResource, theId, theRawResource, theEncoding, theMode, theProfile);
	}

}
