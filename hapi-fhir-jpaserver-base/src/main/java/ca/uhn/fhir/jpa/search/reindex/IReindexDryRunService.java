package ca.uhn.fhir.jpa.search.reindex;

import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IReindexDryRunService {

	IBaseParameters reindexDryRun(IIdType theResourceId);

}
