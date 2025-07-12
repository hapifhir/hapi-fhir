package ca.uhn.fhir.util.bundle;

import org.hl7.fhir.instance.model.api.IBase;

public interface PartsConverter<T> {
	T fromElement(IBase theElement);

	IBase toElement(T theParts);
}
