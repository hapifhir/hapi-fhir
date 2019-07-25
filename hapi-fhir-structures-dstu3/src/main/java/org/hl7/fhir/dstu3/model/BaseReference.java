package org.hl7.fhir.dstu3.model;

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
