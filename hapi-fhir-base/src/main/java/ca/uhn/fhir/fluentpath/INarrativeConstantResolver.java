package ca.uhn.fhir.fluentpath;

import org.hl7.fhir.instance.model.api.IBase;

public interface INarrativeConstantResolver {
	IBase resolveConstant(Object theAppContext, String theName, boolean theBeforeContext);
}
