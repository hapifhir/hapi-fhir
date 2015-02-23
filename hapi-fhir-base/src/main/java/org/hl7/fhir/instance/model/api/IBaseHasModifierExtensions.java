package org.hl7.fhir.instance.model.api;

import java.util.List;

public interface IBaseHasModifierExtensions {

    public List<? extends IBaseExtension<?>> getModifierExtension();

    public IBaseExtension<?> addModifierExtension();

}
