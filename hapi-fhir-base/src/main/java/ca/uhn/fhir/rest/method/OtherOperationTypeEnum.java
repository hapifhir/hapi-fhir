package ca.uhn.fhir.rest.method;

public enum OtherOperationTypeEnum {

	METADATA("metadata"), 
	
	ADD_TAGS("add-tags"), 
	
	DELETE_TAGS("delete-tags"), 
	
	GET_TAGS("get-tags"), 
	
	GET_PAGE("get-page");
	
	private String myCode;

	OtherOperationTypeEnum(String theName) {
		myCode=theName;
	}

	public String getCode() {
		return myCode;
	}
	
}
