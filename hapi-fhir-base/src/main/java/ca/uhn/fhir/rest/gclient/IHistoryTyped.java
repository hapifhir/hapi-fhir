package ca.uhn.fhir.rest.gclient;

import java.util.Date;

import ca.uhn.fhir.model.primitive.InstantDt;

public interface IHistoryTyped<T> extends IClientExecutable<IHistoryTyped<T>, T>  {

	/**
	 * Request that the server return only resource versions that were created at or after the given time (inclusive)
	 */
	IHistoryTyped<T> since(Date theCutoff);

	/**
	 * Request that the server return only resource versions that were created at or after the given time (inclusive)
	 */
	IHistoryTyped<T> since(InstantDt theCutoff);

	/**
	 * Request that the server return only up to <code>theCount</code> number of resources
	 */
	IHistoryTyped<T> count(Integer theCount);
	
	
}
