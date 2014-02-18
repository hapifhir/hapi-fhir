package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.parser.DataFormatException;

public class BaseRuntimeElementCompositeDefinition<T extends ICompositeElement> extends BaseRuntimeElementDefinition<T> {

	private ArrayList<BaseRuntimeChildDefinition> myChildren = new ArrayList<BaseRuntimeChildDefinition>();
	private Map<String, BaseRuntimeChildDefinition> myNameToChild=new HashMap<String, BaseRuntimeChildDefinition>();

	public BaseRuntimeElementCompositeDefinition(String theName, Class<? extends T> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	public void addChild(BaseRuntimeChildDefinition theNext) {
		myChildren.add(theNext);
		myNameToChild.put(theNext.getElementName(), theNext);
	}

	public BaseRuntimeChildDefinition getChildByNameOrThrowDataFormatException(String theName) throws DataFormatException {
		BaseRuntimeChildDefinition retVal = myNameToChild.get(theName);
		if (retVal==null) {
			throw new DataFormatException("Unknown child name: " + theName);
		}
		return retVal;
	}
}
