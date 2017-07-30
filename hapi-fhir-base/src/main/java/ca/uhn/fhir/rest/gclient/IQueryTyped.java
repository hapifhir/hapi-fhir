package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseBundle;

public interface IQueryTyped<T extends IBaseBundle> extends IQuery, IClientExecutable<IClientExecutable<?,?>, T> {

}
