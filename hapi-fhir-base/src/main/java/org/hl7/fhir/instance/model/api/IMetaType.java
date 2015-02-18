package org.hl7.fhir.instance.model.api;

import java.util.Date;

import org.hl7.fhir.instance.model.ICompositeType;

public interface IMetaType extends ICompositeType {

	ICoding addTag();

	IMetaType setLastUpdated(Date theHeaderDateValue);

}
