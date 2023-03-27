package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Parameters;

public interface IReindexDryRunService {

	IBaseParameters reindexDryRun(RequestDetails theRequestDetails, IIdType theResourceId);

}
