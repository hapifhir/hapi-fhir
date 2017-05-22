package ca.uhn.fhir.jpa.demo;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.FhirContext;

public class ContextHolder {

	private static boolean ourAllowExternalRefs;
	private static FhirContext ourCtx;
	private static boolean ourDisableReferentialIntegrity;
	private static String ourPath;

	public static FhirContext getCtx() {
		Validate.notNull(ourPath, "Context not set");
		return ourCtx;
	}

	public static String getPath() {
		Validate.notNull(ourPath, "Context not set");
		return ourPath;
	}

	public static boolean isAllowExternalRefs() {
		return ourAllowExternalRefs;
	}

	public static boolean isDisableReferentialIntegrity() {
		return ourDisableReferentialIntegrity;
	}

	public static void setAllowExternalRefs(boolean theAllowExternalRefs) {
		ourAllowExternalRefs = theAllowExternalRefs;
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

	public static void setDisableReferentialIntegrity(boolean theDisableReferentialIntegrity) {
		ourDisableReferentialIntegrity = theDisableReferentialIntegrity;
	}
	
}
