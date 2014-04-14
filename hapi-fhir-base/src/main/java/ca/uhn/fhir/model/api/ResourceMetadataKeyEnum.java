package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.primitive.InstantDt;

public enum ResourceMetadataKeyEnum {

	VERSION_ID,
	
	/**
	 * The value for this key is the bundle entry <b>Published</b> time. This
	 * is defined by FHIR as "Time resource copied into the feed", which is generally 
	 * best left to the current time. 
	 * <p>
	 * Values for this key are of type {@link InstantDt}
	 * </p>
	 * <p>
	 * <b>Server Note</b>: In servers, it is generally advisable to leave this
	 * value <code>null</code>, in which case the server will substitute the 
	 * current time automatically.
	 * </p>
	 * 
	 * @see InstantDt
	 */
	PUBLISHED,
	
	/**
	 * The value for this key is the bundle entry <b>Updated</b> time. This
	 * is defined by FHIR as "Last Updated for resource".
	 * <p>
	 * Values for this key are of type {@link InstantDt}
	 * </p>
	 * 
	 * @see InstantDt
	 */
	UPDATED;
	
}
