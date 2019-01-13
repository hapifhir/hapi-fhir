package org.hl7.fhir.r4.model;

/*-
 * #%L
 * org.hl7.fhir.r4
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IIdType;

public abstract class BaseReference extends Type implements IBaseReference, ICompositeType {

    /**
     * This is not a part of the "wire format" resource, but can be changed/accessed by parsers
     */
    private transient IBaseResource resource;

	public BaseReference(String theReference) {
    	setReference(theReference);
	}

    public BaseReference(IIdType theReference) {
    	if (theReference != null) {
    		setReference(theReference.getValue());
    	} else {
    		setReference(null);
    	}
    }

	public BaseReference(IAnyResource theResource) {
		resource = theResource;
	}

	public BaseReference() {
	}

	/**
     * Retrieves the actual resource referenced by this reference. Note that the resource itself is not
     * a part of the FHIR "wire format" and is never transmitted or receieved inline, but this property
     * may be changed/accessed by parsers.
     */
    public IBaseResource getResource() {
        return resource;
    }

    @Override
	public IIdType getReferenceElement() {
		return new IdType(getReference());
	}

    abstract String getReference();

    /**
     * Sets the actual resource referenced by this reference. Note that the resource itself is not
     * a part of the FHIR "wire format" and is never transmitted or receieved inline, but this property
     * may be changed/accessed by parsers.
     */
    public void setResource(IBaseResource theResource) {
        resource = theResource;
    }

    @Override
	public boolean isEmpty() {
		return resource == null && super.isEmpty();
	}

}
