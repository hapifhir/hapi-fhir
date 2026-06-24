package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

/**
 * A single property (element) of a generated TypeScript interface.
 */
public class TsProperty {

	private final String myName;
	private final String myTypeName;
	private final TsTypeKind myKind;
	private final boolean myArray;
	private final boolean myOptional;

	public TsProperty(String theName, String theTypeName, TsTypeKind theKind, boolean theArray, boolean theOptional) {
		myName = theName;
		myTypeName = theTypeName;
		myKind = theKind;
		myArray = theArray;
		myOptional = theOptional;
	}

	public String getName() {
		return myName;
	}

	public String getTypeName() {
		return myTypeName;
	}

	public TsTypeKind getKind() {
		return myKind;
	}

	public boolean isArray() {
		return myArray;
	}

	public boolean isOptional() {
		return myOptional;
	}

	/**
	 * The bare TypeScript type reference for this property, without array wrapping. Interface references
	 * are prefixed with "I"; primitives and enums are rendered verbatim.
	 */
	public String getReferencedType() {
		if (myKind == TsTypeKind.INTERFACE) {
			return "I" + myTypeName;
		}
		return myTypeName;
	}

	/**
	 * The fully rendered TypeScript type for this property, including {@code Array<>} wrapping when the
	 * element has a cardinality greater than one.
	 */
	public String getRenderedType() {
		String type = getReferencedType();
		if (myArray) {
			return "Array<" + type + ">";
		}
		return type;
	}

	/**
	 * The complete TypeScript property declaration, e.g. {@code "gender?: AdministrativeGender"}.
	 */
	public String getDeclaration() {
		return myName + (myOptional ? "?" : "") + ": " + getRenderedType();
	}
}
