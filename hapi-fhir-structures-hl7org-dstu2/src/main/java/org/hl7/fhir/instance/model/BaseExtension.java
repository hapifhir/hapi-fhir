package org.hl7.fhir.instance.model;

import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

public abstract class BaseExtension extends Type implements IBaseExtension<Extension, Type>, IBaseHasExtensions {

	@Override
	public Extension setValue(IBaseDatatype theValue) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
