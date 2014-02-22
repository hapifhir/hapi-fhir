package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.ICompositeElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseRuntimeElementCompositeDefinition<T extends ICompositeElement> extends BaseRuntimeElementDefinition<T> {

	private List<BaseRuntimeChildDefinition> myChildren = new ArrayList<BaseRuntimeChildDefinition>();
	private Map<String, BaseRuntimeChildDefinition> myNameToChild = new HashMap<String, BaseRuntimeChildDefinition>();

	public BaseRuntimeElementCompositeDefinition(String theName, Class<? extends T> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	public void addChild(BaseRuntimeChildDefinition theNext) {
		myChildren.add(theNext);
	}

	public BaseRuntimeChildDefinition getChildByNameOrThrowDataFormatException(String theName) throws DataFormatException {
		BaseRuntimeChildDefinition retVal = myNameToChild.get(theName);
		if (retVal == null) {
			throw new DataFormatException("Unknown child name: " + theName);
		}
		return retVal;
	}

	@Override
	public
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		for (BaseRuntimeChildDefinition next : myChildren) {
			next.sealAndInitialize(theClassToElementDefinitions);
		}

		myNameToChild = new HashMap<String, BaseRuntimeChildDefinition>();
		for (BaseRuntimeChildDefinition next : myChildren) {
			for (String nextName : next.getValidChildNames()) {
				if (myNameToChild.containsKey(nextName)) {
					throw new ConfigurationException("Duplicate child name: " + nextName);
				} else {
					myNameToChild.put(nextName, next);
				}
			}
		}
		
		myChildren = Collections.unmodifiableList(myChildren);
		myNameToChild = Collections.unmodifiableMap(myNameToChild);
	}

	public List<BaseRuntimeChildDefinition> getChildren() {
		return myChildren;
	}
}
