package ca.uhn.fhir.storage;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Optional;

public class PreviousVersionReader<T extends IBaseResource> {
	private final IFhirResourceDao<T> myDao;
	public PreviousVersionReader(DaoRegistry myDaoRegistry, Class<T> theResourceClass) {
		myDao = myDaoRegistry.getResourceDao(theResourceClass);
	}
	public Optional<T> readPreviousVersion(T theResource) {
		Long currentVersion = theResource.getIdElement().getVersionIdPartAsLong();
		if (currentVersion == null || currentVersion == 1L) {
			return Optional.empty();
		}
		long previousVersion = currentVersion - 1L;
		IIdType previousId = theResource.getIdElement().withVersion(Long.toString(previousVersion));
		// WIP STR5 preserve partition
		return Optional.ofNullable(myDao.read(previousId));
	}
}
