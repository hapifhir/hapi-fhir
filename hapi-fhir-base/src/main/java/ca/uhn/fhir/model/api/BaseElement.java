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

	/**
	 * Intended to be called by extending classes {@link #isEmpty()} implementations, returns <code>true</code> if all content in this superclass instance is empty per the semantics of
	 * {@link #isEmpty()}.
	 */
	protected boolean isBaseEmpty() {
		if (myUndeclaredExtensions != null) {
			for (UndeclaredExtension next : myUndeclaredExtensions) {
				if (!next.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

}
