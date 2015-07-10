package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.api.IBaseMetaType;

public interface IMetaAddOrDeleteSourced {

	<T extends IBaseMetaType> IClientExecutable<IClientExecutable<?, ?>, T> meta(T theMeta);
}
