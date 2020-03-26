package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.empi.svc.EmpiLinkDaoSvc;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Interceptor
@Service
public class EmpiInterceptor {
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theRootResource) {
		// FIXME EMPI switch IBaseResource to IAnyResource in our parameters
		if (theRootResource instanceof IAnyResource) {
//			myLiveBundleReferenceUpdaterService.addReferencesForRootReference((IAnyResource) theRootResource);
		}
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource) {
		// FIXME EMPI
//		myLiveBundleReferenceUpdaterService.deleteWithAnyReferenceTo(theResource);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldRootResource, IBaseResource theNewRootResource) {
		// FIXME EMPI
		if (theOldRootResource instanceof IAnyResource) {
//			myLiveBundleReferenceUpdaterService.deleteWithRootReferenceTo((IAnyResource) theOldRootResource);
		}
		if (theNewRootResource instanceof IAnyResource) {
//			myLiveBundleReferenceUpdaterService.addReferencesForRootReference((IAnyResource) theNewRootResource);
		}
	}


	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)
	public void expungeAllLiveBundleRecords(AtomicInteger theCounter) {
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(EmpiLink.class));
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllLiveBundleRecords(AtomicInteger theCounter, IBaseResource theResource) {
		// FIXME EMPI
//		theCounter.addAndGet(myLiveBundleReferenceUpdaterService.deleteWithAnyReferenceTo(theResource).intValue());
	}
}
