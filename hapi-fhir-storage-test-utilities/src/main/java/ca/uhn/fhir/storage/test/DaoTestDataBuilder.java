package ca.uhn.fhir.storage.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class DaoTestDataBuilder implements ITestDataBuilder {
	final FhirContext myFhirCtx;
	final DaoRegistry myDaoRegistry;
	SystemRequestDetails mySrd;

	public DaoTestDataBuilder(FhirContext theFhirCtx, DaoRegistry theDaoRegistry, SystemRequestDetails theSrd) {
		myFhirCtx = theFhirCtx;
		myDaoRegistry = theDaoRegistry;
		mySrd = theSrd;
	}

	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		//noinspection rawtypes
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		//noinspection unchecked
		return dao.create(theResource, mySrd).getId().toUnqualifiedVersionless();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		//noinspection rawtypes
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		//noinspection unchecked
		return dao.update(theResource, mySrd).getId().toUnqualifiedVersionless();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}
}
