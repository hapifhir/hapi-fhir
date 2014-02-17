package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IElement;

public abstract class BaseRuntimeElementDefinition<T extends IElement> {

	private String myName;
	private Class<? extends T> myImplementingClass;

	public BaseRuntimeElementDefinition(String theName, Class<? extends T> theImplementingClass) {
		myName = theName;
		myImplementingClass=theImplementingClass;
	}

	/**
	 * @return Returns the runtime name for this resource (i.e. the name that
	 * will be used in encoded messages)
	 */
	public String getName() {
		return myName;
	}

	public Class<? extends T> getImplementingClass() {
		return myImplementingClass;
	}

}
