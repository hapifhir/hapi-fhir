package ca.uhn.fhir.context;

public class RuntimeSearchParamSource {
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
		 * The SP is built-into the base FHIR models.
		 */
		BUILT_IN,
		/**
		 * The originating source is the DB (this was saved
		 * via an API call or the like).
		 */
		DATABASE,
		/**
		 * The originating source is an IG.
		 */
		IMPLEMENTATION_GUIDE;
	}

	public static RuntimeSearchParamSource builtinSource() {
		RuntimeSearchParamSource source = new RuntimeSearchParamSource();
		source.setOriginatingSource(SourceType.BUILT_IN);
		return source;
	}

	public static RuntimeSearchParamSource databaseSource() {
		RuntimeSearchParamSource source = new RuntimeSearchParamSource();
		source.setOriginatingSource(SourceType.DATABASE);
		return source;
	}

	public static RuntimeSearchParamSource implementationGuid(String theIG, String theIGVersion) {
		RuntimeSearchParamSource source = new RuntimeSearchParamSource();
		source.setOriginatingSource(SourceType.IMPLEMENTATION_GUIDE);
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

	/**
	 * The version of the SP itself.
	 * If this SP comes from a builtin fhircontext,
	 * this will be the FHIR version.
	 */
	private String mySPVersion;

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

	public String getSPVersion() {
		return mySPVersion;
	}

	public void setSPVersion(String theSPVersion) {
		mySPVersion = theSPVersion;
	}
}
