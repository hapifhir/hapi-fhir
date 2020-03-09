package ca.uhn.fhir.jpa.model.cross;

import java.util.Date;

public interface IResourceLookup {
	String getResourceType();

	Long getResourceId();

	Date getDeleted();
}
