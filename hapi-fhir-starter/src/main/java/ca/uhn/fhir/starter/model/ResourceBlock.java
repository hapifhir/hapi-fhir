package ca.uhn.fhir.starter.model;

import java.util.List;


public class ResourceBlock extends Child {

	@Override
	public List<BaseElement> getChildren() {
		return super.getChildren();
	}

	public String getClassName() {
		return getElementName().substring(0,1).toUpperCase() + getElementName().substring(1);
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
