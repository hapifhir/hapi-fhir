package ca.uhn.fhir.jpa.demo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.Validate;

public class ContextHolder {

	private static boolean ourAllowExternalRefs;
	private static FhirContext ourCtx;
	private static boolean ourDisableReferentialIntegrity;
	private static String ourPath;
	private static Long ourReuseSearchResultsMillis;

	static {
		ourReuseSearchResultsMillis = DaoConfig.DEFAULT_REUSE_CACHED_SEARCH_RESULTS_FOR_MILLIS;
	}

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
			case R4:
				ourPath = "/baseR4/";
				break;
			default:
				throw new ParseException("FHIR version not supported by this command: " + theCtx.getVersion().getVersion());
		}

		ourCtx = theCtx;
	}

	public static String getPath() {
		Validate.notNull(ourPath, "Context not set");
		return ourPath;
	}

	public static Long getReuseCachedSearchResultsForMillis() {
		return ourReuseSearchResultsMillis;
	}

	public static void setReuseCachedSearchResultsForMillis(Long reuseSearchResultsMillis) {
		ourReuseSearchResultsMillis = reuseSearchResultsMillis;
	}

	public static boolean isAllowExternalRefs() {
		return ourAllowExternalRefs;
	}

	public static void setAllowExternalRefs(boolean theAllowExternalRefs) {
		ourAllowExternalRefs = theAllowExternalRefs;
	}

	public static boolean isDisableReferentialIntegrity() {
		return ourDisableReferentialIntegrity;
	}

	public static void setDisableReferentialIntegrity(boolean theDisableReferentialIntegrity) {
		ourDisableReferentialIntegrity = theDisableReferentialIntegrity;
	}
}
