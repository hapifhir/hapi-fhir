package ca.uhn.fhir.util;

import ca.uhn.fhir.model.api.annotation.SensitiveNoDisplay;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.lang.reflect.Field;

public class SensitiveDataFilter extends SimpleBeanPropertyFilter {

	@Override
	protected boolean include(PropertyWriter writer) {
		return true; // Default include all except explicitly checked and excluded
	}

	@Override
	public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider provider, PropertyWriter writer) throws Exception {
		if (include(writer)) {
			// Check if field is annotated with SensitiveNoDisplay
			if (!isFieldSensitive(writer.getName(), pojo)) {
				super.serializeAsField(pojo, gen, provider, writer);
			} // else do not serialize the field
		}
	}

	private boolean isFieldSensitive(String fieldName, Object pojo) {
		try {
			Field field = pojo.getClass().getDeclaredField(fieldName);
			return field.isAnnotationPresent(SensitiveNoDisplay.class);
		} catch (NoSuchFieldException e) {
			// Field not found, consider it not sensitive
			return false;
		}
	}
}
