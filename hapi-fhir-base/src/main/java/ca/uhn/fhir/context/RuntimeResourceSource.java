package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IFhirVersion;

public class RuntimeResourceSource {
	/**
	 * Enum defining the source of the SearchParameter
	 * (ie, where it's from)
	 */
	public enum SourceType {
		/**
		 * Unknown source; a source should always be provided,
		 * but if one cannot be, specify UNKNOWN.
		 */
		UNKNOWN,
		/**
		 * The Resource is built-into the base FHIR context.
		 */
		FHIR_CONTEXT,
		/**
		 * The originating source is the DB (this was saved
		 * via an API call or the like).
		 */
		DATABASE,
		/**
		 * The originating source is an IG.
		 * These are Implementation Guides from NPM
		 */
		NPM;
	}

	public static RuntimeResourceSource fhirContextSource(FhirContext theFhirContext) {
		RuntimeResourceSource source = new RuntimeResourceSource();
		source.setOriginatingSource(SourceType.FHIR_CONTEXT);
		IFhirVersion fhirVersion = theFhirContext.getVersion();
		source.setIGUrlAndVersion("FhirContext", fhirVersion.getVersion().getFhirVersionString());
		return source;
	}

	public static RuntimeResourceSource databaseSource() {
		RuntimeResourceSource source = new RuntimeResourceSource();
		source.setOriginatingSource(SourceType.DATABASE);
		return source;
	}

	public static RuntimeResourceSource npmSource(String theIG, String theIGVersion) {
		RuntimeResourceSource source = new RuntimeResourceSource();
		source.setOriginatingSource(SourceType.NPM);
		source.setIGUrlAndVersion(theIG, theIGVersion);
		return source;
	}

	/**
	 * The originating source of this RuntimeSearchParameter.
	 */
	private SourceType myOriginatingSource = SourceType.UNKNOWN;

	/**
	 * The name of the source Implementation Guide
	 */
	private String myIGName;

	/**
	 * The version of the IG
	 */
	private String myIGVersion;

	public SourceType getOriginatingSource() {
		return myOriginatingSource;
	}

	public void setOriginatingSource(SourceType theOriginatingSource) {
		myOriginatingSource = theOriginatingSource;
	}

	public String getIGName() {
		return myIGName;
	}

	public String getIGVersion() {
		return myIGVersion;
	}

	public void setIGUrlAndVersion(String theIGUrl, String theVersion) {
		myIGName = theIGUrl;
		myIGVersion = theVersion;
	}
}
