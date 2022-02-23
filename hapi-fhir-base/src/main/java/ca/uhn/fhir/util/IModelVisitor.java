package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;

/**
 * @see FhirTerser#visit(IBaseResource, IModelVisitor)
 */
public interface IModelVisitor {

	/**
	 * @param theResource
	 *           The resource being visited (note that generally this will be the resource upon which
	 *           visit was originally called, but in the case of descending into contained resources this will be the contained resource instead)
	 * @param theElement
	 *           The element
	 * @param theChildDefinition
	 *           May be null if this is a root element
	 * @param theDefinition
	 */
	void acceptElement(IBaseResource theResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition);

}
