package ca.uhn.fhir.model.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.primitive.IdDt;

public abstract class BaseElement implements IIdentifiableElement, ISupportsUndeclaredExtensions {

	private IdDt myId;
	private List<UndeclaredExtension> myUndeclaredExtensions;
	private List<UndeclaredExtension> myUndeclaredModifierExtensions;

	@Override
	public List<UndeclaredExtension> getAllUndeclaredExtensions() {
		ArrayList<UndeclaredExtension> retVal = new ArrayList<UndeclaredExtension>();
		if (myUndeclaredExtensions != null) {
			retVal.addAll(myUndeclaredExtensions);
		}
		if (myUndeclaredModifierExtensions != null) {
			retVal.addAll(myUndeclaredModifierExtensions);
		}
		return Collections.unmodifiableList(retVal);
	}

	@Override
	public IdDt getId() {
		if (myId == null) {
			myId = new IdDt();
		}
		return myId;
	}

	@Override
	public List<UndeclaredExtension> getUndeclaredExtensions() {
		if (myUndeclaredExtensions == null) {
			myUndeclaredExtensions = new ArrayList<UndeclaredExtension>();
		}
		return myUndeclaredExtensions;
	}

	@Override
	public List<UndeclaredExtension> getUndeclaredModifierExtensions() {
		if (myUndeclaredModifierExtensions == null) {
			myUndeclaredModifierExtensions = new ArrayList<UndeclaredExtension>();
		}
		return myUndeclaredModifierExtensions;
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
		if (myUndeclaredModifierExtensions != null) {
			for (UndeclaredExtension next : myUndeclaredModifierExtensions) {
				if (!next.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void setId(IdDt theId) {
		myId = theId;
	}

}
