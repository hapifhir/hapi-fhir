package ca.uhn.fhir.rest.gclient;

public interface ISort {

	IFor ascending(IParam theParam);

	IFor defaultOrder(IParam theParam);
	
	IFor descending(IParam theParam);

}
