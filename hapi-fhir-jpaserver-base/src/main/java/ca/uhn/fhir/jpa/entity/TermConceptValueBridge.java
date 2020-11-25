package ca.uhn.fhir.jpa.entity;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class TermConceptValueBridge implements ValueBridge<TermConceptProperty, String> {

	@Override
	public String toIndexedValue(TermConceptProperty theTermConceptProperty, ValueBridgeToIndexedValueContext theValueBridgeToIndexedValueContext) {
		return theTermConceptProperty.getValue();
	}

}
