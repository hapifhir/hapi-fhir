package ca.uhn.fhir.jpa.dao;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class SearchBuilderFactory {
	@Lookup
	public abstract SearchBuilder newSearchBuilder(BaseHapiFhirDao theBaseHapiFhirResourceDao);
}
