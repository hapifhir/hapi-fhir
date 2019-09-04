package ca.uhn.fhir.fluentpath;

import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;

public interface INarrativeConstantMap extends IBase {
	void addConstant(String name, List<IBase> values);
}
