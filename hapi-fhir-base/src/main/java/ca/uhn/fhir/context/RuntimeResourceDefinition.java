package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IResource;

public class RuntimeResourceDefinition extends BaseRuntimeElementCompositeDefinition<IResource> {

	public RuntimeResourceDefinition(Class<? extends IResource> theClass, String theName) {
		super(theName, theClass);
	}

	public IResource newInstance() {
		try {
			return getImplementingClass().newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate type:"+getImplementingClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate type:"+getImplementingClass().getName(), e);
		}
	}

}
