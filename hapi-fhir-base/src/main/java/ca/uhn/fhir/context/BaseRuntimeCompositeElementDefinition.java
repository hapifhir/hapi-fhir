package ca.uhn.fhir.context;

import java.util.ArrayList;

import ca.uhn.fhir.model.api.ICompositeElement;

public class BaseRuntimeCompositeElementDefinition<T extends ICompositeElement> extends BaseRuntimeElementDefinition<T> {

	private ArrayList<BaseRuntimeChildDefinition> myChildren = new ArrayList<BaseRuntimeChildDefinition>();

	public BaseRuntimeCompositeElementDefinition(String theName, Class<? extends T> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	public void addChild(BaseRuntimeChildDefinition theNext) {
		myChildren.add(theNext);
	}

}
