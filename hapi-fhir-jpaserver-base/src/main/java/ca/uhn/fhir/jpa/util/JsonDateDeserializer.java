package ca.uhn.fhir.jpa.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.hl7.fhir.dstu3.model.DateTimeType;

import java.io.IOException;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JsonDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser theParser, DeserializationContext theDeserializationContext) throws IOException {
		String string = theParser.getValueAsString();
		if (isNotBlank(string)) {
			return new DateTimeType(string).getValue();
		}
		return null;
	}

}
