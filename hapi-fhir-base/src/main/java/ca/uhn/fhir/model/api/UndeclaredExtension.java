package ca.uhn.fhir.model.api;

public class UndeclaredExtension extends BaseElement {

	private String myUrl;
	private IElement myValue;
	private boolean myIsModifier;

	public UndeclaredExtension() {
	}

	public UndeclaredExtension(boolean theIsModifier) {
		myIsModifier = theIsModifier;
	}

	public UndeclaredExtension(boolean theIsModifier, String theUrl) {
		myIsModifier = theIsModifier;
		myUrl = theUrl;
	}

	public boolean isModifier() {
		return myIsModifier;
	}

	public String getUrl() {
		return myUrl;
	}

	/**
	 * Returns the value of this extension, if one exists.
	 * <p>
	 * Note that if this extension contains extensions (instead of a datatype) then <b>this method will return null</b>. In that case, you must use {@link #getUndeclaredExtensions()} and
	 * {@link #getUndeclaredModifierExtensions()} to retrieve the child extensions.
	 * </p>
	 */
	public IElement getValue() {
		return myValue;
	}

	/**
	 * Returns the value of this extension, casted to a primitive datatype. This is a convenience method which should only be called if you are sure that the value for this particular extension will
	 * be a primitive.
	 * <p>
	 * Note that if this extension contains extensions (instead of a datatype) then <b>this method will return null</b>. In that case, you must use {@link #getUndeclaredExtensions()} and
	 * {@link #getUndeclaredModifierExtensions()} to retrieve the child extensions.
	 * </p>
	 * 
	 * @throws ClassCastException
	 *             If the value of this extension is not a primitive datatype
	 */
	public IPrimitiveDatatype<?> getValueAsPrimitive() {
		return (IPrimitiveDatatype<?>) getValue();
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public void setValue(IElement theValue) {
		myValue = theValue;
	}

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && myValue == null || myValue.isEmpty();
	}

}
