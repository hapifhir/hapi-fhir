package ca.uhn.fhir.model.api;

public enum StorageResponseCodeEnum implements ICodingEnum {

	SUCCESSFUL_CREATE("Create succeeded."),
	SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH("Conditional create succeeded: no existing resource matched the conditional URL."),
	SUCCESSFUL_CREATE_WITH_CONDITIONAL_MATCH("Conditional create succeeded: an existing resource matched the conditional URL so no action was taken."),
	SUCCESSFUL_UPDATE("Update succeeded."),
	SUCCESSFUL_UPDATE_NO_CHANGE("Update succeeded: No changes were detected so no action was taken."),
	SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH("Conditional update succeeded: an existing resource matched the conditional URL and was updated."),
	SUCCESSFUL_UPDATE_WITH_CONDITIONAL_MATCH_NO_CHANGE("Conditional update succeeded: an existing resource matched the conditional URL and was updated, but no changes were detected so no action was taken."),
	SUCCESSFUL_DELETE("Delete succeeded."),
	SUCCESSFUL_DELETE_ALREADY_DELETED("Delete succeeded: Resource was already deleted so no action was taken."),
	SUCCESSFUL_DELETE_NOT_FOUND("Delete succeeded: No existing resource was found so no action was taken."),
	;


	public static final String SYSTEM = "https://hapifhir.io/fhir/CodeSystem/hapi-fhir-storage-response-code";

	private final String myDisplay;

	StorageResponseCodeEnum(String theDisplay) {
		myDisplay = theDisplay;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getSystem() {
		return SYSTEM;
	}

	@Override
	public String getDisplay() {
		return myDisplay;
	}
}
