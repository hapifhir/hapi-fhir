package ca.uhn.fhir.jpa.demo;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.FhirContext;

public class ContextHolder {

	private static FhirContext ourCtx;
	private static String ourPath;

	public static FhirContext getCtx() {
		Validate.notNull(ourPath, "Context not set");
		return ourCtx;
	}

	public static void setCtx(FhirContext theCtx) throws ParseException {
		switch (theCtx.getVersion().getVersion()) {
		case DSTU2:
			ourPath = "/baseDstu2/";
			break;
		case DSTU3:
			ourPath = "/baseDstu3/";
			break;
		default:
			throw new ParseException("FHIR version not supported by this command: " + ContextHolder.getCtx().getVersion().getVersion());
		}

		ourCtx = theCtx;
	}

	public static String getPath() {
		Validate.notNull(ourPath, "Context not set");
		return ourPath;
	}
	
}
