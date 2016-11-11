package ca.uhn.fhir.rest.gclient;

public interface IPatch {

	/**
	 * The body of the patch document serialized in either XML or JSON which conforms to
	 * http://jsonpatch.com/ or http://tools.ietf.org/html/rfc5261
	 * 
	 * @param thePatchBody
	 *           The body of the patch
	 */
	IPatchWithBody withBody(String thePatchBody);

}
