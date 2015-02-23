package org.hl7.fhir.instance.model.api;

import java.util.List;

import org.hl7.fhir.instance.model.ICompositeType;

public interface IBaseExtension<T> extends ICompositeType {

	List<T> getExtension();

	String getUrl();

	IBaseDatatype getValue();

	T setUrl(String theUrl);

	T setValue(IBaseDatatype theValue);

}
