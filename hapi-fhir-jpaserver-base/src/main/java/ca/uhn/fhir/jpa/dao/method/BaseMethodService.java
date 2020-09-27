package ca.uhn.fhir.jpa.dao.method;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseMethodService<T extends IBaseResource> {
	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected HapiTransactionService myTransactionService;
	@Autowired
	protected IdHelperService myIdHelperService;
	@Autowired
	protected IRequestPartitionHelperSvc myRequestPartitionHelperService;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected IResourceTableDao myResourceTableDao;

	protected final BaseHapiFhirResourceDao<T> myDao;

	protected BaseMethodService(BaseHapiFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	protected String getResourceName() {
		return myDao.getResourceName();
	}

	protected Class<T> getResourceType() {
		return myDao.getResourceType();
	}
}
