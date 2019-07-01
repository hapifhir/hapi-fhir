package org.hl7.fhir.dstu3.model;

import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public abstract class BaseExtension extends Type implements IBaseExtension<Extension, Type>, IBaseHasExtensions {

	private static final long serialVersionUID = 1L;


	@Override
	public Extension setValue(IBaseDatatype theValue) {
		setValue((Type)theValue);
		return (Extension) this;
	}
	
	public abstract Extension setValue(Type theValue);
	
	
	/**
	 * Returns the value of this extension cast as a {@link IPrimitiveType}. This method is just a convenience method for easy chaining.
	 */
	public IPrimitiveType<?> getValueAsPrimitive() {
		return (IPrimitiveType<?>)getValue();
	}

	
}
