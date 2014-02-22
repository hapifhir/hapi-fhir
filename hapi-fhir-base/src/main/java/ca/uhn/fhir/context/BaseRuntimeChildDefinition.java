package ca.uhn.fhir.context;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IElement;

public abstract class BaseRuntimeChildDefinition {

	public abstract IAccessor getAccessor();

	public abstract BaseRuntimeElementDefinition<?> getChildByName(String theName);

	public abstract BaseRuntimeElementDefinition<?> getChildElementDefinitionByDatatype(Class<? extends IElement> theType);

	public abstract String getChildNameByDatatype(Class<? extends IElement> theDatatype);

	public abstract IMutator getMutator();

	public abstract Set<String> getValidChildNames();

	abstract void sealAndInitialize(Map<Class<? extends IElement>, BaseRuntimeElementDefinition<?>> theClassToElementDefinitions);

	public interface IAccessor {
		List<? extends IElement> getValues(Object theTarget);
	}

	public interface IMutator {
		void addValue(Object theTarget, IElement theValue);
	}
}
