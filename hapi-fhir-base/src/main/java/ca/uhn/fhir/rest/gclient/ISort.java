package ca.uhn.fhir.rest.gclient;

public interface ISort {

	IQuery ascending(IParam theParam);

	IQuery defaultOrder(IParam theParam);
	
	IQuery descending(IParam theParam);

}
