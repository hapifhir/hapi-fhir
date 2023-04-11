package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.function.Function;

public interface ISubmitDataService extends Function<RequestDetails, SubmitDataService> {
}
