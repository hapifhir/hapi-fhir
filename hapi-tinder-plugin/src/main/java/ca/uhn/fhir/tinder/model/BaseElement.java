package ca.uhn.fhir.tinder.model;

import static org.apache.commons.lang.StringUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseElement {

	private String myBinding;
	private String myBindingClass;
	private String myCardMax;
	private String myCardMin;
	private Map<String, Slicing> myChildElementNameToSlicing = new HashMap<String, Slicing>();
	private List<BaseElement> myChildren;
	private String myComments;
	private String myDeclaringClassNameComplete;
	private String myDefinition;
	private String myElementName;
	private String myElementParentName;
	private String myExtensionUrl;
	private boolean myModifier;
	private String myName;
	private String myRequirement;
	private boolean myResourceRef = false;
	private String myShortName;
	private boolean mySummary;
	private List<String> myType;
	private String myV2Mapping;
	private String myBindingUrl;

	public void addChild(Child theElem) {
		if (myChildren == null) {
			myChildren = new ArrayList<BaseElement>();
		}
		myChildren.add(theElem);

		// if (theElem.getDeclaringClassNameComplete()==null) {
		theElem.setDeclaringClassNameComplete(getDeclaringClassNameCompleteForChildren());
		// }

		// clearTypes();
	}

	public void clearTypes() {
		getType().clear();
	}

	public String getBinding() {
		return myBinding;
	}

	public String getBindingClass() {
		return myBindingClass;
	}

	public String getCardMax() {
		return defaultString(myCardMax, "1");
	}

	public String getCardMin() {
		return defaultString(myCardMin, "0");
	}

	public Map<String, Slicing> getChildElementNameToSlicing() {
		return myChildElementNameToSlicing;
	}

	public List<BaseElement> getChildren() {
		if (myChildren == null) {
			myChildren = new ArrayList<BaseElement>();
		}
		return Collections.unmodifiableList(myChildren);
	}

	public String getComments() {
		return myComments;
	}

	public String getDeclaringClassNameComplete() {
		return myDeclaringClassNameComplete;
	}

	public String getDeclaringClassNameCompleteForChildren() {
		return getDeclaringClassNameComplete();
	}

	public String getDefinition() {
		return toStringConstant(myDefinition);
	}

	public String getElementName() {
		return myElementName;
	}

	public String getElementParentName() {
		return myElementParentName;
	}

	public String getExtensionUrl() {
		return myExtensionUrl;
	}

	public String getName() {
		return myName;
	}

	public String getRequirement() {
		return myRequirement;
	}

	public List<ResourceBlock> getResourceBlockChildren() {
		ArrayList<ResourceBlock> retVal = new ArrayList<ResourceBlock>();
		for (BaseElement next : getChildren()) {
			if (next instanceof ResourceBlock) {
				retVal.add((ResourceBlock) next);
			}
		}
		return retVal;
	}

	public String getShortName() {
		return toStringConstant(myShortName);
	}

	public List<String> getType() {
		if (myType == null) {
			myType = new ArrayList<String>();
		}
		return myType;
	}

	public String getV2Mapping() {
		return myV2Mapping;
	}

	public boolean isExtensionLocal() {
		return false; // TODO: implemment
	}

	public boolean isExtensionModifier() {
		return false; // TODO: implemment
	}

	public boolean isHasExtensionUrl() {
		return StringUtils.isNotBlank(myExtensionUrl);
	}

	public boolean isHasMultipleTypes() {
		return getType().size() > 1;
	}

	public boolean isModifier() {
		return myModifier;
	}

	public boolean isResourceRef() {
		return myResourceRef;
	}

	public boolean isSummary() {
		return mySummary;
	}

	public void setBinding(String theCellValue) {
		myBinding = theCellValue;
	}

	public void setBindingClass(String theBindingClass) {
		myBindingClass = theBindingClass;
	}

	public void setCardMax(String theCardMax) {
		myCardMax = theCardMax;
	}

	public void setCardMin(String theCardMin) {
		myCardMin = theCardMin;
	}

	public void setChildElementNameToSlicing(Map<String, Slicing> theChildElementNameToSlicing) {
		myChildElementNameToSlicing = theChildElementNameToSlicing;
	}

	public void setComments(String theComments) {
		myComments = theComments;
	}

	public void setDeclaringClassNameComplete(String theDeclaringClassNameComplete) {
		myDeclaringClassNameComplete = theDeclaringClassNameComplete.trim();
	}

	public void setDefinition(String theDefinition) {
		myDefinition = theDefinition != null ? theDefinition.trim() : null;
	}

	public void setElementName(String theName) {
		myElementName = theName.trim();
	}

	public void setElementNameAndDeriveParentElementName(String theName) {
		String name = theName.trim();
		int lastDot = name.lastIndexOf('.');
		if (lastDot == -1) {
			setElementName(name);
		} else {
			String elementName = name.substring(lastDot + 1);
			String elementParentName = name.substring(0, lastDot);
			setElementName(elementName);
			myElementParentName = (elementParentName);
		}
	}

	public void setExtensionUrl(String theExtensionUrl) {
		myExtensionUrl = theExtensionUrl.trim();
	}

	public void setModifier(String theModifier) {
		if (theModifier == null) {
			myModifier = false;
		} else {
			myModifier = "Y".equals(theModifier.toUpperCase());
		}
	}

	public void setName(String theName) {
		myName = theName.trim();
	}

	public void setRequirement(String theString) {
		myRequirement = theString;
	}

	public void setResourceRef(boolean theResourceRef) {
		myResourceRef = theResourceRef;
	}

	public void setShortName(String theShortName) {
		myShortName = theShortName;
	}

	public void setSummary(String theSummary) {
		if (theSummary == null) {
			mySummary = false;
		} else {
			mySummary = "Y".equals(theSummary.toUpperCase());
		}
	}

	public void setTypeFromString(String theType) {
		if (theType == null) {
			myType = null;
			return;
		}
		String typeString = theType;
		typeString = typeString.replace("Reference (", "Reference(");
		typeString = typeString.replace("Resource (", "Reference(");
		typeString = typeString.replace("Resource(", "Reference(").trim();

		// if (typeString.toLowerCase().startsWith("resource(")) {
		// typeString = typeString.substring("Resource(".length(), typeString.length());
		// myResourceRef = true;
		// } else if (typeString.toLowerCase().startsWith("reference(")) {
		// typeString = typeString.substring("Reference(".length(), typeString.length());
		// myResourceRef = true;
		// } else

		boolean datatype = true;
		if (typeString.startsWith("@")) {
			typeString = typeString.substring(1);
			typeString = ResourceBlock.convertFhirPathNameToClassName(typeString);
			datatype = false;
			// } else if (typeString.equals("Reference(Any)")) {
			// typeString = "Reference(IResource)";
			// datatype = false;
		} else if (typeString.equals("*")) {
			typeString = "IDatatype";
			datatype = false;
		}

		if (StringUtils.isNotBlank(typeString)) {

			int idx = typeString.indexOf("Reference(");
			if (idx != -1) {
				int endIdx = typeString.indexOf(")");
				typeString = typeString.substring(0, idx) + typeString.substring(idx, endIdx).replace("|", ",") + typeString.substring(endIdx);
			}

			if (idx == 0 && typeString.endsWith(")")) {
				myResourceRef = true;
			}

			if (typeString.startsWith("=")) {
				datatype = false;
			}

			String[] types = typeString.replace("=", "").split("\\|");
			for (String nextType : types) {
				nextType = nextType.trim();
				if (nextType.endsWith(")")) {
					nextType = nextType.substring(0, nextType.length() - 1);
				}
				if (nextType.toLowerCase().startsWith("resource(")) {
					nextType = nextType.substring("Resource(".length(), nextType.length());
					nextType = nextType.substring(0, 1).toUpperCase() + nextType.substring(1);
				} else if (nextType.toLowerCase().startsWith("reference(")) {
					nextType = nextType.substring("Reference(".length(), nextType.length());
					nextType = nextType.substring(0, 1).toUpperCase() + nextType.substring(1);
				} else {
					nextType = nextType.substring(0, 1).toUpperCase() + nextType.substring(1);
					if (datatype) {
						nextType = nextType + "Dt";
					}
				}

				for (String next : nextType.split(",")) {
					if (isNotBlank(next.trim())) {
						getType().add(next.trim());
					}
				}
			}
		}

	}

	public void setV2Mapping(String theV2Mapping) {
		myV2Mapping = theV2Mapping;
	}

	public static void main(String[] args) {
		SimpleChild child = new SimpleChild();
		child.setTypeFromString("CodeableConcept | Resource(Any)");
	}

	static String toStringConstant(String theDefinition) {
		if (theDefinition == null) {
			return "";
		}
		StringBuffer b = new StringBuffer();
		for (char next : theDefinition.toCharArray()) {
			if (next < ' ') {
				continue;
			}
			if (next == '"') {
				b.append('\\');
			}
			b.append(next);
		}
		return b.toString().trim();
	}

	public void setBindingUrl(String theBindingUrl) {
		myBindingUrl = theBindingUrl;
	}

	public boolean isHasBindingUrl() {
		return isNotBlank(myBindingUrl);
	}

	public String getBindingUrl() {
		return myBindingUrl;
	}

}
