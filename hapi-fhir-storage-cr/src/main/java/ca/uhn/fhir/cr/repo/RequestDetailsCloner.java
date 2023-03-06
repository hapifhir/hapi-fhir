package ca.uhn.fhir.cr.repo;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Map;

/**
 * This class produces partial clones of RequestDetails, the intent being to reuse the context of a
 * RequestDetails object for reentrant calls. It retains header and tenancy information while scrapping everything else.
 */
class RequestDetailsCloner {

	static DetailsBuilder startWith(RequestDetails theDetails) {
		var newDetails = new SystemRequestDetails(theDetails);
		newDetails.setRequestType(RequestTypeEnum.POST);
		newDetails.setOperation(null);
		newDetails.setResource(null);
		newDetails.setParameters(null);
		newDetails.setResourceName(null);
		newDetails.setCompartmentName(null);

		return new DetailsBuilder(newDetails);
	}

	static class DetailsBuilder {
		private final SystemRequestDetails myDetails;

		DetailsBuilder(SystemRequestDetails theDetails) {
			this.myDetails = theDetails;
		}

		DetailsBuilder addHeaders(Map<String, String> theHeaders) {
			for (var entry : theHeaders.entrySet()) {
				this.myDetails.addHeader(entry.getKey(), entry.getValue());
			}

			return this;
		}

		DetailsBuilder setParameters(IBaseParameters theParameters) {
			this.myDetails.setResource(theParameters);

			return this;
		}

		DetailsBuilder withRestOperationType(RequestTypeEnum theType) {
			this.myDetails.setRequestType(theType);

			return this;
		}
		DetailsBuilder setOperation(String theOperation) {
			this.myDetails.setOperation(theOperation);

			return this;
		}

		DetailsBuilder setResourceType(String theResourceName) {
			this.myDetails.setResourceName(theResourceName);

			return this;
		}

		DetailsBuilder setId(IIdType theId) {
			this.myDetails.setId(theId);

			return this;
		}

		SystemRequestDetails create() {
			return this.myDetails;
		}
	}
}
