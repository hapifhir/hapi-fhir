package ca.uhn.fhir.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.UndeclaredExtension;

public class RuntimeChildUndeclaredExtensionDefinition extends BaseRuntimeChildDefinition {

	
	private Map<String, BaseRuntimeElementDefinition<?>> myAttributeNameToDefinition;
	private Map<Class<? extends IElement>,String> myDatatypeToAttributeName;
	private Map<Class<? extends IElement>,BaseRuntimeElementDefinition<?>> myDatatypeToDefinition;

	public RuntimeChildUndeclaredExtensionDefinition() {
		// nothing
	}

	
	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		return myAttributeNameToDefinition.get(theName);
	}

	@Override
	public Set<String> getValidChildNames() {
		return myAttributeNameToDefinition.keySet();
	}

	@Override
	public String getChildNameByDatatype(Class<? extends IElement> theDatatype) {
		return myDatatypeToAttributeName.get(theDatatype);
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theType) {
		return myDatatypeToDefinition.get(theType);
	}

	@Override
	void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		Map<String, BaseRuntimeElementDefinition<?>> datatypeAttributeNameToDefinition=new HashMap<String, BaseRuntimeElementDefinition<?>>();
		
		for (BaseRuntimeElementDefinition<?> next : theClassToElementDefinitions.values()) {
			if (next instanceof RuntimePrimitiveDatatypeDefinition || next instanceof RuntimeCompositeDatatypeDefinition) {
				String attrName = "value" + next.getName().substring(0, 1).toUpperCase() + next.getName().substring(1);
				datatypeAttributeNameToDefinition.put(attrName, next);
			}
		}
		
		myAttributeNameToDefinition=datatypeAttributeNameToDefinition;
		
		myDatatypeToAttributeName = new HashMap<Class<? extends IElement>, String>();
		myDatatypeToDefinition = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();

		for (Entry<String, BaseRuntimeElementDefinition<?>> next : myAttributeNameToDefinition.entrySet()) {
			@SuppressWarnings("unchecked")
			Class<? extends IDatatype> type = (Class<? extends IDatatype>) next.getValue().getImplementingClass();
			myDatatypeToAttributeName.put(type, next.getKey());
			myDatatypeToDefinition.put(type, next.getValue());
		}
		
		// Resource Reference
		myDatatypeToAttributeName.put(ResourceReference.class, "valueReference");
		RuntimeResourceReferenceDefinition def = new RuntimeResourceReferenceDefinition("valueResource");
		myAttributeNameToDefinition.put("valueResource", def);
		myDatatypeToDefinition.put(ResourceReference.class, def);
	}


	@Override
	public IAccessor getAccessor() {
		return new IAccessor() {
			@Override
			public List<? extends IElement> getValues(Object theTarget) {
				UndeclaredExtension target = (UndeclaredExtension)theTarget;
				if (target.getValue() != null) {
					return Collections.singletonList(target.getValue());
				}
				return target.getUndeclaredExtensions();
			}};
	}


	@Override
	public IMutator getMutator() {
		return new IMutator() {
			@Override
			public void addValue(Object theTarget, IElement theValue) {
				UndeclaredExtension target = (UndeclaredExtension)theTarget;
				if (theValue instanceof IDatatype) {
					target.setValue((IDatatype) theTarget);
				}else {
					target.getUndeclaredExtensions().add((UndeclaredExtension) theValue);
				}
			}};
	}


}
