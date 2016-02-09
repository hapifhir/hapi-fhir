package ca.uhn.fhir.rest.api;

/**
 * Validation mode parameter for the $validate operation (DSTU2+ only) 
 */
public enum ValidationModeEnum {
	/**
	 * The server checks the content, and then checks that the content would be acceptable as a create (e.g. that the content would not validate any uniqueness constraints)
	 */
	CREATE,
	
	/**
	 * The server checks the content, and then checks that it would accept it as an update against the nominated specific resource (e.g. that there are no changes to immutable fields the server does not allow to change, and checking version integrity if appropriate)
	 */
	UPDATE,

	/**
	 * The server ignores the content, and checks that the nominated resource is allowed to be deleted (e.g. checking referential integrity rules)
	 */
	DELETE;

//	@Override
//	public boolean isEmpty() {
//		return false;
//	}
}
