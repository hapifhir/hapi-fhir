package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;

public class RuntimeChildChoiceDefinition extends BaseRuntimeDeclaredChildDefinition {

	private List<Class<? extends IElement>> myChoiceTypes;
	private Map<String, BaseRuntimeElementDefinition<?>> myNameToChildDefinition;
	private Map<Class<? extends IElement>, String> myDatatypeToElementName;
	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myDatatypeToElementDefinition;

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
		myDatatypeToElementName = new HashMap<Class<? extends IElement>, String>();
		myDatatypeToElementDefinition =new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();
		
		for (Class<? extends IElement> next : myChoiceTypes) {
			
			String elementName;
			BaseRuntimeElementDefinition<?> nextDef;
			if (IResource.class.isAssignableFrom(next)) {
				elementName = getElementName() + StringUtils.capitalize(next.getSimpleName());
				nextDef = new RuntimeResourceReferenceDefinition(elementName);
			} else {
				nextDef = theClassToElementDefinitions.get(next);
				elementName = getElementName() + StringUtils.capitalize(nextDef.getName());
			}
			
			myNameToChildDefinition.put(elementName, nextDef);
			myDatatypeToElementDefinition.put(next, nextDef);
			myDatatypeToElementName.put(next, elementName);
		}
		
		myNameToChildDefinition = Collections.unmodifiableMap(myNameToChildDefinition);
		myDatatypeToElementName=Collections.unmodifiableMap(myDatatypeToElementName);
		myDatatypeToElementDefinition=Collections.unmodifiableMap(myDatatypeToElementDefinition);
		
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		return myDatatypeToElementName.get(theDatatype);
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theDatatype) {
		return myDatatypeToElementDefinition.get(theDatatype);
	}


}
