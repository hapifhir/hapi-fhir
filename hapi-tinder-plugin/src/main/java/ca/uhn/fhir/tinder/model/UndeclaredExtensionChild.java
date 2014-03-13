package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;

import ca.uhn.fhir.model.api.UndeclaredExtension;

public class UndeclaredExtensionChild extends Child {

	@Override
	public String getReferenceType() {
		if (isRepeatable()) {
			return ArrayList.class.getCanonicalName() + "<" + UndeclaredExtension.class.getSimpleName()+">";
		}
		return UndeclaredExtension.class.getSimpleName();
	}

	@Override
	public String getAnnotationType() {
		return UndeclaredExtension.class.getSimpleName();
	}

	@Override
	public String getSingleType() {
		return UndeclaredExtension.class.getSimpleName();
	}

	@Override
	public String getTypeSuffix() {
		return "";
	}

	@Override
	public boolean isSingleChildInstantiable() {
		return true;
	}

	
}
