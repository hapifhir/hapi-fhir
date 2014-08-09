package ca.uhn.fhir.jpa.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.server.IBundleProvider;

public interface IFhirSystemDao extends IDao {

	List<IResource> transaction(List<IResource> theResources);

	IBundleProvider history(Date theDate);

	TagList getAllTags();

	Map<String, Long> getResourceCounts();

}
