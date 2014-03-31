package ca.uhn.fhir.model.api;

import org.thymeleaf.util.Validate;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.primitive.StringDt;

@DatatypeDef(name="Extension") 
public class ExtensionDt extends BaseElement {

	private boolean myModifier;
	
	@Child(name="use", type=StringDt.class, order=0, min=1, max=1)	
	private StringDt myUrl;

	@Child(name="value", type=IDatatype.class, order=1, min=0, max=1)	
	private IElement myValue;
	
	public ExtensionDt() {
	}

	public ExtensionDt(boolean theIsModifier) {
		myModifier = theIsModifier;
	}

	public ExtensionDt(boolean theIsModifier, String theUrl) {
		Validate.notEmpty(theUrl, "URL must be populated");

		myModifier = theIsModifier;
		myUrl = new StringDt(theUrl);
	}

	public ExtensionDt(boolean theIsModifier, String theUrl, IDatatype theValue) {
		Validate.notEmpty(theUrl, "URL must be populated");
		Validate.notNull(theValue, "Value must not be null");

		myModifier = theIsModifier;
		myUrl = new StringDt(theUrl);
		myValue=theValue;
	}

	public StringDt getUrl() {
		if (myUrl==null) {
			myUrl=new StringDt();
		}
		return myUrl;
	}

	public String getUrlAsString() {
		return myUrl.getValue();
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
	
	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && myValue == null || myValue.isEmpty();
	}

	public boolean isModifier() {
		return myModifier;
	}

	public void setModifier(boolean theModifier) {
		myModifier = theModifier;
	}

	public void setUrl(String theUrl) {
		myUrl = new StringDt(theUrl);
	}

	public void setUrl(StringDt theUrl) {
		myUrl = theUrl;
	}

	public void setValue(IElement theValue) {
		myValue = theValue;
	}

}
