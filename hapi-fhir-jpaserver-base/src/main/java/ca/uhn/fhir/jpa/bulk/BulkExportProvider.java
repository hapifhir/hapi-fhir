package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.StringType;

public class BulkExportProvider {

	IBaseResource export(
		@OperationParam(name = "_outputFormat", min = 0, max = 1) StringType theOutputFormat,
		@OperationParam(name = "_type", min = 0, max = 1) StringType theType,
		@OperationParam(name = "_since", min = 0, max = 1) DateTimeType theDate,
		@OperationParam(name = "_typeFilter", min = 0, max = 1) StringType theTypeFilter
	) {

		return null;

	}


}
