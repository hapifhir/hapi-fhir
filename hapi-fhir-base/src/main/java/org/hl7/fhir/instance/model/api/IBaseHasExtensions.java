package org.hl7.fhir.instance.model.api;

import java.util.List;

public interface IBaseHasExtensions {

    public List<? extends IBaseExtension<?>> getExtension();

    public IBaseExtension<?> addExtension();

}
