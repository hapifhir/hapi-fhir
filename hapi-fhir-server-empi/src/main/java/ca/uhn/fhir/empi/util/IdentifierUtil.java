package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.model.primitive.IdentifierDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;

public class IdentifierUtil {
	public static IdentifierDt identifierDtFromIdentifier(IBase theIdentifier) {
		IdentifierDt retval = new IdentifierDt();

		// TODO add other fields like "use" etc
		if (theIdentifier instanceof org.hl7.fhir.dstu3.model.Identifier) {
			org.hl7.fhir.dstu3.model.Identifier ident = (org.hl7.fhir.dstu3.model.Identifier) theIdentifier;
			retval.setSystem(ident.getSystem()).setValue(ident.getValue());
		} else if (theIdentifier instanceof org.hl7.fhir.r4.model.Identifier) {
			org.hl7.fhir.r4.model.Identifier ident = (org.hl7.fhir.r4.model.Identifier) theIdentifier;
			retval.setSystem(ident.getSystem()).setValue(ident.getValue());
		} else if (theIdentifier instanceof org.hl7.fhir.r5.model.Identifier) {
			org.hl7.fhir.r5.model.Identifier ident = (org.hl7.fhir.r5.model.Identifier) theIdentifier;
			retval.setSystem(ident.getSystem()).setValue(ident.getValue());
		} else {
			throw new InternalErrorException("Expected 'Identifier' type but was '" + theIdentifier.getClass().getName() + "'");
		}
		return retval;
	}
}
