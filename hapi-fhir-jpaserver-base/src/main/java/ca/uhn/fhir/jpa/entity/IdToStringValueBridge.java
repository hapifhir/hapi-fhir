package ca.uhn.fhir.jpa.entity;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class IdToStringValueBridge implements ValueBridge<Long, String> {

	@Override
	public Long fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
		return Long.parseLong(value);
	}

	//TODO GGG HS think this is what we need to do here. By default HS is indexing this field as a numeric field, which you can't do a term
	//search on .
	@Override
	public String toIndexedValue(Long theLong, ValueBridgeToIndexedValueContext theValueBridgeToIndexedValueContext) {
		if (theLong != null) {
			return theLong.toString();
		} else {
			return "NONE";
		}
	}


}
