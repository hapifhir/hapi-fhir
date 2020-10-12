package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.expunge.ResourceExpungeService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Scope("prototype")
public class ResourceDeleter<T extends IBaseResource> {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceDeleter.class);

	@Autowired
	ResourceExpungeService myResourceExpungeService;

	private final BaseHapiFhirResourceDao<T> myDao;

	protected ResourceDeleter(BaseHapiFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	public DeleteMethodOutcome deleteAndExpungePidList(String theUrl, Slice<Long> thePids, DeleteConflictList theConflicts, RequestDetails theRequest) {
		return myResourceExpungeService.expungeByResourcePids(thePids);
	}
}
