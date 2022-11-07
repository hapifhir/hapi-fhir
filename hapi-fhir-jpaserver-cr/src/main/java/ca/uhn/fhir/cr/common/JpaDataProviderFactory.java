package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.cql.engine.data.DataProvider;
import org.opencds.cqf.cql.engine.terminology.TerminologyProvider;

@FunctionalInterface
public interface JpaDataProviderFactory {
	DataProvider create(RequestDetails requestDetails, TerminologyProvider terminologyProvider);
}
