package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.util.ParametersUtil;

public abstract class BaseJpaSystemProviderDstu2Plus<T, MT> extends BaseJpaSystemProvider<T, MT> {

	//@formatter:off
	@Operation(name=MARK_ALL_RESOURCES_FOR_REINDEXING, idempotent=true, returnParameters= {
		@OperationParam(name="status")
	})
	//@formatter:on
	public IBaseResource markAllResourcesForReindexing() {
		int count = getDao().markAllResourcesForReindexing();
		
		IBaseParameters retVal = ParametersUtil.newInstance(getContext());
		
		IPrimitiveType<?> string = ParametersUtil.createString(getContext(), "Marked " + count + " resources");
		ParametersUtil.addParameterToParameters(getContext(), retVal, string, "status");
		
		return retVal;
	}

	
}
