package ca.uhn.fhir.jpa.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hl7.fhir.dstu3.model.InstantType;

import java.io.IOException;
import java.util.Date;

public class JsonDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date theValue, JsonGenerator theGen, SerializerProvider theSerializers) throws IOException {
		if (theValue != null) {
			theGen.writeString(new InstantType(theValue).getValueAsString());
		}
	}

}
