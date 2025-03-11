package ca.uhn.fhir.tinder.model;

import ca.uhn.fhir.model.api.ExtensionDt;

import java.util.ArrayList;

public class UndeclaredExtensionChild extends Child {

	@Override
	public String getReferenceType() {
		if (isRepeatable()) {
			return ArrayList.class.getCanonicalName() + "<" + ExtensionDt.class.getSimpleName() + ">";
		}
		return ExtensionDt.class.getSimpleName();
	}

	@Override
	public String getAnnotationType() {
		return ExtensionDt.class.getSimpleName();
	}

	@Override
	public String getSingleType() {
		return ExtensionDt.class.getSimpleName();
	}

	@Override
	public boolean isSingleChildInstantiable() {
		return true;
	}
}
