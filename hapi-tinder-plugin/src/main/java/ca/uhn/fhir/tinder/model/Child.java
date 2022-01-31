package ca.uhn.fhir.tinder.model;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Child extends BaseElement {

	private List<SimpleSetter> mySimpleStters = new ArrayList<SimpleSetter>();

	@Override
	public void clearTypes() {
		super.clearTypes();
		mySimpleStters.clear();
	}

	public String getAnnotationType() {
		return getSingleType();
	}

	public String getBoundDatatype() {
		String singleType = getSingleType();
		if ("CodeDt".equals(singleType) || "CodeableConceptDt".equals(singleType)) {
			return "Bound" + singleType;
		}
		throw new IllegalStateException(Msg.code(188));
	}

	public String getCardMaxForChildAnnotation() {
		if (getCardMax().equals("*")) {
			return "Child.MAX_UNLIMITED";
		} else {
			return getCardMax();
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}

	/**
	 * Strips off "[x]"
	 */
	public String getElementNameSimplified() {
		String elementName = getElementName();
		if (elementName.endsWith("[x]")) {
			elementName = elementName.substring(0, elementName.length() - 3);
		}
		return elementName.trim();
	}

	public String getMethodName() {
		String elementName = getElementNameSimplified();
		elementName = elementName.substring(0, 1).toUpperCase() + elementName.substring(1);
		if ("Class".equals(elementName)) {
			elementName = "ClassElement";
		}
		// if ("Language".equals(elementName)) {
		// elementName = "LanguageElement";
		// }
		return elementName;
	}

	public String getReferenceType() {
		String retVal;
		if (this.isResourceRef()) {
			retVal = ResourceReferenceDt.class.getSimpleName();
		} else if (this.getType().size() == 1 || this instanceof ResourceBlock) {
			if (isBoundCode()) {
				retVal = "Bound" + getSingleType() + "<" + getBindingClass() + ">";
			} else {
				retVal = getSingleType();
			}
		} else {
			if (this instanceof Extension && ((Extension) this).getChildExtensions().size() > 0) {
				retVal = ((Extension) this).getNameType();
			} else {
				retVal = IDatatype.class.getSimpleName();
			}
		}

		if (this.isRepeatable()) {
			retVal = ("java.util.List<" + retVal + ">");
		}

		return retVal;
	}

	public String getReferenceTypeForConstructor() {
		return getReferenceType().replaceAll("^java.util.List<", "java.util.ArrayList<");
	}

	public List<String> getReferenceTypesForMultiple() {
		ArrayList<String> retVal = new ArrayList<String>();
		for (String next : getType()) {
			if ("Any".equals(next)) {
				next = "IResource";
			}
			retVal.add(Resource.correctName(next));
			// retVal.add(next + getTypeSuffix());
		}
		return retVal;
	}

	public List<SimpleSetter> getSimpleSetters() {
		if (isBoundCode()) {
			return Collections.emptyList();
		}
		return mySimpleStters;
	}

	public String getSingleType() {
		String retVal;
		String elemName = this.getType().get(0);
		if (elemName.startsWith("ca.") == false) {
			elemName = elemName.substring(0, 1).toUpperCase() + elemName.substring(1);
		}
		// if (this instanceof ResourceBlock) {
		retVal = (elemName);
		// } else {
		// retVal = (elemName + getTypeSuffix());
		// }
		
		if (retVal.equals("ResourceDt")) {
			retVal = "IResource";
		}
		
		return retVal;
	}

	public String getVariableName() {
		String elementName = getMethodName();
		return "my" + elementName;
	}

	public boolean isBlock() {
		return false;
	}

	public boolean isBlockRef() {
		return false;
	}

	public boolean isPrimitive (String theType) {
		return isPrimitiveInternal(theType);
	}

	public boolean isPrimitive() {
		
		if (IDatatype.class.getSimpleName().equals(getReferenceType())) {
			return false;
		}
		return isPrimitiveInternal(getSingleType());
	}

	protected boolean isPrimitiveInternal (String theType) {
		try {
			String name = "ca.uhn.fhir.model.primitive." + theType;
			Class.forName(name);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public String getPrimitiveType (String theType) throws ClassNotFoundException {
		return getPrimitiveTypeInternal(theType);
	}

	public String getPrimitiveType() throws ClassNotFoundException {
		return getPrimitiveTypeInternal(getSingleType());
	}

	protected String getPrimitiveTypeInternal (String theType) throws ClassNotFoundException {
		String name = "ca.uhn.fhir.model.primitive." + theType;
		Class<?> clazz = Class.forName(name);
		if (clazz.equals(IdDt.class)) {
			return String.class.getSimpleName();
		}
			
		while (!clazz.getSuperclass().equals(BasePrimitive.class)) {
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				throw new Error(Msg.code(189) + "Parent of " + name + " is not BasePrimitive");
			}
		}

		ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
		Type type2 = type.getActualTypeArguments()[0];
		if (type2 instanceof GenericArrayType) {
			String arrayType = ((GenericArrayType) type2).getGenericComponentType().toString();
			return arrayType + "[]";
		}
		Class<?> rawType = (Class<?>) type2;
		return rawType.getSimpleName();
	}

	public boolean isBoundCode() {
		String singleType = getSingleType();
		if ("CodeDt".equals(singleType) || "CodeableConceptDt".equals(singleType)) {
			if (StringUtils.isNotBlank(getBindingClass())) {
				return true;
			}
		}
		return false;
	}

	public boolean isRepeatable() {
		return "1".equals(getCardMax()) == false;
	}

	public boolean isSingleChildInstantiable() {
		return true;
	}

}
