package ca.uhn.fhir.model.api;

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.io.Writer;

public interface IStreamingDatatype<T> extends IPrimitiveType<T> {

	void writeAsText(Writer theWriter) throws IOException;

}
