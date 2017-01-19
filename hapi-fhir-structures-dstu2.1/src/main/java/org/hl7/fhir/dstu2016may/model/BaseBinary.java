package org.hl7.fhir.dstu2016may.model;

import org.hl7.fhir.instance.model.api.IBaseBinary;

public abstract class BaseBinary extends Resource implements IBaseBinary {

	private static final long serialVersionUID = 1L;

	@Override
	public String getContentAsBase64() {
		return getContentElement().getValueAsString();
	}

	@Override
	public BaseBinary setContentAsBase64(String theContent) {
		if (theContent != null) {
			getContentElement().setValueAsString(theContent);
		} else {
			setContent(null);
		}
		return this;
	}
	
	abstract Base64BinaryType getContentElement();
	
}
