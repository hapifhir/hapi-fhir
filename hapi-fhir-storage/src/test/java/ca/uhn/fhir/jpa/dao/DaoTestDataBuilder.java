package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoTestDataBuilder implements ITestDataBuilder, AfterEachCallback {
	private static final Logger ourLog = LoggerFactory.getLogger(DaoTestDataBuilder.class);

	final FhirContext myFhirCtx;
	final DaoRegistry myDaoRegistry;
	SystemRequestDetails mySrd;
	final SetMultimap<String, IIdType> myIds = HashMultimap.create();

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
		IIdType id = dao.create(theResource, mySrd).getId().toUnqualifiedVersionless();
		myIds.put(theResource.fhirType(), id);
		return id;
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

	/**
	 * Delete anything created
	 */
	public void cleanup() {
		ourLog.info("cleanup {}", myIds);

		myIds.keySet().forEach(nextType->{
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(nextType);
			myIds.get(nextType).forEach(dao::delete);
		});
		myIds.clear();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		cleanup();
	}
}
