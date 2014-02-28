package ca.uhn.fhir.model.api;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseElement implements IElement, ISupportsUndeclaredExtensions {

	private List<UndeclaredExtension> myUndeclaredExtensions;

	@Override
	public List<UndeclaredExtension> getUndeclaredExtensions() {
		if (myUndeclaredExtensions == null) {
			myUndeclaredExtensions = new ArrayList<UndeclaredExtension>();
		}
		return myUndeclaredExtensions;
	}

}
