package ca.uhn.fhir.context;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.IElement;

public abstract class BaseRuntimeElementDefinition<T extends IElement> {

	private String myName;
	private Class<? extends T> myImplementingClass;

	public BaseRuntimeElementDefinition(String theName, Class<? extends T> theImplementingClass) {
		assert StringUtils.isNotBlank(theName);
		assert theImplementingClass != null;
		
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

	public T newInstance() {
		try {
			return getImplementingClass().newInstance();
		} catch (InstantiationException e) {
			throw new ConfigurationException("Failed to instantiate type:"+getImplementingClass().getName(), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Failed to instantiate type:"+getImplementingClass().getName(), e);
		}
	}

	public Class<? extends T> getImplementingClass() {
		return myImplementingClass;
	}

	/**
	 * Invoked prior to use to perform any initialization and make object mutable 
	 */
	abstract void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions);

	public abstract ChildTypeEnum getChildType();
	
	public enum ChildTypeEnum {
		COMPOSITE_DATATYPE, PRIMITIVE_DATATYPE, RESOURCE, RESOURCE_REF, RESOURCE_BLOCK, PRIMITIVE_XHTML
		
	}
	
}
