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
	private List<RuntimeChildDeclaredExtensionDefinition> myExtensions = new ArrayList<RuntimeChildDeclaredExtensionDefinition>();
	private Map<String, BaseRuntimeChildDefinition> myNameToChild = new HashMap<String, BaseRuntimeChildDefinition>();

	private Map<String, RuntimeChildDeclaredExtensionDefinition> myUrlToExtension = new HashMap<String, RuntimeChildDeclaredExtensionDefinition>();

	public BaseRuntimeElementCompositeDefinition(String theName, Class<? extends T> theImplementingClass) {
		super(theName, theImplementingClass);
	}

	public void addChild(BaseRuntimeChildDefinition theNext) {
		if (theNext == null) {
			throw new NullPointerException();
		}
		if (theNext.getExtensionUrl() != null) {
			throw new IllegalArgumentException("Shouldn't haven an extension URL, use addExtension instead");
		}
		myChildren.add(theNext);
	}

	public void addExtension(RuntimeChildDeclaredExtensionDefinition theExtension) {
		if (theExtension== null) {
			throw new NullPointerException();
		}
		myExtensions.add(theExtension);
	}

	public List<RuntimeChildDeclaredExtensionDefinition> getExtensions() {
		return myExtensions;
	}

	public BaseRuntimeChildDefinition getChildByNameOrThrowDataFormatException(String theName) throws DataFormatException {
		BaseRuntimeChildDefinition retVal = myNameToChild.get(theName);
		if (retVal == null) {
			throw new DataFormatException("Unknown child name '" + theName + "' in element " + getName());
		}
		return retVal;
	}

	public List<BaseRuntimeChildDefinition> getChildren() {
		return myChildren;
	}

	/**
	 * Returns null if none
	 */
	public RuntimeChildDeclaredExtensionDefinition getDeclaredExtension(String theExtensionUrl) {
		return myUrlToExtension.get(theExtensionUrl);
	}

	@Override
	public void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions) {
		for (BaseRuntimeChildDefinition next : myChildren) {
			next.sealAndInitialize(theClassToElementDefinitions);
		}
		for (BaseRuntimeChildDefinition next : myExtensions) {
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

		for (RuntimeChildDeclaredExtensionDefinition next : myExtensions) {
			String extUrl = next.getExtensionUrl();
			if (myUrlToExtension.containsKey(extUrl)) {
				throw new ConfigurationException("Duplicate extension URL: " + extUrl);
			} else {
				myUrlToExtension.put(extUrl, next);
			}
		}

		myChildren = Collections.unmodifiableList(myChildren);
		myNameToChild = Collections.unmodifiableMap(myNameToChild);
		myExtensions = Collections.unmodifiableList(myExtensions);
	}
}
