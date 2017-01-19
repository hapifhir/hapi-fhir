package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.lang.reflect.Method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.DeleteTags;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;

public class DeleteTagsMethodBinding extends BaseAddOrDeleteTagsMethodBinding {

	public DeleteTagsMethodBinding(Method theMethod, FhirContext theContext, Object theProvider, DeleteTags theDeleteTags) {
		super(theMethod, theContext, theProvider, theDeleteTags.type());
	}

	@Override
	protected boolean isDelete() {
		return true;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.DELETE_TAGS;
	}

}
