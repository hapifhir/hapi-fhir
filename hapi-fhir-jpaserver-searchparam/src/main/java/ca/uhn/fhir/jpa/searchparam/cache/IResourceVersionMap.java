package ca.uhn.fhir.jpa.searchparam.cache;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourceVersionMap {
	Long getVersion(IIdType theToVersionless);

	int size();
}
