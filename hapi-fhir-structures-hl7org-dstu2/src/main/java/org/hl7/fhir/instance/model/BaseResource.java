package org.hl7.fhir.instance.model;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;

public abstract class BaseResource extends Base implements IAnyResource {

	private static final long serialVersionUID = 1L;

	/**
     * @param value The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes.
     */
    public BaseResource setId(IIdType value) {
        if (value == null) {
                setIdElement((IdType)null);
        } else if (value instanceof IdType) {
                setIdElement((IdType) value);
        } else {
                setIdElement(new IdType(value.getValue()));
        }
        return this;
    }

	public abstract BaseResource setIdElement(IdType theIdType);
    
}
