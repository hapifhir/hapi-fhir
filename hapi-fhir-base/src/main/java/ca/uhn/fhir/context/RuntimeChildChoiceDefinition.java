package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;

public class RuntimeChildChoiceDefinition extends BaseRuntimeChildDefinition {

	private List<Class<? extends IElement>> myChoiceTypes;
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToChildDefinition;

	public RuntimeChildChoiceDefinition(Field theField, String theElementName, int theMin, int theMax, List<Class<? extends IElement>> theChoiceTypes) {
		super(theField, theMin,theMax, theElementName);
		
		myChoiceTypes= Collections.unmodifiableList(theChoiceTypes);
	}

	public List<Class<? extends IElement>> getChoices() {
		return myChoiceTypes;
	}

	@Override
	public Set<String> getValidChildNames() {
		return myNameToChildDefinition.keySet();
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		assert myNameToChildDefinition.containsKey(theName);
		
		return myNameToChildDefinition.get(theName);
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		myNameToChildDefinition = new HashMap<String, BaseRuntimeElementDefinition<?>>();
		for (Class<? extends IElement> next : myChoiceTypes) {
			BaseRuntimeElementDefinition<?> nextDef = theClassToElementDefinitions.get(next);
			myNameToChildDefinition.put(getElementName() + nextDef.getName(), nextDef);
		}
		myNameToChildDefinition = Collections.unmodifiableMap(myNameToChildDefinition);
	}


}
