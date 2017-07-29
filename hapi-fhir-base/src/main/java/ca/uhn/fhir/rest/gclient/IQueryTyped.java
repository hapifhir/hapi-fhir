package ca.uhn.fhir.rest.gclient;

public interface IQueryTyped<T> extends IQuery, IClientExecutable<IClientExecutable<?,?>, T> {

	/**
	 * Actually execute the client operation
	 */
	@Override
	T execute();

}
