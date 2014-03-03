package ca.uhn.fhir.tinder.model;

import java.util.List;

public class ResourceBlock extends Child {

	@Override
	public List<BaseElement> getChildren() {
		return super.getChildren();
	}

	public String getClassName() {
//		return getElementName().substring(0, 1).toUpperCase() + getElementName().substring(1);
		String name = getName();
		return convertFhirPathNameToClassName(name);
	}

	public static String convertFhirPathNameToClassName(String name) {
		StringBuilder b = new StringBuilder();
		boolean first=true;
		for (String next : name.split("\\.")) {
			if (first) {
				first=false;
				continue;
			}
			b.append(next.substring(0, 1).toUpperCase() + next.substring(1));
		}
		
		return b.toString();
	}


	@Override
	public String getSingleType() {
		return getClassName();
	}

	public boolean isBlock() {
		return true;
	}

	@Override
	public String getTypeSuffix() {
		return "";
	}

}
