package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;

/**
 * This class produces partial clones of RequestDetails, the intent being to reuse the context of a
 * RequestDetails object for reentrant calls. It retains header and tenancy information while
 * scrapping everything else.
 */
class RequestDetailsCloner {

    private RequestDetailsCloner() {}

    static DetailsBuilder startWith(RequestDetails details) {
        var newDetails = new SystemRequestDetails(details);
        newDetails.setRequestType(RequestTypeEnum.POST);
        newDetails.setOperation(null);
        newDetails.setResource(null);
        newDetails.setParameters(new HashMap<>());
        newDetails.setResourceName(null);
        newDetails.setCompartmentName(null);
        newDetails.setResponse(details.getResponse());

        return new DetailsBuilder(newDetails);
    }

    static class DetailsBuilder {
        private final SystemRequestDetails details;

        DetailsBuilder(SystemRequestDetails details) {
            this.details = details;
        }

        DetailsBuilder setAction(RestOperationTypeEnum restOperationType) {
            details.setRestOperationType(restOperationType);
            return this;
        }

        DetailsBuilder addHeaders(Map<String, String> headers) {
            if (headers != null) {
                for (var entry : headers.entrySet()) {
                    details.addHeader(entry.getKey(), entry.getValue());
                }
            }

            return this;
        }

        DetailsBuilder setParameters(IBaseParameters parameters) {
            IParser parser = details.getServer().getFhirContext().newJsonParser();
            details.setRequestContents(parser.encodeResourceToString(parameters).getBytes());

            return this;
        }

        DetailsBuilder setParameters(Map<String, String[]> parameters) {
            details.setParameters(parameters);

            return this;
        }

        DetailsBuilder withRestOperationType(RequestTypeEnum type) {
            details.setRequestType(type);

            return this;
        }

        DetailsBuilder setOperation(String operation) {
            details.setOperation(operation);

            return this;
        }

        DetailsBuilder setResourceType(String resourceName) {
            details.setResourceName(resourceName);

            return this;
        }

        DetailsBuilder setId(IIdType id) {
            details.setId(id);

            return this;
        }

        SystemRequestDetails create() {
            return details;
        }
    }
}
