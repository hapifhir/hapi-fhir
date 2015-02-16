package org.hl7.fhir.instance.model.api;

import java.util.Date;

public interface IMetaType {

	ICoding addTag();

	IMetaType setLastUpdated(Date theHeaderDateValue);

}
