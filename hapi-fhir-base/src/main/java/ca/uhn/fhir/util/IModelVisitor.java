package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;

/**
 * @see FhirTerser#visit(ca.uhn.fhir.model.api.IResource, IModelVisitor)
 */
public interface IModelVisitor {

	/**
	 * 
	 * @param theElement
	 * @param theChildDefinition May be null if this is a root element
	 * @param theDefinition
	 */
	void acceptElement(IElement theElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition);

	/**
	 * 
	 * @param theContainingElement
	 * @param theChildDefinition May be null if this is a root element
	 * @param theDefinition
	 * @param theNextExt
	 */
	void acceptUndeclaredExtension(ISupportsUndeclaredExtensions theContainingElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition, ExtensionDt theNextExt);

	
	
}
