package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;

import ca.uhn.fhir.model.api.BaseResourceReference;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;

public class RuntimeChildUndeclaredExtensionDefinition extends BaseRuntimeChildDefinition {

	private Map<String, BaseRuntimeElementDefinition<?>> myAttributeNameToDefinition;
	private Map<Class<? extends IElement>, String> myDatatypeToAttributeName;
	private Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> myDatatypeToDefinition;

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
		Map<String, BaseRuntimeElementDefinition<?>> datatypeAttributeNameToDefinition = new HashMap<String, BaseRuntimeElementDefinition<?>>();
		myDatatypeToAttributeName = new HashMap<Class<? extends IElement>, String>();

		for (BaseRuntimeElementDefinition<?> next : theClassToElementDefinitions.values()) {
			if (next instanceof IRuntimeDatatypeDefinition) {
				if (!((IRuntimeDatatypeDefinition) next).isSpecialization()) {
					String attrName = "value" + WordUtils.capitalize(next.getName());
					datatypeAttributeNameToDefinition.put(attrName, next);
					datatypeAttributeNameToDefinition.put(attrName.toLowerCase(), next);
					myDatatypeToAttributeName.put(next.getImplementingClass(), attrName);
				}
			}
		}

		myAttributeNameToDefinition = datatypeAttributeNameToDefinition;

		myDatatypeToDefinition = new HashMap<Class<? extends IElement>, BaseRuntimeElementDefinition<?>>();

		for (Entry<String, BaseRuntimeElementDefinition<?>> next : myAttributeNameToDefinition.entrySet()) {
			@SuppressWarnings("unchecked")
			Class<? extends IDatatype> type = (Class<? extends IDatatype>) next.getValue().getImplementingClass();
			myDatatypeToDefinition.put(type, next.getValue());
		}

		// Resource Reference
		myDatatypeToAttributeName.put(ResourceReferenceDt.class, "valueReference");
		List<Class<? extends IResource>> types = new ArrayList<Class<? extends IResource>>();
		types.add(IResource.class);
		RuntimeResourceReferenceDefinition def = new RuntimeResourceReferenceDefinition("valueResource", types);
		def.sealAndInitialize(theClassToElementDefinitions);
		myAttributeNameToDefinition.put("valueResource", def);
		myDatatypeToDefinition.put(BaseResourceReference.class, def);
		myDatatypeToDefinition.put(ResourceReferenceDt.class, def);
	}

	@Override
	public IAccessor getAccessor() {
		return new IAccessor() {
			@Override
			public List<? extends IElement> getValues(Object theTarget) {
				ExtensionDt target = (ExtensionDt) theTarget;
				if (target.getValue() != null) {
					return Collections.singletonList(target.getValue());
				}
				return target.getUndeclaredExtensions();
			}
		};
	}

	@Override
	public IMutator getMutator() {
		return new IMutator() {
			@Override
			public void addValue(Object theTarget, IElement theValue) {
				ExtensionDt target = (ExtensionDt) theTarget;
				if (theValue instanceof IDatatype) {
					target.setValue((IDatatype) theTarget);
				} else {
					target.getUndeclaredExtensions().add((ExtensionDt) theValue);
				}
			}
		};
	}

	@Override
	public int getMax() {
		return 1;
	}

	@Override
	public int getMin() {
		return 0;
	}

}
