package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public record FhirResourceDaoPatientQueryParameters(IPrimitiveType<Integer> theCount,
																	 IPrimitiveType<Integer> theOffset,
                                                    DateRangeParam theLastUpdated,
																	 SortSpec theSort,
                                                    StringAndListParam theContent,
																	 StringAndListParam theNarrative,
                                                    StringAndListParam theFilter,
																	 StringAndListParam theTypes) {
}
